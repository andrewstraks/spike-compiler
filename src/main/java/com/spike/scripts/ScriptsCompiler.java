package com.spike.scripts;

import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Created by Dawid on 2017-01-29.
 */
public class ScriptsCompiler {

    public String compileGStrings(String body) {

        String bracketsSingle = "$[";
        String bracketsDouble = "$[[";
        List<Integer> singleBracketsIndexes = new ArrayList<>();
        List<Integer> doubleBracketsIndexes = new ArrayList<>();

        int index = body.indexOf(bracketsDouble);
        while (index >= 0) {
            doubleBracketsIndexes.add(index);
            index = body.indexOf(bracketsDouble, index + bracketsDouble.length());
        }

        String bracketString = null;

        for (Integer bracketIndex : doubleBracketsIndexes) {

            try {

                bracketString = body.substring(bracketIndex - 1, body.length());
                bracketString = bracketString.substring(0, bracketString.indexOf("]") + 2);

                body = body.replace(bracketString, "'+" + bracketString.replace("$[[", "").replace("]]", "") + "+'");

            } catch (Exception e) {
                System.out.println("Error occurred during GStrings compiling. Probably incorrect usage of $[[ ]] or around.");
                System.out.println("Suggested fragment of code: " + bracketString);
            }

            bracketString = null;

        }

        index = body.indexOf(bracketsSingle);
        while (index >= 0) {
            singleBracketsIndexes.add(index);
            index = body.indexOf(bracketsSingle, index + bracketsSingle.length());
        }

        for (Integer bracketIndex : singleBracketsIndexes) {

            try {

                bracketString = body.substring(bracketIndex, body.length());
                bracketString = bracketString.substring(0, bracketString.indexOf("]") + 1);

                body = body.replace(bracketString, "'+" + bracketString.replace("$[", "").replace("]", "") + "+'");

            } catch (Exception e) {
                System.out.println("Error occurred during GStrings compiling. Probably incorrect usage of $[ ] or around.");
                System.out.println("Suggested fragment of code: " + bracketString);
            }


            bracketString = null;

        }

        return body;

    }


    String getModuleCompileImports(String[] modules, String bodyFragment) {

        for (String module : modules) {

            if (StringUtils.countMatches(bodyFragment, module) != 0) {
                return module;
            }

        }

        return null;

    }

    public String compileImports(String body) {

        String[] modules = new String[]{
                "app.controller.register",
                "app.controller.add",
                "app.component.register",
                "app.component.add",
                "app.modal.register",
                "app.modal.add",
                "app.util.register",
                "app.util.add",
                "app.service.register",
                "app.service.add",
                "app.enumerator.register",
                "app.enumerator.add",
                "app.lister.register",
                "app.lister.add",
                "app.partial.register",
                "app.partial.add",
                "app.abstract.register",
                "app.abstract.add",
        };


        String output = body;

        String importWord = "'import";
        String[] splittedIntoFiles = body.split("SPIKE_IMPORT_END");

        for (int i = 0; i < splittedIntoFiles.length; i++) {

            String fileContent = splittedIntoFiles[i];

            int importStartIndex = fileContent.indexOf(importWord);
            int indexEnd = -1;
            for (int k = 0; k < modules.length; k++) {

                String moduleStarting = getModuleCompileImports(modules, fileContent);

                if (moduleStarting != null) {
                    indexEnd = fileContent.indexOf(moduleStarting);
                }

            }

            if (indexEnd > 0 && importStartIndex >= 0) {

                String moduleDeclaration = fileContent.substring(indexEnd, fileContent.length());
                moduleDeclaration = moduleDeclaration.substring(0, moduleDeclaration.indexOf("{"));
                moduleDeclaration = moduleDeclaration.replace("register","").replace(",","").replace("add","").replace("\"","").replace("'","").replace("(","").trim();

                String importHeading = fileContent.substring(importStartIndex, indexEnd);
                String importHeadingOriginal = importHeading;

                HashMap<String, String> imports = new HashMap<>();
                imports.put("$this", moduleDeclaration);

                importHeading = importHeading.replace("'import", "").replace(" as ", ",").replace("';", ",").replace("\n", "");

                String[] importsStr = importHeading.split(",");


                for (int o = 0; o < importsStr.length; o++) {

                    if (o < importsStr.length - 1) {

                        if (importsStr[o + 1].trim().length() > 0 && importsStr[o].contains("$")) {
                            imports.put(importsStr[o].trim(), importsStr[o + 1].trim());
                        }

                    }

                }

                String compiled = fileContent.replace(importHeadingOriginal, "/**"+importHeadingOriginal.replace("*","")+"**/");

                List<String> listOfSorted = new ArrayList<>();

                for (Map.Entry<String, String> entry : imports.entrySet()) {
                    listOfSorted.add((String) entry.getKey());
                }

                String[] arraySortedKeys = (String[]) listOfSorted.toArray(new String[listOfSorted.size()]);

                Arrays.sort(arraySortedKeys, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.length() > o2.length() ? -1 : 1;
                    }
                });

                for(int l = 0; l < arraySortedKeys.length; l++){

                    compiled = compiled.replace(arraySortedKeys[l], imports.get(arraySortedKeys[l]));
                }

                output = output.replace(fileContent, compiled);

            }

        }


        output = output.replace("SPIKE_IMPORT_END", "/**SPIKE_IMPORT_END**/");


        return output;

    }


}
