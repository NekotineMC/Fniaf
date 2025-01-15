package fr.nekotine.fniaf.wrapper

abstract class AnimatronicController {

    var animatronic: Animatronic? = null
        set(value) {
            field = value
            if (animatronic != null && animatronic?.controller != this){
                animatronic!!.controller = this
            }
        }

    abstract fun tick()

    abstract fun onGameStart()

    abstract fun onGameStop()

}