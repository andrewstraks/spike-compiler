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

        Integer index = body.indexOf(bracketsDouble);
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


    Boolean isModuleCompileImports(String[] modules, String bodyFragment) {

        for (String module : modules) {

            if (StringUtils.countMatches(bodyFragment, module) != 0) {
                return true;
            }

        }

        return false;

    }

    String getForModuleCompileImports(String moduleName, String toCompile) {

        String toGet = "";
        Integer index = toCompile.indexOf(moduleName);

        if (index > -1) {
            toGet = toCompile.substring(index, toCompile.indexOf("{"));
            toGet = toGet.replace(moduleName, "").replace("'", "").replace("'", "").replace(",", "").replace("(", "").trim();

            return moduleName.replace("register", "").replace("add", "") + toGet;
        }

        return null;

    }

    String getThisCompileImports(String[] modules, String toCompile) {

        for (String moduleName : modules) {
            String x = getForModuleCompileImports(moduleName, toCompile);
            if (x != null) {
                return x;
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
        Set<Integer> importIndexes = new HashSet<Integer>();

        int index = body.indexOf(importWord);
        while (index >= 0) {


            if (index > 0) {
                importIndexes.add(index);
            }

            index = body.indexOf(importWord, index + importWord.length());

            if (index > 0) {
                importIndexes.add(index);
            }


        }


        Integer[] importIndexesArray = importIndexes.toArray(new Integer[importIndexes.size()]);

        Set<String> allImportsToReplace = new HashSet<>();

        Map<String, String> currentImports = new HashMap<>();

        String afterImportSubstr = "";

        for (int i = 0; i < importIndexesArray.length; i++) {

            Integer maxLeng = body.length();

            try {
                maxLeng = importIndexesArray[i + 1];
            } catch (Exception e) {
            }

            afterImportSubstr = body.substring(importIndexesArray[i], maxLeng);

            String importSubstr = null;

            try {
                importSubstr = afterImportSubstr.substring(0, afterImportSubstr.indexOf("'; ") + 1);

                String outputreplaceimport = afterImportSubstr.substring(0, afterImportSubstr.indexOf("'; ") + 2);
                allImportsToReplace.add(outputreplaceimport);

                importSubstr = importSubstr.replace("'import", " ").replace(" as ", ", ").replace(" '", "");
                currentImports.put(importSubstr.split(",")[0].trim(), importSubstr.split(",")[1].trim());

                if (StringUtils.countMatches(afterImportSubstr, "'import") == 1 && isModuleCompileImports(modules, afterImportSubstr)) {

                    String toCompile = afterImportSubstr.substring(afterImportSubstr.indexOf("'") + 2, afterImportSubstr.length());
                    toCompile = toCompile.substring(0, toCompile.indexOf("SPIKE_IMPORT_END"));

                    String _this = getThisCompileImports(modules, toCompile);

                    Boolean selfThis = false;
                    if (_this.contains("app.abstract")) {
                        selfThis = true;
                        //super
                    } else {
                        currentImports.put("$this", getThisCompileImports(modules, toCompile));
                    }


                    String compiled = toCompile;

                    for (Map.Entry<String, String> entry : currentImports.entrySet()) {
                        String dependencyName = entry.getKey();
                        String dependencyFullPacakge = entry.getValue();

                        compiled = compiled.replace(dependencyName, dependencyFullPacakge);

                    }


                    if (selfThis) {
                        //      compiled = compileSuper(compiled)
                    }


                    output = output.replace(toCompile, compiled.trim());

                    currentImports = new HashMap<>();

                }

            } catch (Exception e) {
                System.out.println("Error occurred during imports compiling. Probably incorrect declaration of import or around.");
                System.out.println("Suggested fragment of code: " + importSubstr == null ? importSubstr : afterImportSubstr);
            }


        }


        for (String it : allImportsToReplace) {
            output = output.replace(it, "/** " + it + " **/");
        }

        output = output.replace("SPIKE_IMPORT_END", "/**SPIKE_IMPORT_END**/");


        return output;

    }


}
