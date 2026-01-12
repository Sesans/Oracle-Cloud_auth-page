# Login/sign-up in Oracle Cloud

This is a REST API to handle users authorization and authentication using JWT and Oauth2. Available in {soon}


## 🎯 Objective

The objective is to study security patterns using tokens, like generating, authenticating, refreshing, and login using Oauth2. Furthermore using docker and docker compose to improve the containerization acknowledgement and deploying to Oracle Cloud.

## technologies

- Java and Spring Boot
- PostgreSQL
- Spring Security
- JWT and Oauth2
- Docker and Docker compose
- Deployed in Oracle Cloud

## Architecture Overview

The frontend is a Simple Page Application (SPA) built with React, designed to provide a simple but functional user experience. Client-side navigation is handled using React-Router. Credentials submited through the registration or login forms are sent to the backend, and upon successful validetion, an access token is returned, allowing the user to access protected areas.

The authentication service is implemented using Java and Spring Boot following the layered architecture pattern. It exposes REST endpoints responsible for user registration, authentication, token validation, and refresh operations.

An OAuth 2.0 provider is integrated to support social login, allowing users to authenticate using their Google account. The OAuth flow is handled entirely by the backend.

PostgreSQL is used as the persistence layer. It stores user-related data. Users authenticated via social login have their origin set accordingly, without a password. The database also persists refresh tokens, including their expiration date, revocation status and associated user.

Docker used for containerization, ensuring a consistent, isolated, and reproducible environment across development and deployment.
## API Reference

#### Register

```http
  POST /auth/register
```

| Parameter | Type     | Description                      |
| :-------- | :------- | :-------------------------       |
| `firstName`   | `String` | **Required**. Your first name|
| `lasName`| `String` | **Required**. Your last name      |
| `Email`   | `String` | **Required**. Your email         |
| `Password`| `String` | **Required**. Create a password  |



#### Login

```http
  POST /auth/login
```

| Parameter | Type     | Description                        |
| :-------- | :------- | :----------------------------------|
| `Email`   | `String` | **Required**. A registered email   |
| `Password`| `String` | **Required**. A registered password|

