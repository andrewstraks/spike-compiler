package com.spike.cli

/**
 * Created by Dawid on 2017-04-02.
 */
class CliCreator {

    def static createComponent(String distPath, String componentName){

        def componentNameFromUpper = componentName.substring(0,1).toUpperCase()+componentName.substring(1,componentName.length())
        def componentNameFromLower = componentName.substring(0,1).toLowerCase()+componentName.substring(1,componentName.length())
        def className = componentNameFromLower+'Component'

        def componentJS = "";

        componentJS += "'import \$this as app.component."+componentNameFromUpper+"';\n"
        componentJS += "\n"
        componentJS += "app.component.register('"+componentNameFromUpper+"', {\n"
        componentJS += "\n"
        componentJS += "    init: function(params) {\n"
        componentJS += "\n"
        componentJS += "    }\n"
        componentJS += "\n"
        componentJS += "});\n"

        def file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +componentNameFromLower+'/')
        file.mkdirs()

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +componentNameFromLower+'/'+componentNameFromLower+'.component.js')
        file.write(componentJS)

        def componentHTML = "";

        componentHTML += "'import \$this as app.component."+componentNameFromUpper+"';\n"
        componentHTML += "\n"
        componentHTML += '<div class="'+className+'">\n'
        componentHTML += "\n"
        componentHTML += "</div>\n"

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +componentNameFromLower+'/'+componentNameFromLower+'.view.html')
        file.write(componentHTML)

        def componentCSS = "";

        componentCSS += '.'+className+' { \n'
        componentCSS += "\n"
        componentCSS += "}\n"

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +componentNameFromLower+'/'+componentNameFromLower+'.scss')
        file.write(componentCSS)

    }

    def static createController(String distPath, String controllerName){

        def controllerNameFromUpper = controllerName.substring(0,1).toUpperCase()+controllerName.substring(1,controllerName.length())
        def controllerNameFromLower = controllerName.substring(0,1).toLowerCase()+controllerName.substring(1,controllerName.length())
        def className = controllerNameFromLower+'Controller'

        def controllerJS = "";

        controllerJS += "'import \$this as app.controller."+controllerNameFromUpper+"';\n"
        controllerJS += "\n"
        controllerJS += "app.controller.register('"+controllerNameFromUpper+"', {\n"
        controllerJS += "\n"
        controllerJS += "    init: function(params) {\n"
        controllerJS += "\n"
        controllerJS += "    }\n"
        controllerJS += "\n"
        controllerJS += "});\n"

        def file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +controllerNameFromLower+'/')
        file.mkdirs()

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +controllerNameFromLower+'/'+controllerNameFromLower+'.controller.js')
        file.write(controllerJS)

        def controllerHTML = "";

        controllerHTML += "'import \$this as app.controller."+controllerNameFromUpper+"';\n"
        controllerHTML += "\n"
        controllerHTML += '<div class="'+className+'">\n'
        controllerHTML += "\n"
        controllerHTML += "</div>\n"

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +controllerNameFromLower+'/'+controllerNameFromLower+'.view.html')
        file.write(controllerHTML)

        def controllerCSS = "";

        controllerCSS += '.'+className+' { \n'
        controllerCSS += "\n"
        controllerCSS += "}\n"

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +controllerNameFromLower+'/'+controllerNameFromLower+'.scss')
        file.write(controllerCSS)

    }

    def static createPartial(String distPath, String partialName){

        def partialNameFromUpper = partialName.substring(0,1).toUpperCase()+partialName.substring(1,partialName.length())
        def partialNameFromLower = partialName.substring(0,1).toLowerCase()+partialName.substring(1,partialName.length())
        def className = partialNameFromLower+'Partial'

        def partialJS = "";

        partialJS += "'import \$this as app.partial."+partialNameFromUpper+"';\n"
        partialJS += "\n"
        partialJS += "app.partial.register('"+partialNameFromUpper+"', {\n"
        partialJS += "\n"
        partialJS += "    replace: true,\n"
        partialJS += "\n"
        partialJS += "    before: function(model) {\n"
        partialJS += "\n"
        partialJS += "    },\n"
        partialJS += "\n"
        partialJS += "    after: function(model, partialSelector) {\n"
        partialJS += "\n"
        partialJS += "    }\n"
        partialJS += "\n"
        partialJS += "});\n"

        def file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +partialNameFromLower+'/')
        file.mkdirs()

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +partialNameFromLower+'/'+partialNameFromLower+'.partial.js')
        file.write(partialJS)

        def partialHTML = "";

        partialHTML += "'import \$this as app.partial."+partialNameFromUpper+"';\n"
        partialHTML += "\n"
        partialHTML += '<div class="'+className+'">\n'
        partialHTML += "\n"
        partialHTML += "</div>\n"

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +partialNameFromLower+'/'+partialNameFromLower+'.partial.html')
        file.write(partialHTML)

        def partialCSS = "";

        partialCSS += '.'+className+' { \n'
        partialCSS += "\n"
        partialCSS += "}\n"

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +partialNameFromLower+'/'+partialNameFromLower+'.scss')
        file.write(partialCSS)

    }

    def static createModal(String distPath, String modalName){

        def modalNameFromUpper = modalName.substring(0,1).toUpperCase()+modalName.substring(1,modalName.length())
        def modalNameFromLower = modalName.substring(0,1).toLowerCase()+modalName.substring(1,modalName.length())
        def className = modalNameFromLower+'Modal'

        def modalJS = "";

        modalJS += "'import \$this as app.modal."+modalNameFromUpper+"';\n"
        modalJS += "\n"
        modalJS += "app.modal.register('"+modalNameFromUpper+"', {\n"
        modalJS += "\n"
        modalJS += "    init: function(params) {\n"
        modalJS += "\n"
        modalJS += "    }\n"
        modalJS += "\n"
        modalJS += "});\n"

        def file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +modalNameFromLower+'/')
        file.mkdirs()

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +modalNameFromLower+'/'+modalNameFromLower+'.modal.js')
        file.write(modalJS)

        def modalHTML = "";

        modalHTML += "'import \$this as app.modal."+modalNameFromUpper+"';\n"
        modalHTML += "\n"
        modalHTML += '<div class="'+className+'">\n'
        modalHTML += "\n"
        modalHTML += "</div>\n"

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +modalNameFromLower+'/'+modalNameFromLower+'.view.html')
        file.write(modalHTML)

        def modalCSS = "";

        modalCSS += '.'+className+' { \n'
        modalCSS += "\n"
        modalCSS += "}\n"

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +modalNameFromLower+'/'+modalNameFromLower+'.scss')
        file.write(modalCSS)

    }

    def static createUtil(String distPath, String utilName) {

        def utilNameFromUpper = utilName.substring(0, 1).toUpperCase() + utilName.substring(1, utilName.length())
        def utilNameFromLower = utilName.substring(0, 1).toLowerCase() + utilName.substring(1, utilName.length())

        def utilJS = "";

        utilJS += "'import \$this as app.util." + utilNameFromUpper + "';\n"
        utilJS += "\n"
        utilJS += "app.util.register('" + utilNameFromUpper + "', {\n"
        utilJS += "\n"
        utilJS += "});\n"

        def file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +utilNameFromLower+'/')
        file.mkdirs()

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +utilNameFromLower+'/'+utilNameFromLower+'.util.js')
        file.write(utilJS)

    }

    def static createService(String distPath, String serviceName) {

        def serviceNameFromUpper = serviceName.substring(0, 1).toUpperCase() + serviceName.substring(1, serviceName.length())
        def serviceNameFromLower = serviceName.substring(0, 1).toLowerCase() + serviceName.substring(1, serviceName.length())

        def serviceJS = "";

        serviceJS += "'import \$this as app.service." + serviceNameFromUpper + "';\n"
        serviceJS += "\n"
        serviceJS += "app.service.register('" + serviceNameFromUpper + "', {\n"
        serviceJS += "\n"
        serviceJS += "});\n"

        def file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +serviceNameFromLower+'/')
        file.mkdirs()

        file = new File((distPath.endsWith('/') ? distPath : distPath+'/') +serviceNameFromLower+'/'+serviceNameFromLower+'.service.js')
        file.write(serviceJS)

    }
    
}
