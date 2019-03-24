package ir.pepotec.app.game.ui

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ir.pepotec.app.game.R
import kotlinx.android.synthetic.main.morph.*

class MorphAnimate : AppCompatActivity() {

    var x = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.morph)
        App.fullScreen(this)

        imgMorph.setOnClickListener {

            x = if (x == 0) {
                imgMorph.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.menu_animate))
                val d = (imgMorph.drawable as AnimatedVectorDrawable)
                d.start()
                1
            } else if (x == 1) {
                imgMorph.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.menu_close_animate))
                val d = (imgMorph.drawable as AnimatedVectorDrawable)
                d.start()
                2
            } else if (x == 2) {
                imgMorph.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.menu_animate_mute))
                val d = (imgMorph.drawable as AnimatedVectorDrawable)
                d.start()
                3
            } else {
                imgMorph.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.menu_close_animate_mute))
                val d = (imgMorph.drawable as AnimatedVectorDrawable)
                d.start()
                0
            }
            /* doAsync {
                 while (true) {
                     if (!(d.isRunning))
                         break
                 }
                 uiThread {

                 }
             }*/

        }

        btn1.setOnClickListener {
            startHelp()
        }
        btn2.setOnClickListener {
            val li:LayoutInflater = LayoutInflater.from(this@MorphAnimate)
            val v: View = li.inflate(R.layout.test, null, false)
            val d: ViewGroup = window.decorView as ViewGroup
            d.addView(v)
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