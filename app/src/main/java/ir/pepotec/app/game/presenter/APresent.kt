package ir.pepotec.app.game.presenter

import ir.pepotec.app.game.model.DModeA

class APresent(private val listener:GamePresentInterface) {

    interface GamePresentInterface{
        fun gameData(data: DModeA)
    }

    fun getGameData(gId:Int){

        if(gId == 0)
            listener.gameData(DModeA(0, 1f, 1, 0,1))
        if(gId == 1)
            listener.gameData(DModeA(1, 0.5f, 0, 0,1))
        if(gId == 2)
            listener.gameData(DModeA(2, 2f, 2, 1,0))
        if(gId == 3)
            listener.gameData(DModeA(3,0.75f, 1,0,2))
    }
}