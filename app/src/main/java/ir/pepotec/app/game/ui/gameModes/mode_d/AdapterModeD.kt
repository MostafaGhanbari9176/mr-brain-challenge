package ir.pepotec.app.game.ui.gameModes.mode_d

import android.graphics.Point
import android.graphics.drawable.Animatable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.item_mode_d.view.*

class AdapterModeD(private val p: Point, private val easy: Boolean, private val listener:InterFaceAdapterD) :
    RecyclerView.Adapter<AdapterModeD.Holder>() {

    private val length = p.x / 6
    interface InterFaceAdapterD{
        fun currentView(v:View, position:Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(App.instance).inflate(R.layout.item_mode_d, parent, false), p, easy, length)
    }

    override fun getItemCount(): Int = 100000

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindData(position)
        listener.currentView(holder.itemView, position)
    }

    class Holder(v: View, private val p: Point, private val easy: Boolean, private val length: Int) :
        RecyclerView.ViewHolder(v) {
        private val h = p.y / 2
        private var pos = 0
        fun onBindData(position: Int) {
            val r: Int = if (easy) (0..2).random() else (0..4).random()
            pos = position
            itemView.txtPosition.text = "$position"
            initItemView(r)
            startAnimation()
            showBrain(r)
        }

        private fun showBrain(r: Int) {
            if (easy) {
                if (pos % 5 == 0 && r!=0)
                    itemView.imgBrainItemModeD.alpha = 1f
                else
                    itemView.imgBrainItemModeD.alpha = 0f
            } else {
                if (pos % 3 == 0 && r!=0)
                    itemView.imgBrainItemModeD.alpha = 1f
                else
                    itemView.imgBrainItemModeD.alpha = 0f
            }

        }

        private fun startAnimation() {
            itemView.apply {
                (anim1ItemD.background as Animatable).start()
                (anim2ItemD.background as Animatable).start()
                (anim3ItemD.background as Animatable).start()
            }
        }

        private fun initItemView(r: Int) {
            itemView.apply {
                layoutParams.height = h
                imgBrainItemModeD.layoutParams.width = length
                LLBus2ItemModeD.layoutParams.width = length
                LLBlockItemModeD.layoutParams.width = r * length
            }
        }
    }
}