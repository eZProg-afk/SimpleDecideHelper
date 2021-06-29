package spiral.bit.dev.simpledecidehelper.other

import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.databinding.CompleteBottomSheetBinding
import spiral.bit.dev.simpledecidehelper.listeners.ComplDismissListener
import spiral.bit.dev.simpledecidehelper.viewmodels.DecisionsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CompleteBottomSheet : BottomSheetDialogFragment() {

    private lateinit var dismissListener: ComplDismissListener
    private val decisionsViewModel: DecisionsViewModel by viewModels()
    private val complBinding: CompleteBottomSheetBinding by viewBinding()
    @Inject lateinit var mainPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.complete_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (mainPrefs.getSubscribeValueFromPref()) {
            complBinding.adView.isVisible = false
        } else {
            complBinding.adView.isVisible = true
            complBinding.adView.loadAd(AdManagerAdRequest.Builder().build())
        }

        val adapterDaysDelete: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(
                view.context,
                R.array.days,
                android.R.layout.simple_spinner_item
            )
        adapterDaysDelete.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        complBinding.autoDeleteDaysSpinner.adapter = adapterDaysDelete

        complBinding.autoDeleteDaysSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = resources.getStringArray(R.array.days)
                if (choose[selectedItemPosition].equals("Никогда")) {
                    mainPrefs.edit().putInt(
                        "days_auto_delete",
                        100
                    ).apply()
                } else {
                    mainPrefs.edit().putInt(
                        "days_auto_delete",
                        choose[selectedItemPosition].substring(0, 2).trim().toInt()
                    ).apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        complBinding.deleteBtn.setOnClickListener { showDeleteNoteDialog(view) }
        complBinding.closeBottomSheet.setOnClickListener { dismissListener.editDismiss() }
    }

    fun setMyDismissListener(dismissListener: ComplDismissListener) {
        this.dismissListener = dismissListener
    }

    private fun showDeleteNoteDialog(viewLayout: View) {
        val builder = AlertDialog.Builder(viewLayout.context)
        val view = LayoutInflater.from(viewLayout.context)
            .inflate(
                R.layout.layout_delete_note,
                viewLayout.findViewById(R.id.layout_delete_note_container)
            )
        builder.setView(view)
        val dialogDeleteNote = builder.create()
        if (dialogDeleteNote.window != null) dialogDeleteNote.window?.setBackgroundDrawable(
            ColorDrawable(0)
        )
        view.findViewById<View>(R.id.text_delete_note).setOnClickListener {
            decisionsViewModel.deleteAllCompletedDecisions()
            dialogDeleteNote.dismiss()
            dismissListener.editDismiss()
        }
        view.findViewById<View>(R.id.text_cancel).setOnClickListener { dialogDeleteNote.dismiss() }
        dialogDeleteNote.show()
    }
}