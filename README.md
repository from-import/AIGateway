# AIGateway (AI 应用中台 / LLM Control Plane)

An **AI Application Platform** that provides a unified control plane for prompt management, model routing, structured output, caching, async execution, security governance, and RAG ingestion/retrieval, enabling production-grade LLM applications rather than one-off demos.

> Focus: **Reliability / Cost Control / Observability / Security & Compliance / Pluggable Engineering Abstractions**
> 
> **这个项目的目标，不是单纯封装大模型接口，而是构建一个可复用的 AI 应用基础设施层。它向上为业务场景提供统一的文本生成、分类、抽取、问答和知识检索能力，向下屏蔽不同模型供应商、向量数据库和异步执行框架的差异；同时通过 Prompt 版本化、模型路由、缓存幂等、超时重试、限流配额、审计日志与脱敏治理，使 AI 能力具备生产环境可落地的可靠性、成本可控性和安全合规性。**

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

```bash
# build & run
mvn clean package
mvn spring-boot:run