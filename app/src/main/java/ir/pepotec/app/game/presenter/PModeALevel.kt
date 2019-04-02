package ir.pepotec.app.game.presenter

import android.content.Context
import ir.pepotec.app.game.model.ModeAData
import ir.pepotec.app.game.model.local_data_base.GameModeDb
import ir.pepotec.app.game.model.local_data_base.ModeADb
import ir.pepotec.app.game.ui.App

class PModeALevel(private val listener: PModeALevelInterface? = null) {

    val ctx: Context = App.instance

    interface PModeALevelInterface {
        fun levelData(data: ModeAData)
    }

    fun getLevelData(levelId: Int) {
        listener?.levelData(ModeADb(ctx).get(levelId))
    }

    fun saveScore(levelId: Int, score: Int) {
        val db = ModeADb(ctx)
        db.saveScore(levelId, score)
        db.unLockLevel(levelId + 1)
        val count = db.getCount()
        var scoreAve = 0
        val scoreList = db.getScore()
        for (s in scoreList)
        {
            scoreAve+=s
        }
        scoreAve /= count
        GameModeDb(ctx).saveScoreAverage("a", scoreAve)
    }
}