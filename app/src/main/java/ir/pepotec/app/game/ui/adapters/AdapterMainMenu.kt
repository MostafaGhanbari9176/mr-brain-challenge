package ir.pepotec.app.game.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.item_main_menu.view.*

class AdapterMainMenu(private val source: ArrayList<String>,
                      private val listener:() -> Unit) :
    RecyclerView.Adapter<AdapterMainMenu.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(App.instance).inflate(R.layout.item_main_menu, parent, false)
        return ViewHolder(view, listener )
    }

    override fun getItemCount(): Int = source.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(source[position])
    }

    class ViewHolder(itemView: View, private val listener: ()->Unit) : RecyclerView.ViewHolder(itemView) {
        fun binder(subject:String) {
            itemView.txtMainMenu.text = subject
            itemView.txtMainMenu.setShadowLayer(1f,-5f,5f, R.color.primaryColor)
            itemView.setOnClickListener{listener()
            //Toast.makeText(App.instance, "OKOKOK",Toast.LENGTH_SHORT).show()
            }
            //itemView.imageView.setImageResource(R.drawable.main_menu_back_item)
        }
    }
}