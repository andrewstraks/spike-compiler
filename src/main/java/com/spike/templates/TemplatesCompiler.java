package com.spike.templates;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;

/**
 * Created by Dawid on 2017-01-29.
 */
public class TemplatesCompiler {

    String templatesGlobalDeclaration = "_spike_templates";

    public String getTemplatesDeclaration() {
        return "; window['" + templatesGlobalDeclaration + "'] = {} \n";
    }

    String getFileName(File templateFile) {
        return templateFile.getPath().replaceAll("\\\\", "/");
    }

    public String compile(File templateFile) throws IOException {

        String output = null;
        Boolean isPartial = templateFile.getName().contains("partial.html");
        Boolean isTemplate = templateFile.getName().contains("template.html");

        output = "; window['" + templatesGlobalDeclaration + "']['" + getFileName(templateFile) + "'] = function($local) { \n";
        output += "; var html = '' \n";

        BufferedReader in = new BufferedReader(new FileReader(templateFile));
        String str;

        List<String> lines = new ArrayList<String>();
            while((str = in.readLine()) != null){
                lines.add(str);
            }

        if (lines.size() == 0) {
            String wholeFileContent = IOUtils.toString(new FileInputStream(templateFile), "utf-8");

            output += " ' " + wholeFileContent + " ' ";
            System.out.println( "Warning: Template file is almost empty " + templateFile.getName());

        } else if (isTemplate) {

            String templateName = this.getFileName(templateFile);
            templateName = templateName.substring(templateName.lastIndexOf("/")+1, templateName.length());
            templateName = templateName.substring(0, templateName.indexOf("."));


            output = "; window['" + templatesGlobalDeclaration + "']['" + "@template/"+templateName + "'] = ";

            Map<String, String> importsReplacements = new HashMap<>();

            for(String line : lines){

                if (line.contains("<!--")) {
                    output += "/** " + line.replace("<!--","").replace("-->","") + " **/ ";
                }else  if (line.startsWith("'import")) {
                    String key = line.replace("'", "").substring(0, line.indexOf("as") - 1).replace("import", "").trim();
                    String value = line.substring(line.indexOf("as") + 2, line.length()).replace("'", "").replace(";", "").trim();
                    importsReplacements.put(key, value);
                } else {
                    line =  replaceSpikeTranslations(line);
                    output += " '" + escapeEvents(line.replace("'", "\\'")) + "' + \n";
                }

            }

            output = output.substring(0, output.lastIndexOf("+"));

            output += "; \n";

            for (Map.Entry<String, String> entry : importsReplacements.entrySet()) {

                String key = entry.getKey();
                String val = entry.getValue();

                output = output.replace(key.trim(), val.trim());

            }

            output = createPartialEvents(output, true);


        } else {

            Map<String, String> importsReplacements = new HashMap<>();

            for(String line : lines){

                if (line.contains("<!--")) {
                    output += "/** " + line.replace("<!--","").replace("-->","") + " **/ ";
                } else if (line.startsWith("'import")) {

                    String key = line.replace("'", "").substring(0, line.indexOf("as") - 1).replace("import", "").trim();
                    String value = line.substring(line.indexOf("as") + 2, line.length()).replace("'", "").replace(";", "").trim();
                    importsReplacements.put(key, value);

                } else if (line.trim().startsWith("#")) {

                    output += (line.endsWith(",") || line.endsWith("{") || line.endsWith("}") ? " " : "; ") + line.replace("#", "") + " \n";
                } else {

                    line =  replaceSpikeTranslations(line);
                    if (isPartial) {

                        // line = createPartialSpecialAttributes(line)

                        output += "; html += '" + replaceSpecialCharacters(line) + "' \n";
                    } else {
                        output += "; html += '" + escapeEvents(line) + "' \n";
                    }

                }

            }

            output += "; return html \n";
            output += "; } \n";

            for (Map.Entry<String, String> entry : importsReplacements.entrySet()) {

                String key = entry.getKey();
                String val = entry.getValue();

                    output = output.replace(key.trim(), val.trim());
            }

            output = createPartialEvents(output, false);

        }

        return output;

    }

