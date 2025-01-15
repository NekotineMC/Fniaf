package fr.nekotine.fniaf.control.player

import fr.nekotine.core.ioc.Ioc
import fr.nekotine.core.snapshot.PlayerStatusSnaphot
import fr.nekotine.core.snapshot.Snapshot
import fr.nekotine.fniaf.map.FniafMap
import fr.nekotine.fniaf.wrapper.SurvivorController
import org.bukkit.GameMode
import org.bukkit.entity.Player

class PlayerSurvivorController(val player: Player) : SurvivorController() {

    var snapshot: Snapshot<Player>? = null;

    override fun kill() {
        //player.damage(player.health) // Empêche la tp de fin de partie
    }

    override fun tick() {
        if (survivor == null){
            return
        }
        survivor!!.location = player.location
    }

    override fun onGameStart() {
        snapshot = PlayerStatusSnaphot().snapshot(player)
        player.gameMode = GameMode.ADVENTURE
        player.teleport(Ioc.resolve(FniafMap::class.java).playerSpawn)
    }

    override fun onGameStop() {
        snapshot!!.patch(player)
    }
}