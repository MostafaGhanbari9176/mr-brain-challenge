package ir.pepotec.app.game.ui.activityMain.adapters

import android.animation.ValueAnimator
import android.graphics.Point
import android.graphics.drawable.Animatable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.GameModeData
import ir.pepotec.app.game.model.Pref
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import ir.pepotec.app.game.ui.uses.ButtonEvent
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.item_game_mode.view.*
import kotlinx.android.synthetic.main.item_infinite.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AdapterGameMode(private val source: ArrayList<GameModeData>, private val listener: (modeId: String) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val li = LayoutInflater.from(App.instance)
        return if (viewType == 1)
            HolderOne(li.inflate(R.layout.item_game_mode, parent, false), listener)
        else
            HolderTwo(li.inflate(R.layout.item_infinite, parent, false), listener)
    }

    override fun getItemCount(): Int = source.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 3) 2 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == 1)
            (holder as HolderOne).binder(source[position], position)
        else
            (holder as HolderTwo).binder(source[position], position)
    }

    class HolderOne(itemView: View, private val listener: (modeId: String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        fun binder(data: GameModeData, position: Int) {
            initItemView(position)
            itemView.setOnTouchListener { v, event ->
                ButtonEvent(v, event)
                false
            }
            with(data) {
                itemView.subjectItemMode.text = subject
                itemView.imgItemMode.setImageDrawable(
                    ContextCompat.getDrawable(
                        App.instance,
                        when (modeId) {
                            "a" -> R.drawable.item_mode_a
                            "b" -> R.drawable.item_mode_b
                            "c" -> R.drawable.item_mode_c
                            else -> R.drawable.item_mode_a
                        }
                    )
                )
                if (modeId == "c") {
                    startAndRepeatAnim()
                } else
                    (itemView.imgItemMode.drawable as Animatable).start()
                if(lock == 1)
                {
                    itemView.apply{
                        setOnClickListener {  }
                        scoreItemMode.visibility = View.GONE
                        imgStateItemMode.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.ic_lock_black_24dp))
                    }

                }
                else
                {
                    itemView.apply{
                        setOnClickListener {
                            listener(modeId)
                        }
                        scoreItemMode.visibility = View.VISIBLE
                        imgStateItemMode.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.item_mode_progress))
                        animateProgress(scoreAverage)
                    }
                }
            }


        }

        private fun startAndRepeatAnim() {
            val a = itemView.imgItemMode.drawable as Animatable
            a.start()
            val h = Handler()
            Thread(
                Runnable {
                    while(true)
                    {
                        Thread.sleep(100)
                        if(!a.isRunning)
                            h.post { a.start() }
                    }
                }
            ).start()
        }

        private fun initItemView(position: Int) {
            val p = Point()
            (App.instance as ActivityMain).windowManager.defaultDisplay.getRealSize(p)
            itemView.layoutParams.width = p.x / 2
            itemView.layoutParams.height = (p.x / 2f * 1.5f).toInt()
            if (position == 0 || position == 1)
                setMargin(itemView)

        }

        private fun animateProgress(score: Int) {

            ValueAnimator.ofInt(0,  score).apply {
                duration = 500
                startDelay = 400
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    val vector = VectorChildFinder(App.instance, R.drawable.item_mode_progress, itemView.imgStateItemMode)
                    val path = vector.findPathByName("progress") as VectorDrawableCompat.VFullPath
                    val v = it.animatedValue as Int
                    path.trimPathEnd =(if(v == 0) 1 else v) / 100f
                    itemView.scoreItemMode.text = "$v"
                }
                start()
            }
        }

        private fun setMargin(v: View) {
            val params = (v.layoutParams) as GridLayoutManager.LayoutParams
            params.topMargin = ((App.instance) as ActivityMain).imgProgressAM.height
        }
    }

    class HolderTwo(itemView: View, private val listener: (modeId: String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        fun binder(data: GameModeData, position: Int) {
            itemView.setOnTouchListener { v, event ->
                ButtonEvent(v, event)
                false
            }
            (itemView.LLItemInfinite.background as Animatable).start()
            initView()
            with(data)
            {
                itemView.txtScoreInfinite.text = "${Pref().getIntegerValue(Pref.score, 0)}"
                if (lock == 0) {
                    itemView.txtScoreInfinite.visibility = View.VISIBLE
                    itemView.imgLockInfinite.visibility = View.GONE
                    itemView.setOnClickListener { listener(modeId) }
                } else {
                    itemView.txtScoreInfinite.visibility = View.GONE
                    itemView.imgLockInfinite.visibility = View.VISIBLE
                    itemView.setOnClickListener { }
                }
            }
        }

        private fun initView() {
            val p = Point()
            (App.instance as ActivityMain).windowManager.defaultDisplay.getRealSize(p)
            itemView.layoutParams.width = p.x / 2
            itemView.layoutParams.height = (p.x / 2f * 1.5f).toInt()
            itemView.parentInfiniteItem.layoutParams.width = p.x/2 -120
            itemView.parentInfiniteItem.layoutParams.height = p.x/2 -120

        }
    }
}