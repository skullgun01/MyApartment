package th.co.appman.myapartment.utils

import android.content.Context
import android.content.SharedPreferences

class Preference {

    private object Holder {
        val INSTANCE = Preference()
    }

    companion object {
        private const val TAG = "MyApartment"
        val instance: Preference by lazy { Holder.INSTANCE }
    }

    private fun openEditor(context: Context): SharedPreferences.Editor {

        val sharedPref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        return sharedPref.edit()
    }

    fun putBoolean(context: Context, key: String, value: Boolean): Boolean {
        return openEditor(context)
            .putBoolean(key, value)
            .commit()
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        val sharedPref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(key, defaultValue)
    }

    fun clearAllPreference(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        return editor.clear().commit()
    }
}
