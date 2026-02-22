# AIGateway (AI 应用中台 / LLM Control Plane)

A production-oriented **AI Application Platform** that provides a unified **LLM Gateway + PromptOps + Governance** layer for building reliable, cost-effective, and observable AI features.

> Focus: **Reliability / Cost Control / Observability / Security & Compliance / Pluggable Engineering Abstractions**

---

## Why this project

LLM features often fail in production not because “the model is weak”, but because engineering essentials are missing:
- no stable prompt versioning & rollout
- no fallback/retry/timeout policies
- repeated requests burn money
- lack of trace/metrics/audit
- prompt injection / sensitive data leakage risk

This project aims to provide a reusable “control plane” for AI applications.

---

## Core Modules

### 1) Prompt Management (PromptOps)
- Prompt versioning (config-center style)
- AB Test / gray release (traffic split)
- Output contract: **JSON Schema constrained output**

### 2) LLM Gateway (Core)
- Multi-provider integration: OpenAI / Claude / DeepSeek / local Ollama
- Retry / degrade / fallback
- Timeout, token budget, concurrency control

### 3) Result Cache & Idempotency
- `hash(promptVersion + normalizedInput)` → Redis
- Prevent duplicated billing, improve P99 latency

### 4) Async & Task Queue
- RabbitMQ / Kafka (pluggable)
- Long-text processing / batch jobs / document analysis pipelines

### 5) Security & Audit (High-value)
- Prompt injection defense
- Input/output redaction (PII masking)
- End-to-end **structured logs + audit trail**

### 6) Business Demos
- AI Text Moderation
- Ticket / Work-order Auto Attribution
- Knowledge Base QA (RAG)

### Extra: RAG Capability
- Upload PDF/TXT → chunking → embedding → vector DB
- Retrieval + re-ranking + answer generation

---

## Non-functional Goals

### Reliability
- timeout, retry, circuit breaker, fallback
- concurrency limits, rate limit, quotas

### Cost Control
- caching, idempotency
- batching, token budgets
- provider routing policies

### Observability
- tracing (OpenTelemetry-ready)
- metrics (Micrometer/Prometheus-ready)
- structured logs (JSON) + audit events

### Security & Compliance
- redaction, access control, injection defense
- data retention policy & auditability

### Engineering Abstractions
- provider interface, strategy/policy pattern
- plug-in architecture
- versioned configs

---

## Quick Start (Local)

> Prerequisites:
- Java 17+ (recommended)
- Maven 3.9+
- (Optional) Redis / RabbitMQ (for later modules)

```bash
# build & run
mvn clean package
mvn spring-boot:run