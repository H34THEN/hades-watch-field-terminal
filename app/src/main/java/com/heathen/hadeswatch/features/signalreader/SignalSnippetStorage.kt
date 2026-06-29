package com.heathen.hadeswatch.features.signalreader

import org.json.JSONArray
import org.json.JSONObject

object SignalSnippetStorage {

    private const val EXPORT_VERSION = 1

    fun encode(snippets: List<SignalSnippet>): String {
        val array = JSONArray()
        snippets.sortedWith(compareBy<SignalSnippet> { it.sortOrder }.thenBy { it.title.lowercase() })
            .forEach { snippet ->
                array.put(snippetToJson(snippet))
            }
        return JSONObject()
            .put("version", EXPORT_VERSION)
            .put("snippets", array)
            .toString()
    }

    fun encodeArrayOnly(snippets: List<SignalSnippet>): String {
        val array = JSONArray()
        snippets.forEach { array.put(snippetToJson(it)) }
        return array.toString()
    }

    fun decode(raw: String?): List<SignalSnippet> {
        if (raw.isNullOrBlank()) return emptyList()
        return try {
            when {
                raw.trimStart().startsWith("{") -> {
                    val root = JSONObject(raw)
                    val array = root.optJSONArray("snippets") ?: JSONArray()
                    decodeArray(array)
                }
                else -> decodeArray(JSONArray(raw))
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun decodeArray(array: JSONArray): List<SignalSnippet> = buildList {
        for (i in 0 until array.length()) {
            parseSnippet(array.getJSONObject(i))?.let { add(it) }
        }
    }

    private fun snippetToJson(snippet: SignalSnippet): JSONObject =
        JSONObject()
            .put("id", snippet.id)
            .put("title", snippet.title)
            .put("body", snippet.body)
            .put("sourceLabel", snippet.sourceLabel)
            .put("tags", sanitizeTags(snippet.tags))
            .put("createdAt", snippet.createdAt)
            .put("updatedAt", snippet.updatedAt)
            .put("sortOrder", snippet.sortOrder)

    private fun parseSnippet(obj: JSONObject): SignalSnippet? {
        val title = obj.optString("title").trim()
        val body = obj.optString("body").trim()
        if (title.isBlank() || body.isBlank()) return null
        return SignalSnippet(
            id = obj.optString("id").ifBlank { java.util.UUID.randomUUID().toString() },
            title = title,
            body = body,
            sourceLabel = obj.optString("sourceLabel").trim(),
            tags = sanitizeTags(obj.optString("tags")),
            createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
            updatedAt = obj.optLong("updatedAt", System.currentTimeMillis()),
            sortOrder = obj.optInt("sortOrder", 0),
        )
    }

    fun sanitizeTags(raw: String): String =
        raw.split(",", ";")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(", ")

    fun validateImportJson(raw: String): ImportPreview {
        val parsed = try {
            when {
                raw.trimStart().startsWith("{") -> {
                    val root = JSONObject(raw.trim())
                    val array = root.optJSONArray("snippets") ?: JSONArray()
                    (0 until array.length()).mapNotNull { i ->
                        val obj = array.getJSONObject(i)
                        val title = obj.optString("title").trim()
                        val body = obj.optString("body").trim()
                        when {
                            title.isBlank() || body.isBlank() -> null
                            else -> obj
                        }
                    }
                }
                raw.trimStart().startsWith("[") -> {
                    val array = JSONArray(raw.trim())
                    (0 until array.length()).mapNotNull { i ->
                        val obj = array.getJSONObject(i)
                        val title = obj.optString("title").trim()
                        val body = obj.optString("body").trim()
                        when {
                            title.isBlank() || body.isBlank() -> null
                            else -> obj
                        }
                    }
                }
                else -> emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }

        val totalObjects = runCatching {
            when {
                raw.trimStart().startsWith("{") ->
                    JSONObject(raw.trim()).optJSONArray("snippets")?.length() ?: 0
                raw.trimStart().startsWith("[") -> JSONArray(raw.trim()).length()
                else -> 0
            }
        }.getOrDefault(0)

        val valid = parsed.mapNotNull { parseSnippet(it) }
        val invalid = totalObjects - valid.size

        return ImportPreview(
            total = totalObjects,
            validCount = valid.size,
            invalidCount = invalid.coerceAtLeast(0),
            snippets = valid,
        )
    }

    data class ImportPreview(
        val total: Int,
        val validCount: Int,
        val invalidCount: Int,
        val snippets: List<SignalSnippet>,
    )

    data class ImportResult(
        val added: Int,
        val skipped: Int,
        val duplicate: Int,
        val invalid: Int,
    )
}
