package fr.nekotine.fniaf.playercontrol

import fr.nekotine.core.snapshot.PlayerStatusSnaphot
import fr.nekotine.core.snapshot.Snapshot
import fr.nekotine.fniaf.wrapper.SurvivorController
import org.bukkit.GameMode
import org.bukkit.entity.Player

class PlayerSurvivorController(val player: Player) : SurvivorController() {

    var snapshot: Snapshot<Player>? = null;

    override fun kill() {
        player.damage(player.health)
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
    }

    override fun onGameStop() {
        snapshot?.patch(player)
    }
}