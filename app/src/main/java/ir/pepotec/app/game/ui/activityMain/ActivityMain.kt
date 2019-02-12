package ir.pepotec.app.game.ui.activityMain

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.activity_main.*


class ActivityMain : AppCompatActivity(), View.OnClickListener {


    var keyMode = "out"
    val sc = 0.7F
    val du = 120L
    val fade = 0.7f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.instance = this
        initViews()
    }

    private fun initViews() {
        animateViews()
        App.fullScreen(this)
        setView(FragmentMainMenu())
        imgControlGameLevels.setOnClickListener(this)

    }

    private fun animateViews() {
        val sc = 0.1F
        val fade = 0.4F
        val du = 150L
        imgControlGameLevels.scaleX = sc
        imgControlGameLevels.scaleY = sc
        imgControlGameLevels.alpha = fade
        val imgScX = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_X, sc, 1f, 1.5f, 1f)
        val imgScY = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_Y, sc, 1f, 1.5f, 1f)
        val imgFade = ObjectAnimator.ofFloat(imgControlGameLevels, View.ALPHA, fade, 1f)

        val set = AnimatorSet()
        set.playTogether(imgScX, imgScY, imgFade)
        set.duration = du
        set.startDelay = 800
        set.start()
    }

    override fun onResume() {
        super.onResume()
        App.instance = this

    }

    override fun onBackPressed() {
        if (keyMode == "out") {
            this.finish()
            super.onBackPressed()
        }
        else{
            imgControlGameLevels.callOnClick()
        }
    }

    fun animateImageToHome() {

        imgControlGameLevels.isEnabled = false
        var scX = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_X, 1f, 1.2f, sc)
        var scY = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_Y, 1f, 1.2f, sc)
        var op = ObjectAnimator.ofFloat(imgControlGameLevels, View.ALPHA, 1f, fade)
        var set = AnimatorSet()
        set.playTogether(scX, scY, op)
        set.duration = du
        set.start()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                imgControlGameLevels.setImageResource(R.drawable.home__symbol)
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })

        if (keyMode == "out") {
            scX = ObjectAnimator.ofFloat(LLProgress, View.SCALE_X, 0.1f, 1.2f, 1f)
            scY = ObjectAnimator.ofFloat(LLProgress, View.SCALE_Y, 0.1f, 1.2f, 1f)
            op = ObjectAnimator.ofFloat(LLProgress, View.ALPHA, 0f, 1f)
            set = AnimatorSet()
            set.playTogether(scX, scY, op)
            set.duration = 2 * du
            set.start()
        }

        scX = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_X, sc, 1.2f, 1f)
        scY = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_Y, sc, 1.2f, 1f)
        op = ObjectAnimator.ofFloat(imgControlGameLevels, View.ALPHA, fade, 1f)
        set = AnimatorSet()
        set.playTogether(scX, scY, op)
        set.startDelay = du
        set.duration = du
        set.start()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                setView(FragmentGameMode())
                imgControlGameLevels.isEnabled = true
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        keyMode = "home"
    }

    private fun animateImageToOut() {
        keyMode = "out"
        imgControlGameLevels.isEnabled = false
        var scX = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_X, 1f, 1.2f, sc)
        var scY = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_Y, 1f, 1.2f, sc)
        var op = ObjectAnimator.ofFloat(imgControlGameLevels, View.ALPHA, 1f, fade)
        var set = AnimatorSet()
        set.playTogether(scX, scY, op)
        set.duration = du
        set.start()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                imgControlGameLevels.setImageResource(R.drawable.ic_close)
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
        scX = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_X, sc, 1.2f, 1f)
        scY = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_Y, sc, 1.2f, 1f)
        op = ObjectAnimator.ofFloat(imgControlGameLevels, View.ALPHA, fade, 1f)
        set = AnimatorSet()
        set.playTogether(scX, scY, op)
        set.startDelay = du
        set.duration = du
        set.start()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                setView(FragmentMainMenu())
                imgControlGameLevels.isEnabled = true
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        scX = ObjectAnimator.ofFloat(LLProgress, View.SCALE_X, 1f, 1.2f, 0.1f)
        scY = ObjectAnimator.ofFloat(LLProgress, View.SCALE_Y, 1f, 1.2f, 0.1f)
        op = ObjectAnimator.ofFloat(LLProgress, View.ALPHA, 1f, 0f)
        set = AnimatorSet()
        set.playTogether(scX, scY, op)
        set.duration = 2 * du
        set.start()
    }

    fun animateImageToBack() {
        keyMode = "back"
        imgControlGameLevels.isEnabled = false
        var scX = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_X, 1f, 1.2f, sc)
        var scY = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_Y, 1f, 1.2f, sc)
        var op = ObjectAnimator.ofFloat(imgControlGameLevels, View.ALPHA, 1f, fade)
        var set = AnimatorSet()
        set.playTogether(scX, scY, op)
        set.duration = du
        set.start()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                imgControlGameLevels.setImageResource(R.drawable.back_symbol)
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
        scX = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_X, sc, 1.2f, 1f)
        scY = ObjectAnimator.ofFloat(imgControlGameLevels, View.SCALE_Y, sc, 1.2f, 1f)
        op = ObjectAnimator.ofFloat(imgControlGameLevels, View.ALPHA, fade, 1f)
        set = AnimatorSet()
        set.playTogether(scX, scY, op)
        set.startDelay = du
        set.duration = du
        set.start()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                setView(FragmentGameLevel())
                imgControlGameLevels.isEnabled = true
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }


    fun setView(fragment: Fragment) {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.abc_fade_in,
            R.anim.abc_fade_out
        ).replace(R.id.containerGameLevels, fragment).commit()
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.imgControlGameLevels -> when (keyMode) {
                "out" -> this.finish()
                "home" -> {
                    animateImageToOut()
                }
                "back" -> {
                    animateImageToHome()
                }
            }

        }
    }


}