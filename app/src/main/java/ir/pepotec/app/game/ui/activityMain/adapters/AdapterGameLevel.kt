package ir.pepotec.app.game.ui.activityMain.adapters

import android.animation.ValueAnimator
import android.graphics.Point
import android.graphics.drawable.Animatable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
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
import java.lang.Exception

class AdapterGameLevel(
    private val source: ArrayList<ItemData>,
    private val layoutManager: GridLayoutManager,
    private val listener: (levelId: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var highlightPosition = source.size - 1

    init {
        val h = Handler()
        Thread(
            Runnable {
                for (o in source) {
                    if (o.lock == 1) {
                        highlightPosition = source.indexOf(o) - 1
                        break
                    }
                    h.post {
                        try {
                            layoutManager.scrollToPosition(highlightPosition)
                        } catch (e: Exception) {
                        }
                    }
                }

            }
        ).start()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1)
            HolderLock(LayoutInflater.from(App.instance).inflate(R.layout.item_game_level, parent, false))
        else
            HighlightHolder(LayoutInflater.from(App.instance).inflate(R.layout.item_game_level, parent, false))

    }

    override fun getItemCount(): Int = source.size

    override fun getItemViewType(position: Int): Int {
        return if (position == highlightPosition) 2 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val v = holder.itemView
        if (position == highlightPosition)
            startAndRepeatAnimate(v.imgLevelMenu.drawable as Animatable)
        initItemView(position, v)
        v.setOnTouchListener { v, event ->
            ButtonEvent(v, event)
            false
        }
        with(source[position]) {
            v.txtSubjectLevel.text = "$id"
            if (lock == 1) {
                v.txtScoreLevel.visibility = View.GONE
                v.setOnClickListener {}
                v.imgStateLevel.setImageDrawable(
                    ContextCompat.getDrawable(
                        App.instance,
                        R.drawable.ic_lock_black_24dp
                    )
                )
            } else {
                v.txtScoreLevel.visibility = View.VISIBLE
                v.setOnClickListener {
                    listener(id)
                }
                v.imgStateLevel.setImageDrawable(
                    ContextCompat.getDrawable(
                        App.instance,
                        R.drawable.item_level_progress
                    )
                )
                animateProgress(score, v)
            }
        }
    }

    private fun initItemView(position: Int, itemView: View) {

        val p = Point()
        (App.instance as ActivityMain).windowManager.defaultDisplay.getRealSize(p)
        val param = RelativeLayout.LayoutParams(p.x / 2, p.x / 2)
        itemView.layoutParams = param
        if (position == 0 || position == 1)
            setMargin(itemView)
    }

    private fun animateProgress(score: Int, itemView: View) {
        ValueAnimator.ofInt(0, score).apply {
            duration = 500
            startDelay = 100
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                val vector = VectorChildFinder(App.instance, R.drawable.item_level_progress, itemView.imgStateLevel)
                val path = vector.findPathByName("progress") as VectorDrawableCompat.VFullPath
                val v = it.animatedValue as Int
                path.trimPathEnd = (if (v == 0) 1 else v) / 100f
                itemView.txtScoreLevel.text = "$v"
            }
            start()
        }
    }

    private fun setMargin(v: View) {
        val params = (v.layoutParams) as RelativeLayout.LayoutParams
        params.topMargin = ((App.instance) as ActivityMain).imgProgressAM.height
    }

    private fun startAndRepeatAnimate(a: Animatable) {
        val h = Handler()
        Thread(
            Runnable {
                while (true) {
                    Thread.sleep(100)
                    if (!a.isRunning)
                        h.post { a.start() }
                }
            }
        ).start()
    }

    class HolderLock(itemView: View) : RecyclerView.ViewHolder(itemView)

    class HighlightHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}