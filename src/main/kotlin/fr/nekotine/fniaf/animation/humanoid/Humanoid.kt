package fr.nekotine.fniaf.animation.humanoid

import fr.nekotine.core.ticking.event.TickElapsedEvent
import fr.nekotine.core.util.EventUtil
import fr.nekotine.fniaf.FnIAf
import fr.nekotine.fniaf.animation.math.copy
import fr.nekotine.fniaf.animation.tree.EndEffector
import fr.nekotine.fniaf.animation.tree.KinematicJoint
import fr.nekotine.fniaf.animation.tree.KinematicTreeBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerGameModeChangeEvent
import org.joml.Quaterniond
import org.joml.Vector3d

class Humanoid(
    private val position: Vector3d
) : Listener {

    val pelvis = KinematicJoint()
    val rHip = KinematicJoint()
    val rKnee = KinematicJoint()
    val rFoot = EndEffector()
    val lHip = KinematicJoint()
    val lKnee = KinematicJoint()
    val lFoot = EndEffector()

    val leftFootTarget = Vector3d()
    val rightFootTarget = Vector3d()

    init {
        buildHumanoid(position)
        createDisplays()
        Bukkit.getScheduler().runTaskTimer(FnIAf.javaPlugin, Runnable {
            run {
                println(pelvis.localPosition)
                moveHumanoid(pelvis.localPosition.copy().add(.5/20,.0,.0))
            }
        }, 0, 1)
    }

    private fun buildHumanoid(position:Vector3d) {
        KinematicTreeBuilder("pelvis", position, Quaterniond(), Vector3d(.0,1.0,.0), null)
            .addNode(KinematicTreeBuilder("rHip", Vector3d(.0,-.5,.5), Quaterniond(), null, Pair(-90.0,90.0))
                .addNode(KinematicTreeBuilder("rKnee", Vector3d(.0,-1.0,.5), Quaterniond(), Vector3d(.0,.0,1.0), Pair(-90.0,0.0))
                    .addEffector("rFoot", Vector3d(.0, -1.0, .0), rFoot)
                    .build(rKnee)
                )
                .build(rHip)
            )
            .addNode(KinematicTreeBuilder("lHip", Vector3d(.0,-.5,-.5), Quaterniond(), null, Pair(-90.0,90.0))
                .addNode(KinematicTreeBuilder("lKnee", Vector3d(.0,-1.0,-.5), Quaterniond(), Vector3d(.0,.0,1.0), Pair(-90.0,0.0))
                    .addEffector("lFoot", Vector3d(.0, -1.0, .0), lFoot)
                    .build(lKnee)
                )
                .build(lHip)
            )
            .build(pelvis)

        rightFootTarget.set(rFoot.globalPosition.copy())
        leftFootTarget.set(lFoot.globalPosition.copy())
    }

    private fun createDisplays() {
        BoneRenderer(rHip, Material.DIAMOND_BLOCK, .2, Bukkit.getWorlds()[0], 1)
        BoneRenderer(lHip, Material.DIAMOND_BLOCK, .2, Bukkit.getWorlds()[0], 1)
        BoneRenderer(rKnee, Material.IRON_BLOCK, .15, Bukkit.getWorlds()[0], 1)
        BoneRenderer(lKnee, Material.IRON_BLOCK, .15, Bukkit.getWorlds()[0], 1)
        BoneRenderer(rFoot, Material.IRON_BLOCK, .1, Bukkit.getWorlds()[0], 1)
        BoneRenderer(lFoot, Material.IRON_BLOCK, .1, Bukkit.getWorlds()[0], 1)

        JointRenderer(pelvis, Material.EMERALD_BLOCK, .2, Bukkit.getWorlds()[0], 1)
        JointRenderer(rHip, Material.EMERALD_BLOCK, .2, Bukkit.getWorlds()[0], 1)
        JointRenderer(rKnee, Material.EMERALD_BLOCK, .2, Bukkit.getWorlds()[0], 1)
        JointRenderer(lHip, Material.EMERALD_BLOCK, .2, Bukkit.getWorlds()[0], 1)
        JointRenderer(lKnee, Material.EMERALD_BLOCK, .2, Bukkit.getWorlds()[0], 1)
        JointRenderer(pelvis, Material.EMERALD_BLOCK, .2, Bukkit.getWorlds()[0], 1)
        EffectorRenderer(rFoot, Material.GOLD_BLOCK, .2, Bukkit.getWorlds()[0], 1)
        EffectorRenderer(lFoot, Material.GOLD_BLOCK, .2, Bukkit.getWorlds()[0], 1)
    }

    fun moveHumanoid(position: Vector3d) {
        this.pelvis.localPosition.set(position)
        this.pelvis.updateWorldValues()
        rFoot.ccdik(rightFootTarget)
        lFoot.ccdik(leftFootTarget)
    }
}