    static String createPartialSpecialAttributes(String str){

        String[] splitted = str.split("\n");

        for(int i = 0; i < splitted.length; i++){

            String line = splitted[i];

            int index = line.indexOf("spike='");

            try {
                if(index > -1){
                    int index2 = line.replace("spike='","").indexOf("'");

                    String basicLine = line.substring(index, index2+7);

                    String line2 = basicLine.substring(0,basicLine.length()).replace("spike='","");
                    line2 = "\"+ ("+line2 +") +\"";

                    splitted[i] = line.replace(basicLine, line2);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return StringUtils.join(splitted, "\n");


    }

    static String createPartialEvents(String template, Boolean replaceCiapki) {

        List<String> events = Arrays.asList(
                "click",
                "change",
                "keyup",
                "keydown",
                "keypress",
                "blur",
                "focus",
                "dblclick",
                "die",
                "hover",
                "keydown",
                "mousemove",
                "mouseover",
                "mouseenter",
                "mousedown",
                "mouseleave",
                "mouseout",
                "submit",
                "trigger",
                "toggle",
                "load",
                "unload"
        );

        String eventWord = "[";
        Set<Integer> eventsIndexes = new HashSet<>();

        int index = template.indexOf(eventWord);
        while (index >= 0) {

            if (index > 0) {
                eventsIndexes.add(index);
            }

            index = template.indexOf(eventWord, index + eventWord.length());

            if (index > 0) {
                eventsIndexes.add(index);
            }

        }


        List<String> eventsList = new ArrayList<>();

        String fragment = null;
        for(Integer eindex : eventsIndexes){

            fragment = template.substring(eindex, template.length());

            String eventName = fragment.substring(0, fragment.indexOf("]") + 1);
            eventName = eventName.substring(eventName.indexOf("["), eventName.indexOf("]") + 1);

            if (!eventName.startsWith("['") && events.contains(eventName.trim().replace("[", "").replace("]", ""))) {

                eventsList.add(eventName.trim());

            }

            fragment = null;

        }

        for(String event : eventsList){

            if(replaceCiapki){
                template = template.replace(event, event.replace("[", "spike-unbinded spike-event-").replace("]", ""));
            }else{
                template = template.replace(event, event.replace("[", "spike-unbinded spike-event-").replace("]", ""));
            }

        }

        return template;

    }


    static String replaceSpikeTranslations(String template) {

        String translationWord = "spike-translation";
        Set<Integer> translationIndexes = new HashSet<>();

        int index = template.indexOf(translationWord);
        while (index >= 0) {

            if (index > 0) {
                translationIndexes.add(index);
            }

            index = template.indexOf(translationWord, index + translationWord.length());

            if (index > 0) {
                translationIndexes.add(index);
            }

        }

        String fragment = null;

                for(Integer tindex : translationIndexes){

            fragment = template.substring(tindex, template.length());

            try {

                if(fragment.contains("<") && fragment.contains(">")){

                    fragment = fragment.substring(0, fragment.indexOf("<") + 1);

                    String inElement = fragment.substring(fragment.indexOf(">"), fragment.indexOf("<")+1).trim();

                    if(inElement.length() == 2){

                        String translationContent = fragment.substring(fragment.indexOf("'")+1, fragment.length());
                        translationContent = translationContent.substring(0, translationContent.indexOf("'")).trim();

                                String replacement = fragment.replace("><", ">"+translationContent+"<");

                        template = template.replace(fragment, replacement);


                    }

                    fragment = null;

                }

            } catch (Exception e) {
                System.out.println( "Error occurred during spike-translation compiling. Probably incorrect syntax of spike-translation or around.");
                System.out.println( "Suggested fragment of code: " + fragment.replace(";", "").replace("html", "").replace("+=", "").replace("'", "").replace("\n", " "));
            }


        }



        return template;

    }

    static String escapeEvents(String line){

        try {

            line = line.replace("[[[", "'+\"'\"+");
            line = line.replace("]]]", "+\"'\"+'");

        } catch (Exception e) {
            System.out.println( "Error occurred during template compiling. Probably incorrect syntax with: [[ ]] or [[[ ]]] or @include");
            System.out.println( "Suggested fragment of code: " + line);
        }


        return line;


    }

    static String replaceSpecialCharacters(String line) {

        try {

            line = line.replace("@include", "app.partial.include");
            line = line.replace("[[[", "'+\"'\"+");
            line = line.replace("]]]", "+\"'\"+'");
            line = line.replace("[[", "'+");
            line = line.replace("]]", "+'");

        } catch (Exception e) {
            System.out.println( "Error occurred during template compiling. Probably incorrect syntax with: [[ ]] or [[[ ]]] or @include");
            System.out.println( "Suggested fragment of code: " + line);
        }



        return line;

    }


}
