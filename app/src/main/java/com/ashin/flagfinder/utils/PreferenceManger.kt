package com.ashin.flagfinder.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException

class PreferenceManger(applicationContext: Context) {
    var context=applicationContext

    fun getEditor(): SharedPreferences.Editor {
        val editor = providesSharedPreference()!!.edit()
        return editor
    }

    fun providesSharedPreference(): SharedPreferences? {
        var sharedPreferences: SharedPreferences? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                sharedPreferences = EncryptedSharedPreferences.create(
                    context!!,
                    context!!.packageName,
                    getMasterKey()!!,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (e: GeneralSecurityException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            sharedPreferences =
                context!!.getSharedPreferences(
                    context!!.packageName,
                    Context.MODE_PRIVATE
                )
        }
        return sharedPreferences
    }

    private fun getMasterKey(): MasterKey? {
        try {
            return MasterKey.Builder(context!!)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun clearPrefs() {
        val editor = providesSharedPreference()!!.edit()
        editor.clear()
        editor.commit()
    }

    fun setStartTime(start_time: Long?) {
        val editor = providesSharedPreference()!!.edit()
        editor.putLong("start_time", start_time!!)
        editor.commit()
    }

    fun getStartTime(): Long? {
        val prefs = providesSharedPreference()
        return prefs!!.getLong("start_time",0L)
    }
    fun setScore(score: Int?) {
        val editor = providesSharedPreference()!!.edit()
        editor.putInt("score", score!!)
        editor.commit()
    }

    fun getScore(): Int? {
        val prefs = providesSharedPreference()
        return prefs!!.getInt("score", 0)
    }
    fun setTotalScore(tot_score: Int?) {
        val editor = providesSharedPreference()!!.edit()
        editor.putInt("tot_score", tot_score!!)
        editor.commit()
    }

    fun getTotalScore(): Int? {
        val prefs = providesSharedPreference()
        return prefs!!.getInt("tot_score", 0)
    }
    fun setLastIndex(last_index: Int?) {
        val editor = providesSharedPreference()!!.edit()
        editor.putInt("last_index", last_index!!)
        editor.commit()
    }

    fun getLastIndex(): Int? {
        val prefs = providesSharedPreference()
        return prefs!!.getInt("last_index", 0)
    }
    fun setLastTime(last_time: Long?) {
        val editor = providesSharedPreference()!!.edit()
        editor.putLong("last_time", last_time!!)
        editor.commit()
    }

    fun getLastTime(): Long? {
        val prefs = providesSharedPreference()
        return prefs!!.getLong("last_time",System.currentTimeMillis())
    }
    fun setClickedOn(clicked: String?) {
        val editor = providesSharedPreference()!!.edit()
        editor.putString("clicked", clicked!!)
        editor.commit()
    }

    fun getClickedOn(): String? {
        val prefs = providesSharedPreference()
        return prefs!!.getString("clicked","")
    }
    fun setAnseweredId(answered_id: Int?) {
        val editor = providesSharedPreference()!!.edit()
        editor.putInt("answered_id", answered_id!!)
        editor.commit()
    }

    fun getAnseweredId(): Int? {
        val prefs = providesSharedPreference()
        return prefs!!.getInt("answered_id", 0)
    }

}