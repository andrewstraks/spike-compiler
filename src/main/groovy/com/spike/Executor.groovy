package com.spike

import com.spike.scripts.ScriptsCompiler
import com.spike.scripts.ScriptsIO
import com.spike.security.SecurityCompiler
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
    static SecurityCompiler securityCompiler = new SecurityCompiler()

    static void main(def args) {

        def type = args[0]

        if(type == 'test-templates'){
            type = 'templates'
            args = [null, 'templates_input/', 'templates_output/templates.js']
        }else if(type =='test-scripts'){
            type = 'imports-gstrings'
            args = [null, 'scripts_input/test.js', 'scripts_output/compiled.js']
        }else if(type =='test-security'){
            type = 'security'
            args = [null, 'security_output/vendor.min.js', 'security_input/spike-framework.min.js', 'security_input/templates.min.js', 'security_input/app.min.js']
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


        }else if (type == 'security') {

            def argsList = args as List<String>

            def files = templatesIO.getFileListByPaths(argsList.subList(2  as Integer, argsList.size() as Integer))

            def vendorBody = []

            files.each {

                vendorBody << securityCompiler.secure(it.getText())

            }

            templatesIO.saveConcatedFiles(vendorBody, args[1])


        }

    }

}
