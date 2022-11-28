package com.taz.tazmovies.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.DisplayMetrics
import androidx.annotation.RequiresApi

object DialogUtils {

  @JvmStatic fun getWindowHeight(activity: Activity): Int {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @JvmStatic fun setWhiteNavigationBar(dialog: Dialog) {
    val window = dialog.window
    if (window != null) {
      val metrics = DisplayMetrics()
      window.windowManager.defaultDisplay.getMetrics(metrics)
      val dimDrawable = GradientDrawable()
      val navigationBarDrawable = GradientDrawable()
      navigationBarDrawable.shape = GradientDrawable.RECTANGLE
      navigationBarDrawable.setColor(Color.WHITE)
      val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)
      val windowBackground = LayerDrawable(layers)
      windowBackground.setLayerInsetTop(1, metrics.heightPixels)
      window.setBackgroundDrawable(windowBackground)
    }
  }
}