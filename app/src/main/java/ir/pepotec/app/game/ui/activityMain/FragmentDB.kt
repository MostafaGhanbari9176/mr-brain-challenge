package ir.pepotec.app.game.ui.activityMain

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.*
import ir.pepotec.app.game.model.local_data_base.GameModeDb
import ir.pepotec.app.game.model.local_data_base.ModeADb
import ir.pepotec.app.game.model.local_data_base.ModeBDb
import ir.pepotec.app.game.model.local_data_base.ModeCDb
import ir.pepotec.app.game.ui.App

class FragmentDB : Fragment() {

    private val modeASource = ArrayList<ModeAData>()
    private val modeBSource = ArrayList<ModeBData>()
    private val modeCSource = ArrayList<ModeCData>()
    private val alpha = ArrayList<Float>()
    private val ctx = App.instance
    private val act = ctx as ActivityMain
    private val h = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_db, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Thread(
            Runnable {
                ModeADb(ctx).deleteDb()
                ModeBDb(ctx).deleteDb()
                ModeCDb(ctx).deleteDb()
                GameModeDb(ctx).deleteDb()
                Pref().saveIntegerValue(Pref.brain, 100)
                addData()
                createGameModeData()
                createModeAData()
                createModeBData()
                createModeCData()
                Pref().saveBollValue(Pref.dbCreated, true)
                h.post { showHelpMovie() }
            }
        ).start()

    }

    private fun createGameModeData() {
        GameModeDb(ctx).save(GameModeData("a", "مقدماتی", 0, 0))
        GameModeDb(ctx).save(GameModeData("c", "پیشرفته", 0, 1))
        GameModeDb(ctx).save(GameModeData("b", "حرفه ایی", 0, 1))
        GameModeDb(ctx).save(GameModeData("d", "بی پایان", 0, 0))
    }

    private fun showHelpMovie() {

        Pref().saveBollValue(Pref.helpMovie, true)
        AlertDialog.Builder(ctx).apply {
            setTitle("راهنما")
            setMessage("مایل به مشاهده راهنما هستی؟")
            setNegativeButton("نه") { dialog, _ ->
                act.setView(FragmentMainMenu())
                dialog.cancel()
            }
            setPositiveButton("بله") { dialog, _ ->
                act.setView(FragmentHelpMovie())
                dialog.cancel()
            }
            setCancelable(false)
            show()
        }
    }

    private fun createModeCData() {
        for (px in 0..2) {
            for (bx in 0..2) {
                for (s in 0..2) {
                    for (a in 0..4) {
                        modeCSource.add(ModeCData(0, "", 0, 1, alpha[a], s, px, bx, 0, 0))
                    }
                }
            }
        }
        modeCSource[50].lock = 0
        ModeCDb(ctx).save(modeCSource[50])
        modeCSource.removeAt(50)
        while (modeCSource.size > 0) {
            val i = (0..(modeCSource.size - 1)).random()
            if (modeCSource.size == 1)
                modeCSource[i].isFinally = 1
            ModeCDb(ctx).save(modeCSource[i])
            modeCSource.removeAt(i)
        }
    }

    private fun createModeBData() {

        for (py in 0..1) {
            for (a in 0..4) {
                modeBSource.add(ModeBData(0, "", 0, 1, alpha[a], py, 0, 0))
            }

        }
        modeBSource[0].lock = 0
        ModeBDb(ctx).save(modeBSource[0])
        modeBSource.removeAt(0)
        while (modeBSource.size > 0) {
            val i = (0..(modeBSource.size - 1)).random()
            if (modeBSource.size == 1)
                modeBSource[i].isFinally = 1
            ModeBDb(ctx).save(modeBSource[i])
            modeBSource.removeAt(i)
        }
    }

    private fun createModeAData() {
        for (px in 0..2) {
            for (py in 0..1) {
                for (s in 0..2) {
                    for (a in 0..4) {
                        modeASource.add(ModeAData(0, "", 0, 1, alpha[a], s, px, py, 0, 0))
                    }
                }
            }
        }
        modeASource[35].lock = 0
        ModeADb(ctx).save(modeASource[35])
        modeASource.removeAt(35)
        while (modeASource.size > 0) {
            val i = (0..(modeASource.size - 1)).random()
            if (modeASource.size == 1)
                modeASource[i].isFinally = 1
            ModeADb(ctx).save(modeASource[i])
            modeASource.removeAt(i)
        }
    }

    private fun addData() {
        alpha.add(1f)
        alpha.add(2f)
        alpha.add(1.5f)
        alpha.add(0.75f)
        alpha.add(0.5f)
    }
}