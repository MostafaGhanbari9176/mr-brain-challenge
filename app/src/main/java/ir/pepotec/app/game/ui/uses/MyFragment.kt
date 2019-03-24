package ir.pepotec.app.game.ui.uses

import android.widget.Toast
import androidx.fragment.app.Fragment
import ir.pepotec.app.game.ui.App

abstract class MyFragment : Fragment() {

    abstract fun myTouchListener(dx: Int, dy: Int)
    abstract fun myClickListener()
    abstract fun runHelper()
    fun toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(App.instance, message, duration).show()
    }
}