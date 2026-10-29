# REST Assured Capstone Project - API Automation Framework

A production-ready API automation framework built with REST Assured, TestNG, and Maven, targeting the [GoRest API](https://gorest.co.in/).

## Framework Architecture

```
restassured-capstone/
  src/main/java/com/capstone/
    config/ConfigReader.java         Environment-specific property file reader
    models/                          POJO classes for API payloads
      User.java                      User resource model
      Post.java                      Post resource model
      Comment.java                   Comment resource model
    utils/
      ApiHelper.java                 Reusable REST API methods with authentication
      TestDataGenerator.java         Random test data generation
      EndpointConstants.java         API endpoint path constants
  src/main/resources/
    qa.properties                    QA environment configuration
    uat.properties                   UAT environment configuration
  src/test/java/com/capstone/
    tests/
      BaseTest.java                  Suite-level setup (base URI, logging)
      UserApiTest.java               User CRUD tests (5 tests + DataProvider)
      PostApiTest.java               Post API tests (3 tests)
      CommentApiTest.java            Comment API test (1 test)
    listeners/
      ExtentReportListener.java      ExtentReports HTML report generator
  testng.xml                         TestNG suite configuration
  pom.xml                            Maven build configuration
```

### Design Decisions

**POJO Serialization**: Java model classes (User, Post, Comment) with Jackson annotations handle JSON serialization/deserialization, providing type safety and IDE support.

**Reusable ApiHelper**: Static methods wrap REST Assured calls with authentication, content type, and logging. Tests call `ApiHelper.post()` instead of building requests manually.

**Environment Configuration**: Property files (`qa.properties`, `uat.properties`) store base URLs and tokens. Switch environments via `-Denv=uat` without changing code.

**Test Data Isolation**: Each test creates its own data and cleans up in `@AfterMethod`. No shared state between tests, no dependency on pre-existing data.

**DataProvider**: Bonus feature demonstrating TestNG's data-driven testing with multiple user creation scenarios (gender/status combinations).

## Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 17+ |
| Build Tool | Maven | 3.9+ |
| API Testing | REST Assured | 5.5.0 |
| Test Runner | TestNG | 7.10.2 |
| Reporting | ExtentReports | 5.1.2 |
| JSON Processing | Jackson | 2.17.2 |

## Test Coverage (10+ tests across 3 test classes)

| Test Class | Tests | HTTP Methods | Validates |
|-----------|-------|-------------|-----------|
| `UserApiTest` | 5 + DataProvider (4) | GET, POST, PUT, DELETE | Status codes, user fields, response time |
| `PostApiTest` | 3 | GET, POST | Post fields, user filtering |
| `CommentApiTest` | 1 | POST | Comment creation chain |

## Prerequisites

- Java JDK 17+ (`java --version`)
- Maven 3.9+ (`mvn --version`)
- GoRest API token (register at https://gorest.co.in/consumer/login)

## Setup

```bash
git clone <repo-url>
cd restassured-capstone
```

Add your GoRest API token to `src/main/resources/qa.properties`:
```properties
token=YOUR_ACTUAL_TOKEN_HERE
```

## Running Tests

```bash
# Run all tests (QA environment, default)
mvn test

# Run with specific environment
mvn test -Denv=qa
mvn test -Denv=uat

# Pass token via command line (overrides property file)
mvn test -Dtoken=YOUR_TOKEN_HERE

# Run only user tests
mvn test -Dtest=UserApiTest

# Run only post tests
mvn test -Dtest=PostApiTest

# Run only comment tests
mvn test -Dtest=CommentApiTest

# Clean build and test
mvn clean test
```

## Viewing Reports

After running tests, open the ExtentReports HTML report:
```
reports/ExtentReport.html
```

The report contains:
- Execution summary (total, passed, failed, skipped)
- Individual test results with duration
- Failure details with stack traces
- Environment information (framework, base URL, environment name)

## API Endpoints Tested

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/public/v2/users` | GET | List all users |
| `/public/v2/users/{id}` | GET | Get user by ID |
| `/public/v2/users` | POST | Create user |
| `/public/v2/users/{id}` | PUT | Update user |
| `/public/v2/users/{id}` | DELETE | Delete user |
| `/public/v2/posts` | GET | List all posts |
| `/public/v2/posts/{id}` | GET | Get post by ID |
| `/public/v2/users/{id}/posts` | GET | Get posts by user |
| `/public/v2/users/{id}/posts` | POST | Create post for user |
| `/public/v2/posts/{id}/comments` | POST | Create comment on post |

## Authentication

GoRest requires a Bearer token for write operations (POST, PUT, DELETE). GET requests are public.

The token is read from the environment property file and attached to all mutating requests by `ApiHelper`. You can override it at runtime:
```bash
mvn test -Dtoken=your_token_here
```

## Notes

- GoRest is a public test API. Data resets every 3 hours.
- Each test creates and cleans up its own data to avoid conflicts.
- Rate limit: 300 requests/minute per token.
- All tests are independent and can run in any order.
