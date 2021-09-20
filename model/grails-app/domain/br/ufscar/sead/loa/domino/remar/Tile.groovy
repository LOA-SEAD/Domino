package br.ufscar.sead.loa.domino.remar

class Tile {

    long ownerId
    String taskId
    String description

    static constraints = {
        ownerId blank: false, nullable: false
        taskId nullable: true
    }
}
