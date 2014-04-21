package com.novadge.novamail



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional//(readOnly = true)
class MessageInController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond MessageIn.list(params), model:[messageInCount: MessageIn.count()]
    }

    def show(MessageIn messageIn) {
        messageIn.status = "Read"
        messageIn.save(flush:true)
        respond messageIn
    }

    def create() {
        respond new MessageIn(params)
    }

    @Transactional
    def save(MessageIn messageIn) {
        if (messageIn == null) {
            notFound()
            return
        }

        if (messageIn.hasErrors()) {
            respond messageIn.errors, view:'create'
            return
        }

        messageIn.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'messageIn.label', default: 'MessageIn'), messageIn.id])
                redirect messageIn
            }
            '*' { respond messageIn, [status: CREATED] }
        }
    }

    def edit(MessageIn messageIn) {
        respond messageIn
    }

    @Transactional
    def update(MessageIn messageIn) {
        if (messageIn == null) {
            notFound()
            return
        }

        if (messageIn.hasErrors()) {
            respond messageIn.errors, view:'edit'
            return
        }

        messageIn.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'MessageIn.label', default: 'MessageIn'), messageIn.id])
                redirect messageIn
            }
            '*'{ respond messageIn, [status: OK] }
        }
    }

    @Transactional
    def delete(MessageIn messageIn) {

        if (messageIn == null) {
            notFound()
            return
        }

        messageIn.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'MessageIn.label', default: 'MessageIn'), messageIn.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'messageIn.label', default: 'MessageIn'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
