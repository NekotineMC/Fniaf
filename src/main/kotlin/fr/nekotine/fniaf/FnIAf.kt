package fr.nekotine.fniaf

import fr.nekotine.core.NekotinePlugin
import fr.nekotine.core.ioc.Ioc
import fr.nekotine.core.setup.PluginBuilder
import fr.nekotine.core.snapshot.PlayerStatusSnaphot
import fr.nekotine.core.snapshot.Snapshot
import fr.nekotine.core.util.EventUtil
import fr.nekotine.fniaf.map.FniafMap
import fr.nekotine.fniaf.wrapper.AnimatronicController
import fr.nekotine.fniaf.wrapper.Survivor
import io.papermc.paper.util.Tick
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.time.Duration

class FnIAf : JavaPlugin(), Listener, ForwardingAudience{

    lateinit var nekotinePlugin: NekotinePlugin

    val survivors = ArrayList<Survivor>()

    val animatronics = ArrayList<AnimatronicController>()

    val nbSurvivors: Int
        get() = survivors.size

    val nbAnimatronic: Int
        get() = animatronics.size

    var timetask: BukkitTask? = null

    val isRunning: Boolean
        get() = !(timetask?.isCancelled ?: true)

    var playersnapshots: MutableMap<Player, Snapshot<Player>> = LinkedHashMap()

    override fun onLoad() {
        super.onLoad();
        val builder = PluginBuilder(this);
        builder.mapCommandsFor(FniafMap::class.java);
        nekotinePlugin = builder.build();
    }

    override fun onEnable() {
        EventUtil.register(this);
        Ioc.getProvider()
            .registerSingleton(this)
            .registerSingletonInstanceAs(this, JavaPlugin::class.java);
        Commands.registerCommands();
    }

    override fun onDisable() {
        EventUtil.register(this);
        nekotinePlugin.disable();
        super.onDisable();
    }

    fun joinGame(p:Player){
        if (nbAnimatronic <= nbSurvivors){
            joinAnimatronic(p);
        }else{
            joinSurvivor(p);
        }
    }

    fun leaveGame(p:Player){
        leaveSurvivors(p);
        leaveAnimatronic(p);
    }

    fun joinSurvivor(p:Player){
        if (survivors.any { s -> s.player == p}){
            return
        }
        leaveAnimatronic(p);
        val surv = Survivor();
        surv.player = p;
        survivors.add(surv);
        p.sendMessage(Component.text("Vous passez dans l'équipe survivants",NamedTextColor.LIGHT_PURPLE));
    }

    fun leaveSurvivors(p:Player){
        val surv = survivors.firstOrNull { s -> s.player == p };
        if (surv == null){
            return;
        }
        survivors.remove(surv);
        p.sendMessage(Component.text("Vous quittez l'équipe des survivants",NamedTextColor.LIGHT_PURPLE));
    }

    fun joinAnimatronic(p:Player){
        if (animatronics.any { s -> s.player == p}){
            return
        }
        leaveSurvivors(p);
        val anim = AnimatronicController();
        anim.player = p;
        animatronics.add(anim);
        p.sendMessage(Component.text("Vous passez dans l'équipe animatronique",NamedTextColor.LIGHT_PURPLE));
    }

    fun leaveAnimatronic(p:Player){
        val anim = animatronics.firstOrNull { an -> an.player == p };
        if (anim == null){
            return;
        }
        animatronics.remove(anim);
        p.sendMessage(Component.text("Vous quittez l'équipe des animatronics",NamedTextColor.LIGHT_PURPLE));
    }

    fun startGame(){
        if (isRunning){
            return;
        }
        sendMessage(Component.text("La partie commence",NamedTextColor.GOLD))
        survivors.forEach{ s ->
            run {
                s.isAlive = true
                s.player?.let {
                    playersnapshots[it] = PlayerStatusSnaphot().snapshot(it);
                }
            }
        };
        animatronics.forEach{ a ->
            run {
                a.player?.let {
                    it.gameMode = GameMode.ADVENTURE;
                    playersnapshots[it] = PlayerStatusSnaphot().snapshot(it);
                }
            }
        };
        timetask = Bukkit.getScheduler().runTaskLater(this, Runnable
            {
                showTitle(Title.title(Component.text("Victoire des survivants",NamedTextColor.GOLD),Component.empty(), Title.Times.times(
                    Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
                stopGame();
            }, Tick.tick().fromDuration(Duration.ofMinutes(1)).toLong()
        );
    }

    fun stopGame(){
        if (!isRunning){
            return;
        }
        timetask?.cancel();
        timetask = null;
        for (p in players()){
            println(p.displayName().toString());
            playersnapshots[p]?.patch(p)
        }
        sendMessage(Component.text("La partie est finie",NamedTextColor.GOLD))
    }

    fun players(): Set<Player>{
        return survivors.filter { s -> s.isPlayer }.map{ s -> s.player!! }.union(animatronics.filter { a -> a.isPlayer }.map{a -> a.player!!});
    }

    override fun audiences(): MutableIterable<Audience> {
        // Ca ressemble a Linq de .net, c'est tellement smooth
        return players().toMutableSet()
    }

    // --- Events

    @EventHandler
    private fun OnPlayerJoin(evt:PlayerJoinEvent){
        joinGame(evt.player);
    }

    @EventHandler
    private fun OnPlayerDisconnect(evt:PlayerQuitEvent){
        leaveGame(evt.player);
    }

    @EventHandler
    private fun OnPlayerDeath(evt:PlayerDeathEvent){
        if (!isRunning){
            return;
        }
        val survivor = survivors.firstOrNull { s -> s.player == evt.player }
        if (survivor == null){
            return;
        }
        evt.isCancelled = true;
        survivor.isAlive = false;
        if (survivors.all { s -> !s.isAlive }){
            showTitle(Title.title(Component.text("Victoire des animatronics",NamedTextColor.GOLD),Component.empty(), Title.Times.times(
                Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
            stopGame();
        }
    }
}
