package com.spike.scripts

import com.spike.Executor
import org.apache.commons.lang.StringUtils

/**
 * Created by Dawid on 2017-01-29.
 */
class ScriptsCompiler {

    def compileGStrings(String body) {

        def bracketsSingle = "\$[";
        def bracketsDouble = "\$[[";
        def singleBracketsIndexes = [];
        def doubleBracketsIndexes = [];

        def index = body.indexOf(bracketsDouble);
        while (index >= 0) {
            doubleBracketsIndexes << index;
            index = body.indexOf(bracketsDouble, index + bracketsDouble.length());
        }

        def bracketString = null
        doubleBracketsIndexes.each { bracketIndex ->

            try {

                bracketString = body.substring(bracketIndex - 1, body.length())
                bracketString = bracketString.substring(0, bracketString.indexOf(']') + 2)

                Executor.debug bracketString

                println bracketString

                body = body.replace(bracketString, "'+" + bracketString.replace('\$[[', '').replace(']]', '') + "+'")

            } catch (Exception e) {
                println 'Error occurred during GStrings compiling. Probably incorrect usage of $[[ ]] or around.'
                println 'Suggested fragment of code: ' + bracketString
            }

            bracketString = null

        }

        index = body.indexOf(bracketsSingle);
        while (index >= 0) {
            singleBracketsIndexes << index;
            index = body.indexOf(bracketsSingle, index + bracketsSingle.length());
        }

        singleBracketsIndexes.each { bracketIndex ->

            try {

                bracketString = body.substring(bracketIndex, body.length())
                bracketString = bracketString.substring(0, bracketString.indexOf(']') + 1)

                Executor.debug bracketString

                println bracketString

                body = body.replace(bracketString, '"+' + bracketString.replace('\$[', '').replace(']', '') + '+"')

            } catch (Exception e) {
                println 'Error occurred during GStrings compiling. Probably incorrect usage of $[ ] or around.'
                println 'Suggested fragment of code: ' + bracketString
            }


            bracketString = null

        }

        return body

    }


