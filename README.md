# Money-transfer application
Money-transfer application represents interfaces for the following operations: 
- adding account (responsible for balance) and party (responsible for personal info)
- retrieving existed account/party
- transfer money from account to account if balance can afford it

## Technical Requirements
- Java 11
- Maven 3.3+
- Git 

## Used back-end stack
- **Netty Project Reactor** as http core
- **Google Guice** as a simple DI
- **Embedded MongoDB** as a tool of independent executing
- **REST Assured** as a integration tests helper

## How to build
```
mvn clean install
```

## How to run
Make sure that ports those are mentioned in application.properties are free.

Command to run application:
```
java -jar target/money-transfer-XXX.jar
```
