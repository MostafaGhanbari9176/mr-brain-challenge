package ir.pepotec.app.game.model.local_data_base

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ir.pepotec.app.game.model.ModeCData

class ModeCDb(private val ctx: Context, private val tbName: String = "mode_c_tb", dbName: String = "mode_c_db") :
    SQLiteOpenHelper(ctx, dbName, null, 1) {

    companion object {
        const val levelId = "level_id"
        const val subject = "subject"
        const val score = "score"
        const val lock = "lock"
        const val alpha = "alpha"
        const val spaceX = "space_x"
        const val puzzleX = "puzzle_x"
        const val busX = "bus_x"
        const val isFinally = "is_finally"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            " CREATE TABLE $tbName ( $levelId INTEGER PRIMARY KEY AUTOINCREMENT , $subject TEXT , " +
                    "$score TEXT , $lock BOOLEAN , $alpha FLOAT , " +
                    "$spaceX INTEGER , $puzzleX INTEGER , $busX INTEGER , $isFinally INTEGER )"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(" DROP TABLE IF EXISTS $tbName ")
    }

    fun save(data: ModeCData) {

        val writer: SQLiteDatabase = this.writableDatabase
        val cValue = ContentValues()

        with(data) {
            cValue.put(ModeCDb.subject, subject)
            cValue.put(ModeCDb.score, score)
            cValue.put(ModeCDb.lock, lock)
            cValue.put(ModeCDb.alpha, alpha)
            cValue.put(ModeCDb.spaceX, spaceX)
            cValue.put(ModeCDb.puzzleX, puzzleX)
            cValue.put(ModeCDb.busX, busX)
            cValue.put(ModeCDb.isFinally, isFinally)
        }

        writer.insert(tbName, null, cValue)
        writer.close()
    }

    fun get(): ArrayList<ModeCData> {

        val data = ArrayList<ModeCData>()
        val reader: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = reader.rawQuery(" SELECT * FROM $tbName ", null)
        if (cursor.moveToFirst()) {
            do {
                data.add(
                    ModeCData(
                        cursor.getInt(cursor.getColumnIndex(levelId)),
                        cursor.getString(cursor.getColumnIndex(subject)),
                        cursor.getInt(cursor.getColumnIndex(score)),
                        cursor.getInt(cursor.getColumnIndex(lock)),
                        cursor.getFloat(cursor.getColumnIndex(alpha)),
                        cursor.getInt(cursor.getColumnIndex(spaceX)),
                        cursor.getInt(cursor.getColumnIndex(puzzleX)),
                        cursor.getInt(cursor.getColumnIndex(busX)),
                        cursor.getInt(cursor.getColumnIndex(isFinally))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        reader.close()
        return data
    }

    fun get(id: Int): ModeCData {
        var data: ModeCData? = null
        val reader: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = reader.rawQuery(" SELECT * FROM $tbName WHERE $levelId = $id ", null)
        if (cursor.moveToFirst()) {
            data = ModeCData(
                cursor.getInt(cursor.getColumnIndex(levelId)),
                cursor.getString(cursor.getColumnIndex(subject)),
                cursor.getInt(cursor.getColumnIndex(score)),
                cursor.getInt(cursor.getColumnIndex(lock)),
                cursor.getFloat(cursor.getColumnIndex(alpha)),
                cursor.getInt(cursor.getColumnIndex(spaceX)),
                cursor.getInt(cursor.getColumnIndex(puzzleX)),
                cursor.getInt(cursor.getColumnIndex(busX)),
                cursor.getInt(cursor.getColumnIndex(isFinally))
            )

        }
        cursor.close()
        reader.close()
        return data!!
    }

    fun saveScore(levelId: Int, score: Int) {
        val writer = this.writableDatabase
        writer.execSQL(" UPDATE $tbName SET ${ModeADb.score} = $score WHERE ${ModeADb.levelId} = $levelId ")
        writer.close()
    }

    fun unLockLevel(levelId: Int) {
        val writer = this.writableDatabase
        writer.execSQL(" UPDATE $tbName SET $lock = 0 WHERE ${ModeADb.levelId} = $levelId ")
        writer.close()
    }

    fun getCount(): Int {

        val reader = this.readableDatabase
        val cursor = reader.rawQuery(" SELECT * FROM $tbName ", null)
        val data = cursor.count
        cursor.close()
        reader.close()
        return data
    }

    fun getScore():ArrayList<Int>{
        val data = ArrayList<Int>()
        val reader = this.readableDatabase
        val cursor = reader.rawQuery(" SELECT $score FROM $tbName ",null)
        if(cursor.moveToFirst())
        {
            do{
                data.add(cursor.getInt(cursor.getColumnIndex(score)))
            }while (cursor.moveToNext())
        }
        cursor.close()
        reader.close()
        return data
    }

    fun deleteDb() {
        val writer: SQLiteDatabase = this.writableDatabase
        ctx.deleteDatabase(writer.path)
        writer.close()
    }


}


