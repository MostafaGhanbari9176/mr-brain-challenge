package ir.pepotec.app.game.model.local_data_base

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ir.pepotec.app.game.model.GameModeData

class GameModeDb(
    private val ctx: Context,
    dbName: String = "game_mode_db",
    private val tbName: String = "game_mode_tb"
) : SQLiteOpenHelper(ctx, dbName, null, 1) {

    companion object {
        const val id: String = "id"
        const val modeId: String = "mode_id"
        const val subject: String = "subject"
        const val scoreAverage: String = "score_average"
        const val lock: String = "lock"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $tbName ( $id INTEGER PRIMARY KEY AUTOINCREMENT , $modeId TEXT , $subject TEXT , $scoreAverage INTEGER , $lock BOOLEAN ) ")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(" DROP TABLE IF EXISTS $tbName")
    }

    fun save(data: GameModeData) {
        val writer = this.writableDatabase
        val cValue = ContentValues()

        with(data) {
            cValue.put(GameModeDb.modeId, modeId)
            cValue.put(GameModeDb.subject, subject)
            cValue.put(GameModeDb.scoreAverage, scoreAverage)
            cValue.put(GameModeDb.lock, lock)
        }

        writer.insert(tbName, null, cValue)
        writer.close()
    }

    fun get(): ArrayList<GameModeData> {
        val data = ArrayList<GameModeData>()
        val reader = this.readableDatabase
        val cursor: Cursor = reader.rawQuery("SELECT * FROM $tbName", null)
        if (cursor.moveToFirst()) {
            do {
                data.add(
                    GameModeData(
                        cursor.getString(cursor.getColumnIndex(modeId)),
                        cursor.getString(cursor.getColumnIndex(subject)),
                        cursor.getInt(cursor.getColumnIndex(scoreAverage)),
                        cursor.getInt(cursor.getColumnIndex(lock))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        reader.close()
        return data
    }

    fun getSubject(modeId: String): String {
        var data = "notFound"
        val reader = this.readableDatabase
        val cursor =
            reader.rawQuery(" SELECT $subject FROM $tbName WHERE ${GameModeDb.modeId} = '$modeId' ", null)
        if (cursor.moveToFirst())
            data = cursor.getString(cursor.getColumnIndex(GameModeDb.subject))
        reader.close()
        cursor.close()
        return data
    }

    fun getScoreAverage(modeId: String): Int {
        var data = 0
        val reader = this.readableDatabase
        val cursor = reader.rawQuery(" SELECT $scoreAverage FROM $tbName WHERE ${GameModeDb.modeId} = '$modeId' ", null)
        if (cursor.moveToFirst())
            data = cursor.getInt(cursor.getColumnIndex(scoreAverage))
        cursor.close()
        reader.close()
        return data
    }

    fun saveScoreAverage(modeId: String, score: Int) {
        val writer = this.writableDatabase
        writer.execSQL(" UPDATE $tbName SET $scoreAverage = $score WHERE ${GameModeDb.modeId} = '$modeId' ")
        writer.close()

    }

    fun getScoreAverage(): ArrayList<Int> {

        val data = ArrayList<Int>()
        val reader = this.readableDatabase
        val cursor = reader.rawQuery(" SELECT $scoreAverage FROM $tbName ", null)
        if (cursor.moveToFirst()) {
            do {
                data.add(cursor.getInt(cursor.getColumnIndex(scoreAverage)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        reader.close()
        return data
    }

    fun unLock(modeId:String){
        val writer = this.writableDatabase
        writer.execSQL(" UPDATE $tbName SET $lock = 0 WHERE ${GameModeDb.modeId} = '$modeId' ")
        writer.close()
    }

    fun deleteDb() {
        val writer = this.writableDatabase
        ctx.deleteDatabase(writer.path)
        writer.close()
    }
}