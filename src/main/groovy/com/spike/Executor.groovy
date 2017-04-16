package com.spike

import com.spike.cli.CliCreator
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
    static CliCreator cliCreator = new CliCreator()

    static void main(def args) {

        def cli = {

            switch (args[2]) {
                case 'component': cliCreator.createComponent(args[1], args[3]); break;
                case 'partial': cliCreator.createPartial(args[1], args[3]); break;
                case 'controller': cliCreator.createController(args[1], args[3]); break;
                case 'modal': cliCreator.createModal(args[1], args[3]); break;
                case 'service': cliCreator.createService(args[1], args[3]); break;
                case 'util': cliCreator.createUtil(args[1], args[3]); break;
            }

        }

        def type = args[0]

        if (type == 'test-templates') {
            type = 'templates'
            args = [null, 'templates_input/', 'templates_output/templates.js']
        } else if (type == 'test-scripts') {
            type = 'imports-gstrings'
            args = [null, 'scripts_input/test.js', 'scripts_output/compiled.js']
        } else if (type == 'test-cli') {
            args = ['cli', 'cli/test/component/', 'component', 'UserPanel']
            cli(args)
            args = ['cli', 'cli/test/controller/', 'controller', 'Home']
            cli(args)
            args = ['cli', 'cli/test/modal/', 'modal', 'Message']
            cli(args)
            args = ['cli', 'cli/test/partial/', 'partial', 'LoginForm']
            cli(args)
            args = ['cli', 'cli/test/service/', 'service', 'Auth']
            cli(args)
            args = ['cli', 'cli/test/util', 'util', 'Utils']
            cli(args)
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


        } else if (type == 'cli') {

            cli(args)

        }

    }

}
