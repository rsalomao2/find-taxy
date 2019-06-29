package com.mapexample.util

import androidx.fragment.app.Fragment
import android.widget.Toast

fun Fragment.toast(msg:String, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(context, msg, duration).show()
}