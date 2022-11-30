package com.mitrasetia.anda1.Base.view.act

import android.os.Build
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.taz.tazmovies.utils.DialogProgress

open class BaseActivity : AppCompatActivity() {

  private val TAG = this::class.java.simpleName

  var dialogProgress: DialogProgress? = null

  fun showLoading() {
    if(dialogProgress == null) dialogProgress = DialogProgress(this)
    dialogProgress?.show()
  }

  fun dismissLoading() {
    dialogProgress?.dismiss()
  }

  fun setFullScreen(isFullscreen: Boolean) {
    if (isFullscreen) setFullScreen()
    else setNotFullscreen()
  }

  fun setFullScreen() {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.setSoftInputMode(
      WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        and WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
    )
    window.decorView.systemUiVisibility = (
      View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
      )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      window.statusBarColor = resources.getColor(android.R.color.transparent)
      window.navigationBarColor = resources.getColor(android.R.color.transparent)
    }
  }

  fun setNotFullscreen() {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.setSoftInputMode(
      WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        and WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
    )
    window.decorView.systemUiVisibility = (
      View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
      )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      window.statusBarColor = resources.getColor(android.R.color.transparent)
//      window.navigationBarColor = resources.getColor(android.R.color.white)
    }
  }

  fun hideSoftKeyboard() {
    currentFocus?.let { currentFocus ->
      val inputMethodManager =
        this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
      inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
  }

  fun showSoftKeyboard(view: View) {
    view.requestFocus()
    val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
  }

  fun displayShowHomeEnabled(boolean: Boolean) {
    supportActionBar?.setDisplayHomeAsUpEnabled(boolean)
    supportActionBar?.setDisplayShowHomeEnabled(boolean)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    super.onOptionsItemSelected(item)
    when (item.itemId) {
      android.R.id.home -> {
        finish()
      }
    }
    return true
  }

  override fun onBackPressed() {
    super.onBackPressed()
  }
}