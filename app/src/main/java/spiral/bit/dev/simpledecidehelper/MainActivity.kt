package spiral.bit.dev.simpledecidehelper

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.databinding.ActivityMainBinding
import spiral.bit.dev.simpledecidehelper.fragments.MyTasksFragment
import spiral.bit.dev.simpledecidehelper.listeners.DismissListener
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.other.ACTIVITY
import spiral.bit.dev.simpledecidehelper.other.DecisionBottomSheet
import spiral.bit.dev.simpledecidehelper.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), DismissListener {

    private val decisionsBinding: ActivityMainBinding by viewBinding()

    @Inject
    lateinit var myTasksFragment: MyTasksFragment
    @Inject
    lateinit var decisionBottomSheet: DecisionBottomSheet

    private val decisionsViewModel: MainViewModel by viewModels()

    private var decisionsList = arrayListOf<Decision>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

        decisionsViewModel.allDecisions.observe(this, {
            decisionsList = it as ArrayList<Decision>
        })
    }

    private fun initViews() {
        ACTIVITY = this

        supportActionBar?.title = ""
        supportFragmentManager.beginTransaction().add(
            R.id.main_fragment_container,
            myTasksFragment
        ).commit()

        myTasksFragment.setModalBottomSheet(decisionBottomSheet)
        decisionBottomSheet.setMyDismissListener(this)

        decisionsBinding.fabAddTask.setOnClickListener {
            decisionBottomSheet.show(supportFragmentManager, DecisionBottomSheet.TAG)
        }
    }

    override fun dismiss() {
        decisionBottomSheet.dismiss()
    }
}

