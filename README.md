# Translator Bot

A Telegram bot built using **Spring Boot** that translates text between **English**, **Russian**, and **Uzbek** using
the **Google Translate API**.

---

## Features

- Translate text between the supported languages: English, Russian, and Uzbek.
- Seamless integration with the Google Translate API for accurate translations.
- User-friendly Telegram commands and keyboard options.
- Customizable webhook and bot configurations via `application.yml`.

---

## Disclaimer

This bot was developed using the tools and configurations described below. However, technologies and APIs may evolve,
and some information may become outdated. It’s highly recommended to refer to the **official documentation** for the
most up-to-date details.

---

## Getting Started

### Prerequisites

1. **Google Cloud Platform Setup**
    - **Register on Google Cloud Console**: Visit [Google Cloud Console](https://console.cloud.google.com). Enable a
      billing account and create a new project.
    - **Enable the Translate API**: Navigate to
      the [API Library](https://console.cloud.google.com/apis/library/translate.googleapis.com). Enable the **Google
      Translate API** for your project.
    - **Set Up OAuth Consent Screen**: Go to the **OAuth consent screen** section in the Google Cloud Console and
      configure it for your project.
    - **Install Google Cloud CLI (gcloud)**: Follow the instructions to install the
      CLI: [Install gcloud CLI](https://cloud.google.com/cli).
    - **Generate and Inject Access Token**: Use the following commands to generate and inject the access token into the
      local environment:
      ```bash
      gcloud init
      gcloud auth application-default print-access-token
      ```
      This ensures the access token isn’t hardcoded in the source code.

2. **Telegram Bot Setup**
    - Open the [BotFather](https://t.me/botfather) bot on Telegram.
    - Create a new bot using `/newbot` and follow the instructions.
    - Save the bot token provided by BotFather for later use.

---

## Methods and Frameworks Used

- **Java**: The bot is developed in Java for robustness and scalability.
- **Spring Boot**: A framework used to simplify development with dependency injection, RESTful APIs, and more.
- **Google Translate API**: Integrated for reliable and accurate translations.
- **Telegram Bot API**: Used to connect and manage the bot’s interactions on Telegram.

---

## Configuration

### [**application.yml**](src/main/resources/application.yaml)

Update the following configuration file with your bot's details:

```yaml
spring:
  application:
    name: app-translator
  profiles:
    active: dev
server:
  port: 80

telegram:
  webhook-path: "YOUR_BOT_WEBHOOK"
  bot-name: "YOUR_BOT_USERNAME"
  bot-token: "YOUR_BOT_TOKEN"
  chat-id:
    log: YOUR_LOGGING_CHAT_ID

logging:
  level:
    root: warn
  file:
    name: app-translator.log
  logback:
    rollingpolicy:
      max-file-size: 10MB
      max-history: 10
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n"
```

Replace the placeholders (`YOUR_BOT_WEBHOOK`, `YOUR_BOT_USERNAME`, and `YOUR_BOT_TOKEN`) with your bot's information.

### **Setting Up Logging Group**

1. **Create a new Telegram group** for logging. This group will be used to capture bot logs.
2. **Find the group ID** for this group:
    - Open the group in Telegram and use a bot like `@userinfobot` to get the group ID.
    - Add this group ID to the `application.yml` under `telegram.chat-id.log`.

### **Replace `YOUR_CLOUD_PROJECT_ID`** with the actual Google Cloud project ID.

- You can find your project ID in the Google Cloud Console:
    - Go to [Google Cloud Console](https://console.cloud.google.com).
    - Click on the project drop-down menu at the top and select the appropriate project.
    - The project ID is listed in the project details section.

---

## Running the Application

### **1. Clone the Repository**

Clone the project repository to your local machine:

```bash
git clone https://github.com/dilshodlatipov/translator-bot.git
cd translator-bot
```

### **2. Build and Run the Application**

Ensure you have **Java 17+** and **Maven** installed. Then build and run the project:

```bash
mvn clean install
java -jar target/translator-bot.jar
```

---

## Usage

### **Commands**

- **/start**: Check if the bot is active and get a welcome message.
- **/help**: Get instructions on how to use the bot.
- **/english**: Set the source language to English.
- **/russian**: Set the source language to Russian.
- **/uzbek**: Set the source language to Uzbek.
- **/language**: Change the bot's interface language.

### **How It Works**

1. Use a command to set the source language (**/english**, **/russian**, or **/uzbek**).
2. Send the text to be translated.
3. Select the target language using the keyboard provided.
4. Receive the translated text.

---

## Official Documentation

For the latest updates, refer to these official resources:

- **Spring Boot Documentation**: [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
- **Google Translate API Documentation
  **: [https://cloud.google.com/translate/docs](https://cloud.google.com/translate/docs)
- **Google Cloud CLI Documentation**: [https://cloud.google.com/sdk/docs](https://cloud.google.com/sdk/docs)
- **Telegram Bot API Documentation**: [https://core.telegram.org/bots/api](https://core.telegram.org/bots/api)

---

## Logging

Logs are saved in `app-translator.log`. Customize logging settings in `application.yml` under the `logging` section.
Ensure you set up the logging group as described above.

---

## Contributing

Contributions are welcome! Feel free to fork the repository and submit pull requests.

---

## License

This project is licensed under the MIT License.