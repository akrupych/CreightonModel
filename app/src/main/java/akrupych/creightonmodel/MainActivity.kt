package akrupych.creightonmodel

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bloodDropdown.setOnClickListener { toggleBloodDropdown() }
        feelingsDropdown.setOnClickListener { toggleFeelingsDropdown() }
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
}
