package ir.pepotec.app.game.presenter

import ir.pepotec.app.game.model.DA

class APresent(private val listener:GamePresentInterface) {

    interface GamePresentInterface{
        fun gameData(data: DA)
    }

    fun getGameData(gId:Int){

        if(gId == 0)
            listener.gameData(DA(0, 1f, 1, 1))
        if(gId == 1)
            listener.gameData(DA(1, 0.5f, 0, 1))
        if(gId == 2)
            listener.gameData(DA(2, 2f, 2, 0))
        if(gId == 3)
            listener.gameData(DA(3,0.75f, 1,2))
    }
}