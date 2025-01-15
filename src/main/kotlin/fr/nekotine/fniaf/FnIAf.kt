package fr.nekotine.fniaf

import fr.nekotine.core.NekotinePlugin
import fr.nekotine.core.ioc.Ioc
import fr.nekotine.core.setup.PluginBuilder
import fr.nekotine.core.snapshot.PlayerStatusSnaphot
import fr.nekotine.core.snapshot.Snapshot
import fr.nekotine.core.ticking.TickingModule
import fr.nekotine.core.ticking.event.TickElapsedEvent
import fr.nekotine.core.util.EventUtil
import fr.nekotine.fniaf.animatronic.Foxy
import fr.nekotine.fniaf.map.FniafMap
import fr.nekotine.fniaf.playercontrol.PlayerAnimatronicController
import fr.nekotine.fniaf.playercontrol.PlayerSurvivorController
import fr.nekotine.fniaf.wrapper.Animatronic
import fr.nekotine.fniaf.wrapper.AnimatronicController
import fr.nekotine.fniaf.wrapper.Survivor
import fr.nekotine.fniaf.wrapper.SurvivorController
import io.papermc.paper.util.Tick
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.time.Duration

class FnIAf : JavaPlugin(), Listener, ForwardingAudience{
    companion object {
        lateinit var nekotinePlugin: NekotinePlugin
        lateinit var javaPlugin: JavaPlugin
    }

    val survivors = ArrayList<Survivor>()

    val animatronics = ArrayList<Animatronic>()

    val nbSurvivors: Int
        get() = survivors.size

    val nbAnimatronic: Int
        get() = animatronics.size

    var timetask: BukkitTask? = null

    val isRunning: Boolean
        get() = !(timetask?.isCancelled ?: true)

    override fun onLoad() {
        super.onLoad();
        val builder = PluginBuilder(this);
        builder.mapCommandsFor(FniafMap::class.java);

        nekotinePlugin = builder.build();
        javaPlugin = this
    }

