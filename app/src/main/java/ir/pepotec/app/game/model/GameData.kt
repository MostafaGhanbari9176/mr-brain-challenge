package ir.pepotec.app.game.model

data class DPuzzle(var id:Int, var location:Int)

data class DBlock(var id:Int, var rLength:Int, var lLength:Int , var location:Int)

data class DGame(var id:Int, var alpha:Float, var pLocation: Int, var block: ArrayList<DBlock>)

data class DMainMenu(var id:Int, var subject:String, var iUrl:String)

data class DGameMode(var id:Int, var subject:String, var iUrl:String)

data class DGameLevel(var id:Int, var subject:String, var iUrl:String, var pId:Int, var gId:Int)
