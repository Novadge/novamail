grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
    }

    plugins {
        build ':release:release:3.0.1', ':rest-client-builder:1.0.3', {
            export = false
        }

        compile ':quartz:1.0.1'
    }
}
