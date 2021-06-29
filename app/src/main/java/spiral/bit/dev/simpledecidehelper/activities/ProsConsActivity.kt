package spiral.bit.dev.simpledecidehelper.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.databinding.ActivityProfConsBinding
import spiral.bit.dev.simpledecidehelper.listeners.DismissListener
import spiral.bit.dev.simpledecidehelper.listeners.ComplDismissListener
import spiral.bit.dev.simpledecidehelper.listeners.UpdateListener
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.other.EditBottomSheet
import spiral.bit.dev.simpledecidehelper.other.ProsConsBottomSheet
import spiral.bit.dev.simpledecidehelper.other.TAG_MODAL_BOTTOM_SHEET
import spiral.bit.dev.simpledecidehelper.viewmodels.DecisionsViewModel
import spiral.bit.dev.simpledecidehelper.viewmodels.ProsConsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProsConsActivity : AppCompatActivity(R.layout.activity_prof_cons), DismissListener, ComplDismissListener,
UpdateListener {

    private val prosConsBinding: ActivityProfConsBinding by viewBinding()
    private val decisionsViewModel: DecisionsViewModel by viewModels()
    private val prosConsViewModel: ProsConsViewModel by viewModels()

    private val prosConsBottomSheet by lazy { ProsConsBottomSheet() }
    private val editBottomSheet by lazy { EditBottomSheet() }

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

        prosConsViewModel.setParentId(decision.id)
        prosConsViewModel.allProsCons.observe(this, {

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
            decisionsViewModel.updateDecision(decision)
        })

        prosConsBinding.fabAddTask.setOnClickListener {
            prosConsBottomSheet.show(supportFragmentManager, TAG_MODAL_BOTTOM_SHEET)
        }

        prosConsBinding.fabEditTask.setOnClickListener {
            editBottomSheet.setDecision(decision)
            editBottomSheet.show(supportFragmentManager, TAG_MODAL_BOTTOM_SHEET)
        }
    }

    override fun dismiss() {
        prosConsBottomSheet.dismiss()
    }

    override fun editDismiss() {
        editBottomSheet.dismiss()
    }

    override fun onUpdate(decision: Decision) {
        decisionsViewModel.updateDecision(decision)
    }
}