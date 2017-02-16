package com.spike.templates

import groovy.io.FileType

/**
 * Created by Dawid on 2017-01-29.
 */
class TemplatesIO {

    def getFileList(srcPath){

        def list = []

        def dir = new File(srcPath)
        dir.eachFileRecurse (FileType.FILES) { File file ->

            if(this.getFileExtension(file.getName()).equals('html')){
                list << file
            }

        }

        return list

    }
    
    def getFileExtension(String fileName){

        String extension = ""

        int i = fileName.lastIndexOf('.')
        if (i > 0) {
            extension = fileName.substring(i+1)
        }
        
        return  extension.toLowerCase()
        
    }

    def saveConcatedFiles(def functionBodiesList, def distPath){

        File distFile = new File(distPath)
        distFile.write(functionBodiesList.join(""))

    }

}
