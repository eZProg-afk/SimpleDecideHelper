package spiral.bit.dev.simpledecidehelper.other

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jaygoo.widget.RangeSeekBar
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.databinding.ProsConsBottomSheetBinding
import spiral.bit.dev.simpledecidehelper.listeners.DismissListener
import spiral.bit.dev.simpledecidehelper.listeners.InsertProsConsListener
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem
import javax.inject.Inject

@AndroidEntryPoint
class ProsConsBottomSheet : BottomSheetDialogFragment() {

    private val prosConsBinding: ProsConsBottomSheetBinding by viewBinding()
    private lateinit var dismissListener: DismissListener
    private lateinit var insertProsConsListener: InsertProsConsListener
    @Inject lateinit var defSharedPrefs: SharedPreferences
    private var isPros = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.pros_cons_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (defSharedPrefs.getSubscribeValueFromPref()) {
            prosConsBinding.adView.isVisible = false
        } else {
            prosConsBinding.adView.isVisible = true
            prosConsBinding.adView.loadAd(AdManagerAdRequest.Builder().build())
        }

        prosConsBinding.prosBtn.setOnClickListener { isPros = true }
        prosConsBinding.consBtn.setOnClickListener { isPros = false }

        prosConsBinding.btnAddTask.setOnClickListener {
            if (prosConsBinding.etTaskTitle.text.isNotEmpty()) {
                insertProsConsListener.onInsert(
                    ProsConsItem(
                        0,
                        0,
                        prosConsBinding.etTaskTitle.text.toString(),
                        isPros,
                        prosConsBinding.weightSeekBar.leftSeekBar.progress.toInt()
                    ), 0
                )
                dismissListener.dismiss()
                prosConsBinding.etTaskTitle.setText("")
            } else requireContext() showToast "Введите заголовок задачи!"
        }

        prosConsBinding.closeBottomSheet.setOnClickListener {
            dismissListener.dismiss()
        }
    }

    fun setMyDismissListener(dismissListener: DismissListener) {
        this.dismissListener = dismissListener
    }

    fun setMyInsertListener(insertProsConsListener: InsertProsConsListener) {
        this.insertProsConsListener = insertProsConsListener
    }
}