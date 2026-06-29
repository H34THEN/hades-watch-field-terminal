package com.heathen.hadeswatch.features.gateways

import org.json.JSONArray
import org.json.JSONObject

object GatewayStorage {

    private const val EXPORT_VERSION = 1

    fun encode(gateways: List<GatewayDefinition>): String {
        val array = JSONArray()
        gateways.forEach { gateway ->
            array.put(gatewayToJson(gateway))
        }
        return JSONObject()
            .put("version", EXPORT_VERSION)
            .put("gateways", array)
            .toString()
    }

    fun encodeArrayOnly(gateways: List<GatewayDefinition>): String {
        val array = JSONArray()
        gateways.forEach { array.put(gatewayToJson(it)) }
        return array.toString()
    }

    fun decode(raw: String?): List<GatewayDefinition> {
        if (raw.isNullOrBlank()) return emptyList()
        return try {
            when {
                raw.trimStart().startsWith("{") -> {
                    val root = JSONObject(raw)
                    val array = root.optJSONArray("gateways") ?: JSONArray()
                    decodeArray(array)
                }
                else -> decodeArray(JSONArray(raw))
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun decodeArray(array: JSONArray): List<GatewayDefinition> = buildList {
        for (i in 0 until array.length()) {
            parseGateway(array.getJSONObject(i))?.let { add(it) }
        }
    }

    private fun gatewayToJson(gateway: GatewayDefinition): JSONObject =
        JSONObject()
            .put("id", gateway.id)
            .put("displayName", gateway.displayName)
            .put("url", gateway.url)
            .put("iconKey", gateway.iconKey)
            .put("icon", gateway.iconKey) // legacy compat
            .put("customIconUri", gateway.customIconUri)
            .put("category", gateway.category)
            .put("note", gateway.note)
            .put("launchMode", gateway.launchMode.name)
            .put("createdAt", gateway.createdAt)
            .put("updatedAt", gateway.updatedAt)
            .put("lastOpenedAt", gateway.lastOpenedAt ?: JSONObject.NULL)
            .put("sortOrder", gateway.sortOrder)

    private fun parseGateway(obj: JSONObject): GatewayDefinition? {
        val url = obj.optString("url")
        if (url.isBlank()) return null
        val iconKey = obj.optString("iconKey").ifBlank { obj.optString("icon", GatewayIcon.CUSTOM.name) }
        val launchMode = runCatching {
            GatewayLaunchMode.valueOf(obj.optString("launchMode", GatewayLaunchMode.EXTERNAL_BROWSER.name))
        }.getOrDefault(GatewayLaunchMode.EXTERNAL_BROWSER)
        val lastOpened = if (obj.isNull("lastOpenedAt")) null else obj.optLong("lastOpenedAt")
        return GatewayDefinition(
            id = obj.optString("id").ifBlank { java.util.UUID.randomUUID().toString() },
            displayName = obj.optString("displayName", "Gateway"),
            url = url,
            iconKey = iconKey,
            customIconUri = obj.optString("customIconUri").ifBlank { null },
            category = obj.optString("category"),
            note = obj.optString("note"),
            launchMode = launchMode,
            createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
            updatedAt = obj.optLong("updatedAt", System.currentTimeMillis()),
            lastOpenedAt = lastOpened,
            sortOrder = obj.optInt("sortOrder", 0),
        )
    }

    fun validateImportJson(raw: String): ImportPreview {
        val parsed = decode(raw)
        val valid = parsed.filter { GatewayUrlValidator.isValid(it.url) }
        val invalid = parsed.size - valid.size
        return ImportPreview(
            total = parsed.size,
            validCount = valid.size,
            invalidCount = invalid,
            gateways = valid,
        )
    }

    data class ImportPreview(
        val total: Int,
        val validCount: Int,
        val invalidCount: Int,
        val gateways: List<GatewayDefinition>,
    )
}
