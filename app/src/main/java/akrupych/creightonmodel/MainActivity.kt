package akrupych.creightonmodel

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.DateTime

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contentView.requestFocus()
        bloodDropdown.setOnClickListener { toggleBloodDropdown() }
        feelingsDropdown.setOnClickListener { toggleFeelingsDropdown() }
        lookDropdown.setOnClickListener { toggleLookDropdown() }
        stretchDropdown.setOnClickListener { toggleStretchDropdown() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.record, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.submitRecord) submitRecord()
        return true
    }

    private fun submitRecord() {
        val database = FirebaseDatabase.getInstance().reference
        val dateTime = DateTime.now()
        val year = dateTime.year.toString()
        val month = dateTime.monthOfYear.toString()
        val day = dateTime.dayOfMonth.toString()
        if (bloodRecorded()) database.child(year).child(month).child(day).child(getKey("b")).setValue(resolveBlood())
        if (mucusRecorded()) database.child(year).child(month).child(day).child(getKey("m")).setValue(resolveMucus())
        if (temperatureRecorded()) database.child(year).child(month).child(day).child(getKey("t")).setValue(resolveTemperature())
        if (sex.isChecked) database.child(year).child(month).child(day).child(getKey("i")).setValue("I")
        if (kegel.isChecked) database.child(year).child(month).child(day).child(getKey("k")).setValue("KE")
        if (notesEntered()) database.child(year).child(month).child(day).child(getKey("n")).setValue(getNotes())
        finish()
    }

    private fun getKey(type: String) = "${System.currentTimeMillis()}:$type"

    private fun getNotes(): String = notesEditText.text.toString()

    private fun notesEntered(): Boolean = notesEditText.text.isNotEmpty()

    private fun resolveTemperature(): Double = temperatureEditText.text.toString().toDouble()

    private fun temperatureRecorded(): Boolean = temperatureEditText.text.isNotEmpty()

    private fun bloodRecorded(): Boolean = bloodSection.hasSelection()

    private fun resolveBlood(): String = when (bloodSection.checkedRadioButtonId) {
        R.id.brown -> "B"
        R.id.veryLight -> "VL"
        R.id.light -> "L"
        R.id.moderate -> "M"
        R.id.heavy -> "H"
        else -> ""
    }

    private fun mucusRecorded(): Boolean =
            feelingsSection.hasSelection() || lookSection.hasSelection() || stretchSection.hasSelection()

    private fun resolveMucus(): String {
        val number = when {
            stretchSection.hasSelection() -> when (stretchSection.checkedRadioButtonId) {
                R.id.sticky -> "6"
                R.id.tacky -> "8"
                R.id.stretchy -> "10"
                else -> "0"
            }
            noLubrication.isChecked -> when {
                shiny.isChecked -> "4"
                wet.isChecked -> "2W"
                damp.isChecked -> "2"
                else -> "0"
            }
            else -> ""
        }
        val color = when (lookSection.checkedRadioButtonId) {
            R.id.cloudy -> "C"
            R.id.cloudyClear -> "C/K"
            R.id.clear -> "K"
            R.id.yellow -> "Y"
            else -> ""
        }
        val pasty = if (pasty.isChecked) "P" else ""
        val gluey = if (gluey.isChecked) "G" else ""
        val lubrication = if (lubrication.isChecked) {
            when {
                shiny.isChecked -> "SL"
                wet.isChecked -> "WL"
                damp.isChecked -> "DL"
                else -> "L"
            }
        } else ""
        return "$number$color$pasty$gluey$lubrication"
    }

    private fun toggleBloodDropdown() {
        hideKeyboard()
        val shouldOpen = !bloodDropdown.isSelected
        bloodDropdown.isSelected = shouldOpen
        bloodSection.setVisible(shouldOpen)
    }

    private fun toggleFeelingsDropdown() {
        hideKeyboard()
        val shouldOpen = !feelingsDropdown.isSelected
        feelingsDropdown.isSelected = shouldOpen
        feelingsSection.setVisible(shouldOpen)
    }

    private fun toggleLookDropdown() {
        hideKeyboard()
        val shouldOpen = !lookDropdown.isSelected
        lookDropdown.isSelected = shouldOpen
        lookSection.setVisible(shouldOpen)
    }

    private fun toggleStretchDropdown() {
        hideKeyboard()
        val shouldOpen = !stretchDropdown.isSelected
        stretchDropdown.isSelected = shouldOpen
        stretchSection.setVisible(shouldOpen)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(contentView.windowToken, 0)
    }
}
