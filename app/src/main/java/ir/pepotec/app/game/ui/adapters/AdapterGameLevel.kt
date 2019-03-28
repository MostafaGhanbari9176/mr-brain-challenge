package ir.pepotec.app.game.ui.adapters

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ViewAnimator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.ItemData
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import ir.pepotec.app.game.ui.uses.ButtonEvent
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.item_game_level.view.*

class AdapterGameLevel(
    private val source: ArrayList<ItemData>,
    private val listener: (levelId:Int) -> Unit
) : RecyclerView.Adapter<AdapterGameLevel.Holder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(App.instance).inflate(R.layout.item_game_level, p0, false), listener)
    }

    override fun getItemCount(): Int = source.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binder(source[position], position)
    }

    class Holder(itemView: View, private val listener: (levelId:Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun binder(data: ItemData, position:Int) {
            if(position == 0 || position == 1)
                setMargin(itemView)
            itemView.setOnTouchListener{v, event ->
                ButtonEvent(v, event)
                false
            }

            with(data) {
                itemView.txtSubjectLevel.text = subject
               if(lock == 1) {
                   itemView.txtScoreLevel.visibility = View.GONE
                   itemView.imgLevelMenu.setImageDrawable(
                       ContextCompat.getDrawable(
                           App.instance,
                           R.drawable.item_level_lock
                       )
                   )
                   itemView.setOnClickListener {}
               }
                else {
                   itemView.txtScoreLevel.visibility = View.VISIBLE
                   itemView.txtScoreLevel.text = score.toString()
                   itemView.imgLevelMenu.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.item_level))
                   itemView.setOnClickListener {
                       listener(id)
                   }
                   animateProgress(score.toFloat())
               }
            }
        }

        private fun animateProgress(score: Float) {
            ValueAnimator.ofFloat(0f,score).apply {
                duration = 500
                startDelay = 100
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    val vector = VectorChildFinder(App.instance, R.drawable.item_level, itemView.imgLevelMenu)
                    val path = vector.findPathByName("progress") as VectorDrawableCompat.VFullPath
                    path.trimPathEnd = it.animatedValue as Float / 100
                }
                start()
            }
        }

        private fun setMargin(v: View) {
            val params = (v.layoutParams) as GridLayoutManager.LayoutParams
            params.topMargin = ((App.instance) as ActivityMain).LLProgress.height
        }
    }
}