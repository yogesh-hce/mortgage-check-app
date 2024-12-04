# **Mortgage Service Application**

The Mortgage Service Application is a Spring Boot application that provides functionality to check mortgage feasibility and fetch current interest rates for various maturity periods.

---

## **Features**
- Fetch a list of current mortgage interest rates.
- Perform a mortgage feasibility check based on income, loan value, home value, and maturity period.
- Calculate monthly mortgage costs.

---

## **Technology Stack**
- **Java**: Backend development.
- **Spring Boot**: Framework for building REST APIs.
- **Maven**: Dependency management.
- **SLF4J**: Logging framework.
- **JUnit 5**: Unit testing.
- **Mockito**: Mocking framework for tests.

---

## **Getting Started**

### **Prerequisites**
- Java 17 or higher
- Maven 3.8 or higher
- An IDE (e.g., IntelliJ, Eclipse)

### **Build the Project**
mvn clean install

### **Run the Application**
mvn spring-boot:run

also by java -jar target/mortgage-service-1.0.0.jar

### **Run Tests**
mvn clean test

API examples 

POST http://localhost:8080/api/mortgage-check

Request Example:

{
  "income": 100000,
  "maturityPeriod": 20,
  "loanValue": 200000,
  "homeValue": 250000
}

GET : http://localhost:8080/api/interest-rates