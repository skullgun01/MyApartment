package th.co.appman.myapartment.alert

import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import th.co.appman.myapartment.R
import th.co.appman.myapartment.databinding.LayoutAlertDialogBinding

class AlertMessageDialogFragment : DialogFragment() {

    lateinit var binding: LayoutAlertDialogBinding

    private var callback: (() -> Unit)? = null
    private var text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.AlertDialog)
        if (savedInstanceState == null) {
            restoreArguments(arguments!!)
        } else {
            restoreInstanceState(savedInstanceState)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        dialog?.window?.setBackgroundDrawableResource((R.drawable.bg_alert_dialog))
        binding = LayoutAlertDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            text = savedInstanceState.getString("text", "")
        }
        init()
    }

    private fun init() {
        binding.tvMessage.text = text
        binding.btnConfirm.setOnClickListener {
            callback?.invoke()
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.let {
            val size = Point()

            it.windowManager.defaultDisplay.getSize(size)

            val width = size.x

            it.setLayout((width * 0.9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            it.setGravity(Gravity.CENTER)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("text", text)
    }

    private fun restoreInstanceState(bundle: Bundle) {
        text = bundle.getString("text", "")
    }

    private fun restoreArguments(bundle: Bundle) {
        text = bundle.getString("text", "")
    }

    class Builder {
        private lateinit var text: String
        private var callback: (() -> Unit)? = null

        fun setMessage(text: String): Builder {
            this.text = text
            return this
        }

        fun setCallback(callback: () -> Unit): Builder {
            this.callback = callback
            return this
        }

        fun build(): AlertMessageDialogFragment {
            return newInstance(text, callback)
        }
    }

    companion object {

        fun newInstance(text: String, callback: (() -> Unit)?): AlertMessageDialogFragment {
            val fragment = AlertMessageDialogFragment()
            val bundle = Bundle()
            bundle.putString("text", text)
            fragment.arguments = bundle
            fragment.callback = callback
            return fragment
        }
    }
}