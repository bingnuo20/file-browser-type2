package com.dev10000.filebrowsertype2.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件操作类
 */
public class FileUtils {

    /**
     * 显示一个目录的所有的文件
     * @param dirPath 文件目录
     * @return 文件名集合
     */
    public static Map<String,Object> listFiles(String dirPath) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> list = new ArrayList<>();
        List<String> dts = new ArrayList<>();
        File file = new File(dirPath);
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                list.add(0,f.getName() + "[进入]");
            } else {
                list.add(f.getName());
            }

            dts.add(sdf.format(new Date(Long.valueOf(f.lastModified()))));
        }
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("dts",dts);
        return map;
    }

    /**
     * 删除指定的文件
     * @param filePath 文件地址
     * @return trur|false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            delAllFile(file);
            return true;
        }
        return file.delete();
    }

    /**
     * 递归删除所有文件
     * @param directory 文件目录
     */
    public static void delAllFile(File directory) {
        if (!directory.isDirectory()) {
            directory.delete();
        } else {
            File [] files = directory.listFiles();
            // 空文件夹
            if (files.length == 0) {
                directory.delete();
                return;
            }
            // 删除子文件夹和子文件
            for (File file : files) {
                if (file.isDirectory()) {
                    delAllFile(file);
                } else {
                    file.delete();
                }
            }
            // 删除文件夹本身
            directory.delete();
        }
    }
}
