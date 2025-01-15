package fr.nekotine.fniaf.animatronic

import fr.nekotine.core.ioc.Ioc
import fr.nekotine.fniaf.FnIAf
import fr.nekotine.fniaf.wrapper.Animatronic

class Foxy : Animatronic() {

    override fun getName() = "Foxy"

    override fun tick() {
        super.tick()
        var game = Ioc.resolve(FnIAf::class.java);
        game.survivors.filter { it.location.distanceSquared(location) <= 1 }.forEach{ it.kill() }
    }
}