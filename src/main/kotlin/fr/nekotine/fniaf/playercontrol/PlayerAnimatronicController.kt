package fr.nekotine.fniaf.playercontrol

import fr.nekotine.core.snapshot.PlayerStatusSnaphot
import fr.nekotine.core.snapshot.Snapshot
import fr.nekotine.fniaf.wrapper.AnimatronicController
import org.bukkit.GameMode
import org.bukkit.entity.Player

class PlayerAnimatronicController(val player: Player) : AnimatronicController() {

    var snapshot: Snapshot<Player>? = null;

    override fun tick() {
        if (animatronic == null){
            return;
        }
        animatronic!!.location = player.location
    }

    override fun onGameStart() {
        snapshot = PlayerStatusSnaphot().snapshot(player)
        player.gameMode = GameMode.ADVENTURE
    }

    override fun onGameStop() {
        snapshot?.patch(player)
    }
}