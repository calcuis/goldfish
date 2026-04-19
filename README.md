# goldfish 🐠

Goldfish is an advanced, evolving AI agent designed to learn from every interaction. Unlike traditional static agents, it builds a persistent understanding of your preferences, picks up new skills on the fly, and expands its capabilities through a modular ecosystem of tools and skills. Goldfish makes coding task handy, debugging easy, reviewing large codebase like eating a small piece of pizza.

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
