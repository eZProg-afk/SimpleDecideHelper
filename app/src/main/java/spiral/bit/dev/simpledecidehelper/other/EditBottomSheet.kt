package spiral.bit.dev.simpledecidehelper.other

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.databinding.EditBottomSheetBinding
import spiral.bit.dev.simpledecidehelper.listeners.ComplDismissListener
import spiral.bit.dev.simpledecidehelper.listeners.UpdateListener
import spiral.bit.dev.simpledecidehelper.models.Decision
import javax.inject.Inject

@AndroidEntryPoint
class EditBottomSheet : BottomSheetDialogFragment() {

    private val editBinding: EditBottomSheetBinding by viewBinding()
    @Inject
    lateinit var defSharedPrefs: SharedPreferences
    private lateinit var complDismissListener: ComplDismissListener
    private lateinit var updateListener: UpdateListener
    private var selectedNoteColor: String? = "#333333"
    private lateinit var decision: Decision

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.edit_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (defSharedPrefs.getSubscribeValueFromPref()) {
            editBinding.adView.isVisible = false
        } else {
            editBinding.adView.isVisible = true
            editBinding.adView.loadAd(AdManagerAdRequest.Builder().build())
        }

        editBinding.etTaskTitle.setText(decision.title)

        editBinding.btnEditTask.setOnClickListener {
            if (editBinding.etTaskTitle.text.isNotEmpty()) {
                decision.title = editBinding.etTaskTitle.text.toString()
                decision.color = selectedNoteColor

                updateListener.onUpdate(decision)
                complDismissListener.editDismiss()
                editBinding.etTaskTitle.setText("")
            } else requireContext() showToast getString(R.string.enter_title_of_task)
        }

        editBinding.viewColor1.setOnClickListener {
            selectedNoteColor = "#91E3A4"
            editBinding.imageColor1.setImageResource(R.drawable.ic_done)
            editBinding.imageColor2.setImageResource(0)
            editBinding.imageColor3.setImageResource(0)
            editBinding.imageColor4.setImageResource(0)
        }

        editBinding.viewColor2.setOnClickListener {
            selectedNoteColor = "#E79AC4"
            editBinding.imageColor1.setImageResource(0)
            editBinding.imageColor2.setImageResource(R.drawable.ic_done)
            editBinding.imageColor3.setImageResource(0)
            editBinding.imageColor4.setImageResource(0)
        }

        editBinding.viewColor3.setOnClickListener {
            selectedNoteColor = "#3B5998"
            editBinding.imageColor1.setImageResource(0)
            editBinding.imageColor2.setImageResource(0)
            editBinding.imageColor3.setImageResource(R.drawable.ic_done)
            editBinding.imageColor4.setImageResource(0)
        }

        editBinding.viewColor4.setOnClickListener {
            selectedNoteColor = "#FFF5D3"
            editBinding.imageColor1.setImageResource(0)
            editBinding.imageColor2.setImageResource(0)
            editBinding.imageColor3.setImageResource(0)
            editBinding.imageColor4.setImageResource(R.drawable.ic_done)
        }

        if (decision.color != null && decision.color.toString().isNotEmpty()) {
            when (decision.color) {
                "#FDBE3B" -> editBinding.viewColor2.performClick()
                "#FF4842" -> editBinding.viewColor3.performClick()
                "#3A52FC" -> editBinding.viewColor4.performClick()
            }
        }

        editBinding.closeBottomSheet.setOnClickListener {
            complDismissListener.editDismiss()
        }
    }

    fun setMyDismissListener(complDismissListener: ComplDismissListener) {
        this.complDismissListener = complDismissListener
    }

    fun setMyUpdateListener(updateListener: UpdateListener) {
        this.updateListener = updateListener
    }

    fun setDecision(decision: Decision) {
        this.decision = decision
    }
}