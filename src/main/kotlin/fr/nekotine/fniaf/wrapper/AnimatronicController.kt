package fr.nekotine.fniaf.wrapper

import org.bukkit.entity.Player

class AnimatronicController {

    var player: Player? = null;

    val isPlayer:Boolean
        get() = player != null

}