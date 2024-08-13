## _ProductApp_ - store management tool

## Overview
This project is a Java application built with Spring Boot that allows to manage products.
It provides a REST API for basic CRUD operations on products.
It includes global error handling and logging and unit tests.

## Prerequisites
- JDK 17 or higher - Make sure JDK 17 is installed on your system
- Maven 3.9.8 or higher - Apache Maven should be installed to manage dependencies and build the project
- MariaDB - A MariaDB database server should be running and accessible

## Setup
1. Clone the repository.
2. Create MariaDB database - "mariadb_1"
  ```
    CREATE DATABASE mariadb_1;
    
    CREATE TABLE mariadb_1.PRODUCT (
      ID int not null AUTO_INCREMENT ,
      NAME varchar(100) not null,
      PRICE double(10,2) zerofill,
      INSDATE datetime,
      UPDDATE datetime,
      PRIMARY KEY (ID)
    );
  ```
3. Run `mvn clean install` to build the project. 
4. Run `mvn spring-boot:run` to start the application.
		
		
## Usage
### 1. Get all products
Retrieve a list of all products from the database
- **GET** /products
- **Request:** No request body required
- **Response:**
  ```json
  [
    {
      "id": 1,
      "name": "Laptop",
      "price": 2500.00,
      "insDate": "2024-08-12 12:25:00",
      "updDate": ""
    },
    {
      "id": 2,
      "name": "Mouse",
      "price": 15.00,
      "insDate": "2024-08-12 12:45:00",
      "updDate": ""
    }
  ]
  ```

### 2. Get product by ID
Retrieve the details for the product with requested ID
- **GET** /products/{id}
- **Request**
`id` (path parameter): The unique identifier of the product to retrieve
- **Response:**
  ```json
  {
    "id": 1,
    "name": "Laptop",
    "price": 2500.00,
    "insDate": "2024-08-12 12:25:00",
      "updDate": ""
  }
  ```

### 3. Add Product
Insert a new product into the database
- **POST** `/products`
- **Request Body:**
  A JSON object representing the product to be added
  ```json
  {
    "name": "HDD",
    "price": 250.00
  }
  ```
- **Response:**
If successfully added:
  ```json
  {
      "status": "success",
      "message": "Product added successfully"
  }
  ```
  If an error occured:
```json
  {
      "status": "error",
      "message": "Invalid input provided / A product with the same attributes already exists / Unexpected error occurred"
  }
  ```

### 4. Change product price
Update the price of an existing product
- **PUT** `/products/{id}/changePrice`
- **Request**
  `id` (path parameter): The unique identifier of the product to retrieve
- **Request Body:** A JSON object containing the new price for the product
```json
  {
    "price": 2140.00
  }
  ```
- **Response:**
  If successfully updated:
  ```json
  {
      "status": "success",
      "message": "The price has been successfully updated"
  }
  ```
  If an error occured:
```json
  {
      "status": "error",
      "message": "Product not found / Invalid input provided / Unexpected error occurred"
  }
  ```

### 5. Get product count
Get the number of products stored into the database
- **GET** /products/count
- **Request:** No request body required
- **Response:**
```json
  {
    "count": 4
  }
```

### 6. Get products with the price smaller than a specified value
Retrieves a list of products that accomplish the condition that the price is smaller than a specified value
- **GET** /products/getProductsWithPriceLessThan
- **Query Parameters**:
  - `maxPrice` (required): A double value for the maximum of the price for the requested products
- **Response:**
A JSON array with product objects
```json
  [
    {
      "id": 2,
      "name": "Mouse",
      "price": 15.00,
      "insDate": "2024-08-12 12:45:00",
      "updDate": ""
    },
    {
      "id": 31,
      "name": "Headphones",
      "price": 15.00,
      "insDate": "2024-08-12 14:10:25",
      "updDate": "2024-08-12 14:52:25"
    }
  ]
  ```

### 7. Get products with name containing a given substring
Retrieves a list of products whose names contain the specified substring
- **GET** /products/getProductsWithNameContains
- **Query Parameters**:
  - `stringToFind` (required): a string value representing the substring to search for within product name
- **Response:**
  A JSON array with product objects
```json
  [
    {
      "id": 3,
      "name": "Monitor",
      "price": 1122.00,
      "insDate": "2024-08-12 12:21:42",
      "updDate": ""
    },
    {
      "id": 23,
      "name": "Monitor2",
      "price": 1015.00,
      "insDate": "2024-08-12 12:39:24",
      "updDate": "2024-08-12 12:52:25"
    }
  ]
  ```

### 8. Delete all products
Deletes all products from the database
- **DELETE** /products/deleteAllProducts
- **Request:** No request body required
- **Response:**
  
  If successfully deleted:
  - Status Code: `204 No Content`
  - The response does not contain a body because there is no content to return
    
  If an error occured:
  ```json
      {
          "error": "Unexpected error occurred while trying to delete all products"
      }
    ```

### 9. Delete product by ID
Delete from database the product with the specified ID
- **DELETE** /products/deleteProductById/{id}
- **Request:**
`id` (path parameter): The unique identifier of the product to retrieve
- **Response:**

  If successfully deleted:
  - Status Code: `204 No Content`
  - The response does not contain a body because there is no content to return

  If an error occured:
  ```json
      {
          "error": "Unexpected error occurred while trying to delete the product by ID"
      }
    ```

##### HTTP Status Codes:

  ``201 Created``: The product was successfully added

  ``400 Bad Request``: The request was invalid due to bad input

  ``500 Internal Server Error``: An error occurred on the server while processing the request


## Testing


