package fr.nekotine.fniaf.wrapper

import fr.nekotine.core.ioc.Ioc
import fr.nekotine.fniaf.FnIAf
import org.bukkit.Bukkit
import org.bukkit.Location

class Survivor {

    var location: Location = Bukkit.getWorlds().first().spawnLocation

    var controller: SurvivorController? = null
        set(value) {
            field = value
            if (controller != null && controller?.survivor != this){
                controller!!.survivor = this
            }
        }

    var isAlive = true

    val isControlled:Boolean
        get() = controller != null

    fun tick(){
        controller?.tick()
    }

    fun kill(){
        if (!isAlive){
            return;
        }
        isAlive = false;
        controller?.kill();
        val game = Ioc.resolve(FnIAf::class.java)
        game.checkGameEnd()
    }
}