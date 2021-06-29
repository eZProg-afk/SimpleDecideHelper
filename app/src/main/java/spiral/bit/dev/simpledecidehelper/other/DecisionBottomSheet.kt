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
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.listeners.DismissListener
import spiral.bit.dev.simpledecidehelper.listeners.InsertDecisionListener
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.viewmodels.DecisionsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class DecisionBottomSheet : BottomSheetDialogFragment() {

    private lateinit var dismissListener: DismissListener
    @Inject lateinit var defSharedPrefs: SharedPreferences
    private lateinit var insertDecisionListener: InsertDecisionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.decision_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTaskTitle: EditText = view.findViewById(R.id.et_task_title)
        val addTaskBtn: Button = view.findViewById(R.id.btn_add_task)
        val closeBottomSheet: ImageView = view.findViewById(R.id.close_bottom_sheet)
        val adBanner = view.findViewById<AdView>(R.id.adView)

        if (defSharedPrefs.getSubscribeValueFromPref()) {
            adBanner.visibility = View.GONE
        } else {
            adBanner.visibility = View.VISIBLE
            adBanner.loadAd(AdManagerAdRequest.Builder().build())
        }

        addTaskBtn.setOnClickListener {
            if (etTaskTitle.text.isNotEmpty()) {
                insertDecisionListener.onInsert(
                    Decision(
                        0,
                        etTaskTitle.text.toString(),
                        0,
                        "#91E3A4"
                    ), 0
                )
                dismissListener.dismiss()
                etTaskTitle.setText("")
            } else requireContext() showToast getString(R.string.enter_title_of_task)
        }

        closeBottomSheet.setOnClickListener { dismissListener.dismiss() }
    }

    fun setMyDismissListener(dismissListener: DismissListener) {
        this.dismissListener = dismissListener
    }

    fun setMyInsertListener(insertDecisionListener: InsertDecisionListener) {
        this.insertDecisionListener = insertDecisionListener
    }
}