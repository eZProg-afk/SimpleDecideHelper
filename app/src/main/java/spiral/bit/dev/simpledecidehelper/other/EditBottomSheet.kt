package spiral.bit.dev.simpledecidehelper.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.listeners.DismissListener
import spiral.bit.dev.simpledecidehelper.listeners.EditDismissListener
import spiral.bit.dev.simpledecidehelper.listeners.InsertDecisionListener
import spiral.bit.dev.simpledecidehelper.listeners.UpdateListener
import spiral.bit.dev.simpledecidehelper.models.Decision

@AndroidEntryPoint
class EditBottomSheet : BottomSheetDialogFragment() {

    private lateinit var editDismissListener: EditDismissListener
    private lateinit var updateListener: UpdateListener
    private var selectedNoteColor: String? = "#333333"
    private lateinit var decision: Decision

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.edit_bottom_sheet, container, false)

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTaskTitle: EditText = view.findViewById(R.id.et_task_title)
        val editTaskBtn: Button = view.findViewById(R.id.btn_edit_task)
        val closeBottomSheet: ImageView = view.findViewById(R.id.close_bottom_sheet)

        etTaskTitle.setText(decision.title)

        editTaskBtn.setOnClickListener {
            if (etTaskTitle.text.isNotEmpty()) {
                decision.title = etTaskTitle.text.toString()
                decision.color = selectedNoteColor
                updateListener.onUpdate(decision)
                editDismissListener.editDismiss()
                etTaskTitle.setText("")
            } else Toast.makeText(context, "Введите заголовок задачи!", Toast.LENGTH_LONG).show()
        }

        val imageColor1: ImageView = view.findViewById(R.id.image_color_1)
        val imageColor2: ImageView = view.findViewById(R.id.image_color_2)
        val imageColor3: ImageView = view.findViewById(R.id.image_color_3)
        val imageColor4: ImageView = view.findViewById(R.id.image_color_4)

        view.findViewById<View>(R.id.view_color_1).setOnClickListener {
            selectedNoteColor = "#91E3A4"
            imageColor1.setImageResource(R.drawable.ic_done)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(0)
        }

        view.findViewById<View>(R.id.view_color_2).setOnClickListener {
            selectedNoteColor = "#E79AC4"
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(R.drawable.ic_done)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(0)
        }

        view.findViewById<View>(R.id.view_color_3).setOnClickListener {
            selectedNoteColor = "#3B5998"
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(R.drawable.ic_done)
            imageColor4.setImageResource(0)
        }

        view.findViewById<View>(R.id.view_color_4).setOnClickListener {
            selectedNoteColor = "#FFF5D3"
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(R.drawable.ic_done)
        }

        if (decision.color != null && decision.color.toString().isNotEmpty()) {
            when (decision.color) {
                "#FDBE3B" -> view.findViewById<View>(R.id.view_color_2).performClick()
                "#FF4842" -> view.findViewById<View>(R.id.view_color_3).performClick()
                "#3A52FC" -> view.findViewById<View>(R.id.view_color_4).performClick()
            }
        }

        closeBottomSheet.setOnClickListener { editDismissListener.editDismiss() }
    }

    fun setMyDismissListener(editDismissListener: EditDismissListener) {
        this.editDismissListener = editDismissListener
    }

    fun setMyUpdateListener(updateListener: UpdateListener) {
        this.updateListener = updateListener
    }

    fun setDecision(decision: Decision) {
        this.decision = decision
    }
}