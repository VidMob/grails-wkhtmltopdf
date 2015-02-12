grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()

		mavenLocal()
		mavenCentral()
	}

	plugins {
		build(':release:2.2.1', ':rest-client-builder:1.0.3') {
			export = false
		}

		runtime ':mail:1.0.7'
		compile ':plugin-config:0.2.0'
	}
}
