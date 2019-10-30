package com.grzegorziwanek.moviewatcher.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun getStringFromRes(context: Context, id: Int): String = context.getString(id)

fun hideKeyboard(activity: Activity?) {
    activity?.currentFocus?.let {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    } ?: println("Can't hide keyboard when currentFocus for this activity is null")
}