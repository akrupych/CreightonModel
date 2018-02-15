package akrupych.creightonmodel

import android.view.View
import android.widget.RadioGroup

fun View.setVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun RadioGroup.hasSelection() = checkedRadioButtonId != -1