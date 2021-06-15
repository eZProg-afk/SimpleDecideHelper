package spiral.bit.dev.simpledecidehelper.other

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import spiral.bit.dev.simpledecidehelper.R

fun changeFragments(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.main_fragment_container,
                fragment
            )
            .commit()
    } else {
        ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_fragment_container,
                fragment
            )
            .commit()
    }
}

fun getSubscribeValueFromPref(defSharedPrefs: SharedPreferences): Boolean {
    return defSharedPrefs.getBoolean(SUBSCRIBE_KEY, false)
}

fun saveSubscribeValueToPref(defSharedPrefs: SharedPreferences, value: Boolean) {
    defSharedPrefs.edit().putBoolean(SUBSCRIBE_KEY, value).apply()
}