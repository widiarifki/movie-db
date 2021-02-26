package id.widiarifki.movie.utils.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import id.widiarifki.movie.R
import id.widiarifki.movie.databinding.BaseLoadingBinding
import kotlinx.android.synthetic.main.base_loading.view.*

class DialogLoading : DialogFragment() {

    private var message: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.base_loading, container, false)
        message?.let {
            view.tvLoading.text = it
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    companion object {
        const val TAG = "DialogLoading"
        fun newInstance(message: String?) : DialogLoading {
            val dialog = DialogLoading()
            dialog.message = message
            return dialog
        }
    }
}