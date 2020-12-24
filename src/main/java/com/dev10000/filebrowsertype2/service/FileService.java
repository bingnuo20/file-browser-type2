package com.dev10000.filebrowsertype2.service;


import com.dev10000.filebrowsertype2.util.FileUtils;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * 文件服务
 */
@Service
public class FileService {

    /**
     * 根据路径返回文件列表
     * @param path 路径
     * @return 文件列表
     */
    public Map<String,Object> listFiles(String path) {
        Map<String,Object> map = FileUtils.listFiles(path);
        return map;
    }

    /**
     * 删除文件
     * @param filePath 文件路径
     * @return true | false
     */
    public boolean deleteFile(String filePath) {
        return FileUtils.deleteFile(filePath);
    }
}
