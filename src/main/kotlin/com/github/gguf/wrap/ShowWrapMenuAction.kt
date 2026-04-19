package com.github.gguf.wrap

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.WindowManager

class ShowWrapMenuAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        showMenu(e.project ?: return)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    companion object {

        fun showMenu(project: Project) {
            val blocks = WrapConfigLoader.load(project)
            if (blocks == null) {
                val result = Messages.showOkCancelDialog(
                    project,
                    "wrapper.json not found in project root.\n\nCreate one, or configure a custom path in Settings → Tools → Wrap.",
                    "Wrap",
                    "Open Settings",
                    "Cancel",
                    Messages.getWarningIcon()
                )
                if (result == Messages.OK) {
                    ShowSettingsUtil.getInstance().showSettingsDialog(project, WrapSettingsConfigurable::class.java)
                }
                return
            }

            val useWslLabels = blocks.flatMap { it.useWsl }.toSet()

            // Merge categories from all blocks, preserving insertion order
            val categoriesMap = linkedMapOf<String, MutableList<WrapEntry>>()
            for (block in blocks) {
                for (cat in block.categories) {
                    categoriesMap.getOrPut(cat.name) { mutableListOf() }.addAll(cat.entries)
                }
            }
            if (categoriesMap.isEmpty()) return

            showChooser(project, "Wrap — Select Category", categoriesMap.keys.toList()) { selectedCategory ->
                val entries = categoriesMap[selectedCategory] ?: return@showChooser
                showChooser(project, "Wrap — $selectedCategory", entries.map { it.label }) { selectedLabel ->
                    val entry = entries.find { it.label == selectedLabel } ?: return@showChooser
                    if (selectedCategory == "Command") {
                        TerminalHelper.runCommand(project, selectedLabel, entry.value, useWslLabels)
                    } else {
                        TerminalHelper.sendToTerminal(project, entry.value)
                    }
                }
            }
        }

        private fun showChooser(project: Project, title: String, items: List<String>, onSelect: (String) -> Unit) {
            JBPopupFactory.getInstance()
                .createPopupChooserBuilder(items)
                .setTitle(title)
                .setMovable(true)
                .setResizable(true)
                .setRequestFocus(true)
                .setItemChosenCallback { onSelect(it) }
                .createPopup()
                .apply {
                    val frame = WindowManager.getInstance().getFrame(project)
                    if (frame != null) showInCenterOf(frame) else showInFocusCenter()
                }
        }
    }
}
