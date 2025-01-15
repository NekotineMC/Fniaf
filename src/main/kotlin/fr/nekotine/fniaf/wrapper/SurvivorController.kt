package fr.nekotine.fniaf.wrapper

abstract class SurvivorController {

    var survivor: Survivor? = null
        set(value) {
            field = value
            if (survivor != null && survivor?.controller != this){
                survivor!!.controller = this
            }
        }

    abstract fun kill()

    abstract fun tick()

    abstract fun onGameStart()

    abstract fun onGameStop()

}