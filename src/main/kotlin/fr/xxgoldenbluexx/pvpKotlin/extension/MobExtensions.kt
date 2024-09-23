package fr.xxgoldenbluexx.pvpKotlin.extension

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import org.bukkit.Bukkit
import org.bukkit.entity.Mob

fun <T:Mob> T.getAllGoals(): MutableCollection<Goal<T>> = Bukkit.getMobGoals().getAllGoals(this)

fun <T:Mob> T.removeGoal(goal: Goal<T>) = Bukkit.getMobGoals().removeGoal(this, goal)

fun <T:Mob> T.removeGoal(goalKey: GoalKey<T>) = Bukkit.getMobGoals().removeGoal(this, goalKey)

fun <T:Mob> T.removeAllGoals() = Bukkit.getMobGoals().removeAllGoals(this)

fun <T:Mob> T.removeAllGoals(goalType: GoalType) = Bukkit.getMobGoals().removeAllGoals(this, goalType)