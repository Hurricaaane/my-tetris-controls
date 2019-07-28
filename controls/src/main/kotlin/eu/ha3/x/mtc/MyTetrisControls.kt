package eu.ha3.x.mtc

import eu.ha3.x.mtc.E8BitdoAction.*
import eu.ha3.x.mtc.PressableEventListenerBuilder.Companion.pressableEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Robot
import java.awt.event.KeyEvent
import java.util.concurrent.TimeUnit

class MyTetrisControls(private val virtualController: VirtualController<E8BitdoAction>) {
    private val robot = Robot()
    private val keyboardAsButtons: MutableMap<Int, PressableEventListener> = mutableMapOf()

    fun setUp(gameType: GameType) {
        when (gameType) {
            GameType.PUYO_PUYO_TETRIS -> PUYO_PUYO_TETRIS()
            GameType.TETRIS_EFFECT -> TETRIS_EFFECT()
            GameType.HYBRID_SETTINGS -> HYBRID_SETTINGS()
        }
    }

    enum class GameType {
        PUYO_PUYO_TETRIS,
        TETRIS_EFFECT,
        HYBRID_SETTINGS
    }

    fun PUYO_PUYO_TETRIS() {
        COMMON_TETRIS()

        // Puyo Puyo Tetris uses AZERTY keybinds
        hardDropTo(KeyEvent.VK_UP)
        virtualController.whenever(B, thenPress(KeyEvent.VK_W)) // VK_W is letter Z on English keyboards

        // For menus
        virtualController.whenever(B, thenPress(KeyEvent.VK_ENTER))
        virtualController.whenever(START, thenPress(KeyEvent.VK_ESCAPE))
    }

    fun TETRIS_EFFECT() {
        COMMON_TETRIS()

        // Tetris Effect uses QWERTY keybinds
        hardDropTo(KeyEvent.VK_SPACE)
        virtualController.whenever(B, thenPress(KeyEvent.VK_Z)) // VK_Z is letter W on French keyboards
        virtualController.whenever(X, thenPress(KeyEvent.VK_A)) // VK_A is letter Q on French keyboards

        // Keyboard UP/DOWN Mapped to R1/R2 instead of DPAD, because in this game, keyboard UP/DOWN is used for spinning
        virtualController.whenever(R1, thenPress(KeyEvent.VK_UP))
        virtualController.whenever(R2, thenPress(KeyEvent.VK_DOWN))
        virtualController.whenever(SELECT, thenPress(KeyEvent.VK_ENTER))
        virtualController.whenever(START, thenPress(KeyEvent.VK_ESCAPE))
    }

    fun HYBRID_SETTINGS() {
        PUYO_PUYO_TETRIS()
        virtualController.whenever(X, thenPress(KeyEvent.VK_A))
    }

    private fun COMMON_TETRIS() {
        virtualController.whenever(DOWN, thenPress(KeyEvent.VK_DOWN))
        virtualController.whenever(LEFT, thenPress(KeyEvent.VK_LEFT))
        virtualController.whenever(RIGHT, thenPress(KeyEvent.VK_RIGHT))
        virtualController.whenever(A, thenPress(KeyEvent.VK_X))
        virtualController.whenever(L1, thenPress(KeyEvent.VK_C))
        virtualController.whenever(L2, thenPress(KeyEvent.VK_C))
    }


    private fun hardDropTo(hardDrop: Int) {
        val dpad = DpadOn8BitDo(KeyboardPress(robot, hardDrop))
        virtualController.whenever(UP, dpad.tryPressHardDrop())
        virtualController.whenever(LEFT, dpad.notifyMoveLeft())
        virtualController.whenever(RIGHT, dpad.notifyMoveRight())
    }

    private fun thenPress(keyEvent: Int): PressableEventListener {
        var lastPressNanos = System.nanoTime()
        return keyboardAsButtons.computeIfAbsent(keyEvent) {
            val output = KeyboardPress(robot, keyEvent)
            pressableEventListener()
                    .onPress {
                        lastPressNanos = System.nanoTime()
                        output.press()
                    }
                    .onRelease {
                        if (wasPressedAndReleasedSimultaneously(lastPressNanos)) {
                            GlobalScope.launch {
                                delay(20)
                                output.release()
                            }
                        } else {
                            output.release()
                        }
                    }.build()
        }
    }

    private fun wasPressedAndReleasedSimultaneously(lastPressNanos: Long) =
            System.nanoTime() - lastPressNanos < TimeUnit.MILLISECONDS.toNanos(25)
}