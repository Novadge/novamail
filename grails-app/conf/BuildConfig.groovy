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
        runtime 'mysql:mysql-connector-java:5.1.27'
        compile 'javax.mail:javax.mail-api:1.5.1'
        runtime 'com.sun.mail:javax.mail:1.5.1'
    }

    plugins {
        build ':release:3.0.1', ':rest-client-builder:1.0.3', {
            export = false
        }
//       
//       
//        // plugins for the compile step -- remember to remove
        build ":tomcat:7.0.50.1"
        compile ":scaffolding:2.0.3"
        compile ':cache:1.1.1'
//        
//        // plugins needed at runtime but not for compilation
        runtime ":hibernate:3.6.10.8" // or ":hibernate4:4.3.1.1"
        runtime ":database-migration:1.3.8"
//        
        runtime ":resources:1.2.1"
    }
}
