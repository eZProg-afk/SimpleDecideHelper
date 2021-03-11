package spiral.bit.dev.simpledecidehelper.other

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