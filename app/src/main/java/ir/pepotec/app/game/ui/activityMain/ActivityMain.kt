package ir.pepotec.app.game.ui.activityMain

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import ir.pepotec.app.game.R
import ir.pepotec.app.game.presenter.PGameMode
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.main_activity.*


class ActivityMain : AppCompatActivity(), View.OnClickListener {


    private var progress: Float = 0f
    var keyMode = "out"
    val sc = 0.7F
    val du = 120L
    val fade = 0.7f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initViews()
    }

    private fun initViews() {
        App.fullScreen(this)
        animateViews()
        initProgress()
        setView(FragmentMainMenu())
        imgControlGameLevels.setOnClickListener(this)

    }

    private fun initProgress() {
        val p = Point()
        windowManager.defaultDisplay.getRealSize(p)
        val param = RelativeLayout.LayoutParams(p.x, p.x/6)
        imgProgressAM.layoutParams = param
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

    override fun onBackPressed() {
        if (keyMode == "out") {
            this.finish()
            super.onBackPressed()
        } else {
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
            scX = ObjectAnimator.ofFloat(imgProgressAM, View.SCALE_X, 0.1f, 1.2f, 1f)
            scY = ObjectAnimator.ofFloat(imgProgressAM, View.SCALE_Y, 0.1f, 1.2f, 1f)
            op = ObjectAnimator.ofFloat(imgProgressAM, View.ALPHA, 0f, 1f)
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
                animateProgress(PGameMode().getScoreAverage().toFloat())
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

        scX = ObjectAnimator.ofFloat(imgProgressAM, View.SCALE_X, 1f, 1.2f, 0.1f)
        scY = ObjectAnimator.ofFloat(imgProgressAM, View.SCALE_Y, 1f, 1.2f, 0.1f)
        op = ObjectAnimator.ofFloat(imgProgressAM, View.ALPHA, 1f, 0f)
        set = AnimatorSet()
        set.playTogether(scX, scY, op)
        set.duration = 2 * du
        set.start()
    }

    fun animateImageToBack(modeId: String) {
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
                showGameLevel(modeId)
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

    fun animateProgress(to:Float){
        ValueAnimator.ofFloat(progress, to).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                val vector = VectorChildFinder(this@ActivityMain, R.drawable.big_progress, imgProgressAM)
                val path = vector.findPathByName("progress") as VectorDrawableCompat.VFullPath
                path.trimPathEnd = it.animatedValue as Float / 100
            }
            start()
        }
        progress = to
    }

    fun showGameLevel(modeId: String) {
        val f = FragmentGameLevel()
        f.modeId = modeId
        setView(f)
        animateProgress(PGameMode().getScoreAverage(modeId).toFloat())
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

    override fun onResume() {
        super.onResume()
        App.instance = this

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        App.instance = this
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            showGameLevel(data!!.getStringExtra("modeId"))
        }
    }

}