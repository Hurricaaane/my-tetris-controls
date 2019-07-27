my-tetris-controls
====

My 8Bitdo directional pad has an issue where it is too sensitive to diagonal movement. Pressing left or right are often recognized as diagonal-up.

On Tetris games, this causes unintentional hard drops, usually bound to the upwards arrow on the directional pad.

I'm fixing this using convoluted software because why not.

---

In order for me to fix this, I start the gamepad in what the manual calls Android mode, so that it is not recognized as an Xbox gamepad but rather a regular controller on Windows.

This is done by pressing Start+B while the gamepad is turned off (before plugging it), causing the leftmost LED to blink.


After starting the main application, the following will **not** trigger a hard drop:

  - If any sideways direction is held down, pressing the upwards direction will not trigger a hard drop.
  - Releasing any sideways direction while the upwards direction is held down will not trigger a hard drop. 
