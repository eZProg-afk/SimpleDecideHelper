package spiral.bit.dev.simpledecidehelper.other

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun AppCompatActivity.changeFragments(
    fragment: Fragment,
    addStack: Boolean = true,
    container: Int
) {
    if (addStack) {
        this.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                container,
                fragment
            )
            .commit()
    } else {
        this.supportFragmentManager.beginTransaction()
            .replace(
                container,
                fragment
            )
            .commit()
    }
}

fun SharedPreferences.getSubscribeValueFromPref(): Boolean {
    return this.getBoolean(SUBSCRIBE_KEY, false)
}

infix fun SharedPreferences.saveSubscribeValueToPref(value: Boolean) {
    this.edit().putBoolean(SUBSCRIBE_KEY, value).apply()
}

infix fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

infix fun SharedPreferences.startWorkManager(context: Context) {
    val repeatInterval = this.getInt("days_auto_delete", 0).toLong()
    if (repeatInterval == 100L) {
        return
    } else {
        val workRequest = PeriodicWorkRequest.Builder(
            RoomWorker::class.java, repeatInterval, TimeUnit.DAYS
        )
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(workRequest)
    }
}

