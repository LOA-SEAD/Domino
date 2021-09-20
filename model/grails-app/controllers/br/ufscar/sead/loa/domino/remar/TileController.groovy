package br.ufscar.sead.loa.domino.remar

import br.ufscar.sead.loa.propeller.Propeller
import br.ufscar.sead.loa.propeller.domain.TaskInstance
import br.ufscar.sead.loa.remar.api.MongoHelper

import grails.transaction.Transactional
import grails.util.Environment
import org.springframework.security.access.annotation.Secured
import static org.springframework.http.HttpStatus.*
import java.nio.file.Files

@Secured(['isAuthenticated()'])
class TileController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def beforeInterceptor = [action: this.&check, only: ['index']]

    def springSecurityService

    private check() {
        if (springSecurityService.isLoggedIn())
            session.user = springSecurityService.currentUser
        else {
            log.debug "Logout: session.user is NULL !"
            session.user = null
            redirect controller: "login", action: "index"

            return false
        }
    }

    def index(Integer max) {
        if (params.t) {
            session.taskId = params.t
        }

        def userId = springSecurityService.getCurrentUser().getId()

        def list = Tile.findAllByOwnerId(userId)

        if (list.size() == 0) {

            String[] descriptions = ["Fração 1/2", "Fração 1/3", "Fração 1/4", "Fração 1/5",
                                     "Fração 2/3", "Fração 2/5", "Fração 3/5"];

            for (int i = 0; i < descriptions.length; i++) {

                Tile tile = new Tile(description: descriptions[i], ownerId: session.user.id, taskId: session.taskId);
                tile.save flush: true

                def samplesPath = servletContext.getRealPath("/samples")
                File samplesFolder = new File(samplesPath)
                File srcFolder = new File(samplesFolder, String.valueOf(i+1));

                def dataPath = servletContext.getRealPath("/data")
                def destFolder = new File(dataPath, "/" + userId + "/tiles/" + tile.getId())
                destFolder.mkdirs()

                for (int j = 0; j < 7; j++) {
                    String fileName = j + ".png";

                    def srcFile = new File(srcFolder, fileName)
                    def destFile = new File(destFolder, fileName)
                    println srcFile.getAbsolutePath() + " => " + destFile.getAbsolutePath()
                    Files.copy(srcFile.toPath(), destFile.toPath())
                }
            }
        }

        render view: "index", model: [tileInstanceList: Tile.findAllByOwnerId(userId)]
    }

    def show(Tile tileInstance) {
        respond tileInstance
    }

    def create() {
        respond new Tile(params)
    }

    @Transactional
    def save(Tile tileInstance) {


        if (tileInstance == null) {
            notFound()
            return
        }

        if (tileInstance.hasErrors()) {
            respond tileInstance.errors, view: 'create'
            return
        }

        tileInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'tile.label', default: 'Tile'), tileInstance.id])
                redirect tileInstance
            }
            '*' { respond tileInstance, [status: CREATED] }
        }
    }

    def edit(Tile tileInstance) {
        respond tileInstance
    }

    @Transactional
    def update(Tile tileInstance) {
        if (tileInstance == null) {
            notFound()
            return
        }

        if (tileInstance.hasErrors()) {
            respond tileInstance.errors, view: 'edit'
            return
        }

        tileInstance.taskId = session.taskId as String

        tileInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Tile.label', default: 'Tile'), tileInstance.id])
                redirect tileInstance
            }
            '*' { respond tileInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Tile tileInstance) {

        if (tileInstance == null) {
            notFound()
            return
        }

        tileInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Tile.label', default: 'Tile'), tileInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'tile.label', default: 'Tile'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    def ShowImage() {
        def images = Tile.get(params.id)
        byte[] image = images.icone

        response.contentType = "image/png"
        response.outputStream << image
        response.outputStream.flush()
        return

    }


    @Transactional
    def ImagesManager() { // TODO: fix var names + optimize
        def userId = springSecurityService.getCurrentUser().getId()
        def description = request.getParameter("description")
        def tile = new Tile(ownerId: userId, taskId: session.taskId, description: description).save flush: true

        def dataPath = servletContext.getRealPath("/data")
        def userPath = new File(dataPath, "/" + userId + "/tiles/" + tile.getId())
        userPath.mkdirs()

        def uploaded = []
        def files = []

        for (int i = 0; i < 7; i++) {
            uploaded.add(request.getFile("p-" + (i + 1)))
        }

        boolean ok = true;

        for (int i = 0; ok & i < 7; i++) {
            ok = ok && !uploaded[i].isEmpty()
        }

        if (ok) {

            for (int i = 0; i < 7; i++) {
                files.add(new File("$userPath/" + i + ".png"))
            }

            for (int i = 0; i < 7; i++) {
                uploaded[i].transferTo(files[i])
            }
        }

        redirect(controller: "tile", action: "index")

    }

    def choose() {

        def userId = springSecurityService.getCurrentUser().getId()
        def userPath = servletContext.getRealPath("/data/" + userId.toString())
        def destFolder = new File(userPath, "${session.taskId}")
        destFolder.mkdirs()

        // Criação do arquivo title.txt

        File titleFile = new File("$destFolder/title.txt");
        def fw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(titleFile), "UTF-8"))
        TaskInstance task = Propeller.instance.getTaskInstance(session.taskId, userId)
        fw.write(task.getProcess().getName())
        fw.close();
        def fileId = MongoHelper.putFile(titleFile.absolutePath)
        def files = "?files=${fileId}"

        // Criação do arquivo room_code.txt

        File roomCodeFile = new File("$destFolder/room_code.txt");
        fw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(roomCodeFile), "UTF-8"))
        fw.write("[" + session.taskId + "]")
        fw.close();

        fileId = MongoHelper.putFile(roomCodeFile.absolutePath)
        files += "&files=${fileId}"

        for (int i = 0; i < params.ids.length; i++) {
            def id = params.ids[i];
            def srcFolder = servletContext.getRealPath("/data/${Tile.get(id).ownerId}/tiles/${id}")

            // Criação dos arquivos description_0.txt .. description_6.txt

            File descriptionFile = new File(destFolder, "description_" + i + ".txt")
            fw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(descriptionFile), "UTF-8"))
            fw.write(Tile.get(id).description)
            fw.close();

            fileId = MongoHelper.putFile(descriptionFile.absolutePath)
            files += "&files=${fileId}"

            // Cópia das imagens

            for (int j = 0; j < 7; j++) {
                def srcFile = new File(srcFolder, "${j}.png")
                def fileName = "Image" + i + j + ".png"
                def destFile = new File(destFolder, fileName)
                Files.copy(srcFile.toPath(), destFile.toPath())
                fileId = MongoHelper.putFile(destFile.absolutePath)
                files += "&files=${fileId}"
            }
        }

        def port = request.serverPort
        if (Environment.current == Environment.DEVELOPMENT) {
            port = 8080
        }

        redirect uri: "http://${request.serverName}:${port}/process/task/complete/${session.taskId}${files}"
    }
}
