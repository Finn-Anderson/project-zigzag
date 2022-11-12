package com.uhi.mad.zigzag

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (ctx: Context) {
    private val DatabaseName = "leaderboardDatabase"
    private val DatabaseVersion = 1
    private val TableName = "leaderboard"
    private val DatabaseCreate = "CREATE TABLE " + TableName + " (" +
            "username VARCHAR(50) PRIMARY KEY NOT NULL, " +
            "score MEDIUMINT UNSIGNED NOT NULL, " +
            "country VARCHAR(50) NOT NULL);"

    @Throws(SQLException::class)
    fun open(): DatabaseHelper {
        mDb = mDbHelper.writableDatabase
        return this
    }

    fun close() {
        mDbHelper.close()
    }

    @Throws(SQLException::class)
    fun insertScore(username: String, score: Int, country: String) {
        val args = ContentValues()
        args.put("username", username)
        args.put("score", score)
        args.put("country", country)

        mDb?.insert(TableName, null, args)
    }

    @Throws(SQLException::class)
    fun updateScore(username: String, score: Int, country: String) {
        val args = ContentValues()
        args.put("score", score)
        args.put("country", country)

        mDb?.update(TableName, args, "username = '$username'", null)
    }

    @Throws(SQLException::class)
    fun getUser(username: String): ArrayList<Array<String>> {
        val results = ArrayList<Array<String>>()

        val cursor = mDb?.query(TableName, arrayOf("username", "score", "country"), "username = '$username'", null, null, null, null)

        if (cursor!!.moveToFirst()) {
            results.add(arrayOf(cursor.getString(0), cursor.getString(1), cursor.getString(2)))
        }
        cursor.close()
        return results
    }

    @Throws(SQLException::class)
    fun getScores(): ArrayList<Array<String>> {
        val results = ArrayList<Array<String>>()

        val cursor = mDb?.query(TableName, arrayOf("username", "score", "country"), null, null, null, null, "score desc", "100")

        if (cursor!!.moveToFirst()) {
            do {
                results.add(arrayOf(cursor.getString(0), cursor.getString(1), cursor.getString(2)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return results
    }

    private val mDbHelper: OpenHelper = OpenHelper(ctx)
    private var mDb: SQLiteDatabase? = null

    private inner class OpenHelper (context: Context) :
        SQLiteOpenHelper(context, DatabaseName, null, DatabaseVersion) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(DatabaseCreate)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TableName")
            onCreate(db)
        }
    }
}