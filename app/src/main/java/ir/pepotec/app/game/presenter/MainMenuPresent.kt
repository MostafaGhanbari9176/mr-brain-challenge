package ir.pepotec.app.game.presenter

import ir.pepotec.app.game.model.DMainMenu

class MainMenuPresent(private var listener: MainMenuInterface) {
    interface MainMenuInterface {
        fun getMainMenuList(data:ArrayList<DMainMenu>)
    }
    fun getData(){
        val data = ArrayList<DMainMenu>()
        for (i in 0..10){
            data.add(DMainMenu(i, "Subject$i","iMage/g.png"))
        }

        listener.getMainMenuList(data)
    }
}