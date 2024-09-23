package fr.xxgoldenbluexx.pvpKotlin.extension

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason

fun Location.spawnEntity(type: EntityType) = this.world.spawnEntity(this,type)

fun Location.spawnEntity(type: EntityType, reason: SpawnReason) = this.world.spawnEntity(this,type,reason)

fun Location.spawnParticle(particle: Particle, count: Int) =
    this.world.spawnParticle(particle,this.x,this.y,this.z,count)