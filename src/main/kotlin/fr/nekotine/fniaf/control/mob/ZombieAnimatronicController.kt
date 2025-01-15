package fr.nekotine.fniaf.control.mob

import fr.nekotine.core.ioc.Ioc
import fr.nekotine.core.snapshot.PlayerStatusSnaphot
import fr.nekotine.core.snapshot.Snapshot
import fr.nekotine.fniaf.map.FniafMap
import fr.nekotine.fniaf.wrapper.AnimatronicController
import org.bukkit.GameMode
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.CreatureSpawnEvent

class ZombieAnimatronicController() : AnimatronicController() {

    var zombie: Zombie? = null

    override fun tick() {
        if (animatronic == null || zombie == null){
            return
        }
        animatronic!!.location = zombie!!.location
    }

    override fun onGameStart() {
        val loc = Ioc.resolve(FniafMap::class.java).playerSpawn
        zombie = loc.world.spawnEntity(loc, EntityType.ZOMBIE, CreatureSpawnEvent.SpawnReason.CUSTOM) as Zombie
    }

    override fun onGameStop() {
        zombie?.remove()
    }
}