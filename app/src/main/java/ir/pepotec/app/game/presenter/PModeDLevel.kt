package ir.pepotec.app.game.presenter

import android.content.Context
import ir.pepotec.app.game.model.Pref
import ir.pepotec.app.game.ui.App

class PModeDLevel(private val listener: PModeDInterface) {
    private val ctx: Context = App.instance

    interface PModeDInterface {
        fun result(lScore: Int, lBlock: Int)
    }

    fun saveData(score: Int, block: Int) {
        var hScore = false
        val p =Pref()
        val lScore = p.getIntegerValue(Pref.score, 0)
        val lBlock = p.getIntegerValue(Pref.block, 0)
        if(score > lScore) {
            p.saveIntegerValue(Pref.score, score)
            hScore = true
        }
        if(block > lBlock)
            p.saveIntegerValue(Pref.block, block)

        listener.result(lScore, lBlock)
    }
}