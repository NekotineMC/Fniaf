package fr.xxgoldenbluexx.pvpKotlin.skills

import fr.xxgoldenbluexx.pvpKotlin.extension.step
import io.papermc.paper.util.Tick
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.time.Duration
import java.util.WeakHashMap

class AxeFlash(override val di: DI) : Listener, AutoCloseable, DIAware{

    private val charges = WeakHashMap<Player,Int>()

    private val rechargeSeconds = 5L
    private val nbChargeMax = 5
    private val tpDistance = 5.0

    private var rechargeTask: BukkitTask? = null

    private val plugin: JavaPlugin by instance()

    fun enable(){
        plugin.server.also {
            it.pluginManager.registerEvents(this,plugin)
            it.onlinePlayers.forEach { p ->
                charges[p] = nbChargeMax
            }
        }
        rechargeTask = object : BukkitRunnable() {
            override fun run() {
                for (key in charges.keys) {
                    val old = charges[key] ?: 0
                    if (old < nbChargeMax) {
                        val nbCharge = ((old) + 1).coerceAtMost(nbChargeMax)
                        charges[key] = nbCharge
                        key.sendMessage(Component.text("Charge : $nbCharge"))
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, Tick.tick().fromDuration(Duration.ofSeconds(rechargeSeconds)).toLong())
    }

    override fun close() {
        HandlerList.unregisterAll(this)
        if (rechargeTask != null && !rechargeTask!!.isCancelled){
            rechargeTask!!.cancel()
        }
    }

    @Suppress("UnstableApiUsage")
    @EventHandler
    fun onInteract(evt: PlayerInteractEvent){
        if (evt.hand == EquipmentSlot.OFF_HAND ||
            !evt.action.isRightClick ||
            evt.useItemInHand() == Event.Result.DENY) {
                return
            }
        val p = evt.player
        val i = p.inventory.itemInMainHand
        i.itemMeta ?: return

        if (evt.action == Action.RIGHT_CLICK_BLOCK && evt.clickedBlock?.type?.asBlockType()?.isInteractable == true){
            return
        }

        if (i.type == Material.DIAMOND_AXE && charges[p]?.let { it > 0 } == true){
            flash(p)
        }
    }

    @EventHandler
    fun onPlayerJoin(evt:PlayerJoinEvent){
        charges[evt.player] = nbChargeMax
    }

    @EventHandler
    fun onPlayerQuit(evt:PlayerQuitEvent){
        charges[evt.player] = nbChargeMax
    }

    private fun flash(p: Player) {
        val nbCharge = (charges[p] ?: nbChargeMax)-1
        charges[p] = nbCharge
        p.sendMessage(Component.text("Charge : $nbCharge"))
        val ploc = p.location
        val dir = p.eyeLocation.direction.normalize()
        var found = false
        for (dist in (0.0..tpDistance step 0.2).reversed()){
            val dest = ploc.clone().add(dir.clone().multiply(dist))
            if (found){
                Particle.FIREWORK.builder().location(dest).count(2).receivers(100).extra(0.0).spawn()
            }else{

                if (dest.block.isPassable && dest.add(0.0,1.0,0.0).block.isPassable){
                    found = true
                    shiftTp(p, dest.add(0.0,-1.0,0.0))
                }
            }
        }
        p.world.playSound(ploc, Sound.ENTITY_WITHER_SHOOT, 0.4F, 1.2F)
        p.world.playSound(ploc, Sound.ENTITY_SILVERFISH_DEATH, 1.0F, 1.6F)
    }

    private fun shiftTp(p: Player, loc: Location){
        p.teleport(loc)
    }

}