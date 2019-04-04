package ir.pepotec.app.game.ui

import android.graphics.Point
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.devs.vectorchildfinder.VectorDrawableCompat
import ir.pepotec.app.game.R
import kotlinx.android.synthetic.main.morph.*

class MorphAnimate : AppCompatActivity() {

    var x = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.morph)
        App.fullScreen(this)
        val p = Point()
        this.windowManager.defaultDisplay.getRealSize(p)
        imgMorph.layoutParams.height = p.x
        imgMorph2.layoutParams.height = p.x
        imgMorph.setOnClickListener {
            (imgMorph.drawable as AnimatedVectorDrawable).start()

        }
        imgMorph2.setOnClickListener {
            (imgMorph2.drawable as AnimatedVectorDrawable).start()

        }


    }

    private fun startHelp() {
/*
        val v = HelpModeA(this)
        val pv = window.decorView as ViewGroup
        pv.addView(v)*/
//
//
//         val inflater = LayoutInflater.from(this@MorphAnimate)
//
//        val t = SimpleTarget.Builder(this)
//            .setPoint(btn1)
//            .build()
//
//        Spotlight.with(this)
//            .setDuration(1000)
//            .setOverlayColor(R.color.primaryColor)
//            .setAnimation(AccelerateInterpolator())
//            .setTargets(t)
//            .setClosedOnTouchedOutside(true)
//            .start()
    }

}