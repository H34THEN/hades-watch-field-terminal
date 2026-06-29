package com.heathen.hadeswatch.features.signalreader

import org.json.JSONArray
import org.json.JSONObject

object SignalSnippetStorage {

    fun encode(snippets: List<SignalSnippet>): String {
        val array = JSONArray()
        snippets.forEach { snippet ->
            array.put(
                JSONObject()
                    .put("id", snippet.id)
                    .put("title", snippet.title)
                    .put("body", snippet.body)
                    .put("sourceLabel", snippet.sourceLabel)
                    .put("tags", snippet.tags)
                    .put("createdAt", snippet.createdAt)
                    .put("updatedAt", snippet.updatedAt),
            )
        }
        return array.toString()
    }

    fun decode(raw: String?): List<SignalSnippet> {
        if (raw.isNullOrBlank()) return emptyList()
        return try {
            val array = JSONArray(raw)
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    add(
                        SignalSnippet(
                            id = obj.optString("id").ifBlank { java.util.UUID.randomUUID().toString() },
                            title = obj.optString("title"),
                            body = obj.optString("body"),
                            sourceLabel = obj.optString("sourceLabel"),
                            tags = obj.optString("tags"),
                            createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
                            updatedAt = obj.optLong("updatedAt", System.currentTimeMillis()),
                        ),
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}
