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
        const val modeId: String = "mode_id"
        const val subject: String = "subject"
        const val scoreAverage: String = "score_average"
        const val lock: String = "lock"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $tbName ( $modeId TEXT , $subject TEXT , $scoreAverage INTEGER , $lock BOOLEAN ) ")
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
        val cursor:Cursor = reader.rawQuery("SELECT * FROM $tbName", null)
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

    fun deleteDb() {
        val writer = this.writableDatabase
        ctx.deleteDatabase(writer.path)
        writer.close()
    }
}