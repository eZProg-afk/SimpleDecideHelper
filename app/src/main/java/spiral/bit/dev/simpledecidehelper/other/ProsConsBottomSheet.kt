package spiral.bit.dev.simpledecidehelper.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jaygoo.widget.RangeSeekBar
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.listeners.DismissListener
import spiral.bit.dev.simpledecidehelper.listeners.InsertProsConsListener
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem

@AndroidEntryPoint
class ProsConsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var dismissListener: DismissListener
    private lateinit var insertProsConsListener: InsertProsConsListener
    private var isPros = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.pros_cons_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTaskTitle: EditText = view.findViewById(R.id.et_task_title)
        val addTaskBtn: Button = view.findViewById(R.id.btn_add_task)
        val prosBtn: Button = view.findViewById(R.id.pros_btn)
        val consBtn: Button = view.findViewById(R.id.cons_btn)
        val weightSeekBar: RangeSeekBar = view.findViewById(R.id.weight_seek_bar)
        val closeBottomSheet: ImageView = view.findViewById(R.id.close_bottom_sheet)
        val adBanner = view.findViewById<AdView>(R.id.adView)
        val defSharedPrefs = view.context.getSharedPreferences("def_prefs", 0)

        if (getSubscribeValueFromPref(defSharedPrefs)) {
            adBanner.visibility = View.GONE
        } else {
            adBanner.visibility = View.VISIBLE
            adBanner.loadAd(AdManagerAdRequest.Builder().build())
        }

        prosBtn.setOnClickListener { isPros = true }
        consBtn.setOnClickListener { isPros = false }

        addTaskBtn.setOnClickListener {
            if (etTaskTitle.text.isNotEmpty()) {
                insertProsConsListener.onInsert(
                    ProsConsItem(
                        0,
                        0,
                        etTaskTitle.text.toString(),
                        isPros,
                        weightSeekBar.leftSeekBar.progress.toInt()
                    ), 0
                )
                dismissListener.dismiss()
                etTaskTitle.setText("")
            } else Toast.makeText(context, "Введите заголовок задачи!", Toast.LENGTH_LONG).show()
        }

        closeBottomSheet.setOnClickListener { dismissListener.dismiss() }
    }

    fun setMyDismissListener(dismissListener: DismissListener) {
        this.dismissListener = dismissListener
    }

    fun setMyInsertListener(insertProsConsListener: InsertProsConsListener) {
        this.insertProsConsListener = insertProsConsListener
    }
}