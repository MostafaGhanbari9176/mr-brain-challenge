package ir.pepotec.app.game.ui.activityMain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.ItemData
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.uses.ButtonEvent
import kotlinx.android.synthetic.main.item_main_menu.view.*


class AdapterMainMenu(
    private val source: ArrayList<ItemData>,
    private val listener: () -> Unit
) :
    RecyclerView.Adapter<AdapterMainMenu.ViewHolder>() {

    val ctx: Context = App.instance

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.item_main_menu, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = source.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(source[position], position)
    }

    class ViewHolder(itemView: View, private val listener: () -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun binder(data: ItemData, position: Int) {
            with(data) {
                itemView.txtMainMenu.text = subject
                itemView.txtMainMenu.setShadowLayer(1f, -5f, 5f, R.color.primaryColor)
                itemView.setOnTouchListener { v, event ->
                    ButtonEvent(v, event, R.raw.sound_primary)
                    false }
                itemView.setOnClickListener {
                    listener()
                }
            }
        }

    }
}