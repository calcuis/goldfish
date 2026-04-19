package com.github.gguf.wrap

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.github.gguf.wrap.WrapSettingsState",
    storages = [Storage("WrapPlugin.xml")]
)
class WrapSettingsState : PersistentStateComponent<WrapSettingsState> {

    var configPath: String = ""

    override fun getState(): WrapSettingsState = this

    override fun loadState(state: WrapSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): WrapSettingsState =
            ApplicationManager.getApplication().getService(WrapSettingsState::class.java)
    }
}
