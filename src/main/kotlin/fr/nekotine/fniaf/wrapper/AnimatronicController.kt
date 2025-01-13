package fr.nekotine.fniaf.wrapper

import org.bukkit.entity.Player

class AnimatronicController {

    var animatronic: Animatronic? = null
        set(value) {
            field = value
            if (animatronic != null && animatronic?.controller != this){
                animatronic!!.controller = this
            }
        }

    var player: Player? = null;

    val isPlayer:Boolean
        get() = player != null

    fun tick(){
        if (animatronic == null){
            return;
        }
        if (player != null){
            animatronic!!.location = player!!.location
        }
    }

}