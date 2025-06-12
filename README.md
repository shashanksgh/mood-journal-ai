# 🧠 Mood Journal AI

A smart journaling backend that lets users write personal journal entries and receive AI-powered mood analysis. Built with **Spring Boot**, **H2 Database**, and designed for future integration with **OpenAI GPT APIs**.

---

## 📌 Features

- 📝 **Create, Read journal entries**
- 🧠 **(Upcoming)**: AI-powered emotion detection per entry
- 👤 Associate journal entries with individual users
- 🗃️ In-memory H2 DB for quick testing and dev
- 🧪 Easily extensible, testable, and production-ready structure

---

## ⚙️ Tech Stack

- **Backend**: Spring Boot, Spring Web, Spring Data JPA
- **Database**: H2 (in-memory for now)
- **Language**: Java 17+
- **Build Tool**: Maven
- **(Future)**: OpenAI API, Kafka (optional)

---

## 🚀 Getting Started

### 📦 Prerequisites

- Java 17+
- Maven

### ▶️ Run Locally

```bash
git clone https://github.com/shashanksgh/mood-journal-ai.git
cd mood-journal-ai
./mvnw spring-boot:run
