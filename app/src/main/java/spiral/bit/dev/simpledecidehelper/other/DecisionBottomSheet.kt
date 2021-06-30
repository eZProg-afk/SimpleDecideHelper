package spiral.bit.dev.simpledecidehelper.other

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.databinding.DecisionBottomSheetBinding
import spiral.bit.dev.simpledecidehelper.listeners.DismissListener
import spiral.bit.dev.simpledecidehelper.listeners.InsertDecisionListener
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.viewmodels.DecisionsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class DecisionBottomSheet : BottomSheetDialogFragment() {

    private val decisionBottomBinding: DecisionBottomSheetBinding by viewBinding()
    private lateinit var insertDecisionListener: InsertDecisionListener
    private lateinit var dismissListener: DismissListener
    @Inject lateinit var defSharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.decision_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (defSharedPrefs.getSubscribeValueFromPref()) {
            decisionBottomBinding.adView.visibility = View.GONE
        } else {
            decisionBottomBinding.adView.visibility = View.VISIBLE
            decisionBottomBinding.adView.loadAd(AdManagerAdRequest.Builder().build())
        }

        decisionBottomBinding.btnAddTask.setOnClickListener {
            if (decisionBottomBinding.etTaskTitle.text.isNotEmpty()) {
                insertDecisionListener.onInsert(
                    Decision(
                        0,
                        decisionBottomBinding.etTaskTitle.text.toString(),
                        0,
                        "#91E3A4"
                    ), 0
                )
                dismissListener.dismiss()
                decisionBottomBinding.etTaskTitle.setText("")
            } else requireContext() showToast getString(R.string.enter_title_of_task)
        }

        decisionBottomBinding.closeBottomSheet.setOnClickListener {
            dismissListener.dismiss()
        }
    }

    fun setMyDismissListener(dismissListener: DismissListener) {
        this.dismissListener = dismissListener
    }

    fun setMyInsertListener(insertDecisionListener: InsertDecisionListener) {
        this.insertDecisionListener = insertDecisionListener
    }
}