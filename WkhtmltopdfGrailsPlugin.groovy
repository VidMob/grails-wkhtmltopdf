class WkhtmltopdfGrailsPlugin {
    def version = "0.1.9"
    def grailsVersion = "2.0 > *"
    def loadAfter = ['mail', 'controllers']
    def author = "Tobias Nendel"
    def authorEmail = "tobias.nendel@scubical.com"
    def title = "Wkhtmltopdf Plugin"
    def description = 'Provides a Wrapper for wkhtmltopdf, a simple shell utility to convert HTML to PDF using the WebKit rendering engine and Qt'
    def observe = ['controllers']
    def documentation = "http://grails.org/plugin/wkhtmltopdf"
    def license = "APACHE"
    def issueManagement = [system: 'Github', url: 'https://github.com/rlovtangen/grails-wkhtmltopdf/issues']
    def scm = [url: 'https://github.com/rlovtangen/grails-wkhtmltopdf']

    def doWithDynamicMethods = {ctx ->
        // hooking into render method
        application.controllerClasses.each { controllerClass ->
            replaceRenderMethod(controllerClass, ctx)
        }
    }

    def onChange = { event ->
        // only process controller classes
        if (application.isControllerClass(event.source)) {
            def clazz = application.getControllerClass(event.source.name)
            replaceRenderMethod(clazz, event.ctx)
        }
    }

    /**
     * This implementation is based on Marc Palmer's feed plugin. It hooks into the render method
     * of a Grails controller class and adds an alternative behaviour for the mime type
     * 'text/calendar' used by the iCalendar plugin.
     */
    private void replaceRenderMethod(controllerClass, ctx) {
        def oldRender = controllerClass.metaClass.pickMethod("render", [Map] as Class[])
        controllerClass.metaClass.render = { Map params ->
            if (params.contentType?.toLowerCase() == 'application/pdf' || response.format == "pdf") {
                def filename = params.remove("filename")

                def data = ctx.wkhtmltoxService.makePdf(params)

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
