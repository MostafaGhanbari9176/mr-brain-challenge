package ir.pepotec.app.game.model

data class GameModeData(
    val modeId: String,
    val subject: String,
    val scoreAverage: Int,
    val lock: Int
)

data class ModeAData(
    val levelId: Int,
    val subject: String,
    val score: Int,
    val lock: Int,
    val alpha: Float,
    val spaceX: Int,
    val puzzleX: Int,
    val puzzleY: Int,
    val isFinally: Int
)

data class ModeBData(
    val levelId: Int,
    val subject: String,
    val score: Int,
    val lock: Int,
    val alpha: Float,
    val puzzleY: Int,
    val isFinally: Int
)

data class ModeCData(
    val levelId: Int,
    val subject: String,
    val score: Int,
    val lock: Int,
    val alpha: Float,
    val spaceX: Int,
    val puzzleX: Int,
    val busX: Int,
    val isFinally: Int
)

data class ItemData(
    val id: Int,
    val subject: String,
    val score: Int,
    val lock: Int
)