    override fun onEnable() {
        EventUtil.register(this);
        Ioc.getProvider()
            .registerSingleton(this)
            .registerSingletonInstanceAs(this, JavaPlugin::class.java);
        Commands.registerCommands();
        Ioc.resolve(TickingModule::class.java)


        /*world = Bukkit.getWorlds()[0]
        id1 = (world!!.spawnEntity(Location(world,.0, .0 , .0 ), EntityType.ITEM_DISPLAY)) as ItemDisplay
        id2 = (world!!.spawnEntity(Location(world,.0, .0, .0), EntityType.ITEM_DISPLAY)) as ItemDisplay
        id3 = (world!!.spawnEntity(Location(world,.0, .0, .0), EntityType.ITEM_DISPLAY)) as ItemDisplay
        id4 = (world!!.spawnEntity(Location(world,.0, .0, .0), EntityType.ITEM_DISPLAY)) as ItemDisplay
        endAffectorId = (world!!.spawnEntity(Location(world,.0, .0, .0), EntityType.ITEM_DISPLAY)) as ItemDisplay
        otherId = (world!!.spawnEntity(Location(world,.0, .0, .0), EntityType.ITEM_DISPLAY)) as ItemDisplay
        goalId = (world!!.spawnEntity(Location(world,.0, .0, .0), EntityType.ITEM_DISPLAY)) as ItemDisplay*/
        /*kc = KinematicChainBuilder().addLocalJoint(
            Vector3d(.0,75.0,.0),
            Vector3d(.0,.0,1.0),
            null,
        ).addLocalJoint(
            Vector3d(.0,.5,.0),
            null,
            Pair(.0,.0),
        ).addLocalJoint(
            Vector3d(0.0,1.0,.0),
            null,
            Pair(.0,.0),
        ).addLocalJoint(
            Vector3d(.0,.5,.0),
            null,
            Pair(-90.0,90.0),
        ).addLocalEndEffector(
            Vector3d(.0,.25,.0))
        kinematicJoint1 = kc!!.kinematicJoints[0]
        kinematicJoint2 = kc!!.kinematicJoints[1]
        kinematicJoint3 = kc!!.kinematicJoints[2]
        kinematicJoint4 = kc!!.kinematicJoints[3]*/
        /*kinematicJoint1 = KinematicJoint()
        kinematicJoint2 = KinematicJoint()
        kinematicJoint3 = KinematicJoint()
        kinematicJoint4 = KinematicJoint()
        endEffector = EndEffector()
        otherEffect = EndEffector()

        KinematicTreeBuilder("1", Vector3d(.0,75.0,.0), Quaterniond(), Vector3d(.0,.0,1.0),Pair(-90.0,90.0))
            .addEffector("other", Vector3d(-1.0, .0, .0), otherEffect!!)
            .addNode(
                KinematicTreeBuilder("2", Vector3d(.0,.5,.0), Quaterniond(), null,Pair(.0,.0))
                    .addNode(
                        KinematicTreeBuilder("3", Vector3d(0.0,1.0,.0), Quaterniond(), null,Pair(.0,.0))
                            .addNode(
                                KinematicTreeBuilder("4", Vector3d(.0,.5,.0), Quaterniond(), null,Pair(-90.0,90.0))
                                    .addEffector("end", Vector3d(.0,.25,.0), endEffector!!)
                                    .build(kinematicJoint4!!)
                            )
                            .build(kinematicJoint3!!)
                    )
                    .build(kinematicJoint2!!)
            )
            .build(kinematicJoint1!!)

        id1!!.setItemStack ( ItemStack(Material.BIRCH_STAIRS))
        id2!!.setItemStack (ItemStack(Material.BIRCH_STAIRS))
        id3!!.setItemStack (ItemStack(Material.BIRCH_STAIRS))
        id4!!.setItemStack (ItemStack(Material.BIRCH_STAIRS))
        endAffectorId!!.setItemStack (ItemStack(Material.IRON_BLOCK))
        otherId!!.setItemStack (ItemStack(Material.IRON_BLOCK))
        goalId!!.setItemStack (ItemStack(Material.EMERALD_BLOCK))
        //val scaleMatrix = Matrix4f().scale(0.5f)
        var scaleMatrix = Transformation(Vector3f(), AxisAngle4f(), Vector3f(0.1f,0.1f,0.1f),AxisAngle4f())
        id1!!.transformation = scaleMatrix
        id2!!.transformation = scaleMatrix
        id3!!.transformation = scaleMatrix
        endAffectorId!!.transformation = scaleMatrix
        otherId!!.transformation = scaleMatrix
        goalId!!.transformation = scaleMatrix
        id1!!.interpolationDelay = 1
        id1!!.interpolationDuration = 1
        id2!!.interpolationDelay = 1
        id2!!.interpolationDuration = 1
        id3!!.interpolationDelay = 1
        id3!!.interpolationDuration = 1
        id4!!.interpolationDelay = 1
        id4!!.interpolationDuration = 1*/
    }
    /*var world:World?=null
    var kinematicJoint1:KinematicChain.KinematicJoint? = null
    var kinematicJoint2:KinematicChain.KinematicJoint? = null
    var kinematicJoint3:KinematicChain.KinematicJoint? = null
    var kinematicJoint4:KinematicChain.KinematicJoint? = null
    var kc:KinematicChain?=null*/
    /*var kinematicJoint1:KinematicJoint? = null
    var kinematicJoint2:KinematicJoint? = null
    var kinematicJoint3:KinematicJoint? = null
    var kinematicJoint4:KinematicJoint? = null
    var endEffector:EndEffector?=null
    var otherEffect:EndEffector?=null
    var id1:ItemDisplay? = null
    var id2:ItemDisplay? = null
    var id3:ItemDisplay? = null
    var id4:ItemDisplay? = null
    var endAffectorId:ItemDisplay? = null
    var otherId:ItemDisplay? = null
    var goalId:ItemDisplay? = null
    */
    /*

    @EventHandler
    fun moveEvent(playerMoveEvent: PlayerMoveEvent){
        if(playerMoveEvent.player.gameMode == GameMode.CREATIVE){

            /*endEffector!!.ccdik(playerMoveEvent.player.location.toVector().toVector3d() /*Vector3d(5.0,.0,.0)*/)
            //System.out.println(a[3].toString())
            id1!!.teleport(Location(world,kinematicJoint1!!.globalPosition.x, kinematicJoint1!!.globalPosition.y , kinematicJoint1!!.globalPosition.z ))
            id2!!.teleport(Location(world,kinematicJoint2!!.globalPosition.x, kinematicJoint2!!.globalPosition.y , kinematicJoint2!!.globalPosition.z ))
            id3!!.teleport(Location(world,kinematicJoint3!!.globalPosition.x, kinematicJoint3!!.globalPosition.y , kinematicJoint3!!.globalPosition.z ))
            id4!!.teleport(Location(world,kinematicJoint4!!.globalPosition.x, kinematicJoint4!!.globalPosition.y , kinematicJoint4!!.globalPosition.z ))

            endAffectorId!!.teleport(Location(world,endEffector!!.globalPosition.x, endEffector!!.globalPosition.y , endEffector!!.globalPosition.z ))
            otherId!!.teleport(Location(world,otherEffect!!.globalPosition.x, otherEffect!!.globalPosition.y , otherEffect!!.globalPosition.z ))

            goalId!!.teleport(playerMoveEvent.player)
            //System.out.println("HELLO: " + joint1!!.yaw())
            //val a = Vector3f(joint1!!.globalPosition.x.toFloat(), joint1!!.globalPosition.y.toFloat(), joint1!!.globalPosition.z.toFloat())

            var t = Transformation(Vector3f(), Quaternionf(kinematicJoint1!!.localRotation), Vector3f(.1f,.1f,.1f), Quaternionf())
            id1!!.transformation = t

            t = Transformation(Vector3f(), Quaternionf(kinematicJoint2!!.localRotation), Vector3f(.1f,.1f,.1f), Quaternionf())
            id2!!.transformation = t

            t = Transformation(Vector3f(), Quaternionf(kinematicJoint3!!.localRotation), Vector3f(.1f,.1f,.1f), Quaternionf())
            id3!!.transformation = t

            t = Transformation(Vector3f(), Quaternionf(kinematicJoint4!!.localRotation), Vector3f(.1f,.1f,.1f), Quaternionf())
            id4!!.transformation = t


            /*id1!!.setRotation(
                (joint1!!.yaw()).toFloat(),
                joint1!!.pitch().toFloat()
                //joint1!!.pitch().toFloat()
            )*/
            //System.out.println("HELLO2: " + id1!!.yaw)
            /*id2!!.setRotation(
                (joint2!!.yaw()).toFloat(),
                joint2!!.pitch().toFloat()
            )
            id3!!.setRotation(
                (joint3!!.yaw()).toFloat(),
                joint3!!.pitch().toFloat()
            )*/
            /*System.out.println("start")
            System.out.println(joint1!!.position.toString())
            System.out.println(joint2!!.position.toString())
            System.out.println(joint3!!.position.toString())
            System.out.println("end")*/
            /*System.out.println("start")
            System.out.println(Vector3d(1.0,.0,.0).rotate(joint1!!.quaterniond).toString())
            System.out.println(Vector3d(1.0,.0,.0).rotate(joint2!!.quaterniond).toString())
            System.out.println(Vector3d(1.0,.0,.0).rotate(joint3!!.quaterniond).toString())
            System.out.println("end")*/*/
        }


    }
    */

    override fun onDisable() {
        EventUtil.unregister(this);
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
        if (survivors.any { s -> s.controller != null && s.controller is PlayerSurvivorController && (s.controller as PlayerSurvivorController).player == p}){
            return
        }
        leaveAnimatronic(p);
        val surv = Survivor();
        surv.controller = PlayerSurvivorController(p);
        survivors.add(surv);
        p.sendMessage(Component.text("Vous passez dans l'équipe survivants",NamedTextColor.LIGHT_PURPLE));
    }

    fun leaveSurvivors(p:Player){
        val surv = survivors.firstOrNull { s -> s.controller != null && s.controller is PlayerSurvivorController && (s.controller as PlayerSurvivorController).player == p };
        if (surv == null){
            return;
        }
        survivors.remove(surv);
        p.sendMessage(Component.text("Vous quittez l'équipe des survivants",NamedTextColor.LIGHT_PURPLE));
    }

    fun joinAnimatronic(p:Player){
        if (animatronics.any { a -> a.controller != null && a.controller is PlayerAnimatronicController && (a.controller as PlayerAnimatronicController).player == p }){
            return
        }
        leaveSurvivors(p);
        val anim = Foxy()
        anim.controller = PlayerAnimatronicController(p)
        animatronics.add(anim);
        p.sendMessage(Component.text("Vous passez dans l'équipe animatronique",NamedTextColor.LIGHT_PURPLE));
    }

    fun leaveAnimatronic(p:Player){
        val anim = animatronics.firstOrNull { a -> a.controller != null && a.controller is PlayerAnimatronicController && (a.controller as PlayerAnimatronicController).player == p };
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
                s.controller?.onGameStart()
            }
        };
        animatronics.forEach{ a -> a.controller?.onGameStart() }
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
        timetask?.cancel()
        timetask = null
        survivors.forEach{ s -> s.controller?.onGameStop()}
        animatronics.forEach{ s -> s.controller?.onGameStop()}
        sendMessage(Component.text("La partie est finie",NamedTextColor.GOLD))
    }

    fun players(): Set<Player>{
        return survivors.filter { s -> s.controller != null && s.controller is PlayerSurvivorController }.map{ s -> (s.controller as PlayerSurvivorController).player }
            .union(animatronics.filter { a -> a.controller != null && a.controller is PlayerAnimatronicController }.map{ a -> (a.controller as PlayerAnimatronicController).player});
    }

    override fun audiences(): MutableIterable<Audience> {
        // Ca ressemble a Linq de .net, c'est tellement smooth
        return players().toMutableSet()
    }

    fun checkGameEnd(){
        if (!isRunning){
            return;
        }
        if (survivors.all { s -> !s.isAlive }){
            showTitle(Title.title(Component.text("Victoire des animatronics",NamedTextColor.GOLD),Component.empty(), Title.Times.times(
                Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
            stopGame();
        }
    }

    // --- Events

    @EventHandler
    private fun OnTick(evt: TickElapsedEvent) {
        if (!isRunning){
            return;
        }
        animatronics.forEach{ it.tick() }
        survivors.forEach { it.tick() }
    }

    @EventHandler
    private fun OnPlayerJoin(evt:PlayerJoinEvent){
        joinGame(evt.player);
        //Humanoid(Vector3d(.0,71.5,.0))
    }

    @EventHandler
    private fun OnPlayerDisconnect(evt:PlayerQuitEvent){
        leaveGame(evt.player);
    }
}
