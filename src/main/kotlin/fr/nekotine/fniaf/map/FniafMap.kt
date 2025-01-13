package fr.nekotine.fniaf.map

import fr.nekotine.core.serialization.configurationserializable.ConfigurationSerializableAdapted
import org.bukkit.Bukkit
import org.bukkit.Location

class FniafMap(dict: MutableMap<String, Any>?) : ConfigurationSerializableAdapted(dict) {

    var playerSpawn : Location = Bukkit.getWorlds().first().spawnLocation

    var animSpawn : Location = Bukkit.getWorlds().first().spawnLocation

}