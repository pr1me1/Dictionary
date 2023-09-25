package uz.gita.lesson21.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat


fun String.spanText(query: String, context: Context): SpannableString {
    val st = SpannableString(this)

    val spanColor = ForegroundColorSpan(ContextCompat.getColor(context, com.google.android.material.R.color.material_blue_grey_800))
    val startingIndex = this.indexOf(query, 0, true)
    if (startingIndex > -1) {
        st.setSpan(spanColor, startingIndex, startingIndex + query.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }


    return st
}

fun String.spannable(query:String,context: Context):SpannableString{
    val span = SpannableString(this)
    val color = ForegroundColorSpan(ContextCompat.getColor(context, com.google.android.material.R.color.material_deep_teal_500))
    val startIndex = this.indexOf(query,0,true)
    if (startIndex>-1){
        span.setSpan(color,startIndex,startIndex+query.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return span
}