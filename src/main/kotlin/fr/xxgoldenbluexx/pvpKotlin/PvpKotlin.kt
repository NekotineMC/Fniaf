package fr.xxgoldenbluexx.pvpKotlin

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.FloatArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import fr.xxgoldenbluexx.pvpKotlin.building.Turet
import fr.xxgoldenbluexx.pvpKotlin.extension.spawnEntity
import fr.xxgoldenbluexx.pvpKotlin.skills.AxeFireball
import fr.xxgoldenbluexx.pvpKotlin.skills.AxeFlash
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Ghast
import org.bukkit.plugin.java.JavaPlugin
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import java.util.LinkedList

@Suppress("unused")
class PvpKotlin : JavaPlugin() {

    private val di = DI{
        bind<JavaPlugin>() with instance(this@PvpKotlin)
        bind<AxeFlash>() with singleton { AxeFlash(di) }
        bind<AxeFireball>() with singleton { AxeFireball(di) }
    }
    private val flash by di.instance<AxeFlash>()
    private val fireball by di.instance<AxeFireball>()

    private val turets = LinkedList<Turet>();

    override fun onEnable() {
        registerTickrateCommand()
        CommandAPICommand("1").executesPlayer(PlayerCommandExecutor { commandSender, commandArguments ->
            run {
                val tu = Turet(di,commandSender.world)
                turets += tu
                tu.enable()
            }
        }).register()
        flash.enable()
        fireball.enable()
    }

    override fun onDisable() {
        flash.close()
        fireball.close()
        for(turet in turets){
            turet.close()
        }
        turets.clear()
    }

    private fun registerTickrateCommand(){
        val command = CommandAPICommand("tickrate")
        command.withArguments(FloatArgument("rate",1f,10000f))
        command.executes(CommandExecutor { commandSender, commandArguments ->
            run {
                val tickRate = commandArguments["rate"] as Float
                server.serverTickManager.tickRate = tickRate
                commandSender.sendMessage("Tickrate set to $tickRate")
            }
        })
        command.register()
    }
}
