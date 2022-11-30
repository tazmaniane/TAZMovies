package com.taz.tazmovies.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat

import com.google.android.material.tabs.TabLayout

class TabLayoutCenter : TabLayout {
  constructor(context: Context?) : super(context!!) {}
  constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context!!,
    attrs,
    defStyleAttr
  ) {
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    super.onLayout(changed, l, t, r, b)
    val firstTab: View? = (getChildAt(0) as ViewGroup)?.getChildAt(0)
    val lastTab: View? =
      (getChildAt(0) as ViewGroup)?.getChildAt((getChildAt(0) as ViewGroup)?.childCount?.minus(1) ?: 0)
    ViewCompat.setPaddingRelative(
      getChildAt(0),
      width / 2 - (firstTab?.width?.div(2) ?: 0),
      0,
      width / 2 - (lastTab?.width?.div(2) ?: 0),
      0
    )
  }
}