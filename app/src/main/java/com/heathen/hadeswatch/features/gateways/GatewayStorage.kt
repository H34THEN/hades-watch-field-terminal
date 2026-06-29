package com.heathen.hadeswatch.features.gateways

import org.json.JSONArray
import org.json.JSONObject

object GatewayStorage {

    fun encode(gateways: List<GatewayDefinition>): String {
        val array = JSONArray()
        gateways.forEach { gateway ->
            array.put(
                JSONObject()
                    .put("id", gateway.id)
                    .put("displayName", gateway.displayName)
                    .put("url", gateway.url)
                    .put("icon", gateway.icon.name)
                    .put("category", gateway.category)
                    .put("note", gateway.note),
            )
        }
        return array.toString()
    }

    fun decode(raw: String?): List<GatewayDefinition> {
        if (raw.isNullOrBlank()) return emptyList()
        return try {
            val array = JSONArray(raw)
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    add(
                        GatewayDefinition(
                            id = obj.optString("id"),
                            displayName = obj.optString("displayName"),
                            url = obj.optString("url"),
                            icon = GatewayIcon.fromName(obj.optString("icon")),
                            category = obj.optString("category"),
                            note = obj.optString("note"),
                        ),
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}
