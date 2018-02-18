package akrupych.creightonmodel

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var observations = listOf<Pair<String, String>>()
    private val inflater: LayoutInflater by lazy { LayoutInflater.from(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addButton.setOnClickListener { startActivity<ObservationActivity>() }
        observationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ObservationsAdapter()
            loadTodayObservations()
        }
    }

    private fun loadTodayObservations() {
        val database = FirebaseDatabase.getInstance().reference
        val dateTime = DateTime.now()
        val year = dateTime.year.toString()
        val month = dateTime.monthOfYear.toString()
        val day = dateTime.dayOfMonth.toString()
        database.child(year).child(month).child(day).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("qwerty", snapshot.toString())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                observations = (snapshot.value as? Map<String, String>)?.toList()?.map {
                    Pair(timeFormat.format(Date(it.first.split(":")[0].toLong())), it.second)
                } ?: emptyList()
                observationsRecyclerView.adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    private inner class ObservationsAdapter : RecyclerView.Adapter<ObservationViewHolder>() {

        override fun getItemCount() = observations.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
                ObservationViewHolder(inflater.inflate(R.layout.item_observation, parent, false))

        override fun onBindViewHolder(holder: ObservationViewHolder, position: Int) {
            val item = observations[position]
            holder.apply {
                observationTime.text = item.first
                observationValue.text = item.second
            }
        }

    }

    private class ObservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val observationTime = itemView.findViewById<TextView>(R.id.observationTime)
        val observationValue = itemView.findViewById<TextView>(R.id.observationValue)
    }
}
