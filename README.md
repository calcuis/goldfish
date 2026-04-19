# goldfish 🐠

Goldfish is an advanced, evolving AI agent designed to learn from every interaction. Unlike traditional static agents, Gold builds a persistent understanding of your preferences, picks up new skills on the fly, and expands its capabilities through a modular ecosystem of tools and skills.

![screenshot](https://raw.githubusercontent.com/calcuis/goldfish/master/demo.gif)

## ✨ Key Features

- **🧠 Adaptive Learning**: Remembers context, user preferences, and past conversations to build a personalized experience.
- **🛠️ Modular Toolsets**: A robust library of tools ranging from terminal orchestration and file manipulation to web browsing and code execution.
- **🌟 Skill Ecosystem**: Easily discover, install, and use "Skills"—specialized capabilities (e.g., blockchain, creative, devops) that extend the agent's power.
- **🔌 Multi-Platform Gateway**: Interact with your agent via your favorite interfaces:
  - **CLI**: A high-performance, customizable terminal interface with "skins" and "kawaii" animations.
  *   **Messaging Platforms**: Connect via Telegram, Discord, Slack, WhatsApp, and more.
- **⚙️ Agentic Workflow**: Supports subagent delegation, complex task planning, and background process management.
- **🛡️ Secure & Sandboxed**: Execute code and tools within controlled environments (Local, Docker, SSH, Modal, etc.).

---

## 🏗️ Architecture Overview

The Golden Agent architecture is built on a decoupled, modular design:

```mermaid
graph TD
    User[User Interface: CLI / Telegram / Discord] --> Gateway[Gateway: Message Dispatch & Session Management]
    Gateway --> Agent[AIAgent: Core Loop & Reasoning]
    Agent --> Tools[Tool Registry: Terminal, Web, File, etc.]
    Agent --> Skills[Skill Hub: Specialized Modules]
    Agent --> Memory[Memory Manager: Context & Long-term Storage]
    Tools --> Env[Environments: Local, Docker, SSH, etc.]
```

### Core Components

1.  **The Agent (`AIAgent`)**: The central intelligence. It manages the conversation loop, processes tool calls, and maintains the internal state.
2.  **The Tool Registry**: A unified interface for all capabilities. Every tool (e.g., `terminal_tool.py`) registers itself, allowing the agent to discover and invoke them dynamically.
3.  **The Gateway**: The bridge between the agent and the outside world. It adapts the agent's capabilities to various messaging protocols (Webhooks, Bot APIs).
4.   **Skills & Plugins**: High-level abstractions built on top of tools. Skills allow the agent to perform complex, domain-specific tasks (e.g., `research-paper-writing`).
