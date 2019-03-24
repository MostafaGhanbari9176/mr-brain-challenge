package ir.pepotec.app.game.model

data class DModeA(var id:Int, var alpha:Float, var pX: Int,var pY:Int, var sLocation:Int)

data class DMainMenu(var id:Int, var subject:String, var iUrl:String)

data class DGameMode(var id:Int, var subject:String, var iUrl:String)

data class DGameLevel(var id:Int, var subject:String, var iUrl:String, var pId:Int, var gId:Int)
