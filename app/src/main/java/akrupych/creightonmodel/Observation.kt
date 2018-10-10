package akrupych.creightonmodel

import org.joda.time.DateTime

data class Observation(
        var dateTime: DateTime = DateTime.now(),
        var type: String = "",
        var value: String = ""
) {
    companion object {

        const val TYPE_BLOOD = "b"
        const val TYPE_MUCUS = "m"
        const val TYPE_TEMPERATURE = "t"
        const val TYPE_SEX = "i"
        const val TYPE_KEGEL = "k"
        const val TYPE_NOTE = "n"

        fun parse(key: String?, value: String?): Observation? {
            if (key == null || value == null) return null
            val dateAndType = key.split(":")
            val dateTime = DateTime(dateAndType[0].toLong())
            val type = dateAndType[1]
            return Observation(dateTime, type, value)
        }
    }

    val key: String
        get() = "${System.currentTimeMillis()}:$type"
}