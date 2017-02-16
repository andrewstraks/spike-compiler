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

        return output

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
