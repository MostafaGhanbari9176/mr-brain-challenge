package ir.pepotec.app.game.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App

class AdapterLevelMenu (private val source:ArrayList<String>): RecyclerView.Adapter<AdapterLevelMenu.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
       val view:View = LayoutInflater.from(App.instance).inflate(R.layout.item_level_menu, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = source.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.binder(source[position])
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun binder(data:String){}
    }
}