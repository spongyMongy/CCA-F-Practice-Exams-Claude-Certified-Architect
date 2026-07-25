package com.arslan.ccafprep.domain.model

enum class ExamDomain(val id: Int, val title: String, val weight: Double) {
    AGENTIC_ARCHITECTURE(1, "Agentic Architecture & Orchestration", 0.27),
    TOOL_DESIGN(2, "Tool Design & MCP Integration", 0.18),
    CLAUDE_CODE(3, "Claude Code Configuration & Workflows", 0.20),
    PROMPT_ENGINEERING(4, "Prompt Engineering & Structured Output", 0.20),
    CONTEXT_MANAGEMENT(5, "Context Management & Reliability", 0.15)
}
