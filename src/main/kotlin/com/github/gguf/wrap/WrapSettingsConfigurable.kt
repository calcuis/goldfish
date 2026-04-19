package com.github.gguf.wrap

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class WrapSettingsConfigurable : Configurable {

    private var panel: JPanel? = null
    private val configPathField = TextFieldWithBrowseButton()

    override fun getDisplayName(): String = "Wrap"

    override fun createComponent(): JComponent {
        configPathField.addBrowseFolderListener(
            "Select wrapper.json",
            "Choose the wrapper.json configuration file for this project",
            null,
            FileChooserDescriptorFactory.createSingleFileDescriptor("json")
        )
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Custom wrapper.json path:"), configPathField, 1, false)
            .addTooltip("Leave empty to use wrapper.json in the project root.")
            .addComponentFillVertically(JPanel(), 0)
            .panel
        return panel!!
    }

    override fun isModified(): Boolean =
        configPathField.text != WrapSettingsState.getInstance().configPath

    override fun apply() {
        WrapSettingsState.getInstance().configPath = configPathField.text
    }

    override fun reset() {
        configPathField.text = WrapSettingsState.getInstance().configPath
    }

    override fun disposeUIResources() {
        panel = null
    }
}
