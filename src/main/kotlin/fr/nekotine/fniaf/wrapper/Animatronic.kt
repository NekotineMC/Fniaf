package fr.nekotine.fniaf.wrapper

class Animatronic {

    var controller: AnimatronicController? = null

    val isControlled:Boolean
        get() = controller != null

}