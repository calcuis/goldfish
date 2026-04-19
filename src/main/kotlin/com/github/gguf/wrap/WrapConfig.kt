package com.github.gguf.wrap

import com.google.gson.JsonParser
import com.intellij.openapi.project.Project
import java.io.File

data class WrapEntry(val label: String, val value: String)
data class WrapCategory(val name: String, val entries: List<WrapEntry>)
data class WrapBlock(val useWsl: List<String>, val categories: List<WrapCategory>)

object WrapConfigLoader {

    fun load(project: Project): List<WrapBlock>? {
        val configPath = WrapSettingsState.getInstance().configPath
        val file = when {
            configPath.isNotBlank() -> File(configPath)
            project.basePath != null -> File(project.basePath!!, "wrapper.json")
            else -> return null
        }
        if (!file.exists()) return null
        return runCatching { parse(file.readText()) }.getOrNull()
    }

    private fun parse(json: String): List<WrapBlock> {
        val array = JsonParser.parseString(json).asJsonArray
        return array.map { element ->
            val obj = element.asJsonObject
            val useWsl = mutableListOf<String>()
            val categories = mutableListOf<WrapCategory>()
            for ((key, value) in obj.entrySet()) {
                if (key == "useWsl") {
                    value.asJsonArray.forEach { useWsl.add(it.asString) }
                } else {
                    val entries = value.asJsonArray.flatMap { entryEl ->
                        entryEl.asJsonObject.entrySet().map { (k, v) ->
                            WrapEntry(k, v.asString)
                        }
                    }
                    categories.add(WrapCategory(key, entries))
                }
            }
            WrapBlock(useWsl, categories)
        }
    }
}
