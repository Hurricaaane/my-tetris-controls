package eu.ha3.x.mtc

interface Pressable {
    fun isPressed(): Boolean

    fun press()

    fun release()
}

interface PressableEventListener {
    fun onPressEvent()

    fun onReleaseEvent()
}

interface PressableEventObservable {
    fun addEventListener(listener: PressableEventListener)

    fun removeEventListener(listener: PressableEventListener)
}

class PressableEventListenerBuilder private constructor() {
    private var doOnPress: (() -> Unit)? = null
    private var doOnRelease: (() -> Unit)? = null

    companion object {
        fun pressableEventListener(): PressableEventListenerBuilder = PressableEventListenerBuilder()
    }

    fun onPress(action: () -> Unit): PressableEventListenerBuilder {
        this.doOnPress = action;
        return this
    }

    fun onRelease(action: () -> Unit): PressableEventListenerBuilder {
        this.doOnRelease = action;
        return this
    }

    fun build(): PressableEventListener {
        val onPress = doOnPress
        val onRelease = doOnRelease
        return object : PressableEventListener {
            override fun onPressEvent() {
                onPress?.invoke()
            }

            override fun onReleaseEvent() {
                onRelease?.invoke()
            }
        }
    }
}

class ListenablePressable : Pressable, PressableEventObservable {
    private val listeners: MutableCollection<PressableEventListener> = mutableSetOf()
    private var isPressed: Boolean = false

    override fun press() {
        if (isPressed) {
            return
        }

        this.isPressed = true
        listeners.forEach(PressableEventListener::onPressEvent)
    }

    override fun release() {
        if (!isPressed) {
            return
        }

        this.isPressed = false
        listeners.forEach(PressableEventListener::onReleaseEvent)
    }

    override fun addEventListener(listener: PressableEventListener) {
        listeners.add(listener)
    }

    override fun removeEventListener(listener: PressableEventListener) {
        listeners.remove(listener)
    }

    override fun isPressed(): Boolean = isPressed
}
