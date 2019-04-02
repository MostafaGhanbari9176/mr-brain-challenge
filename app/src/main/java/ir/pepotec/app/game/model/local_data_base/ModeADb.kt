package ir.pepotec.app.game.model.local_data_base

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ir.pepotec.app.game.model.ModeAData

class ModeADb(private val ctx: Context, private val tbName: String = "mode_a_tb", dbName: String = "mode_a_db") :
    SQLiteOpenHelper(ctx, dbName, null, 1) {

    companion object {
        const val levelId = "level_id"
        const val subject = "subject"
        const val score = "score"
        const val lock = "lock"
        const val alpha = "alpha"
        const val spaceX = "space_x"
        const val puzzleX = "puzzle_x"
        const val puzzleY = "puzzle_y"
        const val isFinally = "is_finally"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            " CREATE TABLE $tbName ( $levelId INTEGER PRIMARY KEY AUTOINCREMENT , $subject TEXT , " +
                    "$score TEXT , $lock BOOLEAN , $alpha FLOAT , " +
                    "$spaceX INTEGER , $puzzleX INTEGER , $puzzleY INTEGER , $isFinally INTEGER )"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(" DROP TABLE IF EXISTS $tbName ")
    }

    fun save(data: ModeAData) {

        val writer: SQLiteDatabase = this.writableDatabase
        val cValue = ContentValues()

        with(data) {
            cValue.put(ModeADb.subject, subject)
            cValue.put(ModeADb.score, score)
            cValue.put(ModeADb.lock, lock)
            cValue.put(ModeADb.alpha, alpha)
            cValue.put(ModeADb.spaceX, spaceX)
            cValue.put(ModeADb.puzzleX, puzzleX)
            cValue.put(ModeADb.puzzleY, puzzleY)
            cValue.put(ModeADb.isFinally, isFinally)
        }

        writer.insert(tbName, null, cValue)
        writer.close()
    }

    fun get(): ArrayList<ModeAData> {

        val data = ArrayList<ModeAData>()
        val reader: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = reader.rawQuery(" SELECT * FROM $tbName ", null)
        if (cursor.moveToFirst()) {
            do {
                data.add(
                    ModeAData(
                        cursor.getInt(cursor.getColumnIndex(levelId)),
                        cursor.getString(cursor.getColumnIndex(subject)),
                        cursor.getInt(cursor.getColumnIndex(score)),
                        cursor.getInt(cursor.getColumnIndex(lock)),
                        cursor.getFloat(cursor.getColumnIndex(alpha)),
                        cursor.getInt(cursor.getColumnIndex(spaceX)),
                        cursor.getInt(cursor.getColumnIndex(puzzleX)),
                        cursor.getInt(cursor.getColumnIndex(puzzleY)),
                        cursor.getInt(cursor.getColumnIndex(isFinally))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        reader.close()
        return data
    }

    fun get(id: Int): ModeAData {
        var data: ModeAData? = null
        val reader: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = reader.rawQuery(" SELECT * FROM $tbName WHERE $levelId = $id ", null)
        if (cursor.moveToFirst()) {
            data = ModeAData(
                cursor.getInt(cursor.getColumnIndex(levelId)),
                cursor.getString(cursor.getColumnIndex(subject)),
                cursor.getInt(cursor.getColumnIndex(score)),
                cursor.getInt(cursor.getColumnIndex(lock)),
                cursor.getFloat(cursor.getColumnIndex(alpha)),
                cursor.getInt(cursor.getColumnIndex(spaceX)),
                cursor.getInt(cursor.getColumnIndex(puzzleX)),
                cursor.getInt(cursor.getColumnIndex(puzzleY)),
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
        val cursor = reader.rawQuery(" SELECT ${ModeCDb.score} FROM $tbName ",null)
        if(cursor.moveToFirst())
        {
            do{
                data.add(cursor.getInt(cursor.getColumnIndex(ModeCDb.score)))
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


