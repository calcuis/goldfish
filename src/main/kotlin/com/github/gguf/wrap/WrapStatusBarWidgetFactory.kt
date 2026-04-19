package com.github.gguf.wrap

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class WrapStatusBarWidgetFactory : StatusBarWidgetFactory {

    override fun getId(): String = WrapStatusBarWidget.ID

    override fun getDisplayName(): String = "Wrap"

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget = WrapStatusBarWidget(project)

    override fun disposeWidget(widget: StatusBarWidget) = widget.dispose()

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}
