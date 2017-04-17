package com.spike.templates
/**
 * Created by Dawid on 2017-01-29.
 */
class TemplatesCompiler {

    def templatesGlobalDeclaration = '_spike_templates'

    def getTemplatesDeclaration() {
        return '; window["' + templatesGlobalDeclaration + '"] = {} \n'
    }

    def getFileName(File templateFile) {
        return templateFile.getPath().replaceAll('\\\\', '/')
    }

    def compile(File templateFile) {

        String output = null;
        def isPartial = templateFile.getName().contains('partial.html')
        def isTemplate = templateFile.getName().contains('template.html')

        output = '; window["' + templatesGlobalDeclaration + '"]["' + this.getFileName(templateFile) + '"] = function($local) { \n'
        output += '; var html = "" \n'

        def lines = templateFile.readLines()

        if (lines.size() == 0) {
            output += ' " ' + templateFile.getText("UTF-8") + ' " ';
            println 'Warning: Template file is almost empty ' + templateFile.getName()
        } else if (isTemplate) {

            def templateName = this.getFileName(templateFile)
            templateName = templateName.substring(templateName.lastIndexOf('/')+1, templateName.length())
            templateName = templateName.substring(0, templateName.indexOf('.'))


            output = '; window["' + templatesGlobalDeclaration + '"]["' + '@template/'+templateName + '"] = '

            def importsReplacements = [:]

            lines.each { String line ->

                if (line.contains('<!--')) {
                }else  if (line.startsWith("'import")) {
                    def key = line.replace("'", '').substring(0, line.indexOf('as') - 1).replace('import', '').trim()
                    def value = line.substring(line.indexOf('as') + 2, line.length()).replace("'", '').replace(";", '').trim()
                    importsReplacements[key] = value;
                } else {
                    line =  replaceSpikeTranslations(line)
                    output += ' "' + this.escapeEvents(line.replace('"', '\\"')) + '" + \n'
                }

            }

            output = output.substring(0, output.lastIndexOf('+'))

            output += '; \n'

            importsReplacements.each { key, val ->
                output = output.replace(key.trim(), val.trim())
            }

            output = createPartialEvents(output, true)


        } else {

            def importsReplacements = [:]

            lines.each { String line ->

                if (line.contains('<!--')) {
                } else if (line.startsWith("'import")) {

                    def key = line.replace("'", '').substring(0, line.indexOf('as') - 1).replace('import', '').trim()
                    def value = line.substring(line.indexOf('as') + 2, line.length()).replace("'", '').replace(";", '').trim()
                    importsReplacements[key] = value;

                } else if (line.trim().startsWith('#')) {

                    output += (line.endsWith(',') || line.endsWith('{') || line.endsWith('}') ? ' ' : '; ') + line.replace('#', '') + ' \n'
                } else {

                    line =  replaceSpikeTranslations(line);
                    if (isPartial) {

                       // line = createPartialSpecialAttributes(line)

                        output += "; html += '" + this.replaceSpecialCharacters(line) + "' \n"
                    } else {
                        output += "; html += '" + this.escapeEvents(line) + "' \n"
                    }

                }

            }

            output += '; return html \n'
            output += '; } \n'

            importsReplacements.each { key, val ->
                output = output.replace(key.trim(), val.trim())
            }

            output = createPartialEvents(output, false)

        }

        return output

    }

    def createPartialSpecialAttributes(String str){

        def splitted = str.split('\n')

        for(int i = 0; i < splitted.length; i++){

            def line = splitted[i]

            def index = line.indexOf('spike="');

            try {
                if(index > -1){
                    def index2 = line.replace('spike="','').indexOf('"')

                    println index
                    println index2
                    println line
                    def basicLine = line.substring(index, index2+7)

                    println basicLine

                    def line2 = basicLine.substring(0,basicLine.length()).replace('spike="','')
                    line2 = '\'+ ('+line2 +') +\''

                    splitted[i] = line.replace(basicLine, line2)
                }
            }catch (Exception e){
                e.printStackTrace()
            }



        }



        return  splitted.join('\n')

    }

