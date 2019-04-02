package ir.pepotec.app.game.ui.activityMain.adapters

import android.animation.ValueAnimator
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.GameModeData
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import ir.pepotec.app.game.ui.uses.ButtonEvent
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.item_game_mode.view.*

class AdapterGameMode(private val source: ArrayList<GameModeData>, private val listener: (modeId: String) -> Unit) :
    RecyclerView.Adapter<AdapterGameMode.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(App.instance).inflate(R.layout.item_game_mode, p0, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = source.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(source[position], position)
    }

    class ViewHolder(itemView: View, private val listener: (modeId: String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        fun binder(data: GameModeData, position: Int) {
            initItemView(position)
            itemView.setOnTouchListener { v, event ->
                ButtonEvent(v, event)
                false
            }
            with(data) {
                itemView.txtSubjectItemMode.text = subject
                if (lock == 1) {
                    itemView.txtScoreMode.visibility = View.GONE
                    itemView.imgModeMenu.setImageDrawable(
                        ContextCompat.getDrawable(
                            App.instance,
                            R.drawable.item_game_mode_lock
                        )
                    )
                    itemView.setOnClickListener {}
                } else {
                    itemView.txtScoreMode.visibility = View.VISIBLE
                    itemView.txtScoreMode.text = scoreAverage.toString()
                    itemView.imgModeMenu.setImageDrawable(
                        ContextCompat.getDrawable(
                            App.instance,
                            R.drawable.item_game_mode
                        )
                    )
                    animateProgress(scoreAverage.toFloat())
                    itemView.setOnClickListener {
                        listener(modeId)
                    }
                }
            }
        }

        private fun initItemView(position: Int) {
            val p = Point()
            (App.instance as ActivityMain).windowManager.defaultDisplay.getRealSize(p)
            val param = RelativeLayout.LayoutParams(p.x / 2, p.x / 2)
            itemView.layoutParams = param
            if (position == 0 || position == 1)
                setMargin(itemView)

        }

        private fun animateProgress(score: Float) {

            ValueAnimator.ofFloat(0f, score).apply {
                duration = 500
                startDelay = 400
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    val vector = VectorChildFinder(App.instance, R.drawable.item_game_mode, itemView.imgModeMenu)
                    val path = vector.findPathByName("progress") as VectorDrawableCompat.VFullPath
                    path.trimPathEnd = it.animatedValue as Float / 100
                }
                start()
            }
        }

        private fun setMargin(v: View) {
            val params = (v.layoutParams) as RelativeLayout.LayoutParams
            params.topMargin = ((App.instance) as ActivityMain).imgProgressAM.height
        }
    }
}