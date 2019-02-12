package ir.pepotec.app.game.ui.adapters

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.DGameLevel
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_game_level.view.*

class AdapterGameLevel(
    private val source: ArrayList<DGameLevel>,
    private val listener: (DGameLevel) -> Unit
) : RecyclerView.Adapter<AdapterGameLevel.Holder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(App.instance).inflate(R.layout.item_game_level, p0, false), listener)
    }

    override fun getItemCount(): Int = source.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binder(source[position], position)
    }

    class Holder(itemView: View, private val listener: (DGameLevel) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun binder(data: DGameLevel, position:Int) {
            if(position == 0 || position == 1)
                setMargin(itemView.imgLevelMenu)
            with(data) {
                itemView.setOnClickListener {
                    listener(this)
                }
            }
        }

        private fun setMargin(v:ImageView) {
             val params = (v.layoutParams) as GridLayoutManager.LayoutParams
            params.topMargin = ((App.instance) as ActivityMain).LLProgress.height

        }
    }
}