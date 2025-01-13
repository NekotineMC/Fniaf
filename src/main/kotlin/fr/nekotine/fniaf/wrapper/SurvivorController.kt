package fr.nekotine.fniaf.wrapper

import org.bukkit.entity.Player

class SurvivorController {

    var survivor: Survivor? = null
        set(value) {
            field = value
            if (survivor != null && survivor?.controller != this){
                survivor!!.controller = this
            }
        }

    var player: Player? = null;

    val isPlayer:Boolean
        get() = player != null

    fun kill(){
        if (player != null){
            player!!.damage(player!!.health)
        }
    }

    fun tick(){
        if (survivor == null){
            return
        }
        if (player != null){
            survivor!!.location = player!!.location
        }
    }

}