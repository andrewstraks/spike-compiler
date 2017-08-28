package com.spike.templates;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Dawid on 2017-01-29.
 */
public class TemplatesIO {

    static List<File> getFileListByPaths(List<String> pathsList){

        List<File> list = new ArrayList<>();

        for(String it : pathsList){
            list.add(new File(it));
        }

        return list;

    }

    public static File findFileByName(File dir, String nameWithExtension) {
        Set<File> fileTree = listFileTree(dir);

        for (File file : fileTree) {
            System.out.println(file.getName());
            System.out.println(nameWithExtension);

            if(file.getName().equals(nameWithExtension)){
                return file;
            }

        }

        return null;
    }

    public static Set<File> listFileTree(File dir) {
        Set<File> fileTree = new HashSet<>();

        if(dir==null||dir.listFiles()==null){
            return fileTree;
        }

        for (File entry : dir.listFiles()) {
            if (entry.isFile() && getFileExtension(entry.getName()).equals("html")){
                fileTree.add(entry);
            }  else {
                fileTree.addAll(listFileTree(entry));
            }
        }

        return fileTree;
    }

    public static List<File>  getFileList(String srcPath){

        List<File> list = new ArrayList<>();

        list.addAll(listFileTree(new File(srcPath)));

        return list;

    }

    static String getFileExtension(String fileName){

        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }

        return  extension.toLowerCase();

    }

    public void saveConcatedFiles(List<String> functionBodiesList, String distPath) throws IOException {

        File distFile = new File(distPath);
        distFile.getParentFile().mkdirs();

        FileUtils.writeStringToFile(distFile, StringUtils.join(functionBodiesList, ""), "UTF-8");

    }

}
