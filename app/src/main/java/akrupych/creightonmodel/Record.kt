package akrupych.creightonmodel

data class Record(
        val time: Long = System.currentTimeMillis(),
        val value: String = "testValue"
)