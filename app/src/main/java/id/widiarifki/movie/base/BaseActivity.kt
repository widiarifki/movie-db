package id.widiarifki.movie.base

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.hilt.android.AndroidEntryPoint
import id.widiarifki.movie.R
import id.widiarifki.movie.utils.ui.DialogLoading

abstract class BaseActivity<Binding: ViewDataBinding> : AppCompatActivity() {

    private var dialogProgress: DialogLoading? = null

    protected abstract val resourceLayout: Int
    protected abstract fun onViewReady(savedInstanceState: Bundle?)

    protected lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, resourceLayout)
        onViewReady(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun showToast(message: String?) {
        Toast.makeText(this,
                if (message.isNullOrEmpty()) resources.getString(R.string.msg_error_global) else message,
                Toast.LENGTH_SHORT).show()
    }

    protected fun setupToolbar(toolbarTitle: String?, needHomeBtn: Boolean = false) {
        supportActionBar?.apply {
            title = toolbarTitle
            setDisplayHomeAsUpEnabled(needHomeBtn)
        }
    }

    protected fun showProgressDialog(message: String? = resources.getString(R.string.msg_loading_global)) {
        dialogProgress?.dismiss()

        dialogProgress = DialogLoading.newInstance(message)
        dialogProgress?.show(supportFragmentManager, DialogLoading.TAG)
    }

    protected fun hideProgressDialog() {
        dialogProgress?.dismiss()
    }
}