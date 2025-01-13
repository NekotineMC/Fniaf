package fr.nekotine.fniaf.wrapper

import org.bukkit.Bukkit
import org.bukkit.Location

abstract class Animatronic {

    var location: Location = Bukkit.getWorlds().first().spawnLocation

    var controller: AnimatronicController? = null
        set(value) {
            field = value
            if (controller != null && controller!!.animatronic != this){
                controller!!.animatronic = this
            }
        }

    val isControlled:Boolean
        get() = controller != null


    open fun tick(){
        controller?.tick()
    }

    abstract fun getName():String

}