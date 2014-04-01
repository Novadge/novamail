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
mgsOutClassName = 'MessageOut'
mailDispatchJobName = 'NovaMailJob'
requestmapClassName = ''

target(n2Quickstart: 'Creates artifacts for the Nova Mail plugin') {
	depends(checkVersion, configureProxy, packageApp, classpath)

	if (!configure()) {
		return 1
	}
	createDomains()
	copyControllersAndViews()
	updateConfig()

	printMessage """
*******************************************************
* Created domain classes, controllers, and GSPs. Your *
* grails-app/conf/Config.groovy has been updated with *
* the class names of the configured domain classes;   *
* please verify that the values are correct.          *
*******************************************************
"""
}

private boolean configure() {

	def argValues = parseArgs()
	if (!argValues) {
		return false
	}

	if (argValues.size() == 4) {
		(packageName, mgsOutClassName, mailDispatchJobName, requestmapClassName) = argValues
	}
	else {
		(packageName, mgsOutClassName, mailDispatchJobName) = argValues
	}

	templateAttributes = [packageName: packageName,
	                      mgsOutClassName: mgsOutClassName,
	                      userClassProperty: GrailsNameUtils.getPropertyName(mgsOutClassName),
	                      mailDispatchJobName: mailDispatchJobName,
	                      roleClassProperty: GrailsNameUtils.getPropertyName(mailDispatchJobName),
	                      requestmapClassName: requestmapClassName]

	if (Metadata.current.getGrailsVersion().startsWith('1.2')) {
		templateAttributes.dependencyInjections = '''\
	transient springSecurityService
	transient grailsApplication
	transient sessionFactory
'''
	templateAttributes.dirtyMethods = '''

	private boolean isDirty(String fieldName) {
		def session = sessionFactory.currentSession
		def entry = findEntityEntry(session)
		if (!entry) {
			return false
		}

		Object[] values = entry.persister.getPropertyValues(this, session.entityMode)
		int[] dirtyProperties = entry.persister.findDirty(values, entry.loadedState, this, session)
		int fieldIndex = entry.persister.propertyNames.findIndexOf { fieldName == it }
		return fieldIndex in dirtyProperties
	}

	private findEntityEntry(session) {
		def entry = session.persistenceContext.getEntry(this)
		if (!entry) {
			return null
		}

		if (!entry.requiresDirtyCheck(this) && entry.loadedState) {
			return null
		}

		entry
	}'''
	}
	else {
		templateAttributes.dependencyInjections = '''\
	transient springSecurityService
'''
		templateAttributes.dirtyMethods = ''
	}

	true
}

private void createDomains() {

	String dir = packageToDir(packageName)
	generateFile "$templateDir/MessageOut.groovy.template", "$appDir/domain/${dir}${mgsOutClassName}.groovy"
	//generateFile "$templateDir/MailDispatch.groovy.template", "$appDir/domain/${dir}${mailDispatchJobName}.groovy"
	
}

private void copyControllersAndViews() {
	//ant.mkdir dir: "$appDir/views/login"
	String dir = packageToDir(packageName)
	copyFile "$templateDir/MailDispatchJob.groovy.template", "$appDir/jobs/${dir}${mailDispatchJobName}.groovy"
	copyFile "$templateDir/mail.gsp.template", "$appDir/views/layouts/_mail.gsp"
	
}

private void updateConfig() {

	def configFile = new File(appDir, 'conf/Config.groovy')
	if (configFile.exists()) {
		configFile.withWriterAppend {
			it.writeLine '\n// Added by the Novamail plugin:'
                        it.writeLine "grails.plugins.novamail.hostname = 'your email host name eg. Gmail'"
			it.writeLine "grails.plugins.novamail.username = 'your username here eg. john@hotmail.com'"
			it.writeLine "grails.plugins.novamail.password = 'your password here'"
			
		}
	}
}

private parseArgs() {
	def args = [:] //argsMap.params
	args[0] = 'com.novadge.novamail'
	args[1] = 'MessageOut'
	args[2] = 'MailDispatchJob'
	if (3 == args.size()) {
		printMessage "Creating ${args[1]}, ${args[2]} in package ${args[0]}"
		return args
	}


	errorMessage USAGE
	null
}

setDefaultTarget 'n2Quickstart'
