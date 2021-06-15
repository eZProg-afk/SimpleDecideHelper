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
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.listeners.ComplDismissListener
import spiral.bit.dev.simpledecidehelper.viewmodels.MainViewModel

@AndroidEntryPoint
class CompleteBottomSheet : BottomSheetDialogFragment() {

    private lateinit var dismissListener: ComplDismissListener
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mainPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.complete_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainPrefs = view.context.getSharedPreferences("def_prefs", 0)

        val daysSpinner: Spinner = view.findViewById(R.id.auto_delete_days_spinner)
        val deleteAllTasksBtn: Button = view.findViewById(R.id.delete_btn)
        val closeBottomSheet: ImageView = view.findViewById(R.id.close_bottom_sheet)
        val adBanner = view.findViewById<AdView>(R.id.adView)

        if (getSubscribeValueFromPref(mainPrefs)) adBanner.visibility = View.GONE
        else {
            adBanner.visibility = View.VISIBLE
            adBanner.loadAd(AdManagerAdRequest.Builder().build())
        }

        val adapterDaysDelete: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(
                view.context,
                R.array.days,
                android.R.layout.simple_spinner_item
            )
        adapterDaysDelete.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daysSpinner.adapter = adapterDaysDelete

        daysSpinner.onItemSelectedListener = object :
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

        deleteAllTasksBtn.setOnClickListener { showDeleteNoteDialog(view) }
        closeBottomSheet.setOnClickListener { dismissListener.editDismiss() }
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
            mainViewModel.deleteAllCompletedDecisions()
            dialogDeleteNote.dismiss()
            dismissListener.editDismiss()
        }
        view.findViewById<View>(R.id.text_cancel).setOnClickListener { dialogDeleteNote.dismiss() }
        dialogDeleteNote.show()
    }
}