    def compileImports(String body) {

        def modules = [
                'app.controller.register',
                'app.controller.add',
                'app.component.register',
                'app.component.add',
                'app.modal.register',
                'app.modal.add',
                'app.util.register',
                'app.util.add',
                'app.service.register',
                'app.service.add',
                'app.enumerator.register',
                'app.enumerator.add',
                'app.lister.register',
                'app.lister.add',
                'app.partial.register',
                'app.partial.add',
                'app.abstract.register',
                'app.abstract.add',
        ]

        def isModule = { bodyFragment ->

            for (String module : modules) {

                if (StringUtils.countMatches(bodyFragment, module) != 0) {
                    return true;
                }

            }

            return false

        }

        def getForModule = { moduleName, toCompile ->

            def toGet = ''
            def index = toCompile.indexOf(moduleName)
            if (index > -1) {
                toGet = toCompile.substring(index, toCompile.indexOf('{'))
                toGet = toGet.replace(moduleName, '').replace('"', '').replace("'", "").replace(",", "").replace("(", "").trim();

                return moduleName.replace('register', '').replace('add', '') + toGet
            }

            return null

        }

        def getThis = { toCompile ->

            for (String moduleName : modules) {
                def x = getForModule(moduleName, toCompile)
                if (x != null) {
                    return x;
                }
            }

            return null

        }

        String output = body;

        def importWord = "'import";
        def importIndexes = [] as Set;

        int index = body.indexOf(importWord);
        while (index >= 0) {


            if (index > 0) {
                importIndexes << index;
            }

            index = body.indexOf(importWord, index + importWord.length());

            if (index > 0) {
                importIndexes << index;
            }


        }

        importIndexes = importIndexes as List

        def allImportsToReplace = [] as Set;
        def currentImports = [:];
        def afterImportSubstr;
        for (def i = 0; i < importIndexes.size(); i++) {

            def maxLeng = importIndexes[i + 1] ? importIndexes[i + 1] : body.length()
            afterImportSubstr = body.substring(importIndexes[i], maxLeng);

            def importSubstr = null

            try {
                importSubstr = afterImportSubstr.substring(0, afterImportSubstr.indexOf("';") + 1)

                def outputreplaceimport = afterImportSubstr.substring(0, afterImportSubstr.indexOf("';") + 2)
                allImportsToReplace << outputreplaceimport;

                importSubstr = importSubstr.replace("'import ", "").replace(" as ", ",").replace("'", "");
                currentImports[importSubstr.split(',')[0].trim()] = importSubstr.split(',')[1].trim();

                if (StringUtils.countMatches(afterImportSubstr, "'import") == 1 && isModule(afterImportSubstr)) {

                    def toCompile = afterImportSubstr.substring(afterImportSubstr.indexOf("';") + 2, afterImportSubstr.length());
                    toCompile = toCompile.substring(0, toCompile.indexOf('SPIKE_IMPORT_END'))

                    Executor.debug 'TO COMPILE +++++++ with imports: ' + currentImports
                    Executor.debug toCompile

                    def _this = getThis(toCompile)

                    def selfThis = false
                    if (_this.contains('app.abstract')) {

                        selfThis = true
                        currentImports['$super'] = '___super'

                    } else {
                        currentImports['$this'] = getThis(toCompile)
                    }


                    def compiled = toCompile

                    currentImports.each { dependencyName, dependencyFullPacakge ->
                        Executor.debug 'dependencyName: ' + dependencyName
                        compiled = compiled.replace(dependencyName, dependencyFullPacakge)
                    }

                    if (selfThis) {

                      //  compiled = compileSuper(compiled)

                    }

                    Executor.debug 'COMPILED +++++++ with imports: ' + currentImports
                    Executor.debug compiled


                    output = output.replace(toCompile, compiled.trim())

                    currentImports = [:]

                }

            } catch (Exception e) {
                println 'Error occurred during imports compiling. Probably incorrect declaration of import or around.'
                println 'Suggested fragment of code: ' + importSubstr ? importSubstr : afterImportSubstr
            }

        }



        allImportsToReplace.each {
            output = output.replace(it, '/** ' + it + ' **/');
        }

        output = output.replace('SPIKE_IMPORT_END', '/**SPIKE_IMPORT_END**/')


        return output

    }

    def compileSuper(compiled) {

        try {

            def replacement = '\n var ___super = this;'

            def functionWords = [": function", ":function"];
            def functionIndexes = [] as Set;

            try {

                functionWords.each { functionWord ->

                    int functionIndex = compiled.indexOf(functionWord);
                    while (functionIndex >= 0) {


                        if (functionIndex > 0) {
                            functionIndexes << functionIndex;
                        }

                        functionIndex = compiled.indexOf(functionWord, functionIndex + functionWord.length());

                        if (functionIndex > 0) {
                            functionIndexes << functionIndex;
                        }


                    }

                }

            } catch (Exception e) {
                println 'Error occurred during $super word compiling. Probably incorrect usage of $super or around.'
                println 'No suggested fragment of code, fail on preparation'
            }

            def fragment = null
            functionIndexes.each { index ->

                try {

                    fragment = compiled.substring(index, compiled.length())
                    fragment = fragment.substring(fragment.indexOf('{') + 1, fragment.length())

                    compiled = compiled.replace(fragment, replacement + ' ' + fragment)

                } catch (Exception e) {
                    println 'Error occurred during $super word compiling. Probably incorrect usage of $super or around.'
                    println 'Suggested fragment of code: ' + fragment.replace(';', '').replace('html', '').replace('+=', '').replace("'", '').replace('\n', ' ')
                }

                fragment = null

            }

        } catch (Exception e) {
            println 'Error occurred during compiling $super word. Analyse your $super usages.'
        }


        return compiled;


    }


}
