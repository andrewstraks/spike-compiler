package com.spike.templates

/**
 * Created by Dawid on 2017-01-29.
 */
class TemplatesCompiler {

    def templatesGlobalDeclaration = '_spike_templates'

    def getTemplatesDeclaration() {
        return '; window["'+templatesGlobalDeclaration+'"] = {} \n'
    }

    def getFileName(File templateFile){
        return templateFile.getPath().replaceAll('\\\\','/')
    }

    def compile(File templateFile){

        String output = null;

        if(templateFile.getName().contains('partial.html')){

            output = '; window["'+templatesGlobalDeclaration+'"]["'+this.getFileName(templateFile)+'"] = function($local) { \n'
            output += '; var html = "" \n'

            def lines = templateFile.readLines()
            def importsReplacements = [:]

            lines.each { String line ->

                if(line.contains('<!--')){
                }else if(line.startsWith("'import")){

                    def key = line.replace("'",'').substring(0, line.indexOf('as')-1).replace('import','').trim()
                    def value = line.substring(line.indexOf('as')+2, line.length()).replace("'",'').replace(";",'').trim()
                    importsReplacements[key] = value;

                }else if(line.trim().startsWith('#')){
                    output += '; '+line.replace('#','')+' \n'
                }else {
                    output += "; html += '"+this.replaceSpecialCharacters(line)+"' \n"
                }

            }

            output += '; return html \n'
            output += '; } \n'

            importsReplacements.each { key, val ->
                output = output.replace(key.trim(), val.trim())
            }

        }else {

            output = '; window["'+templatesGlobalDeclaration+'"]["'+this.getFileName(templateFile)+'"] = '

            def lines = templateFile.readLines()

            lines.each { String line ->

                if(line.contains('<!--')) {
                }else {
                    output += ' "'+line.replace('"','\\"')+'" + \n'
                }

            }

            output = output.substring(0, output.lastIndexOf('+'))

            output += '; \n'



        }

        output = replaceSpikeTranslations(output)

        return output

    }

    def replaceSpikeTranslations(String template){


        def translationWord = "spike-translation";
        def translationIndexes = [] as Set;

        int index = template.indexOf(translationWord);
        while (index >= 0) {

            if(index > 0){
                translationIndexes << index;
            }

            index = template.indexOf(translationWord, index + translationWord.length());

            if(index > 0){
                translationWord << index;
            }

        }

        translationIndexes.each { def tindex ->

            def fragment = template.substring(tindex, template.length())
            fragment = fragment.substring(0, fragment.indexOf('<')+1)

            println fragment

            def between = fragment.substring(fragment.indexOf('>'), fragment.indexOf('<')+1);

            if(between.replace('<', '').replace('>','').trim().length() == 0){

                def translation = fragment.substring(fragment.indexOf('spike-translation='), fragment.length())
                translation = translation.substring(0, translation.lastIndexOf('"')+1)
                translation = translation.replace('spike-translation=', '')

                if(translation.startsWith('\\')){
                    translation = translation.substring(2, translation.length())
                    translation = translation.substring(0, translation.indexOf('\\"'))+translation.substring(translation.indexOf('\\"')+2, translation.length())
                }else {
                    translation = translation.substring(1, translation.length())
                    translation = translation.substring(0, translation.indexOf('"'))+translation.substring(translation.indexOf('"')+1, translation.length())
                }

                if(translation.contains("'") || translation.contains(";")){
                    translation = translation.substring(0, translation.indexOf("'"))
                }

               def fragmentToReplace = fragment.replace(between, '>' + translation + '<');

                template = template.replace(fragment, fragmentToReplace)

            }


        }

        return template

    }

    def replaceSpecialCharacters(String line){

        line = line.replace('@include', 'app.partial.include')
        line = line.replace('[[[', "'+\"'\"+")
        line = line.replace(']]]', "+\"'\"+'")
        line = line.replace("[[", "'+")
        line = line.replace(']]',"+'")

        return line

    }


}
