package com.spike.scripts;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Created by Dawid on 2017-01-29.
 */
public class ScriptsIO {

     public String getFile(String srcPath) throws IOException {

         return IOUtils.toString(new FileInputStream(new File(srcPath)), "utf-8");

    }

    public void saveFile(String fileBody, String distPath) throws IOException {
        FileUtils.writeStringToFile(new File(distPath), fileBody, "UTF-8");
    }

}
