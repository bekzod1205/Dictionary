package uz.gita.bekzod1205.dictionary.utils

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import uz.gita.bekzod1205.dictionary.R
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun FragmentActivity.openScreenUnSaveStack(fm: Fragment) {
    supportFragmentManager.beginTransaction().replace(R.id.container, fm).commit()
}


@OptIn(ExperimentalUuidApi::class)
fun FragmentActivity.openScreenSaveStack(fm: Fragment) {
    supportFragmentManager.beginTransaction().replace(R.id.container, fm)
        .addToBackStack(Uuid.random().toHexString()).commit()
}

fun Fragment.openScreenSaveStack(fm: Fragment, args: Bundle? = null) {
    fm.arguments = args
    requireActivity().openScreenSaveStack(fm)
}

fun String.createSpannable(query: String): SpannableString {
    val spannable = SpannableString(this)
    val startIndex = this.indexOf(query)
    val endIndex = startIndex + query.length
    if (startIndex < 0 || endIndex > this.length) return spannable
    spannable.setSpan(
        ForegroundColorSpan(Color.GREEN),
        startIndex, // start
        endIndex, // end
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
    return spannable
}