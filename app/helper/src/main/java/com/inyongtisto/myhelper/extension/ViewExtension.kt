package com.inyongtisto.myhelper.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.NestedScrollView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible(status: Boolean) {
    if (status) this.toVisible() else this.toGone()
}

fun SwipeRefreshLayout.setDefaultColor() {
    this.setColorSchemeColors(
        context.getColor(android.R.color.holo_green_light),
        context.getColor(android.R.color.holo_orange_light),
        context.getColor(android.R.color.holo_red_light)
    )
}

fun SwipeRefreshLayout.showLoading() {
    this.isRefreshing = true
}

fun SwipeRefreshLayout.dismissLoading() {
    this.isRefreshing = false
}

fun View.toSquare() {
    val observer = this.viewTreeObserver
    observer.addOnGlobalLayoutListener {
        this.viewTreeObserver
//            val a = view.measuredHeight
        val b = this.measuredWidth
        this.layoutParams.height = b
        this.requestLayout()
    }
}

fun Activity.setStatusBarBackgroudColor(color: Int) {
    val window: Window = window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = getColor(color)
}

fun AppCompatImageView.setTintColor(context: Context, color: Int) {
    setColorFilter(
        ContextCompat.getColor(context, color),
        android.graphics.PorterDuff.Mode.MULTIPLY
    )
}

fun AppCompatImageView.setTintColor(color: Int) {
    ImageViewCompat.setImageTintList(this, ContextCompat.getColorStateList(context, color))
}

fun Activity.lightStatusBar() {
    val decor = this.window.decorView
    decor.systemUiVisibility =
        decor.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() //set status text  light
}

@SuppressLint("ObsoleteSdkInt")
fun Activity.blackStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else if (Build.VERSION.SDK_INT >= 23) {
        val decor: View = this.window.decorView
        if (decor.systemUiVisibility != View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility = 0
        }
    }
}

fun Activity.setLightStatusBar() {
    lightStatusBar()
}

fun Activity.setBlackStatusBar() {
    blackStatusBar()
}

fun View.setVisibelityWithAnimation(
    views: ViewGroup,
    transition: Transition = Slide(Gravity.START)
) {
    // example
    // var transition: Transition = Fade()
    // var transition: Transition = Slide(Gravity.BOTTOM)
    val show: Boolean = this.visibility != View.VISIBLE

    transition.duration = 400
    transition.addTarget(this)
    TransitionManager.beginDelayedTransition(views, transition)
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun NestedScrollView.scrollToBottom() {
    post {
        fullScroll(ScrollView.FOCUS_DOWN)
    }
}

fun NestedScrollView.scrollToTop() {
    post {
        fullScroll(ScrollView.FOCUS_DOWN)
    }
}

fun ScrollView.scrollToBottom() {
    post {
        fullScroll(ScrollView.FOCUS_DOWN)
    }
}

fun ScrollView.scrollToTop() {
    post {
        fullScroll(ScrollView.FOCUS_DOWN)
    }
}

fun MaterialButton.setStrokeColor(color: Int) {
    strokeColor = ColorStateList.valueOf(context.getColor(color))
}

fun Activity.transparentStatusBar() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
}