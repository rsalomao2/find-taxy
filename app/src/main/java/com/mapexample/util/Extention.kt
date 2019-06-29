package com.mapexample.util

import android.support.v4.app.Fragment
import android.widget.Toast

fun Fragment.toast(msg:String, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(context, msg, duration).show()
}