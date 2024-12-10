package fr.nekotine.fniaf.animation.observer

open class Observable {
    private val subscribers: HashSet<Observer> = HashSet()
    fun subscribe(observer: Observer):Boolean {
        return subscribers.add(observer)
    }
    fun unsubscribe(observer: Observer):Boolean {
        return subscribers.remove(observer)
    }

    fun notifySubscribers() {
        subscribers.forEach { subscriber -> subscriber.update(this) }
    }
}