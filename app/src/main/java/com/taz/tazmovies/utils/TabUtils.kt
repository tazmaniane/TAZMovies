package com.taz.tazmovies.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayout
import com.taz.tazmovies.R

object TabUtils {

  @JvmStatic fun getTabItemTransparent(context: Context, text: String): View {
    val view = LayoutInflater.from(context).inflate(R.layout.tab_item, null)
    val tv: TextView = view.findViewById(R.id.tab_item_tv)
    val imv: ImageView = view.findViewById(R.id.tab_item_imv)
    imv.visibility = View.GONE
    tv.text = text
    // imv.setImageResource(drawable)
    return view
  }

  @JvmStatic fun getTabItemSemiTransparentBlackView(context: Context, text: String): View {
    val view = LayoutInflater.from(context).inflate(R.layout.tab_item_semi_transparent_black_view, null)
    val tv: TextView = view.findViewById(R.id.tab_item_tv)
    val imv: ImageView = view.findViewById(R.id.tab_item_imv)
    imv.visibility = View.GONE
    tv.text = text
    // imv.setImageResource(drawable)
    return view
  }

  @JvmStatic fun getTabItemPrimaryView(context: Context, text: String): View {
    val view = LayoutInflater.from(context).inflate(R.layout.tab_item_primary_view, null)
    val tv: TextView = view.findViewById(R.id.tab_item_tv)
    val imv: ImageView = view.findViewById(R.id.tab_item_imv)
    imv.visibility = View.GONE
    tv.text = text
    // imv.setImageResource(drawable)
    return view
  }

  @JvmStatic fun setTabTextNormal(context: Context?, tab: TabLayout.Tab) {
    val tabCustomView = tab.customView
    if (context != null && tabCustomView != null) {
      val typeface = ResourcesCompat.getFont(
        context, R.font.font_primary
      )
      val tabTextView = tabCustomView.findViewById<TextView>(R.id.tab_item_tv)
      tabTextView.typeface = typeface
    }
  }

  @JvmStatic fun setTabTextBold(context: Context?, tab: TabLayout.Tab) {
    val tabCustomView = tab.customView
    if (context != null && tabCustomView != null) {
      val typeface = ResourcesCompat.getFont(
        context, R.font.font_primary_bold
      )
      val tabTextView = tabCustomView.findViewById<TextView>(R.id.tab_item_tv)
      tabTextView.typeface = typeface
    }
  }
}