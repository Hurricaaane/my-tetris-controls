package eu.ha3.x.mtc

class VirtualController<T> {
    private val actionToListeners: MutableMap<T, ListenablePressable> = mutableMapOf()

    fun press(action: T) {
        val pressable = actionToListeners[action]
        pressable?.press()
    }

    fun release(action: T) {
        val pressable = actionToListeners[action]
        pressable?.release()
    }

    fun whenever(action: T, eventListener: PressableEventListener) {
        val pressable = getOrCreate(action)
        pressable.addEventListener(eventListener)
    }

    private fun getOrCreate(action: T): ListenablePressable {
        return actionToListeners.computeIfAbsent(action) { x -> ListenablePressable() }
    }
}
