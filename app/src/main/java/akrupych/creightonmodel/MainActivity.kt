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
        val key = System.currentTimeMillis().toString()
        database.child("excretion").child(key).setValue("10KL")
        database.child("temperature").child(key).setValue("36.6")
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
