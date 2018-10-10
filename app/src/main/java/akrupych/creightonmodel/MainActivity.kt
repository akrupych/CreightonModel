package akrupych.creightonmodel

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private var listItems = mutableListOf<Observation>()
    private val inflater: LayoutInflater by lazy { LayoutInflater.from(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addButton.setOnClickListener { startActivity<ObservationActivity>() }
        observationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ObservationsAdapter()
            loadObservations()
        }
    }

    private fun loadObservations() {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { yearSnapshot ->
                    yearSnapshot.children.forEach { monthSnapshot ->
                        monthSnapshot.children.forEach { daySnapshot  ->
                            val section = listOf(Observation(DateTime( // indicator for date header
                                    yearSnapshot.key!!.toInt(),
                                    monthSnapshot.key!!.toInt(),
                                    daySnapshot.key!!.toInt(),
                                    0,
                                    0
                            ))).plus(daySnapshot.children.mapNotNull {
                                Observation.parse(it.key, it.value.toString())
                            })
                            listItems.addAll(0, section)
                        }
                    }
                }
                observationsRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    private inner class ObservationsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val VIEW_TYPE_TITLE = 0
        val VIEW_TYPE_ITEM = 1

        val dateFormat: DateTimeFormatter = DateTimeFormat.forPattern("E, d.M.y")
        val timeFormat: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm")

        override fun getItemCount() = listItems.size

        override fun getItemViewType(position: Int): Int =
                if (listItems[position].type.isEmpty()) VIEW_TYPE_TITLE else VIEW_TYPE_ITEM

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
            VIEW_TYPE_TITLE -> object : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_title, parent, false)) {}
            else -> ObservationViewHolder(inflater.inflate(R.layout.item_observation, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = listItems[position]
            if (holder is ObservationViewHolder) {
                holder.apply {
                    observationTime.text = timeFormat.print(item.dateTime)
                    observationValue.text = item.value
                    if (item.type == Observation.TYPE_BLOOD) observationValue.setTextColor(Color.RED)
                    observationValue.setTextColor(when (item.type) {
                        Observation.TYPE_BLOOD -> Color.RED
                        Observation.TYPE_MUCUS -> Color.GREEN
                        Observation.TYPE_TEMPERATURE -> Color.BLUE
                        else -> Color.BLACK
                    })
                }
            } else {
                val textView = holder.itemView as TextView
                textView.text = dateFormat.print(item.dateTime)
            }
        }

    }

    private class ObservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val observationTime: TextView = itemView.findViewById(R.id.observationTime)
        val observationValue: TextView = itemView.findViewById(R.id.observationValue)
    }
}
