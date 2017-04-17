package com.spike.cli;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Dawid on 2017-04-02.
 */
public class CliCreator {

    public static void createComponent(String distPath, String componentName) throws IOException {

        String componentNameFromUpper = componentName.substring(0, 1).toUpperCase() + componentName.substring(1, componentName.length());
        String componentNameFromLower = componentName.substring(0, 1).toLowerCase() + componentName.substring(1, componentName.length());
        String className = componentNameFromLower + "Component";

        String componentJS = "";

        componentJS += "'import $this as app.component. "+componentNameFromUpper+"';\n";
        componentJS += "\n";
        componentJS += "app.component.register('"+componentNameFromUpper+"', {\n";
        componentJS += "\n";
        componentJS += "    init: function(params) {\n";
        componentJS += "\n";
        componentJS += "    }\n";
        componentJS += "\n";
        componentJS += "});\n";

        File file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + componentNameFromLower + "/");
        file.mkdirs();

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + componentNameFromLower + "/" + componentNameFromLower + ".component.js");
        FileUtils.writeStringToFile(file, componentJS, "UTF-8");

        String componentHTML = "";

        componentHTML += "'import $this as app.component. "+componentNameFromUpper+"';\n";
        componentHTML += "\n";
        componentHTML += "<div class=\" "+className+"\">\n";
        componentHTML += "\n";
        componentHTML += "</div>\n";

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + componentNameFromLower + "/" + componentNameFromLower + ".view.html");
        FileUtils.writeStringToFile(file, componentHTML, "UTF-8");

        String componentCSS = "";

        componentCSS += "." + className + " { \n";
        componentCSS += "\n";
        componentCSS += "}\n";

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + componentNameFromLower + "/" + componentNameFromLower + ".scss");
        FileUtils.writeStringToFile(file, componentCSS, "UTF-8");

    }

    public static void createController(String distPath, String controllerName) throws IOException {

        String controllerNameFromUpper = controllerName.substring(0, 1).toUpperCase() + controllerName.substring(1, controllerName.length());
        String controllerNameFromLower = controllerName.substring(0, 1).toLowerCase() + controllerName.substring(1, controllerName.length());
        String className = controllerNameFromLower + "Controller";

        String controllerJS = "";

        controllerJS += "'import $this as app.controller. "+controllerNameFromUpper+"';\n";
        controllerJS += "\n";
        controllerJS += "app.controller.register('"+controllerNameFromUpper+"', {\n";
        controllerJS += "\n";
        controllerJS += "    init: function(params) {\n";
        controllerJS += "\n";
        controllerJS += "    }\n";
        controllerJS += "\n";
        controllerJS += "});\n";

        File file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + controllerNameFromLower + "/");
        file.mkdirs();

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + controllerNameFromLower + "/" + controllerNameFromLower + ".controller.js");
        FileUtils.writeStringToFile(file, controllerJS, "UTF-8");

        String controllerHTML = "";

        controllerHTML += "'import $this as app.controller. "+controllerNameFromUpper+"';\n";
        controllerHTML += "\n";
        controllerHTML += "<div class=\""+className+"\">\n";
        controllerHTML += "\n";
        controllerHTML += "</div>\n";

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + controllerNameFromLower + "/" + controllerNameFromLower + ".view.html");
        FileUtils.writeStringToFile(file, controllerHTML, "UTF-8");

        String controllerCSS = "";

        controllerCSS += "." + className + " { \n";
        controllerCSS += "\n";
        controllerCSS += "}\n";

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + controllerNameFromLower + "/" + controllerNameFromLower + ".scss");
        FileUtils.writeStringToFile(file, controllerCSS, "UTF-8");

    }

    public static void createPartial(String distPath, String partialName) throws IOException {

        String partialNameFromUpper = partialName.substring(0, 1).toUpperCase() + partialName.substring(1, partialName.length());
        String partialNameFromLower = partialName.substring(0, 1).toLowerCase() + partialName.substring(1, partialName.length());
        String className = partialNameFromLower + "Partial";

        String partialJS = "";;

        partialJS += "'import $this as app.partial. "+partialNameFromUpper+"';\n";
        partialJS += "\n";
        partialJS += "app.partial.register('"+partialNameFromUpper+"', {\n";
        partialJS += "\n";
        partialJS += "    replace: true,\n";
        partialJS += "\n";
        partialJS += "    before: function(model) {\n";
        partialJS += "\n";
        partialJS += "    },\n";
        partialJS += "\n";
        partialJS += "    after: function(model, partialSelector) {\n";
        partialJS += "\n";
        partialJS += "    }\n";
        partialJS += "\n";
        partialJS += "});\n";

        File file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + partialNameFromLower + "/");
        file.mkdirs();

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + partialNameFromLower + "/" + partialNameFromLower + ".partial.js");
        FileUtils.writeStringToFile(file, partialJS, "UTF-8");

        String partialHTML = "";

        partialHTML += "'import $this as app.partial. "+partialNameFromUpper+"';\n";
        partialHTML += "\n";
        partialHTML += "<div class=\""+className+"\">\n";
        partialHTML += "\n";
        partialHTML += "</div>\n";

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + partialNameFromLower + "/" + partialNameFromLower + ".partial.html");
        FileUtils.writeStringToFile(file, partialHTML, "UTF-8");

        String partialCSS = "";

        partialCSS += "." + className + " { \n";
        partialCSS += "\n";
        partialCSS += "}\n";

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + partialNameFromLower + "/" + partialNameFromLower + ".scss");
        FileUtils.writeStringToFile(file, partialCSS, "UTF-8");

    }

    public static void createModal(String distPath, String modalName) throws IOException {

        String modalNameFromUpper = modalName.substring(0, 1).toUpperCase() + modalName.substring(1, modalName.length());
        String modalNameFromLower = modalName.substring(0, 1).toLowerCase() + modalName.substring(1, modalName.length());
        String className = modalNameFromLower + "Modal";

        String modalJS = "";

        modalJS += "'import $this as app.modal. "+modalNameFromUpper+"';\n";
        modalJS += "\n";
        modalJS += "app.modal.register('"+modalNameFromUpper+"', {\n";
        modalJS += "\n";
        modalJS += "    init: function(params) {\n";
        modalJS += "\n";
        modalJS += "    }\n";
        modalJS += "\n";
        modalJS += "});\n";

        File file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + modalNameFromLower + "/");
        file.mkdirs();

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + modalNameFromLower + "/" + modalNameFromLower + ".modal.js");
        FileUtils.writeStringToFile(file, modalJS, "UTF-8");

        String modalHTML = "";

        modalHTML += "'import $this as app.modal. "+modalNameFromUpper+"';\n";
        modalHTML += "\n";
        modalHTML += "<div class=\""+className+"\">\n";
        modalHTML += "\n";
        modalHTML += "</div>\n";

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + modalNameFromLower + "/" + modalNameFromLower + ".view.html");
        FileUtils.writeStringToFile(file, modalHTML, "UTF-8");

        String modalCSS = "";

        modalCSS += "." + className + " { \n";
        modalCSS += "\n";
        modalCSS += "}\n";

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + modalNameFromLower + "/" + modalNameFromLower + ".scss");
        FileUtils.writeStringToFile(file, modalCSS, "UTF-8");

    }

    public static void createUtil(String distPath, String utilName) throws IOException {

        String utilNameFromUpper = utilName.substring(0, 1).toUpperCase() + utilName.substring(1, utilName.length());
        String utilNameFromLower = utilName.substring(0, 1).toLowerCase() + utilName.substring(1, utilName.length());

        String utilJS = "";

        utilJS += "'import $this as app.util. " + utilNameFromUpper + "';\n";
        utilJS += "\n";
        utilJS += "app.util.register('" + utilNameFromUpper + "', {\n";
        utilJS += "\n";
        utilJS += "});\n";

        File file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + utilNameFromLower + "/");
        file.mkdirs();

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + utilNameFromLower + "/" + utilNameFromLower + ".util.js");
        FileUtils.writeStringToFile(file, utilJS, "UTF-8");

    }

    public static void createService(String distPath, String serviceName) throws IOException {

        String serviceNameFromUpper = serviceName.substring(0, 1).toUpperCase() + serviceName.substring(1, serviceName.length());
        String serviceNameFromLower = serviceName.substring(0, 1).toLowerCase() + serviceName.substring(1, serviceName.length());

        String serviceJS = "";

        serviceJS += "'import $this as app.service. " + serviceNameFromUpper + "';\n";
        serviceJS += "\n";
        serviceJS += "app.service.register('" + serviceNameFromUpper + "', {\n";
        serviceJS += "\n";
        serviceJS += "});\n";

        File file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + serviceNameFromLower + "/");
        file.mkdirs();

        file = new File((distPath.endsWith("/") ? distPath : distPath + "/") + serviceNameFromLower + "/" + serviceNameFromLower + ".service.js");
        FileUtils.writeStringToFile(file, serviceJS, "UTF-8");

    }

}
