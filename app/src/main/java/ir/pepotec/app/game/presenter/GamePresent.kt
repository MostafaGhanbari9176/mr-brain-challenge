package ir.pepotec.app.game.presenter

import ir.pepotec.app.game.model.DBlock
import ir.pepotec.app.game.model.DGame
import ir.pepotec.app.game.model.DPuzzle

class GamePresent(private val listener:GamePresentInterface) {

    interface GamePresentInterface{
        fun gameData(data:DGame)
    }

    fun getGameData(gId:Int){

        val blocks = ArrayList<DBlock>()
        blocks.add(DBlock(0,1,1,2))
        if(gId == 0)
            listener.gameData(DGame(1,1f,1,blocks))
        if(gId == 1)
            listener.gameData(DGame(1,2f,1,blocks))
        if(gId == 2)
            listener.gameData(DGame(1,0.5f,1,blocks))
    }
}