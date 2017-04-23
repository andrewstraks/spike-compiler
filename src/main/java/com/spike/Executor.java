package com.spike;

import com.spike.cli.CliCreator;
import com.spike.scripts.ScriptsCompiler;
import com.spike.scripts.ScriptsIO;
import com.spike.templates.TemplatesCompiler;
import com.spike.templates.TemplatesIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dawid on 2017-01-29.
 */
public class Executor {

    static Boolean DEBUG = false;

    static void debug(Object data) {

        if (DEBUG) {
            System.out.println(data);
        }

    }

    static ScriptsIO scriptsIO = new ScriptsIO();
    static ScriptsCompiler scriptsCompiler = new ScriptsCompiler();
    static TemplatesIO templatesIO = new TemplatesIO();
    static TemplatesCompiler templatesCompiler = new TemplatesCompiler();
    static CliCreator cliCreator = new CliCreator();

    static void cli(String[] args) throws IOException {

        switch (args[2]) {
            case "component":
                cliCreator.createComponent(args[1], args[3]);
                break;
            case "partial":
                cliCreator.createPartial(args[1], args[3]);
                break;
            case "controller":
                cliCreator.createController(args[1], args[3]);
                break;
            case "modal":
                cliCreator.createModal(args[1], args[3]);
                break;
            case "service":
                cliCreator.createService(args[1], args[3]);
                break;
            case "util":
                cliCreator.createUtil(args[1], args[3]);
                break;
        }

    }

    public static void main(String[] args) throws IOException {

        String type = args[0];

        if (type.equals("test-templates")) {
            type = "templates";
            args = new String[]{null, "templates_input/", "templates_output/templates.js"};
        } else if (type.equals("test-scripts")) {
            type = "imports-gstrings";
            args = new String[]{null, "scripts_input/test.js", "scripts_output/compiled.js"};
        } else if (type.equals("test-cli")) {
            args = new String[]{"cli", "cli/test/component/", "component", "UserPanel"};
            cli(args);
            args = new String[]{"cli", "cli/test/controller/", "controller", "Home"};
            cli(args);
            args = new String[]{"cli", "cli/test/modal/", "modal", "Message"};
            cli(args);
            args = new String[]{"cli", "cli/test/partial/", "partial", "LoginForm"};
            cli(args);
            args = new String[]{"cli", "cli/test/service/", "service", "Auth"};
            cli(args);
            args = new String[]{"cli", "cli/test/util", "util", "Utils"};
            cli(args);
        }

        if (type.equals("imports-gstrings")) {

            String fileBody = scriptsIO.getFile(args[1]);

            fileBody = scriptsCompiler.compileImports(fileBody);
            fileBody = scriptsCompiler.compileGStrings(fileBody);

            scriptsIO.saveFile(fileBody, args[2]);

        } else if (type.equals("templates")) {

            List<File> files = templatesIO.getFileList(args[1]);
            List<String> functionBodies = new ArrayList<>();
            functionBodies.add(templatesCompiler.getTemplatesDeclaration());

            for (File file : files) {
                functionBodies.add(templatesCompiler.compile(file));
            }

            templatesIO.saveConcatedFiles(functionBodies, args[2]);

        } else if (type.equals("cli")) {

            cli(args);

        }

    }

}
