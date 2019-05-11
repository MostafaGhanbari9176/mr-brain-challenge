package ir.pepotec.app.game.ui.activityMain

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.uses.ButtonEvent
import kotlinx.android.synthetic.main.fragment_help_movie.*

class FragmentHelpMovie : Fragment() {

    private val act = (App.instance as ActivityMain)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_help_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        App.fullScreen(act)
        btnCloseHelpMovie.setOnTouchListener { v, event ->
            ButtonEvent(v, event)
            false
        }
        btnCloseHelpMovie.setOnClickListener {
            act.setView(FragmentMainMenu())
        }

        val path = "android.resource://"+act.packageName+"/"+R.raw.help2
        videoView.setVideoURI(Uri.parse(path))
        videoView.setOnCompletionListener {
            act.setView(FragmentMainMenu())
        }
        videoView.start()

    }
}