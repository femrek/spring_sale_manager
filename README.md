# PRODUCT MANAGEMENT AND SALE MANAGEMENT SYSTEM

## About the project

This app aims to manage a simple cash register. Also list the products and offers. The app has user management and
authorization. The app has a report system to list the sales and generate pdf receipts. The app has a logging system to
log the actions of the system. The app has an authentication service to authenticate the users and authorize the other
services. The app has an interface for other services to manage users and

### Features

- Listing Products
- Listing Offers
- Creating Sales
- Listing Sales
- Generate Reports of Sales
- User Management and Authorization

### Technologies

The project developed around the Java Spring framework. And, PostgreSQL is the database solution of the project.

- Java 17
- PostgreSQL
- Spring Boot
- Spring Security (for authentication and authorization)
- Spring Data JPA (for database operations)
- Eureka (for service discovery)
- Docker
- itextpdf html2pdf (for generating pdf reports)
- Feign (for calling other services)
- Log4j2 (for logging)

## How to run (Docker)

### Requirements

- Docker has to be installed on your machine.
- After running the docker compose command, the database must be created by executing `db_structure_public_scheme.sql`
  file.
- Create a text file named `db_credential.txt` in the root directory of the project. Write the database password for
  postgres user directly.
- Create a text file named `jwt_secret.txt` in the root directory of the project. Write the secret key for JWT directly.

Run the following command in the root directory of the project.

```bash
docker build . -f BuilderDockerfile 
docker compose build
docker compose up
```

## API Documentation

