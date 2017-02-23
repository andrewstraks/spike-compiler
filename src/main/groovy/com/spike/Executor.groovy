package com.spike

import com.spike.scripts.ScriptsCompiler
import com.spike.scripts.ScriptsIO
import com.spike.templates.TemplatesCompiler
import com.spike.templates.TemplatesIO

/**
 * Created by Dawid on 2017-01-29.
 */
class Executor {

    static DEBUG = false;

    static debug(def data) {

        if (DEBUG) {
            println data;
        }

    }

    static ScriptsIO scriptsIO = new ScriptsIO()
    static ScriptsCompiler scriptsCompiler = new ScriptsCompiler()
    static TemplatesIO templatesIO = new TemplatesIO()
    static TemplatesCompiler templatesCompiler = new TemplatesCompiler()

    static void main(def args) {

        def type = args[0]

        if(type == 'test-templates'){
            type = 'templates'
            args = [null, 'F:\\layout-prototype\\mobile-new\\dist\\tmp', 'templates_output/templates.js']
        }else if(type =='test-scripts'){
            type = 'imports-gstrings'
            args = [null, 'scripts_input/test.js', 'scripts_output/compiled.js']
        }


        if (type == 'imports-gstrings') {

            def fileBody = scriptsIO.getFile(args[1]);

            fileBody = scriptsCompiler.compileImports(fileBody)
            fileBody = scriptsCompiler.compileGStrings(fileBody)

            scriptsIO.saveFile(fileBody, args[2]);

        } else if (type == 'templates') {


            def files = templatesIO.getFileList(args[1])

            def functionBodies = []

            functionBodies << templatesCompiler.getTemplatesDeclaration()

            files.each {

                functionBodies << templatesCompiler.compile(it)

            }

            templatesIO.saveConcatedFiles(functionBodies, args[2])


        }

    }

}
