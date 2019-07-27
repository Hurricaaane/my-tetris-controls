package eu.ha3.x.mtc

import eu.ha3.x.mtc.PressableEventListenerBuilder.Companion.pressableEventListener

/**
 * The 8bitdo directional pad has a hardware design issue where diagonals may be pressed when pressing left or right.
 * Therefore:
 * - prevent hard drop button from being pressed if a sideways movement key is "held down" at that moment.
 * - also release the hard drop button whenever a sideways movement key just went from "not pressed" to "pressed"
 */
class DpadOn8BitDo(private val hardDrop: Pressable) {
    private var leftIsPressed = false
    private var rightIsPressed = false

    private val hardDropListener = pressableEventListener()
            .onPress {
                if (!leftIsPressed && !rightIsPressed) {
                    hardDrop.press()
                }

            }
            .onRelease(hardDrop::release)
            .build()


    private val moveLeftListener = pressableEventListener()
            .onPress {
                leftIsPressed = true
                hardDrop.release()

            }
            .onRelease {
                leftIsPressed = false
            }
            .build()

    private val moveRightListener = pressableEventListener()
            .onPress {
                rightIsPressed = true
                hardDrop.release()

            }
            .onRelease {
                rightIsPressed = false
            }
            .build()

    fun tryPressHardDrop() = hardDropListener
    fun notifyMoveLeft() = moveLeftListener
    fun notifyMoveRight() = moveRightListener
}