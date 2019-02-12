package ir.pepotec.app.game.ui.adapters

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.DGameMode
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_game_mode.view.*

class AdapterGameMode(private val source: ArrayList<DGameMode>, private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<AdapterGameMode.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(App.instance).inflate(R.layout.item_game_mode, p0, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = source.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(source[position], position)
    }

    class ViewHolder(itemView: View, private val listener: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun binder(data: DGameMode, position: Int) {
            if(position == 0 || position == 1)
                setMargin(itemView)
            with(data) {
                itemView.imgModeMenu.setOnClickListener {
                    listener(id)
                }
            }
        }

        private fun setMargin(v:View) {
            val params = (v.layoutParams) as GridLayoutManager.LayoutParams
            params.topMargin = ((App.instance) as ActivityMain).LLProgress.height
        }
    }
}