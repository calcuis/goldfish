package com.github.gguf.wrap

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.SystemInfo
import org.jetbrains.plugins.terminal.ShellTerminalWidget
import org.jetbrains.plugins.terminal.TerminalView
import java.awt.datatransfer.StringSelection

object TerminalHelper {

    @Volatile
    private var sharedWidget: ShellTerminalWidget? = null

    fun runCommand(project: Project, label: String, command: String, useWslLabels: Set<String>) {
        val terminalView = TerminalView.getInstance(project)
        val workDir = project.basePath
        val actualCommand = if (SystemInfo.isWindows && label in useWslLabels) {
            wslWrap(command)
        } else {
            command
        }

        ApplicationManager.getApplication().invokeLater {
            val widget = terminalView.createLocalShellWidget(workDir, "Wrap [$label]", true)
            sharedWidget = widget
            // Give the shell a moment to initialise before sending the command
            ApplicationManager.getApplication().executeOnPooledThread {
                Thread.sleep(800)
                ApplicationManager.getApplication().invokeLater {
                    runCatching { widget.executeCommand(actualCommand) }
                }
            }
        }
    }

    fun sendToTerminal(project: Project, text: String) {
        val widget = aliveWidget()
        if (widget == null) {
            CopyPasteManager.getInstance().setContents(StringSelection(text))
            notify(project, "No active Wrap terminal — text copied to clipboard. Paste it in your terminal.", NotificationType.WARNING)
            return
        }
        ApplicationManager.getApplication().invokeLater {
            runCatching {
                val connector = widget.ttyConnector
                if (connector != null && connector.isConnected) {
                    connector.write(text.toByteArray(Charsets.UTF_8))
                } else {
                    CopyPasteManager.getInstance().setContents(StringSelection(text))
                    notify(project, "Terminal not ready — text copied to clipboard.", NotificationType.WARNING)
                }
            }.onFailure {
                CopyPasteManager.getInstance().setContents(StringSelection(text))
                notify(project, "Could not reach terminal — text copied to clipboard.", NotificationType.WARNING)
            }
        }
    }

    private fun aliveWidget(): ShellTerminalWidget? {
        val w = sharedWidget ?: return null
        return if (Disposer.isDisposed(w)) { sharedWidget = null; null } else w
    }

    private fun wslWrap(command: String): String {
        val escaped = command.replace("'", "'\\''")
        return "wsl -d Ubuntu -e sh -c '$escaped'"
    }

    private fun notify(project: Project, message: String, type: NotificationType) {
        Notifications.Bus.notify(Notification("Wrap", "Wrap", message, type), project)
    }
}