Check [postman documentation](https://documenter.getpostman.com/view/29982062/2sA3XTdKMi) for more details.

All the success responses are in AppSuccessResponse format that includes status, message and data fields.

Response format:

```json
{
  "status": 200,
  "message": "Success",
  "data": { }
}
```

```json
{
  "status": 200,
  "message": "Success",
  "data": [
    { },
    { }
  ]
}
```

### Authentication Service (/api/v1/auth/)

- POST /api/v1/auth/login
    - **Authentication:** no authentication
    - **Request Body:** LoginRequest
    - **Response Body:** LoginResponse: contains the JWT token
- GET /api/v1/auth/user
    - **Authentication:** authenticated
    - **Request Body:** no request body
    - **Response Body:** UserDTO: the authenticated user

### User Management Service (/api/v1/user/)

- GET /api/v1/user
    - **Authentication:** ADMIN only
    - **Request Body:** no request body
    - **Response Body:** List<UserDTO>: list of users
- GET /api/v1/user/{userId}
    - **Authentication:** ADMIN only
    - **Request Body:** no request body
    - **Response Body:** UserDTO: the user
- POST /api/v1/user
    - **Authentication:** ADMIN only
    - **Request Body:** UserCreateRequest
    - **Response Body:** UserDTO: the created user
- PATCH /api/v1/user/{userId}
    - **Authentication:** ADMIN only
    - **Request Body:** UserUpdateRequest
    - **Response Body:** UserDTO: the updated user
- DELETE /api/v1/user/{userId}
    - **Authentication:** ADMIN only
    - **Request Body:** no request body
    - **Response Body:** no request body: success or error response
- GET /api/v1/user/role
    - **Authentication:** ADMIN only
    - **Request Body:** no request body
    - **Response Body:** List<UserRoleDTO>: list of roles
- GET /api/v1/user/role/{roleId}
    - **Authentication:** ADMIN only
    - **Request Body:** no request body
    - **Response Body:** UserRoleDTO: the role

### Product Service (/api/v1/product/)

- GET /api/v1/product
    - **Authentication:** no authentication
    - **Request Body:** no request body
    - **Response Body:** List<ProductDTO>: list of products
- GET /api/v1/product/{id}
    - **Authentication:** no authentication
    - **Request Body:** no request body
    - **Response Body:** ProductDTO: the product

### Sale Service (/api/v1/sale/ | /api/v1/offer/ | /api/v1/payment-method/)

- POST /api/v1/sale
    - **Authentication:** CASHIER only
    - **Request Body:** SaleCreateRequest
    - **Response Body:** SaleDTO: the created sale
- POST /api/v1/sale/preview
    - **Authentication:** CASHIER only
    - **Request Body:** SaleCreateRequest
    - **Response Body:** SalePreviewDTO: the preview of the sale if it is valid
- GET /api/v1/offer
    - **Authentication:** CASHIER only
    - **Request Body:** no request body
    - **Response Body:** List<OfferDTO>: list of offers
- GET /api/v1/offer/{id}
    - **Authentication:** CASHIER only
    - **Request Body:** no request body
    - **Response Body:** OfferDTO: the offer
- GET /api/v1/payment-method
    - **Authentication:** CASHIER only
    - **Request Body:** no request body
    - **Response Body:** List<PaymentMethodDTO>: list of payment methods
- GET /api/v1/payment-method/{id}
    - **Authentication:** CASHIER only
    - **Request Body:** no request body
    - **Response Body:** PaymentMethodDTO: the payment method

### Report Service (/api/v1/report/)

- GET /api/v1/report
    - **Authentication:** MANAGER only
    - **Request Body:** no request body
    - **Request Param:**
        - p (required): Integer: page number
        - s (required): Integer: page size
        - orderBy (optional) String: the field to order by [id, receivedMoney, createdAt, cashier]
        - orderByAsc (optional) Boolean: true for ascending, false for descending
        - dateFilterAfter (optional) Long: timestamp in milliseconds
        - dateFilterBefore (optional) Long: timestamp in milliseconds
        - cashierFilter (optional) Long: the cashier id
        - receivedMoneyFilterMin (optional) Double: received money filter min or equal
        - receivedMoneyFilterMax (optional) Double: received money filter max or equal
    - **Response Body:** List<ReportDTO>: list of reports
- GET /api/v1/report/{id}
    - **Authentication:** MANAGER only
    - **Request Body:** no request body
    - **Response Body:** ReportDTO: the sale in json format
- GET /api/v1/report/{id}/receipt
    - **Authentication:** MANAGER only
    - **Request Body:** no request body
    - **Response Body:** byte[] (file): the pdf receipt
- GET /api/v1/report/{id}/receipt-html
    - **Authentication:** MANAGER only
    - **Request Body:** no request body
    - **Response Body:** byte[] (file): the html receipt

### Error Responses

`AppHttpError` class is used for error responses. The `AppHttpError` class has a `code` field for the error code and a
`message` field for the error message. Example error response:

```json
{
  "status": 400,
  "message": "<<Error explanation>>"
}
```

## Inter-service Communication

### User Management - Authentication Service Interface

- POST /api/v1/auth/user-management
    - **Request Body:** UserCreateRequest
    - **Response Body:** UserDTO: the created user

- PATCH /api/v1/auth/user-management/{id}
    - **Request Body:** UserUpdateRequest
    - **Response Body:** UserDTO: the updated user

- DELETE /api/v1/auth/user-management/{id}
    - **Request Body:** no request body
    - **Response Body:** no request body: success or error response

### Authentication and Authorization Interface for Other Services

- GET /api/v1/auth/accessibility
    - **Request Body:** no request body
    - **Response Body:** no request body: success or error response

## Logging

The log4j2 library is used for logging the actions of the system. The logs are stored in the home directory of the
container. `log4j2.xml` file is used for configuration for all the modules.

## Explanation of the Features of the Project

### Response Format

The project has a `AppSuccessResponse` class for the success responses. It has a `data` field for the response data.
`status` field for the response status code. `message` field to response with a message, by default it is `Success`.

### Exception Handling

`GlobalExceptionHandler` class is used for handling exceptions over all the project. `AppHttpError` class is used for
responding to the client in case of an error. The `AppHttpError` class has a `code` field for the error code and a
`message` field for the error message. The `AppHttpError` class is used in the `GlobalExceptionHandler` class. All
defined exceptions are a `AppHttpError` instance. The `GlobalExceptionHandler` class is used for handling all the
exceptions and returning the `AppHttpError` instance as a response.

Also, all the undefined exceptions are handled by the `GlobalExceptionHandler` class. The `GlobalExceptionHandler` class
returns a `AppHttpError` instance with the error code `500` and the error message `Internal Server Error`.

### Authentication & Authorization

The project has an `auth` service to manage the authentication and authorization of the users. The `auth` service
generates a JWT tokens. The JWT token is used for the authorization on whole the project.

The `auth` service has a `/login` endpoint to authenticate the users. The `/login` endpoint returns a JWT token in the
response body. The JWT token is used for the authorization of the other services.

The `auth` service has a `/user` endpoint to get the authenticated user. The `/user` endpoint returns the authenticated
user information in the response body.

The `auth` service has a `/accessibility` endpoint to check the accessibility of the user. The `/accessibility` endpoint
returns a success response if the user is authorized. Otherwise, it returns an error response. There is a Before Aspect
on all the controller methods in the project excluding the `auth` service. This aspect sends a request to the `auth`
service to check the accessibility of the user to reach the endpoint via `auth` service.

### Logging

The log4j2 library is used for logging. The logs are stored in the home directory of the container. `log4j2.xml` file is
used for configuration for all the modules.

All logs (including trace) are saved into the `app.log` file in the home directory of the container. Info level logs are
saved to the `general-info.log` file in the home directory of the container.

### Report PDF Generation

This application can generate pdf reports for the sales. The pdf reports are generated by the `itextpdf.html2pdf`
library with thymeleaf. When creating PDFs, first, it creates a PDF with many pages. Then, it calculates the total
height of the content and creates a new single page PDF with the total height. There is no caching for the PDF files.
The PDF files are generated on the fly.
