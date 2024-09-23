package fr.xxgoldenbluexx.pvpKotlin.ai

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import org.bukkit.NamespacedKey
import org.bukkit.entity.Mob
import java.util.*

class TargetInRangeGoal : Goal<Mob>{

    companion object{
        private val key = GoalKey.of(Mob::class.java,NamespacedKey("KotlinPlugin","TargetInRangeGoal"))

        private val types = EnumSet.of(GoalType.TARGET)
    }

    override fun shouldActivate(): Boolean {
        return true
    }

    override fun getKey(): GoalKey<Mob> {
        return key
    }

    override fun getTypes(): EnumSet<GoalType> {
        return types
    }

    override fun tick() {
        get
    }
}