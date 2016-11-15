package com.hcz.core.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by chz on 2015/6/17.
 */
public class FileUtils {

    /**
     * 获取文件夹下所有文件，文件夹在前面，文件在后面
     * @param file
     */
    public static List<File> getFileList(File file){
        if(file.isFile()){
            return null;
        }
        List<File> files = Arrays.asList(file.listFiles());
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if(lhs.isDirectory() && rhs.isFile()){
                    return -1;
                }
                if(lhs.isFile() && rhs.isDirectory()){
                    return 1;
                }
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        return files;
    }
}
