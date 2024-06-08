package aut.fhooe.sail.android.md2ua_prototype.interfaces


enum class ObserverCases {
    main_route,
    turnOn_route,
    turnOff_route,
    vibrate_route,
    vibrate_rand_route,
    vibrate_wrong_route,
    screenOn
}

// Subject interface
interface Subject {
    fun registerObserver(observer: Observer)
    fun removeObserver(observer: Observer)
    fun notifyObservers(case: ObserverCases)
}


// Observer interface
interface Observer {
    fun update(case: ObserverCases)
}

