package eu.ha3.x.mtc

import java.awt.Robot
import java.awt.event.KeyEvent

class KeyboardPress(private val robot: Robot, private val event: Int) : Pressable {
    private var isPressed: Boolean = false

    override fun isPressed(): Boolean = isPressed

    override fun press() {
        if (isPressed) {
            return
        }

        this.isPressed = true
        robot.keyPress(event)
        println("Pressing " + KeyEvent.getKeyText(event))
    }

    override fun release() {
        if (!isPressed) {
            return
        }

        this.isPressed = false
        robot.keyRelease(event)
        println("Releasing " + KeyEvent.getKeyText(event))
    }
}
