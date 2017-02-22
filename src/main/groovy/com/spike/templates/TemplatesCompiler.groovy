package com.spike.templates

import java.util.concurrent.ThreadLocalRandom

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

        if (templateFile.getName().contains('partial.html')) {

            output = '; window["' + templatesGlobalDeclaration + '"]["' + this.getFileName(templateFile) + '"] = function($local) { \n'
            output += '; var html = "" \n'

            def lines = templateFile.readLines()
            def importsReplacements = [:]

            lines.each { String line ->

                if (line.contains('<!--')) {
                } else if (line.startsWith("'import")) {

                    def key = line.replace("'", '').substring(0, line.indexOf('as') - 1).replace('import', '').trim()
                    def value = line.substring(line.indexOf('as') + 2, line.length()).replace("'", '').replace(";", '').trim()
                    importsReplacements[key] = value;

                } else if (line.trim().startsWith('#')) {
                    output += '; ' + line.replace('#', '') + ' \n'
                } else {
                    output += "; html += '" + this.replaceSpecialCharacters(line) + "' \n"
                }

            }

            output += '; return html \n'
            output += '; } \n'

            importsReplacements.each { key, val ->
                output = output.replace(key.trim(), val.trim())
            }

            output = createPartialEvents(output)

        } else {

            output = '; window["' + templatesGlobalDeclaration + '"]["' + this.getFileName(templateFile) + '"] = '

            def lines = templateFile.readLines()

            lines.each { String line ->

                if (line.contains('<!--')) {
                } else {
                    output += ' "' + line.replace('"', '\\"') + '" + \n'
                }

            }

            output = output.substring(0, output.lastIndexOf('+'))

            output += '; \n'


        }

        output = replaceSpikeTranslations(output)

        return output

    }


    def createUniqueHash(){

        def d = 'ABCDEFGHIJKLMNOPRSTUWYZ1234567890abcdefghijklmnoprstuwxyz'
        def hash = ''

        for(def i = 0; i < 6; i++){
            int randIndex = ThreadLocalRandom.current().nextInt(0, d.length() -1);
            hash += d.split("")[randIndex]
        }

        return hash

    }

    def createPartialEvents(String template){

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

            if(!eventName.startsWith('["') && events.contains(eventName)){

                eventsList << eventName.trim()

            }

            fragment = null

        }

        eventsList.each { event ->

            template = template.replace(event, event.replace('[','spike-event="'+event.replace('[','').replace(']','')+'" spike-event-').replace(']',''))

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

                fragment = fragment.substring(0, fragment.indexOf('<') + 1)

                def between = fragment.substring(fragment.indexOf('>'), fragment.indexOf('<') + 1);

                if (between.replace('<', '').replace('>', '').trim().length() == 0) {

                    def translation = fragment.substring(fragment.indexOf('spike-translation='), fragment.length())
                    translation = translation.substring(0, translation.lastIndexOf('"') + 1)
                    translation = translation.replace('spike-translation=', '')

                    if (translation.startsWith('\\')) {
                        translation = translation.substring(2, translation.length())
                        translation = translation.substring(0, translation.indexOf('\\"')) + translation.substring(translation.indexOf('\\"') + 2, translation.length())
                    } else {
                        translation = translation.substring(1, translation.length())
                        translation = translation.substring(0, translation.indexOf('"')) + translation.substring(translation.indexOf('"') + 1, translation.length())
                    }

                    if (translation.contains("'") || translation.contains(";")) {
                        translation = translation.substring(0, translation.indexOf("'"))
                    }

                    def fragmentToReplace = fragment.replace(between, '>' + translation + '<');

                    template = template.replace(fragment, fragmentToReplace)

                }



                fragment = null

            } catch (Exception e) {
                println 'Error occurred during spike-translation compiling. Probably incorrect syntax of spike-translation or around.'
                println 'Suggested fragment of code: '+fragment.replace(';','').replace('html','').replace('+=','').replace("'",'').replace('\n',' ')
            }


        }



        return template

    }

    def replaceSpecialCharacters(String line) {

        try {

            line = line.replace('@include', 'app.partial.include')
            line = line.replace('[[[', "'+\"'\"+")
            line = line.replace(']]]', "+\"'\"+'")
            line = line.replace("[[", "'+")
            line = line.replace(']]', "+'")

        }catch (Exception e){
            println 'Error occurred during template compiling. Probably incorrect syntax with: [[ ]] or [[[ ]]] or @include'
            println 'Suggested fragment of code: '+line
        }



        return line

    }


}
