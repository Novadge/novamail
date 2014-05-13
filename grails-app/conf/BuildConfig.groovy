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
       // runtime 'mysql:mysql-connector-java:5.1.27'
        compile 'javax.mail:javax.mail-api:1.5.1'
        runtime 'com.sun.mail:javax.mail:1.5.1'
    }

    plugins {
        build ':release:3.0.1', ':rest-client-builder:1.0.3', {
            export = false
        }
//        build ":tomcat:7.0.52.1"
//
//        // plugins for the compile step
//        compile ":scaffolding:2.0.3"
//        compile ':cache:1.1.2'
//
//        // plugins needed at runtime but not for compilation
//        runtime ":hibernate:3.6.10.13" // or ":hibernate4:4.3.5.1"
//        runtime ":database-migration:1.4.0"
//        runtime ":jquery:1.11.0.2"
//        runtime ":resources:1.2.7"
   
    }
    
}
