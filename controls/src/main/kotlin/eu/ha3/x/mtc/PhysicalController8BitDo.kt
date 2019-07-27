package eu.ha3.x.mtc

import eu.ha3.x.mtc.E8BitdoAction.*
import net.java.games.input.ControllerEnvironment
import net.java.games.input.Event
import java.util.*

class PhysicalController8BitDo(private val virtualController: VirtualController<E8BitdoAction>) {
    fun loop(): EExitResult {
        val controller = ControllerEnvironment.getDefaultEnvironment().controllers
                .firstOrNull { it.name.contains("8Bitdo") || it.name.contains("Bluetooth Wireless Controller") }
                ?: return EExitResult.CONTROLLER_NOT_FOUND
        println(controller.name)

        val queue = controller.eventQueue
        val event = Event()

        while (controller.poll()) {
            while (queue.getNextEvent(event)) {
                convertEventToButtonPress(event)
            }
        }

        return EExitResult.CONTROLLER_LOST
    }

    private fun convertEventToButtonPress(event: Event) {
        val component = event.component
        val direction = event.value
        val whenPressed = event.value > 0f
        when (component.name) {
            "Commande de pouce" -> {
                handleDpad(direction)
            }
            "Bouton 11" -> onVirtualController(START, whenPressed)
            "Bouton 10" -> onVirtualController(SELECT, whenPressed)
            "Bouton 0" -> onVirtualController(A, whenPressed)
            "Bouton 1" -> onVirtualController(B, whenPressed)
            "Bouton 4" -> onVirtualController(Y, whenPressed)
            "Bouton 3" -> onVirtualController(X, whenPressed)
            "Bouton 6" -> onVirtualController(L1, whenPressed)
            "Bouton 8" -> onVirtualController(L2, whenPressed)
            "Bouton 7" -> onVirtualController(R1, whenPressed)
            "Bouton 9" -> onVirtualController(R2, whenPressed)
        }
    }

    private fun handleDpad(direction: Float) {
        val pressed = when (direction) {
            1.0f -> EnumSet.of(LEFT)
            0.125f -> EnumSet.of(LEFT, UP)
            0.25f -> EnumSet.of(UP)
            0.375f -> EnumSet.of(UP, RIGHT)
            0.5f -> EnumSet.of(RIGHT)
            0.625f -> EnumSet.of(RIGHT, DOWN)
            0.75f -> EnumSet.of(DOWN)
            0.875f -> EnumSet.of(DOWN, LEFT)
            else -> EnumSet.noneOf(E8BitdoAction::class.java)
        }

        pressed.forEach(virtualController::press)
        EnumSet.complementOf(pressed).forEach(virtualController::release)
    }

    private fun onVirtualController(action: E8BitdoAction, whenPressed: Boolean) {
        if (whenPressed) {
            this.virtualController.press(action)

        } else {
            this.virtualController.release(action)
        }
    }
}

enum class EExitResult {
    CONTROLLER_NOT_FOUND,
    CONTROLLER_LOST
}

enum class E8BitdoAction {
    LEFT, RIGHT, UP, DOWN, START, L1, L2, R1, R2, A, B, X, Y, SELECT
}