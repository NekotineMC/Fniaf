package fr.xxgoldenbluexx.pvpKotlin.skills

import fr.xxgoldenbluexx.pvpKotlin.extension.spawnEntity
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.util.*

class AxeFireball(override val di: DI) : Listener, AutoCloseable, DIAware{

    private val data = WeakHashMap<Player,AxeFireballData>()

    private val cooldownSeconds = 3L
    private val launchSpd = 0.1
    private val pullSpd = 0.1

    private var tickTask: BukkitTask? = null

    private val plugin: JavaPlugin by instance()

    fun enable(){
        plugin.server.also {
            it.pluginManager.registerEvents(this,plugin)
        }
        tickTask = object : BukkitRunnable() {
            override fun run() {
                onFireballTick()
            }
        }.runTaskTimer(plugin, 0L, 1)
    }

    override fun close() {
        HandlerList.unregisterAll(this)
        if (tickTask != null && !tickTask!!.isCancelled){
            tickTask!!.cancel()
        }
    }

    @Suppress("UnstableApiUsage")
    @EventHandler
    fun onInteract(evt: PlayerInteractEvent){
        if (evt.hand == EquipmentSlot.OFF_HAND ||
            !evt.action.isLeftClick ||
            evt.useItemInHand() == Event.Result.DENY) {
                return
            }
        val p = evt.player
        val i = p.inventory.itemInMainHand
        i.itemMeta ?: return

        if (evt.action == Action.LEFT_CLICK_BLOCK && evt.clickedBlock?.type?.asBlockType()?.isInteractable == true){
            return
        }

        if (i.type == Material.DIAMOND_AXE && data.computeIfAbsent(p) { AxeFireballData() }.cooldownTimeStamp <= System.currentTimeMillis()){
            shoot(p)
        }


    }

    @EventHandler
    fun onPlayerQuit(evt:PlayerQuitEvent){
        data.remove(evt.player)
    }

    fun onFireballTick(){
        val fireballs = data.entries.stream().filter{e -> e.value.fireball != null }
        for (entry in fireballs){
            val fireball = entry.value.fireball!!
            if (fireball.isDead){
                data[entry.key]?.fireball = null
            }
            val pvec = entry.key.location.toVector()
            val fvec = fireball.location.toVector()
            val dir = pvec.clone().subtract(fvec)
            val dot = pvec.dot(fvec)
            if (fireball.hasLeftShooter() && dir.length() <= fireball.velocity.length()+0.1){
                fireball.remove()
                data[entry.key]?.fireball = null
            }else{
                val toAdd = dir.normalize().multiply(pullSpd)
                fireball.acceleration = fireball.acceleration.add(toAdd)
            }
        }
    }

    private fun shoot(p: Player) {
        val ploc = p.eyeLocation
        val d = data.computeIfAbsent(p) { AxeFireballData() }
        d.cooldownTimeStamp = System.currentTimeMillis() + (cooldownSeconds * 1000)
        d.fireball = ploc.spawnEntity(EntityType.FIREBALL) as Fireball
        d.fireball!!.direction = ploc.direction.multiply(launchSpd)
        d.fireball!!.setIsIncendiary(false)
        d.fireball!!.shooter = p
        p.world.playSound(ploc, Sound.ENTITY_GHAST_SHOOT, 0.4F, 1.0F)
    }

}

class AxeFireballData {
    var cooldownTimeStamp = 0L
    var fireball: Fireball? = null
}