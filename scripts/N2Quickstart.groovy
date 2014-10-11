/* Copyright 2006-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import grails.util.GrailsNameUtils
import grails.util.Metadata

includeTargets << new File("$novamailPluginDir/scripts/_N2Common.groovy")

USAGE = """
Usage: grails n2-quickstart <domain-class-package> <messageOut-class-name> <mailDispatch-class-name>

Creates a user and role class (and optionally a requestmap class) in the specified package

Example: grails n2-quickstart com.yourapp MessageOut MailDispatch

"""

includeTargets << grailsScript('_GrailsBootstrap')

packageName = 'com.novadge.novamail'
messageOutClassName = 'MessageOut'
messageInClassName = 'MessageIn'
mailDispatchJobName = 'NovaMailJob'
bodyClassName = 'Body'
attachmentClassName = 'Attachment'

target(n2Quickstart: 'Creates artifacts for the Nova Mail plugin') {
	depends(checkVersion, configureProxy, packageApp, classpath)

	if (!configure()) {
		return 1
	}
	createDomains()
	copyControllersAndViews()
	updateConfig()

	printMessage """
*********************************************************
* Created grails-app/domain/MessageOut.groovy,          *
* Created grails-app/domain/MessageIn.groovy,           *
* Created grails-app/domain/Body.groovy,                *
* Created grails-app/domain/Attachment.groovy,          *
* grails-app/jobs/MailDispatchJob.groovy and            *
* grails-app/views/layouts/_mail.gsp.                   *
* Your grails-app/conf/Config.groovy has been updated   *
* with config attributes.                               *
* You may modify as needed.                             *
******************************************************* *
"""
}

private boolean configure() {

	def argValues = parseArgs()
	if (!argValues) {
		return false
	}

//	if (argValues.size() == 4) {
//		(packageName, messageOutClassName, mailDispatchJobName, requestmapClassName) = argValues
//	}
//	else {
//		(packageName, messageOutClassName, mailDispatchJobName) = argValues
//	}

	templateAttributes = [packageName: packageName,
	                      messageOutClassName: messageOutClassName,
	                      userClassProperty: GrailsNameUtils.getPropertyName(messageOutClassName),
                              messageInClassName: messageInClassName,
	                      userClassProperty: GrailsNameUtils.getPropertyName(messageInClassName),
                              bodyClassName: bodyClassName,
	                      userClassProperty: GrailsNameUtils.getPropertyName(bodyClassName),
                              attachmentClassName: attachmentClassName,
	                      userClassProperty: GrailsNameUtils.getPropertyName(attachmentClassName),
	                      mailDispatchJobName: mailDispatchJobName,
	                      mailDispatchJobClassProperty: GrailsNameUtils.getPropertyName(mailDispatchJobName)
                            ]

	if (Metadata.current.getGrailsVersion().startsWith('1.2')) {
		templateAttributes.dependencyInjections = '''\
                        transient messagingService
                        transient grailsApplication
                        transient sessionFactory
                '''
	templateAttributes.dirtyMethods = ''
	}
	else {
		templateAttributes.dependencyInjections = '''\
                        transient messagingService
                '''
		templateAttributes.dirtyMethods = ''
	}

	true
}

private void createDomains() {

	String dir = packageToDir(packageName)
	generateFile "$templateDir/MessageOut.groovy.template", "$appDir/domain/${dir}${messageOutClassName}.groovy"
        generateFile "$templateDir/MessageIn.groovy.template", "$appDir/domain/${dir}${messageInClassName}.groovy"
        generateFile "$templateDir/Body.groovy.template", "$appDir/domain/${dir}${bodyClassName}.groovy"
        generateFile "$templateDir/Attachment.groovy.template", "$appDir/domain/${dir}${attachmentClassName}.groovy"
	generateFile "$templateDir/MailDispatchJob.groovy.template", "$appDir/jobs/${dir}${mailDispatchJobName}.groovy"
	
}

private void copyControllersAndViews() {
	//ant.mkdir dir: "$appDir/views/login"
	String dir = packageToDir(packageName)
	//copyFile "$templateDir/MailDispatchJob.groovy.template", "$appDir/jobs/${dir}${mailDispatchJobName}.groovy"
	copyFile "$templateDir/mail.gsp.template", "$appDir/views/layouts/_mail.gsp"
	
}

private void updateConfig() {

	def configFile = new File(appDir, 'conf/Config.groovy')
	if (configFile.exists()) {
		configFile.withWriterAppend {
			it.writeLine '\n// Added by the Novamail plugin:'
                        it.writeLine """\n\
                                    novamail{\n\\n\
                                        hostname ='your hostname'// eg. Gmail, Yahoo, Hotmail, etc.\n\
                                        username ='your username'\n\ // eg. 'john@gmail.com' or 'John <john@gmail.com>'
                                        password ='your password'\n\\n\\n\
// Uncomment to declare your custom email host properties. \n\\n\
//                                      store="imap"
// 
//                                      hostProps = [
//                                            "Host":"imap.gmail.com",
//                                            "mail.imap.host":"imap.gmail.com",
//                                            "mail.store.protocol": "imaps",
//                                            "mail.imap.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
//                                            "mail.imap.socketFactory.fallback": "false",
//                                            "mail.imaps.partialfetch": "false",
//
//                                            "mail.smtp.starttls.enable": "true",
//                                            "mail.smtp.host": "smtp.gmail.com",
//                                            "mail.smtp.auth": "true",
//                                            "mail.smtp.socketFactory.port": "465",
//                                            "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
//                                            "mail.smtp.socketFactory.fallback": "false"
//                                        ]
                                        
                                    }
                                    
                        """
			
		}
	}
}

private parseArgs() {
	def args = [:] //argsMap.params
        
	args[0] = packageName
	args[1] = messageOutClassName
        args[2] = messageInClassName
        args[3] = bodyClassName
        args[4] = attachmentClassName
	args[5] = mailDispatchJobName
	if (6 == args.size()) {
		printMessage "Creating ${args[1]}, ${args[2]}, ${args[3]}, ${args[4]}, ${args[5]} in package ${args[0]}"
		return args
	}


	errorMessage USAGE
	null
}

setDefaultTarget 'n2Quickstart'
