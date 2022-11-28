package com.taz.tazmovies.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import android.widget.TextView

object TextUtils {
  @JvmStatic fun getSpannableString(textview: TextView) {
    val s: Spannable = SpannableString(textview.text)
    val spans = s.getSpans(0, s.length, URLSpan::class.java)
    for (span in spans) {
      val start = s.getSpanStart(span)
      val end = s.getSpanEnd(span)
      s.removeSpan(span)
      val span = URLSpanNoUnderline(span.url)
      s.setSpan(span, start, end, 0)
    }
    textview.text = s
  }

  @JvmStatic fun capitalize(str: String): String {
    val words = str.split(" ")
    val sb = StringBuilder()
    words.forEach {
      if (it != "") {
        sb.append(it[0].toUpperCase()).append(it.substring(1))
      }
      sb.append(" ")
    }

    return sb.toString().trim { it <= ' ' }
  }

  private class URLSpanNoUnderline(url: String?) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
      super.updateDrawState(ds)
      ds.isUnderlineText = false
    }
  }

  @JvmStatic fun getTypeFaceFontPrimary(context: Context): Typeface? {
    return Typeface.createFromAsset(context.assets, "fonts/font_primary.ttf")
  }

  @JvmStatic fun getTypeFaceFontPrimaryBold(context: Context): Typeface? {
    val typeFace = Typeface.createFromAsset(context.assets, "fonts/font_primary_bold.ttf")
    typeFace.isBold
    return typeFace
  }

  @JvmStatic fun getTypeFace(context: Context, fontNameInAsset: String): Typeface? {
    return Typeface.createFromAsset(context.assets, "fonts/$fontNameInAsset.ttf")
  }

  @JvmStatic fun getHiddenEmail(email: String): String {
    return if(email.contains("@")){
      val strs = email.split("@")
      val lengthReplacement = (strs[0].length / 2).toInt()
      val lastText = strs[0].takeLast(lengthReplacement)
      var replacement = ""
      for (i in 1..lengthReplacement) {
        replacement += "*"
      }
      "$replacement$lastText@${strs[1]}"
    } else {
      email
    }
  }

  @JvmStatic fun getHiddenHolderNumber(holderNumber: String): String {
    if (holderNumber.isNotEmpty() && holderNumber.length > 4) {
      val last = holderNumber.takeLast(4)
      val firstLength = holderNumber.length - 4
      var str = ""
      for (i in 1..firstLength) {
        str += "*"
      }
      return "$str$last"
    }
    return holderNumber
  }

  @JvmStatic fun getHiddenMobileNumber(holderNumber: String): String {
    if (holderNumber.isNotEmpty() && holderNumber.length > 6) {
      val last = holderNumber.takeLast(6)
      val firstLength = holderNumber.length - 6
      var str = ""
      for (i in 1..firstLength) {
        str += "*"
      }
      return "$str$last"
    }
    return holderNumber
  }

  @JvmStatic fun getHiddenKTPNumber(contractNumber: String): String {
    if (contractNumber.isNotEmpty() && contractNumber.length > 4) {
      val last = contractNumber.takeLast(4)
      val firstLength = contractNumber.length - 4
      var str = ""
      for (i in 1..firstLength) {
        str += "*"
      }
      return "$str$last"
    }
    return contractNumber
  }
}