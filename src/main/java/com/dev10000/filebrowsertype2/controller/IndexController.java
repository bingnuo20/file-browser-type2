package com.dev10000.filebrowsertype2.controller;

import com.dev10000.filebrowsertype2.service.FileService;
import com.dev10000.filebrowsertype2.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页文件控制器
 */
@Controller
public class IndexController {
    /** 根目录 */
    @Value("${dirPath}")
    private String baseDirPath;
    /** 密钥 */
    @Value("${certCode}")
    private String certCode;
    /** 文件服务 */
    @Resource
    private FileService fileService;


    /**
     * 映射主界面
     * @param request 请求实例
     * @return html
     */
    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("loginFlag") == null) {
            return "redirect:login";
        }
        request.setAttribute("baseDirPath",baseDirPath);
        return "index";

    }

    /**
     * 根据路径返回文件列表
     * @param path 路径
     * @return 文件列表
     */
    @ResponseBody
    @RequestMapping("listFiles")
    public Map<String,Object> listFiles(String path) {
        String  p = null;
        try {
            p = java.net.URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String,Object> map = fileService.listFiles(p);
        return map;
    }

    /**
     * 删除文件
     * @param fileName 文件名
     * @param key 密钥
     * @return Map
     */
    @ResponseBody
    @RequestMapping("deleteFile")
    public Map<String,Object> deleteFile(String fileName,String key) {
        String  filePath = null;
        try {
            filePath = java.net.URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String,Object> map = new HashMap<>();
        if (key.equals(certCode)) {
            boolean flag = FileUtils.deleteFile(filePath);
            map.put("flag",flag);
            if (flag) {
                map.put("msg","删除成功");
            } else {
                map.put("msg","删除失败");
            }
        } else {
            map.put("flag",false);
            map.put("msg","证书错误");
        }
        return map;
    }

    /**
     * 下载文件
     * @param dirPath 父级目录
     * @param fileName 文件名
     * @return ResponseEntity实例
     * @throws IOException 文件IO异常
     */
    @RequestMapping(value = "/download")
    public ResponseEntity<byte[]> download(String dirPath, String fileName) throws IOException {
        String  dirPath1 = null;
        String  fileName1 = null;
        try {
            dirPath1 = java.net.URLDecoder.decode(dirPath, "UTF-8");
            fileName1 = java.net.URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String downloadFilePath = dirPath1 + "/" + fileName1;
        File file = new File(downloadFilePath);//新建一个文件
        HttpHeaders headers = new HttpHeaders();//http头信息
        String downloadFileName = new String(fileName1.getBytes("UTF-8"),"iso-8859-1");//设置编码
        headers.setContentDispositionFormData("attachment", downloadFileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(org.apache.commons.io.FileUtils.readFileToByteArray(file),headers, HttpStatus.OK);
    }

    /**
     * 同步上传
     * @param file 文件
     * @param path 路径
     * @param key 密钥
     * @return String
     */
    @RequestMapping(value = "/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file,String path,String key) throws Exception {
        if (key.equals(certCode)) {
            String name = file.getOriginalFilename();
            File f = new File(path + "/" + name);
            file.transferTo(f);
        }
        return "redirect:/";
    }

    /**
     * 异步上传
     * @param file 文件
     * @param path 路径
     * @param key 密钥
     * @return Map
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxUploadFile")
    public Map<String,Object> ajaxUploadFile(@RequestParam("file") MultipartFile file, String path, String key) {
        Map<String,Object> map = new HashMap<>();
        if (key.equals(certCode)) {
            try {
                String name = file.getOriginalFilename();
                File f = new File(path + "/" + name);
                file.transferTo(f);
                map.put("flag",true);
                map.put("msg","上传成功");
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                map.put("flag",false);
                map.put("msg","上传失败");
                return map;
            }
        }
        map.put("flag",false);
        map.put("msg","证书错误");
        return map;
    }

    /**
     * 创建目录
     * @param path 父级路径
     * @param newDir 新的目录
     * @return Map
     */
    @ResponseBody
    @RequestMapping(value = "/createDir")
    public Map<String,Object> createDir(String path, String newDir) {
        Map<String,Object> map = new HashMap<>();
        String pathTmp = null;
        try {
            pathTmp = java.net.URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File f = new File(pathTmp + "/" + newDir);
        if (f.exists()) {
            map.put("flag",false);
            map.put("msg","文件已存在");
            return map;
        }
        boolean flag = f.mkdir();
        map.put("flag",flag);
        if (flag) {
            map.put("msg","创建成功");
        } else {
            map.put("msg","创建失败");
        }
        return map;
    }

    /**
     * 重命名目录
     * @param path 父级路径
     * @param newDir 新的目录
     * @param oldDir 旧的目录
     * @return Map
     */
    @ResponseBody
    @RequestMapping(value = "/renameDir")
    public Map<String,Object> renameDir(String path, String newDir, String oldDir) {
        Map<String,Object> map = new HashMap<>();
        String pathTmp = null;
        try {
            pathTmp = java.net.URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File oldPath = new File(pathTmp + "/" + oldDir);
        File newPath = new File(pathTmp + "/" + newDir);
        if (newPath.exists()) {
            map.put("flag",false);
            map.put("msg","文件名已存在");
            return map;
        }
        boolean flag = oldPath.renameTo(newPath);
        map.put("flag",flag);
        if (flag) {
            map.put("msg","操作成功");
        } else {
            map.put("msg","操作失败");
        }
        return map;
    }
}

