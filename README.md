Profidata FX OrderBook CLI Application
Table of Contents
Introduction

Requirements

Technical Stack

Project Structure

How to Build and Run

Prerequisites

Starting the Order Service

Building the CLI Application

Running the CLI Application

Usage

Production Readiness Considerations

Author

1. Introduction
This project implements a Command-Line Interface (CLI) application for an FX OrderBook, developed as a solution to the Profidata OrderBook Exercise. The application interacts with a provided external HTTP-based Order Service to manage and query foreign exchange orders. It is designed to be production-ready, focusing on robustness, maintainability, and adherence to specified requirements.

2. Requirements
The CLI application supports the following commands:

new [buy|sell] <investment ccy> <counter ccy> <limit> <validity>:
Creates a new order in the order book. Prints the ID of the newly created order.

Example: new buy EUR CHF 1.14 31.12.2025

cancel <ID>:
Cancels the order with the given ID.

Example: cancel 5

rates:
Prints an overview of the current exchange rates as provided by the Order Service.

orders:
Prints an overview of all orders currently in the order book. Orders are sorted by currency pair and their distance to the current market rate (which is also displayed).

Example output:

buy    EUR   USD   1.4   31.12.2018  0.1
sell   EUR   USD   1.2   31.12.2018  0.3
buy    EUR   USD   1     31.12.2018  0.5

summary:
Calculates and prints a summary of the current order book. Orders are grouped and sorted by investment currency, counter currency, and buy/sell type. The output includes the number of orders and their average limit for each group.

Example output:

TYPE    INV     CTR     COUNT AVERAGE
====================================
sell    EUR     CHF     2     1.16
buy     EUR     USD     1     1.00
sell    EUR     USD     1     2.00

3. Technical Stack
Java: JDK 21+

Build Tool: Maven

Libraries:

Lombok: For reducing boilerplate code in model classes (@Data, @NoArgsConstructor, @AllArgsConstructor, @Builder).

Jackson: For JSON serialization and deserialization (used for communication with the HTTP service).

java.net.http.HttpClient: The built-in Java 11+ HTTP client for making API calls to the Order Service.

4. Project Structure
The repository contains two main components:

order-service/: This directory contains the provided executable JAR for the Order Service. It is an external component that our CLI application communicates with.

order-service.jar

openapi.yaml (API documentation for the service)

order-service-cli/: This directory contains the source code for the Java CLI application you developed.

pom.xml (Maven project file)

src/main/java/... (Java source code for commands, models, service integration)

5. How to Build and Run
Prerequisites
Java Development Kit (JDK) 21 or higher.

Apache Maven (compatible with JDK 21+).

Starting the Order Service
The Order Service must be running for the CLI application to function.

Open a new terminal window.

Navigate to the order-service directory:

cd order-service/

Run the Order Service JAR file:

java -jar order-service.jar

The service typically starts on http://localhost:8080 by default. Keep this terminal window open as the service needs to remain running.

Note: If you need to reset the service's data, stop the server, delete fxRates.ser and orders.ser files from the order-service/ directory, and then restart the service.

Building the CLI Application
Open a separate terminal window.

Navigate to the order-service-cli directory:

cd order-service-cli/

Build the project using Maven:

mvn clean install

This command compiles the source code, runs tests, and packages the application into a JAR file in the target/ directory.

Running the CLI Application
From the order-service-cli directory in your terminal, execute the built JAR:

java -jar target/order-service-cli-<version>.jar

(Replace <version> with the actual version number from your pom.xml, e.g., 1.0-SNAPSHOT)

The application will now be running and ready to accept commands via standard input.

6. Usage
Once the CLI application is running, type commands into the terminal and press Enter.

Create a new buy order:

new buy EUR CHF 1.14 31.12.2025

(You will see the generated order ID printed)

Create a new sell order:

new sell USD JPY 105.50 01.01.2026

View current exchange rates:

rates

View all orders with market rate proximity:

orders

Cancel an order (replace 1 with the actual order ID):

cancel 1

Get a summary of the order book:

summary

To exit the CLI application, you can usually press Ctrl+C.

7. Production Readiness Considerations
In developing this solution, the following aspects were prioritized to ensure production readiness:

Modularity and Separation of Concerns: The application logic is divided into distinct components (commands, service integration, models) to enhance readability and maintainability.

Robust Error Handling: Mechanisms are in place to gracefully handle invalid user input, network issues when communicating with the order service, and unexpected API responses. User-friendly error messages are provided.

Clear Output Formatting: All output to stdout is formatted for clarity and ease of reading, especially for tabular data like rates, orders, and summary.

Data Validation: Input validation for commands (e.g., date formats, numeric limits) ensures data integrity before interacting with the service.

Logging (Potential Enhancement): While not explicitly implemented in this basic CLI, a production-ready application would incorporate a logging framework (e.g., SLF4J with Logback/Log4j2) for better diagnostics and monitoring.

Dependency Management: Maven is used to manage all project dependencies, ensuring consistent and reproducible builds.

Modern Java Features: Leveraging features from JDK 21+ (e.g., HttpClient, Stream API) for efficient and concise code.

8. Author
[Your Name/GitHub Username]
(e.g., Madalin Ghergut)
