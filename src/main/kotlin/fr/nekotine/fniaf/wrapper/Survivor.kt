package fr.nekotine.fniaf.wrapper

import org.bukkit.GameMode
import org.bukkit.entity.Player

class Survivor {

    var player: Player? = null;

    val isPlayer:Boolean
        get() = player != null

    var isAlive:Boolean = true
        get() = field;
        set(value){
            field = value;
            if (field){
                player?.gameMode = GameMode.ADVENTURE;
            }else{
                player?.gameMode = GameMode.SPECTATOR;
            }
        }

}