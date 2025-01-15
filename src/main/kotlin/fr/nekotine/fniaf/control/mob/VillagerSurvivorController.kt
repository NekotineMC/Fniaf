package fr.nekotine.fniaf.control.mob

import fr.nekotine.core.ioc.Ioc
import fr.nekotine.core.snapshot.PlayerStatusSnaphot
import fr.nekotine.core.snapshot.Snapshot
import fr.nekotine.fniaf.map.FniafMap
import fr.nekotine.fniaf.wrapper.SurvivorController
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.event.entity.CreatureSpawnEvent

class VillagerSurvivorController() : SurvivorController() {

    var villager: Villager? = null

    override fun kill() {
        villager?.damage(villager?.health ?: 0.0)
    }

    override fun tick() {
        if (survivor == null || villager == null){
            return
        }
        survivor!!.location = villager!!.location
    }

    override fun onGameStart() {
        val loc = Ioc.resolve(FniafMap::class.java).playerSpawn
        villager = loc.world.spawnEntity(loc, EntityType.VILLAGER, CreatureSpawnEvent.SpawnReason.CUSTOM) as Villager
    }

    override fun onGameStop() {
        villager?.remove()
    }
}