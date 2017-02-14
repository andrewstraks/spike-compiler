package com.spike.compiler


/**
 * Created by Dawid on 2017-01-29.
 */
class TemplateIO {

    def getFile(srcPath) {
        return new File(srcPath).getText("UTF-8");
    }

    def saveFile(def fileBody, def distPath) {
        File file = new File(distPath)
        file.write(fileBody)

    }

}
