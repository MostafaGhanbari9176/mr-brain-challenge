package ir.pepotec.app.game.ui.uses

import android.os.Handler
import android.view.View

class MyAnimation(private val v: View) {

    private var TDelay = 10
    private var moveXTo = 0f
    private var moveYTo = 0f
    private var scaleXTo = 0f
    private var scaleYTo = 0f
    private var alphaTo = 0f
    private var moveXStep = 0F
    private var moveYStep = 0F
    private var scaleXStep = 0F
    private var scaleYStep = 0F
    private var alphaStep = 0F
    private var moveXCurrent = 0F
    private var moveYCurrent = 0F
    private var scaleXCurrent = 0F
    private var scaleYCurrent = 0F
    private var alphaCurrent = 0F
    private var run = true

    fun moveX(to: Float, duration: Int) {
        moveXStep = (to) / (duration.toFloat() / TDelay)
        moveXTo = to
    }

    fun moveY(to: Float, duration: Int) {
        moveYStep = (to) / (duration.toFloat() / TDelay)
        moveYTo = to
    }

    fun scaleX(from: Float, to: Float, duration: Int) {
        scaleXStep = (to - from) / (duration.toFloat() / TDelay)
        scaleXCurrent = from
        v.scaleX = from
        scaleXTo = to
    }

    fun scaleY(from: Float, to: Float, duration: Int) {
        scaleYStep = (to - from) / (duration.toFloat() / TDelay)
        scaleYCurrent = from
        v.scaleY = from
        scaleYTo = to
    }

    fun alpha(from: Float, to: Float, duration: Int) {
        alphaStep = (to - from) / (duration.toFloat() / TDelay)
        alphaCurrent = from
        v.alpha = from
        alphaTo = to
    }

    fun startAnimation() {
        val handler = Handler()
        Thread(
            Runnable {
                while ((moveXTo != moveXCurrent || moveYTo != moveYCurrent || scaleXTo != scaleXCurrent || scaleYTo != scaleYCurrent || alphaTo != alphaCurrent) && run) {
                    handler.post {
                        if (moveXStep != 0f && moveXTo != moveXCurrent) {
                            moveXCurrent += moveXStep
                            v.translationX = moveXCurrent
                        }
                        if (moveYStep != 0f && moveYTo != moveYCurrent) {
                            moveYCurrent += moveYStep
                            v.translationY = moveYCurrent
                        }
                        if (scaleXStep != 0f && scaleXTo != scaleXCurrent) {
                            scaleXCurrent += scaleXStep
                            v.scaleX = scaleXCurrent
                        }
                        if (scaleYStep != 0f && scaleYTo != scaleYCurrent) {
                            scaleYCurrent += scaleYStep
                            v.scaleY = scaleYCurrent
                        }
                        if (alphaStep != 0f && alphaTo != alphaCurrent) {
                            alphaCurrent += alphaStep
                            v.alpha = alphaCurrent
                        }
                    }
                    Thread.sleep(TDelay.toLong())
                }
            }
        ).start()
    }

    fun stop()
    {
        run = false
    }
}