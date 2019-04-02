package ir.pepotec.app.game.ui.gameModes.mode_d

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation.INFINITE
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.item_mode_d.view.*

class AdapterModeD(private val p: Point, private val listener: () -> Unit) :
    RecyclerView.Adapter<AdapterModeD.Holder>() {

    private val length = p.x/6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(App.instance).inflate(R.layout.item_mode_d, parent, false), p, length)
    }

    override fun getItemCount(): Int = 100000

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindData(position, listener)
    }

    class Holder(v: View, private val p: Point, private val length:Int) : RecyclerView.ViewHolder(v) {
        val x = 0
        fun onBindData(position: Int, listener: () -> Unit) {
            itemView.txtPosition.text = "$position"
            initItemView()
            ObjectAnimator.ofFloat(itemView.LLTest, View.TRANSLATION_Y, -100f, App.getBlockHeight() + 100).apply {
                duration = 5000
                interpolator = AccelerateInterpolator()
                repeatCount = INFINITE
                start()
            }
        }

        private fun initItemView() {
            itemView.layoutParams.height = p.y / 2
            itemView.LLBus1ItemModeD.layoutParams.width = length
            itemView.LLBus2ItemModeD.layoutParams.width = length

            val r:Int = (0 .. 4).random()

            itemView.LLBlockItemModeD.layoutParams.width = r * length
        }
    }
}