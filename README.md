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
docker compose up
```

## API Documentation

Check [postman documentation](https://documenter.getpostman.com/view/29982062/2sA3XTdKMi) for more details.

All the success responses are in AppSuccessResponse format.

### Authentication Service (/api/v1/auth/)

- POST /api/v1/auth/login
    - Request Body: LoginRequest
    - Response Body: LoginResponse: contains the JWT token
- GET /api/v1/auth/user
    - no request body
    - Response Body: UserDTO: the authenticated user

### User Management Service (/api/v1/user/)

- GET /api/v1/user
    - no request body
    - Response Body: List<UserDTO>: list of users
- GET /api/v1/user/{userId}
    - no request body
    - Response Body: UserDTO: the user
- POST /api/v1/user
    - Request Body: UserCreateRequest
    - Response Body: UserDTO: the created user
- PATCH /api/v1/user/{userId}
    - Request Body: UserUpdateRequest
    - Response Body: UserDTO: the updated user
- DELETE /api/v1/user/{userId}
    - no request body
    - no response body: success or error response
- GET /api/v1/user/role
    - no request body
    - Response Body: List<UserRoleDTO>: list of roles
- GET /api/v1/user/role/{roleId}
    - no request body
    - Response Body: UserRoleDTO: the role

### Product Service (/api/v1/product/)

- GET /api/v1/product
    - no request body
    - Response Body: List<ProductDTO>: list of products
- GET /api/v1/product/{id}
    - no request body
    - Response Body: ProductDTO: the product

### Sale Service (/api/v1/sale/ | /api/v1/offer/)

- POST /api/v1/sale
    - Request Body: SaleCreateRequest
    - Response Body: SaleDTO: the created sale
- POST /api/v1/sale/preview
    - Request Body: SaleCreateRequest
    - Response Body: SalePreviewDTO: the preview of the sale if it is valid
- GET /api/v1/sale/offer
    - no request body
    - Response Body: List<OfferDTO>: list of offers
- GET /api/v1/sale/offer/{id}
    - no request body
    - Response Body: OfferDTO: the offer

### Report Service (/api/v1/report/)

- GET /api/v1/report
    - no request body
    - Request Param:
        - p (required): Integer: page number
        - s (required): Integer: page size
        - orderBy (optional) String: the field to order by [id, receivedMoney, createdAt, cashier]
        - orderByAsc (optional) Boolean: true for ascending, false for descending
        - dateFilterAfter (optional) Long: timestamp in milliseconds
        - dateFilterBefore (optional) Long: timestamp in milliseconds
        - cashierFilter (optional) Long: the cashier id
        - receivedMoneyFilterMin (optional) Double: received money filter min or equal
        - receivedMoneyFilterMax (optional) Double: received money filter max or equal
    - Response Body: List<ReportDTO>: list of reports
- GET /api/v1/report/{id}
    - no request body
    - Response Body: ReportDTO: the sale in json format
- GET /api/v1/report/{id}/receipt
    - no request body
    - Response Body: byte[] (file): the pdf receipt
- GET /api/v1/report/{id}/receipt-html
    - no request body
    - Response Body: String: the html receipt

## Inter-service Communication

### User Management - Authentication Service Interface

- POST /api/v1/auth/user-management
    - Request Body: UserCreateRequest
    - Response Body: UserDTO: the created user

- PATCH /api/v1/auth/user-management/{id}
    - Request Body: UserUpdateRequest
    - Response Body: UserDTO: the updated user

- DELETE /api/v1/auth/user-management/{id}
    - no request body
    - no response body: success or error response

### Authentication and Authorization Interface for Other Services

- GET /api/v1/auth/accessibility
    - no request body
    - no response body: success or error response

## Logging

The log4j2 library is used for logging the actions of the system. The logs are stored in the home directory of the
container. `log4j2.xml` file is used for configuration for all the modules.
