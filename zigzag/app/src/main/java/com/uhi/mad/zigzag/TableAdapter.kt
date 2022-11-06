package com.uhi.mad.zigzag

import android.content.Context
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.setPadding

class TableAdapter {
    fun createRows(table: TableLayout, scores: ArrayList<Array<String>>, ctx: Context) {
        for (position in scores) {
            val row = TableRow(ctx)
            row.setPadding(8)

            val name = TextView(ctx)
            name.layoutParams = TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f)
            name.gravity = Gravity.CENTER
            name.text = position[0]
            name.textSize = 16F

            val score = TextView(ctx)
            score.layoutParams = TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f)
            score.gravity = Gravity.CENTER
            score.text = position[1]
            name.textSize = 16F

            val country = TextView(ctx)
            country.layoutParams = TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f)
            country.gravity = Gravity.CENTER
            country.text = position[2]
            name.textSize = 16F

            row.addView(name)
            row.addView(score)
            row.addView(country)
            table.addView(row)


        }
    }
}