package ir.pepotec.app.game.presenter

import android.content.Context
import ir.pepotec.app.game.model.ItemData
import ir.pepotec.app.game.model.ModeAData
import ir.pepotec.app.game.model.local_data_base.ModeADb
import ir.pepotec.app.game.ui.App

class PGameLevel(private val listener:PGameLevelListener) {

    private val ctx:Context = App.instance

    interface PGameLevelListener{
        fun levelData(data:ArrayList<ItemData>)
    }

    fun getLevelData(modeId:String){

        when(modeId){
            "a" -> {
                listener.levelData(dataMaper(ModeADb(ctx).get()))
            }
            "b" -> {}
        }
    }

    private fun dataMaper(data: ArrayList<ModeAData>): ArrayList<ItemData> {

        val dataArr = ArrayList<ItemData>()
        for (d in data){
            with(d) {
                dataArr.add(ItemData(levelId, subject, score, lock))
            }
        }
        return dataArr
    }
}