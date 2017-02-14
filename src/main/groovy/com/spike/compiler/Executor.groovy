package com.spike.compiler

/**
 * Created by Dawid on 2017-01-29.
 */
class Executor {

    static DEBUG = false;

    static debug(def data){

        if(DEBUG){
            println data;
        }

    }

    static TemplateIO io = new TemplateIO()
    static TemplateCompiler compiler = new TemplateCompiler()
    
    static void main(def args){

        def type = args[0]

        if(type == 'imports-gstrings'){
            def fileBody = io.getFile(args[1]);

            //DEBUG = true
            fileBody = compiler.compileImports(fileBody)

            //DEBUG = false
            fileBody = compiler.compileGStrings(fileBody)
            io.saveFile(fileBody, args[2]);
        }

    }
    
}
