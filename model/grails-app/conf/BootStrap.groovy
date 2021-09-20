import br.ufscar.sead.loa.propeller.Propeller

import grails.util.Holders

class BootStrap {

    def init = { servletContext ->

        println "Initializing Propeller"

        Propeller.instance.init([dbHost  : Holders.grailsApplication.config.dataSource.dbHost,
                                 dbName  : 'remar-propeller', wipeDb: false,
                                 username: Holders.grailsApplication.config.dataSource.username,
                                 authDb  : 'admin',
                                 password: Holders.grailsApplication.config.dataSource.password])
    }
    def destroy = {
    }
}
