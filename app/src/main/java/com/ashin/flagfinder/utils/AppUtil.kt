package com.ashin.flagfinder.utils

import android.R
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import java.lang.reflect.Field

class AppUtil {


    companion object {
        fun readJsonFromAssets(context: Context, fileName: String): String {
            return context.assets.open(fileName).bufferedReader().use { it.readText() }
        }
         fun getAllDrawables(context: Context): List<Drawable> {
            val drawables: MutableList<Drawable> = ArrayList()
            val fields: Array<Field> = R.drawable::class.java.declaredFields
            for (field in fields) {
                try {
                    @DrawableRes val drawableId: Int = field.getInt(null)
                    val drawable = context.resources.getDrawable(drawableId)
                    drawables.add(drawable)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
            return drawables
        }

         fun getDrawableByName(context: Context, name: String): Drawable? {
            val drawableId = context.resources.getIdentifier(name, "drawable", context.packageName)
            if (drawableId != 0) {
                return context.resources.getDrawable(drawableId)
            }
            return null
        }
    }

}