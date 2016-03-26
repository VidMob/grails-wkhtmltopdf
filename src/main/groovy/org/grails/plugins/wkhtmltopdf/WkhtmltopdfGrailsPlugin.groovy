package org.grails.plugins.wkhtmltopdf

import grails.plugins.*

class WkhtmltopdfGrailsPlugin extends Plugin {

    def grailsVersion = "3.0.3 > *"
    def pluginExcludes = []
    def observe = ['controllers']
    def loadAfter = ['mail', 'controllers']
    def title = "Wkhtmltopdf"
    def author = "Tobias Nendel"
    def authorEmail = "tobias.nendel@scubical.com"
    def description = 'Provides a Wrapper for wkhtmltopdf, a command line tool to render HTML into PDF'
    def profiles = ['web']
    def documentation = "https://github.com/rlovtangen/grails-wkhtmltopdf"
    def license = "APACHE"
    def developers = [[name: "Ronny Løvtangen", email: "ronny@lovtangen.com"]]
    def issueManagement = [system: 'Github', url: 'https://github.com/rlovtangen/grails-wkhtmltopdf/issues']
    def scm = [url: 'https://github.com/rlovtangen/grails-wkhtmltopdf']

}
