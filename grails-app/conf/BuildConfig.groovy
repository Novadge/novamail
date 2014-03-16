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
        compile 'javax.mail:javax.mail-api:1.5.1'
        runtime 'com.sun.mail:javax.mail:1.5.1'
    }

    plugins {
        build ':release:release:3.0.1', ':rest-client-builder:1.0.3', {
            export = false
        }

        compile ':quartz:1.0.1'
    }
}
