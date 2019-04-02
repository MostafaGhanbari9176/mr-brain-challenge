package ir.pepotec.app.game.presenter

import android.content.Context
import ir.pepotec.app.game.model.ItemData
import ir.pepotec.app.game.model.ModeAData
import ir.pepotec.app.game.model.ModeBData
import ir.pepotec.app.game.model.ModeCData
import ir.pepotec.app.game.model.local_data_base.ModeADb
import ir.pepotec.app.game.model.local_data_base.ModeBDb
import ir.pepotec.app.game.model.local_data_base.ModeCDb
import ir.pepotec.app.game.ui.App

class PGameLevel(private val listener: PGameLevelListener) {

    private val ctx: Context = App.instance

    interface PGameLevelListener {
        fun levelData(data: ArrayList<ItemData>)
    }

    fun getLevelData(modeId: String) {

        when (modeId) {
            "a" -> listener.levelData(mapModeAData(ModeADb(ctx).get()))
            "b" -> listener.levelData(mapModeBData(ModeBDb(ctx).get()))
            "c" -> listener.levelData(mapModeCData(ModeCDb(ctx).get()))
            "d" -> listener.levelData(getLevelModeD())

        }
    }

    private fun getLevelModeD(): ArrayList<ItemData> {
        val data = ArrayList<ItemData>()
        data.add(
            ItemData(
                0,
                "آسان",
                0,
                0
            )
        )
        data.add(
            ItemData(
                1,
                "سخت",
                0,
                0
            )
        )
        data.add(
            ItemData(
                2,
                "دشوار",
                0,
                0
            )
        )
        return data
    }

    private fun mapModeAData(data: ArrayList<ModeAData>): ArrayList<ItemData> {

        val dataArr = ArrayList<ItemData>()
        for (d in data) {
            with(d) {
                dataArr.add(ItemData(levelId, subject, score, lock))
            }
        }
        return dataArr
    }

    private fun mapModeCData(data: ArrayList<ModeCData>): ArrayList<ItemData> {

        val dataArr = ArrayList<ItemData>()
        for (d in data) {
            with(d) {
                dataArr.add(ItemData(levelId, subject, score, lock))
            }
        }
        return dataArr
    }

    private fun mapModeBData(data : ArrayList<ModeBData>) : ArrayList<ItemData>{

        val dataArr = ArrayList<ItemData>()
        for (d in data) {
            with(d) {
                dataArr.add(ItemData(levelId, subject, score, lock))
            }
        }
        return dataArr
    }
}