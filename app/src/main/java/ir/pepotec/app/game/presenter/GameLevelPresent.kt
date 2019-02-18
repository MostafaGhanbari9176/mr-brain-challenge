package ir.pepotec.app.game.presenter

import ir.pepotec.app.game.model.DGameLevel

class GameLevelPresent(private val listenr: GameLevelInterface) {

    interface GameLevelInterface{
        fun GameLevelData(data: ArrayList<DGameLevel>)
    }

    fun getData(){
        val data = ArrayList<DGameLevel>()
        for(i in 0..3){
            data.add(DGameLevel(i,"Subject$i", "", i, i))
        }
        listenr.GameLevelData(data)
    }
}