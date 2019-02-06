package ir.pepotec.app.game.ui.activities

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.adapters.AdapterLevelMenu
import ir.pepotec.app.game.ui.adapters.AdapterMainMenu
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.item_main_menu.*
import org.jetbrains.anko.toast

class MainMenuActivity : AppCompatActivity() {
    internal val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
      /*  if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            this.window.decorView.systemUiVisibility = flags
            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            val decorView = this.window.decorView
            decorView
                .setOnSystemUiVisibilityChangeListener { visibility ->
                    if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        decorView.systemUiVisibility = flags
                    }
                }
        }
*/
        MainMenu.layoutManager = LinearLayoutManager(this)
        val source = ArrayList<String>()
        for(t in 0..10){
            source.add(t.toString()+"Mostafa")
        }
        val adapter = AdapterMainMenu(source){gotoNextActivity()}
        MainMenu.adapter = adapter

    }

    private fun gotoNextActivity() {
        //toast("INActivity")
        val intent = Intent(this,LevelMenuActivity::class.java)
        startActivity(intent)
    }
}