    def createPartialEvents(String template, def replaceCiapki) {

        def events = [
                'click',
                'change',
                'keyup',
                'keydown',
                'keypress',
                'blur',
                'focus',
                'dblclick',
                'die',
                'hover',
                'keydown',
                'mousemove',
                'mouseover',
                'mouseenter',
                'mousedown',
                'mouseleave',
                'mouseout',
                'submit',
                'trigger',
                'toggle',
                'load',
                'unload'
        ];

        def eventWord = "[";
        def eventsIndexes = [] as Set;

        int index = template.indexOf(eventWord);
        while (index >= 0) {

            if (index > 0) {
                eventsIndexes << index;
            }

            index = template.indexOf(eventWord, index + eventWord.length());

            if (index > 0) {
                eventsIndexes << index;
            }

        }


        def eventsList = []
        def fragment = null
        eventsIndexes.each { def eindex ->

            fragment = template.substring(eindex, template.length())

            def eventName = fragment.substring(0, fragment.indexOf(']') + 1)
            eventName = eventName.substring(eventName.indexOf('['), eventName.indexOf(']') + 1);

            if (!eventName.startsWith('["') && events.contains(eventName.trim().replace('[', '').replace(']', ''))) {

                eventsList << eventName.trim()

            }

            fragment = null

        }

        eventsList.each { event ->

            if(replaceCiapki){
                template = template.replace(event, event.replace('[', 'spike-unbinded spike-event-').replace(']', ''))
            }else{
                template = template.replace(event, event.replace('[', 'spike-unbinded spike-event-').replace(']', ''))
            }

        }

        return template

    }


    def replaceSpikeTranslations(String template) {

        def translationWord = "spike-translation";
        def translationIndexes = [] as Set;

        int index = template.indexOf(translationWord);
        while (index >= 0) {

            if (index > 0) {
                translationIndexes << index;
            }

            index = template.indexOf(translationWord, index + translationWord.length());

            if (index > 0) {
                translationIndexes << index;
            }

        }

        def fragment = null

        translationIndexes.each { def tindex ->

            fragment = template.substring(tindex, template.length())

            try {

                if(fragment.contains('<') && fragment.contains('>')){

                    fragment = fragment.substring(0, fragment.indexOf('<') + 1)

                    def inElement = fragment.substring(fragment.indexOf(">"), fragment.indexOf("<")+1).trim()

                    if(inElement.length() == 2){

                        def translationContent = fragment.substring(fragment.indexOf('"')+1, fragment.length())
                        translationContent = translationContent.substring(0, translationContent.indexOf('"')).trim()

                        def replacement = fragment.replace('><', '>'+translationContent+'<')

//                    println fragment
//                    println 'translationContent: '+translationContent
//                    println 'inElement: '+inElement
//                    println 'replacement: '+replacement
//                    println '\n*************************'

                        template = template.replace(fragment, replacement)


                    }

                    fragment = null

                }

            } catch (Exception e) {
                e.printStackTrace()
                println 'Error occurred during spike-translation compiling. Probably incorrect syntax of spike-translation or around.'
                println 'Suggested fragment of code: ' + fragment.replace(';', '').replace('html', '').replace('+=', '').replace("'", '').replace('\n', ' ')
            }


        }



        return template

    }

    def escapeEvents(String line){

        try {

            line = line.replace('[[[', "'+\"'\"+")
            line = line.replace(']]]', "+\"'\"+'")

        } catch (Exception e) {
            println 'Error occurred during template compiling. Probably incorrect syntax with: [[ ]] or [[[ ]]] or @include'
            println 'Suggested fragment of code: ' + line
        }



        return line


    }

    def replaceSpecialCharacters(String line) {

        try {

            line = line.replace('@include', 'app.partial.include')
            line = line.replace('[[[', "'+\"'\"+")
            line = line.replace(']]]', "+\"'\"+'")
            line = line.replace("[[", "'+")
            line = line.replace(']]', "+'")

        } catch (Exception e) {
            println 'Error occurred during template compiling. Probably incorrect syntax with: [[ ]] or [[[ ]]] or @include'
            println 'Suggested fragment of code: ' + line
        }



        return line

    }


}
