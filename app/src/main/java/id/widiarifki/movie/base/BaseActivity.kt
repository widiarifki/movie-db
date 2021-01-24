package id.widiarifki.movie.base

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<Binding: ViewDataBinding> : AppCompatActivity() {

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

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun setupToolbar(toolbarTitle: String?, needHomeBtn: Boolean = false) {
        supportActionBar?.apply {
            title = toolbarTitle
            setDisplayHomeAsUpEnabled(needHomeBtn)
        }
    }
}