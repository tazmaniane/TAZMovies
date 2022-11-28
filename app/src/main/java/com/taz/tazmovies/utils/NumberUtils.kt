package com.taz.tazmovies.utils

import android.content.Context
import android.util.DisplayMetrics
import java.text.DecimalFormat
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin

object NumberUtils {
  @JvmStatic fun getCurrencyFrom(double: Double?): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(double ?: 0.0).replace(",", ".")
  }

  @JvmStatic fun isNumber(s: String?): Boolean {
    return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
  }

  @JvmStatic fun convertDpToPixel(context: Context, dp: Float): Int {
    return (dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toInt()
  }

  @JvmStatic fun getDistance(lat0: Double, lng0: Double, latitude: Double?, longitude: Double?): Double {
    val R = 6517219
    val B1 = getRad(lat0)
    val A1 = getRad(lng0)
    val B2 = latitude?.toDouble()?.let { getRad(it) }
    val A2 = longitude?.toDouble()?.let { getRad(it) }
    val SB = B2?.minus(B1)
    val SA = A2?.minus(A1)

    val x1 = SB?.div(2.0)?.let { asin(it) }
    val x2 = SB?.div(2.0)?.let { sin(it) }
    val y1 = cos(B1) * cos(B2!!)
    val z1 = SA?.div(2.0)?.let { sin(it) }
    val z2 = SA?.div(2.0)?.let { sin(it) }

    val aa = x1!! * x2!! + y1 * z1!! * z2!!
    val cc = 2 * Math.atan2(
      Math.sqrt(aa),
      Math.sqrt(1 - aa)
    )
    val dd = roundToDecimals(R * cc / 1000, 2)
    return dd
  }

  @JvmStatic fun getRad(coordinat: Double): Double {
    return coordinat * 3.1415926535897931 / 180
  }

  @JvmStatic fun roundToDecimals(number: Double, precision: Int): Double {
    var factor = 10.0.pow(precision.toDouble())
    return (number * factor).roundToInt() / factor
  }

}