package fr.nekotine.fniaf.map

import fr.nekotine.core.map.annotation.GenerateCommandFor
import fr.nekotine.core.serialization.configurationserializable.ConfigurationSerializableAdapted
import fr.nekotine.core.serialization.configurationserializable.annotation.ComposingConfiguration
import org.bukkit.Location

class FniafMap(dict: MutableMap<String, Any>?) : ConfigurationSerializableAdapted(dict) {

    @ComposingConfiguration
    @GenerateCommandFor
    lateinit var playerSpawn : Location

    @ComposingConfiguration
    @GenerateCommandFor
    lateinit var animSpawn : Location

}