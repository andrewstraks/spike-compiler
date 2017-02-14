package com.spike.compiler

import org.apache.commons.lang.StringUtils

/**
 * Created by Dawid on 2017-01-29.
 */
class TemplateCompiler {

    def compileGStrings(String body){

        def bracketsSingle = "\${";
        def bracketsDouble = "\${{";
        def singleBracketsIndexes = [];
        def doubleBracketsIndexes = [];

        def index = body.indexOf(bracketsDouble);
        while (index >= 0) {
            doubleBracketsIndexes << index;
            index = body.indexOf(bracketsDouble, index + bracketsDouble.length());
        }

        doubleBracketsIndexes.each { bracketIndex ->

            def bracketString = body.substring(bracketIndex-1, body.length())
            bracketString = bracketString.substring(0, bracketString.indexOf('}')+2)

            Executor.debug bracketString

            body = body.replace(bracketString, "'+"+bracketString.replace('\${{','').replace('}}','')+"+'")

        }

        index = body.indexOf(bracketsSingle);
        while (index >= 0) {
            singleBracketsIndexes << index;
            index = body.indexOf(bracketsSingle, index + bracketsSingle.length());
        }

        singleBracketsIndexes.each { bracketIndex ->

            def bracketString = body.substring(bracketIndex, body.length())
            bracketString = bracketString.substring(0, bracketString.indexOf('}')+1)

            Executor.debug bracketString

            body = body.replace(bracketString, '"+'+bracketString.replace('\${','').replace('}','')+'+"')

        }


//
//        var gstring = 'Person name: ${person.name}';
//        gstring += ", surname: ${person.surname}}";
//        gstring = ', nick: "{{person.nick}}" ';

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
        ]

        def isModule = { bodyFragment ->

            for(String module : modules){

                if(StringUtils.countMatches(bodyFragment, module) != 0){
                    return true;
                }

            }

            return false

        }

//        def getIndexOfNewModuleRegisterOrAdd = { def bodyFragment ->
//
//            def concatSign = "SPIKE_IMPORT_END";
//
//            def index1 = bodyFragment.indexOf(concatSign)
//
//            if(index1 > -1){
//                return index1
//            }
//
//            return bodyFragment.length()
//
//        }

        def getForModule = { moduleName, toCompile ->

            def toGet = ''
            def index = toCompile.indexOf(moduleName)
            if(index > -1){
                toGet = toCompile.substring(index, toCompile.indexOf('{'))
                toGet = toGet.replace(moduleName,'').replace('"','').replace("'","").replace(",","").replace("(","").trim();

                return moduleName.replace('register','').replace('add','')+toGet
            }

            return null

        }

        def getThis = { toCompile ->

            for(String moduleName : modules){
                def x = getForModule(moduleName, toCompile)
                if(x != null){
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


            if(index > 0){
                importIndexes << index;
            }

            index = body.indexOf(importWord, index + importWord.length());

            if(index > 0){
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

            def importSubstr = afterImportSubstr.substring(0, afterImportSubstr.indexOf("';") + 1)

            def outputreplaceimport = afterImportSubstr.substring(0, afterImportSubstr.indexOf("';")+2)
            allImportsToReplace << outputreplaceimport;

            importSubstr = importSubstr.replace("'import ", "").replace(" as ", ",").replace("'", "");
            currentImports[importSubstr.split(',')[0].trim()] = importSubstr.split(',')[1].trim();

            if(StringUtils.countMatches(afterImportSubstr, "'import") == 1 && isModule(afterImportSubstr)){

                def toCompile = afterImportSubstr.substring(afterImportSubstr.indexOf("';")+2, afterImportSubstr.length());
                toCompile = toCompile.substring(0, toCompile.indexOf('SPIKE_IMPORT_END'))

                Executor.debug 'TO COMPILE +++++++ with imports: '+currentImports
                Executor.debug toCompile

                currentImports['$this'] = getThis(toCompile)

                def compiled = toCompile

                currentImports.each { dependencyName, dependencyFullPacakge ->
                    Executor.debug 'dependencyName: '+dependencyName
                    compiled = compiled.replace(dependencyName, dependencyFullPacakge)
                }

                Executor.debug 'COMPILED +++++++ with imports: '+currentImports
                Executor.debug compiled

                output = output.replace(toCompile, compiled.trim())

                currentImports = [:]

            }


        }



        allImportsToReplace.each {
            output = output.replace(it, '/** '+it+' **/');
        }

        output = output.replace('SPIKE_IMPORT_END', '/**SPIKE_IMPORT_END**/')


        return output

    }


}
