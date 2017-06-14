/**
 * Created by omasiri on 6/14/17.
 */
Map hostProps = [
        'host':'imap.gmail.com',
        "mail.imap.host":'imap.gmail.com',
        "mail.store.protocol": 'imaps',
        "mail.imap.socketFactory.class": 'javax.net.ssl.SSLSocketFactory',
        "mail.imap.socketFactory.fallback": 'false',
        "mail.imaps.partialfetch":'false',
        "mail.mime.address.strict": 'false',

        "mail.smtp.starttls.enable": 'true',
        "mail.smtp.host": 'smtp.gmail.com',
        "mail.smtp.auth": 'true',
        "mail.smtp.socketFactory.port": '465',
        "mail.smtp.socketFactory.class": 'javax.net.ssl.SSLSocketFactory',
        "mail.smtp.socketFactory.fallback": 'false'
]
// Added by the Novamail plugin:
novamail{
    hostname=""
    username='$(NOVAMAIL_USERNAME)'
    password='$(NOVAMAIL_PASSWORD)'

    hostProps = hostProps

}