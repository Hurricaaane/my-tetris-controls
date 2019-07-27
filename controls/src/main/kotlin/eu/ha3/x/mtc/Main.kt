package eu.ha3.x.mtc

fun main() {
    val virtualController: VirtualController<E8BitdoAction> = VirtualController()
    val physicalController = PhysicalController8BitDo(virtualController)
    MyTetrisControls(virtualController).setUp(MyTetrisControls.GameType.TETRIS_EFFECT)

    var firstLoop = true

    while (true) {
        val exitReason = physicalController.loop()

        println(exitReason)
        if (firstLoop && exitReason == EExitResult.CONTROLLER_NOT_FOUND) {
            println("""Reminder that if there is java.lang.UnsatisfiedLinkError: no jinput-dx8_64:
                |- make sure mvn clean package has been run
                |- in the run configuration, add -Djava.library.path=<absolute path>/target/natives inside java.library.path
                |  (i.e. -Djava.library.path=C:\my-tetris-controls\controls\target\natives)""".trimMargin())
        }
        Thread.sleep(1000)
        firstLoop = false
    }
}