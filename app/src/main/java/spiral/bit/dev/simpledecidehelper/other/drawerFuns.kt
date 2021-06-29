package spiral.bit.dev.simpledecidehelper.other

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import spiral.bit.dev.simpledecidehelper.databinding.ActivityMainBinding

infix fun AppCompatActivity.rateApp(decisionsBinding: ActivityMainBinding) {
    decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
    val uri: Uri = Uri.parse("market://details?id=$packageName")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    try {
        startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

infix fun AppCompatActivity.inviteFriend(decisionsBinding: ActivityMainBinding) {
    decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
    val uri: Uri = Uri.parse("market://details?id=$packageName")
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "Привет! Скачай это крутое приложение пожалуйста!))" +
                    "\n Вот ссылка - $uri"
        )
    }
    startActivity(Intent.createChooser(sendIntent, null))
}

infix fun MenuItem.setUpNightMode(decisionsBinding: ActivityMainBinding) {
    decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
    val currentMode = this.isChecked
    if (currentMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}