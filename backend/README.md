# Rural Entrepreneur Advisor - Backend

## SIH26091 - AI-Driven Hyper-Local Business Advisory System

Spring Boot backend for the Rural Entrepreneur Advisor application.

## Features

### Phase 1 - Financial Module (Current)
- ✅ Financial calculations based on available margin capital
- ✅ Automatic scheme routing (Micro Finance / Term Loan)
- ✅ EMI calculation using reducing balance method
- ✅ Quarterly repayment schedule generation
- ✅ Working capital and operational cost estimation
- ✅ Comprehensive input validation
- ✅ Global exception handling
- ✅ Unit tests for all financial calculations

### Phase 2 - Feasibility Module (Complete ✅)
- ✅ Business category management (JPA entity, repository, service)
- ✅ Location data services (hyper-local 5-10 km radius)
- ✅ AI advisory service interface (clean abstraction)
- ✅ Mock AI implementation (no real AI/ML yet)
- ✅ Structured market analysis (consumer base, demand, competition)
- ✅ SWOT analysis (strengths, weaknesses, opportunities, threats)
- ✅ Competitor mapping (2-3 competitors with details)
- ✅ Pricing recommendations (6+ guidance points)
- ✅ Distribution channel recommendations
- ✅ Risk assessment
- ✅ Complete feasibility reports
- ✅ REST API endpoint: POST /api/advisory/analyze
- ✅ 34+ unit tests with comprehensive coverage

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Web** (REST APIs)
- **Spring Data JPA**
- **PostgreSQL** (production database)
- **H2** (test database)
- **Lombok**
- **Maven**
- **JUnit 5** (testing)

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/sih/advisor/
│   │   │   ├── config/          # CORS, configuration
│   │   │   ├── controller/      # REST endpoints
│   │   │   ├── service/         # Business logic
│   │   │   ├── dto/             # Data transfer objects
│   │   │   ├── exception/       # Exception handling
│   │   │   └── AdvisorApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/
│       └── java/com/sih/advisor/service/
└── pom.xml
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 15+ (for production)

## Setup

### 1. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE sih_advisor;
```

### 2. Environment Variables

Set the following environment variables or update `application.properties`:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/sih_advisor
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
```

### 3. Build the Project

```bash
cd backend
mvn clean install
```

### 4. Run Tests

```bash
mvn test
```

### 5. Run the Application

**Development mode:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Production mode:**
```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

## API Endpoints

### Financial Module

#### Calculate Financial Breakdown

```http
POST /api/financial/calculate
Content-Type: application/json

{
  "availableMargin": 14000
}
```

**Response:**
```json
{
  "availableMargin": 14000.00,
  "projectCost": 140000.00,
  "maxLoanAmount": 126000.00,
  "actualLoanAmount": 125000.00,
  "applicableScheme": {
    "schemeName": "Micro Finance Scheme",
    "schemeType": "MICRO_FINANCE",
    "maxFunding": 125000.00,
    "interestRate": 6.5,
    "tenureYears": 3,
    "moratoriumMonths": 3
  },
  "emiAmount": 3838.50,
  "totalRepayment": 138186.00,
  "totalInterest": 13186.00,
  "totalInstallments": 36,
  "estimatedWorkingCapital": 21000.00,
  "estimatedOperationalCost": 28000.00,
  "message": "Your loan amount is capped at ₹125000.0 due to Micro Finance Scheme maximum funding limit."
}
```

#### Get Repayment Schedule

```http
GET /api/financial/repayment-schedule?loanAmount=125000&interestRate=6.5&tenureYears=3&moratoriumMonths=3
```

**Response:**
```json
{
  "loanAmount": 125000.00,
  "interestRate": 6.5,
  "tenureYears": 3,
  "moratoriumMonths": 3,
  "emiAmount": 3838.50,
  "totalRepayment": 138186.00,
  "totalInterest": 13186.00,
  "totalInstallments": 36,
  "schedule": [
    {
      "installmentNumber": 1,
      "quarter": 1,
      "year": 1,
      "principalAmount": 9476.34,
      "interestAmount": 2038.16,
      "totalPayment": 11514.50,
      "outstandingBalance": 115523.66
    }
    // ... more quarters
  ]
}
```

#### Health Check

```http
GET /api/financial/health
```

## Scheme Rules

### Micro Finance Scheme
- **Eligibility:** Project cost ≤ ₹1.40 lakh
- **Maximum funding:** ₹1.25 lakh
- **Interest rate:** 6.5% per annum
- **Tenure:** 3 years
- **Moratorium:** 3 months

### Term Loan Scheme
- **Eligibility:** Project cost > ₹1.40 lakh AND ≤ ₹50 lakh
- **Maximum funding:** ₹45 lakh
- **Interest rate:** 8% per annum
- **Tenure:** 7 years
- **Moratorium:** 6 months

## Financial Calculation Logic

```
Project Cost = Available Margin / 0.10
Maximum Loan = Project Cost × 0.90
Actual Loan = min(Maximum Loan, Scheme Max Funding)
EMI = [P × R × (1+R)^N] / [(1+R)^N - 1]
  where P = principal, R = monthly rate, N = months
```

## Testing

Run all tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=EMICalculatorServiceTest
```

## Error Handling

All errors return a consistent format:

```json
{
  "timestamp": "2026-09-04T13:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Available margin must be at least ₹1,000",
  "path": "/api/financial/calculate",
  "validationErrors": [
    {
      "field": "availableMargin",
      "message": "Available margin must be at least ₹1,000"
    }
  ]
}
```

## Development Notes

- All financial calculations are deterministic (no AI/ML involved)
- EMI uses reducing balance method
- Repayment schedules are aggregated quarterly
- During moratorium, only interest is paid
- All monetary values use `BigDecimal` for precision
- Comprehensive unit tests ensure calculation accuracy

## Phase 2 API Endpoints

### Advisory Module

#### Analyze Business Feasibility

```http
POST /api/advisory/analyze
Content-Type: application/json

{
  "village": "Rampur",
  "block": "Sadar",
  "district": "Meerut",
  "state": "Uttar Pradesh",
  "businessCategory": "Grocery Store",
  "availableMargin": 15000
}
```

**Response:** Complete feasibility report with:
- Location details
- Market analysis (consumer base, demand level, competition)
- SWOT analysis (strengths, weaknesses, opportunities, threats)
- Competitor analysis (2-3 local competitors)
- Pricing guidance (6+ recommendations)
- Distribution channels (4+ channels)
- Risk assessment (6+ risks)
- Financial summary (integrated from Phase 1)

#### Health Check

```http
GET /api/advisory/health
```

## Next Steps (Phase 3)

- [ ] React + Vite + TypeScript frontend
- [ ] Tailwind CSS styling
- [ ] Financial calculator UI
- [ ] Advisory analysis UI
- [ ] Responsive design
- [ ] API integration

## License

Smart India Hackathon 2026 Project
