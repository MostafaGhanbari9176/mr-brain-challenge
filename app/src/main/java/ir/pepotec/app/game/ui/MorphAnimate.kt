package ir.pepotec.app.game.ui

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import ir.pepotec.app.game.R
import kotlinx.android.synthetic.main.morph.*
import org.jetbrains.anko.toast

class MorphAnimate : AppCompatActivity() {

    var x = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.morph)
        App.fullScreen(this)
        btnMenuLoserDialog.setOnClickListener {
            toast("menuPressed")
        }
        btnReplayLoserDialog.setOnClickListener {
            toast("replayPressed")
        }
        imgMorph.setOnClickListener {
            if(!x) {
                x=true
                imgMorph.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.loser_message))

            }else{
                x = false
                imgMorph.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.winner_message))
            }
            (imgMorph.drawable as Animatable).start()
            }
    }

}