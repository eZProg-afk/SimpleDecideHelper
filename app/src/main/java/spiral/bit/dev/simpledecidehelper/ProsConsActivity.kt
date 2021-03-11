package spiral.bit.dev.simpledecidehelper

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.databinding.ActivityProfConsBinding
import spiral.bit.dev.simpledecidehelper.fragments.ProsConsFragment
import spiral.bit.dev.simpledecidehelper.listeners.DismissListener
import spiral.bit.dev.simpledecidehelper.listeners.EditDismissListener
import spiral.bit.dev.simpledecidehelper.listeners.UpdateListener
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.other.EditBottomSheet
import spiral.bit.dev.simpledecidehelper.other.ProsConsBottomSheet
import spiral.bit.dev.simpledecidehelper.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProsConsActivity : AppCompatActivity(R.layout.activity_prof_cons), DismissListener, EditDismissListener,
UpdateListener {

    private val prosConsBinding: ActivityProfConsBinding by viewBinding()
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var prosConsBottomSheet: ProsConsBottomSheet

    @Inject
    lateinit var editBottomSheet: EditBottomSheet

    @Inject
    lateinit var prosConsFragment: ProsConsFragment

    private val prosList = arrayListOf<Int>()
    private val consList = arrayListOf<Int>()
    private var taskWeight: Float = 0.0f

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val decision = intent.getSerializableExtra("decision") as Decision
        prosConsBinding.titleOfDecision.text = decision.title

        prosConsBottomSheet.setMyDismissListener(this)
        editBottomSheet.setMyDismissListener(this)
        editBottomSheet.setMyUpdateListener(this)

        mainViewModel.setParentId(decision.id)
        mainViewModel.allProsCons.observe(this, {

            var allPros = 0
            var allCons = 0

            for (i in it) {
                if (i.isProf) prosList.add(i.weight)
                else consList.add(i.weight)
            }

            for (i in prosList) {
                for (j in consList) {
                    allPros += i
                    allCons += j
                }
            }

            taskWeight = allPros / (allPros.toFloat() + allCons)
            if ((taskWeight * 100).toInt() == 0) prosConsBinding.decProgressText.text = "100%"
            else prosConsBinding.decProgressText.text = "${(taskWeight * 100).toInt()}%"
            prosConsBinding.decProgressCircular.progress = (taskWeight * 100).toInt()
            decision.currentProgress = (taskWeight * 100).toInt()
            mainViewModel.updateDecision(decision)
        })

        prosConsBinding.fabAddTask.setOnClickListener {
            prosConsBottomSheet.show(supportFragmentManager, ProsConsBottomSheet.TAG)
        }

        prosConsBinding.fabEditTask.setOnClickListener {
            editBottomSheet.setDecision(decision)
            editBottomSheet.show(supportFragmentManager, EditBottomSheet.TAG)
        }

        prosConsFragment.setModalBottomSheet(prosConsBottomSheet)
        val bundle = Bundle()
        bundle.putSerializable("decision", decision)
        prosConsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.pros_cons_container, prosConsFragment)
            .commit()
    }

    override fun dismiss() {
        prosConsBottomSheet.dismiss()
    }

    override fun editDismiss() {
        editBottomSheet.dismiss()
    }

    override fun onUpdate(decision: Decision) {
        mainViewModel.updateDecision(decision)
    }
}