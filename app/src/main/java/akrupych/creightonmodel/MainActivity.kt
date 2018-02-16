package akrupych.creightonmodel

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

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
        if (bloodRecorded()) database.child("excretion").child(getKey()).setValue(resolveBlood())
        if (mucusRecorded()) database.child("excretion").child(getKey()).setValue(resolveMucus())
        if (temperatureRecorded()) database.child("temperature").child(getKey()).setValue(resolveTemperature())
        if (sex.isChecked) database.child("marks").child(getKey()).setValue("I")
        if (kegel.isChecked) database.child("marks").child(getKey()).setValue("K")
        finish()
    }

    private fun getKey() = System.currentTimeMillis().toString()

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
        val shouldOpen = !bloodDropdown.isSelected
        bloodDropdown.isSelected = shouldOpen
        bloodSection.setVisible(shouldOpen)
    }

    private fun toggleFeelingsDropdown() {
        val shouldOpen = !feelingsDropdown.isSelected
        feelingsDropdown.isSelected = shouldOpen
        feelingsSection.setVisible(shouldOpen)
    }

    private fun toggleLookDropdown() {
        val shouldOpen = !lookDropdown.isSelected
        lookDropdown.isSelected = shouldOpen
        lookSection.setVisible(shouldOpen)
    }

    private fun toggleStretchDropdown() {
        val shouldOpen = !stretchDropdown.isSelected
        stretchDropdown.isSelected = shouldOpen
        stretchSection.setVisible(shouldOpen)
    }
}
