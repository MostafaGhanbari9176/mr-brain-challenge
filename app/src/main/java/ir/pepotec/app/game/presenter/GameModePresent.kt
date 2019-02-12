package ir.pepotec.app.game.presenter

import ir.pepotec.app.game.model.DGameMode

class GameModePresent(private val listener: ModeGameInterface) {

    interface ModeGameInterface{
        fun modeMenuData(data: ArrayList<DGameMode>)
    }

    fun getData(){
        var data = ArrayList<DGameMode>()
        for (i in 0..10){
            data.add(DGameMode(i, "Subject$i", ""))
        }
        listener.modeMenuData(data)
    }
}