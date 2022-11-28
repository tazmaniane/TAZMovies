package com.taz.tazmovies.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.taz.tazmovies.R

class PrimaryToolbar(
  private val activity: Activity?
) {

  private var tvTitle: TextView? = null
  private var tvSubtitle: TextView? = null
  private var imvBack: ImageView? = null
  private var layout: RelativeLayout? = null
  var btnMenu: MaterialButton? = null

  private var listener: PrimaryToolbarListener? = null

  interface PrimaryToolbarListener {
    fun onClickAction()
  }

  fun setListener(listener: PrimaryToolbarListener?): PrimaryToolbar {
    this.listener = listener
    return this
  }

  private var toolbar: Toolbar? = null
  fun setToolbar(toolbar: Toolbar?): PrimaryToolbar {
    this.toolbar = toolbar
    return this
  }

  private var backgroundColor: Int = R.color.colorPrimary
  fun setBackgroundColor(backgroundColor: Int): PrimaryToolbar {
    this.backgroundColor = backgroundColor
    return this
  }

  var title = ""
  fun setTitle(title: String): PrimaryToolbar {
    this.title = title
    tvTitle?.text = title
    return this
  }

  var subtitle = ""
  fun setSubtitle(subtitle: String): PrimaryToolbar {
    this.subtitle = subtitle
    tvSubtitle?.text = subtitle
    if(this.subtitle.isEmpty()) tvSubtitle?.visibility = GONE
    else tvSubtitle?.visibility = VISIBLE
    return this
  }

  var titleColor: Int = android.R.color.white
  fun setTitleColor(titleColor: Int): PrimaryToolbar {
    this.titleColor = titleColor
    return this
  }

  var isHideBackButton = false
  fun setHideBackButton(isHideBackButton: Boolean): PrimaryToolbar {
    this.isHideBackButton = isHideBackButton
    if (this.isHideBackButton) {
      imvBack?.visibility = View.INVISIBLE
    } else {
      imvBack?.visibility = View.VISIBLE
    }
    return this
  }

  var isHideActionButton = false

  /** default is FALSE */
  fun setHideActionButton(isHideActionButton: Boolean): PrimaryToolbar {
    this.isHideActionButton = isHideActionButton
    if (this.isHideActionButton) {
      btnMenu?.visibility = View.INVISIBLE
    } else {
      btnMenu?.visibility = View.VISIBLE
    }
    return this
  }

  var actionIcon: Drawable? = null
  fun setActionIcon(actionIcon: Drawable?): PrimaryToolbar {
    this.actionIcon = actionIcon
    btnMenu?.icon = this.actionIcon
    return this
  }

  var actionText: String? = null
  fun setActionText(actionText: String?): PrimaryToolbar {
    this.actionText = actionText
    btnMenu?.text = this.actionText
    return this
  }

  var actionTextColor: Int = android.R.color.white
  fun setActionTextColor(actionTextColor: Int): PrimaryToolbar {
    this.actionTextColor = actionTextColor
    activity?.let { activity -> 
      btnMenu?.setTextColor(actionTextColor)
      btnMenu?.iconTint = ContextCompat.getColorStateList(activity, actionTextColor)
    }
    return this
  }

  var actionBackground: Int = R.color.colorPrimary
  fun setActionBackground(actionBackground: Int): PrimaryToolbar {
    this.actionBackground = actionBackground
    activity?.let { activity -> btnMenu?.backgroundTintList = ContextCompat.getColorStateList(activity, actionBackground) }
    return this
  }

  fun init(): PrimaryToolbar {
    activity?.let { activity ->
      toolbar?.let { toolbar ->
        (activity as AppCompatActivity).window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        (activity as AppCompatActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          // (activity as AppCompatActivity).window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
          (activity as AppCompatActivity).window.statusBarColor = ContextCompat.getColor(activity, backgroundColor)
        }
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        if ((activity as AppCompatActivity).supportActionBar != null) {
          (activity as AppCompatActivity).supportActionBar?.displayOptions =
            ActionBar.DISPLAY_SHOW_CUSTOM
          (activity as AppCompatActivity).supportActionBar?.setCustomView(R.layout.action_bar_primary_layout)
        }

        layout = (activity as AppCompatActivity).findViewById(R.id.layout)
        layout?.setBackgroundColor(ContextCompat.getColor(activity, backgroundColor))

        tvTitle = (activity as AppCompatActivity).findViewById(R.id.title_tv)
        tvTitle?.text = title
        tvTitle?.setTextColor(ContextCompat.getColor(activity, titleColor))

        tvSubtitle = (activity as AppCompatActivity).findViewById(R.id.subtitle_tv)
        tvSubtitle?.text = subtitle
        tvSubtitle?.setTextColor(ContextCompat.getColor(activity, titleColor))
        if(this.subtitle.isEmpty()) tvSubtitle?.visibility = GONE
        else tvSubtitle?.visibility = VISIBLE

        imvBack = (activity as AppCompatActivity).findViewById(R.id.btn_back)
        imvBack?.setOnClickListener { (activity as AppCompatActivity).onBackPressed() }
        imvBack?.visibility = View.INVISIBLE
        if (isHideBackButton) {
          imvBack?.visibility = View.INVISIBLE
        } else {
          imvBack?.visibility = View.VISIBLE
        }
        imvBack?.setColorFilter(ContextCompat.getColor(activity, titleColor))

        btnMenu = (activity as AppCompatActivity).findViewById(R.id.btn_menu)
        btnMenu?.backgroundTintList = ContextCompat.getColorStateList(activity, actionBackground)
        btnMenu?.iconTint = ContextCompat.getColorStateList(activity, actionTextColor)
        btnMenu?.icon = actionIcon
        btnMenu?.text = actionText
        btnMenu?.setTextColor(ContextCompat.getColor(activity, actionTextColor))
        btnMenu?.setOnClickListener { listener?.onClickAction() }

        if (isHideActionButton) {
          btnMenu?.visibility = View.INVISIBLE
        } else {
          btnMenu?.visibility = View.VISIBLE
        }
      }
    }
    return this
  }
}