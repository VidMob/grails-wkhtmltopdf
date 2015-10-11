package org.grails.plugins.wkhtmltopdf

import grails.artefact.*
import grails.artefact.controller.support.ResponseRenderer

/**
 * Inspired by the feeds plugin.
 * Replaces the render method on controllers with one that can handle pdf as well.
 */
@Enhances("Controller")
trait WkhtmltopdfRenderer extends ResponseRenderer {

    void render(Map params) {
        // if the "file" parameter is provided then we assume the user wants to render an existing file. I.e. render(file: new File(absolutePath), fileName: "book.pdf")
        if ((params.contentType?.toLowerCase() == 'application/pdf' || response.format == "pdf") && (!params.file)) {
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
            super.render(params)
        }
    }
}
