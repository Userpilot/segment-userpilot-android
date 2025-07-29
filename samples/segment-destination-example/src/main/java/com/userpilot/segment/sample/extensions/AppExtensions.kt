package com.userpilot.segment.sample.extensions

/**
 * Created by Motasem Hamed
 * on 12 Sep, 2024
 */

fun Map<String, Any>.getKeyValueByIndex(index: Int): Pair<String, Any>? {
    return if (index in 0 until this.size) {
        val entry = this.entries.elementAt(index)
        entry.toPair() // Convert Map.Entry to a Pair<String, Any>
    } else {
        null // Return null if index is out of bounds
    }
}
