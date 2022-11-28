package com.taz.tazmovies.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.taz.tazmovies.R

class DialogProgress(
  context: Context
) : AlertDialog(context, R.style.DialogSemiTransparentTheme) {

  companion object {
    private val TAG = this::class.java.simpleName
    val PLEASE_WAIT = "Mohon menunggu"
  }

  var tvProgress: TextView

  init {
    val layoutInflater = LayoutInflater.from(context)
    val view = layoutInflater.inflate(R.layout.dialog_progress_view, null)
    tvProgress = view.findViewById(R.id.progress_text_tv)
    tvProgress.text = PLEASE_WAIT
    setView(view)
    setCancelable(false)
    setInverseBackgroundForced(false)
    setCanceledOnTouchOutside(false)
  }

  fun setProgressText(text: String): DialogProgress{
    Log.d(TAG, "setProgressText: $text")
    tvProgress.text = text
    this.onContentChanged()
    Log.d(TAG, "onContentChanged...")
    return this
  }
}