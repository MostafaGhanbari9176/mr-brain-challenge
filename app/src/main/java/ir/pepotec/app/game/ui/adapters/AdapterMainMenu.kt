package ir.pepotec.app.game.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.DMainMenu
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.item_main_menu.view.*

class AdapterMainMenu(
    private val source: ArrayList<DMainMenu>,
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
        fun binder(data: DMainMenu, position: Int) {
            with(data) {
                if (position != 0)
                    App.loadImage(itemView.imgMainMenu, iUrl, R.drawable.game_mode_item_back)
                else
                    loadGif(itemView.imgMainMenu)
                itemView.txtMainMenu.text = subject
                itemView.txtMainMenu.setShadowLayer(1f, -5f, 5f, R.color.primaryColor)
                itemView.setOnClickListener {
                    listener()
                    //Toast.makeText(ctx, "OKOKOK",Toast.LENGTH_SHORT).show()
                }
            }
            //itemView.imageView.setImageResource(R.drawable.main_menu_back_item)
        }

        private fun loadGif(img: ImageView) {
            Glide.with(App.instance)
                .asGif()
                .load(R.drawable.game_first_menu_item)
                .into(img)
        }
    }
}