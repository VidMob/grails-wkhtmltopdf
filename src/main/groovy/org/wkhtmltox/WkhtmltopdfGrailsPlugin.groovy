package org.wkhtmltox

import grails.plugins.*

class WkhtmltopdfGrailsPlugin extends Plugin {

    def grailsVersion = "3.0.0 > *"
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    def observe = ['controllers']
    def loadAfter = ['mail', 'controllers']
    def title = "Wkhtmltopdf"
    def author = "Tobias Nendel"
    def authorEmail = "tobias.nendel@scubical.com"
    def description = 'Provides a Wrapper for wkhtmltopdf, a simple shell utility to convert HTML to PDF using the WebKit rendering engine and Qt'
    def profiles = ['web']
    def documentation = "http://grails.org/plugin/wkhtmltopdf"
    def license = "APACHE"
    def developers = [[name: "Ronny Løvtangen", email: "ronny@lovtangen.com" ]]
    def issueManagement = [system: 'Github', url: 'https://github.com/rlovtangen/grails-wkhtmltopdf/issues']
    def scm = [url: 'https://github.com/rlovtangen/grails-wkhtmltopdf']

    void doWithDynamicMethods() {
        // hooking into render method
        grailsApplication.controllerClasses.each { controllerClass ->
//            replaceRenderMethod(controllerClass)
            addRenderPdfMethod(controllerClass)
        }
    }

    void onChange(Map<String, Object> event) {
        // only process controller classes
        if (grailsApplication.isControllerClass(event.source)) {
            def clazz = grailsApplication.getControllerClass(event.source.name)
//            replaceRenderMethod(clazz, event)
            addRenderPdfMethod(clazz)
        }
    }

    /**
     * This implementation is based on Marc Palmer's feed plugin. It hooks into the render method
     * of a Grails controller class and adds an alternative behaviour for the mime type
     * 'text/calendar' used by the iCalendar plugin.
     */
    private void replaceRenderMethod(controllerClass) {
        def oldRender = controllerClass.metaClass.pickMethod("render", [Map] as Class[])
        // TODO find out how to replace render method now that the method is added by a trait (grails.artefact.controller.support.ResponseRenderer)
        controllerClass.metaClass.render = { Map params ->
            if (params.contentType?.toLowerCase() == 'application/pdf' || response.format == "pdf") {
                def filename = params.remove("filename")

                def data = applicationContext.wkhtmltoxService.makePdf(params)

                response.setHeader("Content-disposition", "attachment; filename=${filename}")
                response.contentType = "application/pdf"
                response.outputStream.write(data)
                response.characterEncoding = 'UTF-8'
                response.setHeader('Cache-Control', 'no-store, no-cache, must-revalidate') //HTTP/1.1
                response.setHeader('Pragma', 'no-cache') // HTTP/1.0
                response.outputStream.flush()

            } else {
                // Defer to original render method
                oldRender.invoke(delegate, [params] as Object[])
            }
        }
    }

    /**
     * Add renderPdf method as an alternative to replacing render method until we find out how to do that.
     * replaceRenderMethod above doesn't work as is in Grails 3, probably because render method now is added by a trait.
     */
    private void addRenderPdfMethod(controllerClass) {
        def oldRender = controllerClass.metaClass.pickMethod("render", [Map] as Class[])
        controllerClass.metaClass.renderPdf = { Map params ->
            if (params.contentType?.toLowerCase() == 'application/pdf' || response.format == "pdf") {
                def filename = params.remove("filename")

                def data = applicationContext.wkhtmltoxService.makePdf(params)

                response.setHeader("Content-disposition", "attachment; filename=${filename}")
                response.contentType = "application/pdf"
                response.outputStream.write(data)
                response.characterEncoding = 'UTF-8'
                response.setHeader('Cache-Control', 'no-store, no-cache, must-revalidate') //HTTP/1.1
                response.setHeader('Pragma', 'no-cache') // HTTP/1.0
                response.outputStream.flush()

            } else {
                // Defer to original render method
                oldRender.invoke(delegate, [params] as Object[])
            }
        }
    }

}
