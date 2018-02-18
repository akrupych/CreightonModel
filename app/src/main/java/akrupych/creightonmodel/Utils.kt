package akrupych.creightonmodel

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RadioGroup

fun View.setVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun RadioGroup.hasSelection() = checkedRadioButtonId != -1

inline fun <reified T> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}