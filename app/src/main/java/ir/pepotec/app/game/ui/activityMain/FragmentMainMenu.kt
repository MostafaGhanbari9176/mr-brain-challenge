package ir.pepotec.app.game.ui.activityMain

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.ItemData
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.adapters.AdapterMainMenu
import kotlinx.android.synthetic.main.fragment_main_menu.*

class FragmentMainMenu : Fragment() {

    private val ctx:Context = App.instance
    lateinit private var act:ActivityMain
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act = activity as ActivityMain
        init()
    }

    private fun init() {
        val listData = ArrayList<ItemData>()
        listData.add(ItemData(0, "بازی مرحله ایی", 0, 0))
        listData.add(ItemData(0, "بازی تحت شبکه", 0, 1))
        listData.add(ItemData(0, "تنظیمات", 0, 0))

        RVMainMenu.layoutManager = LinearLayoutManager(ctx)
        val adapter = AdapterMainMenu(listData){act.animateImageToHome()}
        RVMainMenu.adapter = adapter
    }

}
