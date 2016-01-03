class NovamailGrailsPlugin {
    // the plugin version
    def version = "0.3.9"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.4 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Novamail Plugin" // Headline display name of the plugin
    def author = "Omasirichukwu Udeinya"
    def authorEmail = ""
    def description = '''\
The Novamail plug-in provides e-mail sending and retrieving capabilities to a \n\
Grails application. It is also capable of sending emails asynchronously by using a scheduled Job.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/novamail"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [name: 'Novadge', url: 'http://www.novadge.com/']

    // Any additional developers beyond the author specified above.
    def developers = [
        [name: 'Omasirichukwu Udeinya', email: 'omasiri@novadge.com']
    ]

    // Location of the plugin's issue tracker.
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/Omasiri/novamail/issues']

    // Online location of the plugin's browseable source code.
    def scm = [url: 'https://github.com/Omasiri/novamail']

    
}
