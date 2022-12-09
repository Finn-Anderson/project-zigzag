package com.uhi.mad.zigzag

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (ctx: Context) {
    // Initialise variables to use later
    private val DatabaseName = "leaderboardDatabase"
    private val DatabaseVersion = 1
    private val TableName = "leaderboard"
    private val DatabaseCreate = "CREATE TABLE " + TableName + " (" +
            "username VARCHAR(50) PRIMARY KEY NOT NULL, " +
            "score MEDIUMINT UNSIGNED NOT NULL, " +
            "country VARCHAR(50) NOT NULL);"

    // Opens database
    @Throws(SQLException::class)
    fun open(): DatabaseHelper {
        mDb = mDbHelper.writableDatabase
        return this
    }

    // Closes database
    fun close() {
        mDbHelper.close()
    }

    /**
     * Insert a new user's score into the database
     *
     * @property username string to insert
     * @property score integer to insert
     * @property country string to insert
     * @throws SQLException returns if an error has occurred
     */
    @Throws(SQLException::class)
    fun insertScore(username: String, score: Int, country: String) {
        val args = ContentValues()
        args.put("username", username)
        args.put("score", score)
        args.put("country", country)

        mDb?.insert(TableName, null, args)
    }

    /**
     * Update a specific username's details from the database
     *
     * @property username string to search against username in database
     * @property score integer to insert
     * @property country string to insert
     * @throws SQLException returns if an error has occurred
     */
    @Throws(SQLException::class)
    fun updateScore(username: String, score: Int, country: String) {
        val args = ContentValues()
        args.put("score", score)
        args.put("country", country)

        mDb?.update(TableName, args, "username = '$username'", null)
    }

    /**
     * Gets a specific username's details from the database
     *
     * @property username string to search for usernames in database
     * @throws SQLException returns if an error has occurred
     * @return returns results ArrayList
     */
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

    /**
     * Gets top 100 scores from database
     *
     * @throws SQLException returns if an error has occurred
     * @return returns results ArrayList of all rows in database
     */
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

    /**
     * Gets top score from database
     *
     * @throws SQLException returns if an error has occurred
     * @return returns results ArrayList of all rows in database
     */
    @Throws(SQLException::class)
    fun getHighScore(): Int {
        var results = 0

        val cursor = mDb?.query(TableName, arrayOf("score"), null, null, null, null, "score desc", "1")

        if (cursor!!.moveToFirst()) {
            do {
                results = cursor.getString(0).toInt()
            } while (cursor.moveToNext())
        }
        cursor.close()
        return results
    }

    private val mDbHelper: OpenHelper = OpenHelper(ctx)
    private var mDb: SQLiteDatabase? = null

    private inner class OpenHelper (context: Context) :
        SQLiteOpenHelper(context, DatabaseName, null, DatabaseVersion) {

        // Create database
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(DatabaseCreate)
        }

        // Recreates database if updated
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TableName")
            onCreate(db)
        }
    }
}