# ThreatAlert

Web application for scanning URLs and files for malicious threats using the VirusTotal API, with AI-generated summaries, scan history, bookmarks, and a discussion section.

## Features
- URL and file scanning via the VirusTotal API
- AI-generated scan summaries (OpenAI API)
- Scan history with bookmarking
- Comment/discussion section per scan
- Docker support

## Tech Stack
- Java 17, Spring Boot, Thymeleaf
- PostgreSQL
- HTML/CSS, JavaScript, Bootstrap
- Docker

## Prerequisites
- Java 17+
- A PostgreSQL database (run locally via Docker, or use any hosted provider)
- API keys: [VirusTotal](https://www.virustotal.com/gui/join-us) and [OpenAI](https://platform.openai.com)

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/anniecee/threat-alert.git
   cd threat-alert
   ```

2. Set the required environment variables:

   | Variable | Description | Example |
   |---|---|---|
   | `DB_URL` | JDBC connection string | `jdbc:postgresql://localhost:5432/threatalert` |
   | `DB_USERNAME` | Database username | `postgres` |
   | `DB_PASSWORD` | Database password | — |
   | `VIRUSTOTAL_API_KEY` | VirusTotal API key | — |
   | `OPENAI_API_KEY` | OpenAI API key | — |

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Open http://localhost:8080 in your browser.

### Docker

```bash
docker build -t threatalert .
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/threatalert \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=secret \
  -e VIRUSTOTAL_API_KEY=your-key \
  -e OPENAI_API_KEY=your-key \
  threatalert
```

## Demo
- Landing page
![Landing page](https://i.imgur.com/8YI0l5w.png)
- URL Scanning
![URL Scanning](https://i.imgur.com/KIXnkc2.png)
![URL Scanning 2](https://i.imgur.com/IwUP8YJ.png)
- File Scanning
![File Scanning](https://i.imgur.com/fTHshKV.png)
![File Scanning 2](https://i.imgur.com/o4ROg0W.png)
- Scan History
![Scan History](https://i.imgur.com/tpYraJi.png)
