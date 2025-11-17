# Taller Backend - Spring Boot Application

A secure Spring Boot REST API for managing a vehicle workshop (taller mecánico) with comprehensive security features.

## 🔒 Security Features

This application implements enterprise-grade security features including:

- ✅ **Anti-CSRF Protection** - Double-submit cookie pattern for stateless authentication
- ✅ **Security HTTP Headers** - X-Frame-Options, X-Content-Type-Options, CSP, etc.
- ✅ **Secure CORS Configuration** - No wildcards, specific origins only
- ✅ **Server Information Hiding** - Minimal information disclosure
- ✅ **Content-Type Headers** - Proper MIME types on all endpoints
- ✅ **BCrypt Password Encoding** - Secure password hashing
- ✅ **JWT Authentication** - Stateless token-based authentication
- ✅ **Role-Based Access Control** - Fine-grained authorization

For detailed security information, see [SECURITY.md](SECURITY.md)

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE taller;
```

2. Configure database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taller
spring.datasource.username=root
spring.datasource.password=your_password
```

### Running the Application

```bash
# Build the project
./mvnw clean package -DskipTests

# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the interactive API documentation at:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 🔐 Authentication

### Register a new user

```bash
POST /api/auth/register
Content-Type: application/json

{
  "correo": "user@example.com",
  "password": "securePassword123",
  "nombre": "John Doe",
  "rol": "EMPLEADO"
}
```

### Login

```bash
POST /api/auth/login
Content-Type: application/json

{
  "correo": "user@example.com",
  "password": "securePassword123"
}
```

Response: JWT token (string)

### Using the JWT Token

Include the token in the Authorization header for protected endpoints:

```bash
Authorization: Bearer <your-jwt-token>
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/taller/modiesel/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── model/          # Entity models
│   │   ├── repository/     # Data repositories
│   │   ├── security/       # Security filters and utilities
│   │   ├── service/        # Business logic
│   │   └── ModieselApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/taller/modiesel/
```

## 🛡️ API Endpoints

### Public Endpoints (No Authentication Required)

- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/productos` - List all products (read-only)

### Protected Endpoints

#### Admin Only

- `GET /api/usuarios` - List all users
- `POST /api/usuarios` - Create user
- `PUT /api/usuarios/{id}` - Update user
- `DELETE /api/usuarios/{id}` - Delete user

#### Admin & Employee

- `GET /api/ventas` - List sales
- `POST /api/ventas` - Create sale
- `GET /api/inventario` - List inventory
- `POST /api/inventario` - Add to inventory

#### All Authenticated Users

- `GET /api/clientes` - List clients
- `POST /api/clientes` - Create client
- `GET /api/vehiculos` - List vehicles
- `GET /api/facturas` - List invoices
- `GET /api/repuestos` - List spare parts

## 🔧 Configuration

### CORS Configuration

Configure allowed origins in `application.properties`:

```properties
# Single origin
cors.allowed.origins=http://localhost:3000

# Multiple origins (comma-separated)
cors.allowed.origins=http://localhost:3000,https://yourdomain.com
```

### Security Headers

Security headers are automatically applied by `SecurityHeadersFilter`. For HTTPS environments, uncomment the HSTS header in `SecurityHeadersFilter.java`.

## 🧪 Testing

For manual testing instructions, see [TESTING.md](TESTING.md)

## 🚀 Production Deployment

Before deploying to production:

1. ✅ Enable HTTPS
2. ✅ Uncomment HSTS header in `SecurityHeadersFilter.java`
3. ✅ Update `cors.allowed.origins` with production URLs
4. ✅ Set secure cookie flags (`secure=true`) in `CsrfProtectionFilter.java`
5. ✅ Use strong JWT secret key
6. ✅ Configure proper database credentials (use environment variables)
7. ✅ Review and adjust Content-Security-Policy
8. ✅ Enable application logging (without sensitive data)

## 📖 Documentation

- [SECURITY.md](SECURITY.md) - Detailed security documentation
- [TESTING.md](TESTING.md) - Manual testing guide

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.6
- **Security**: Spring Security + JWT
- **Database**: MySQL + JPA/Hibernate
- **Build Tool**: Maven
- **Java Version**: 17
- **Documentation**: Swagger/OpenAPI

## 🐛 Known Issues

- Unit tests require database connection (consider using H2 in-memory DB for tests)
- CSRF protection requires client-side implementation for token extraction

## 📝 License

[Specify your license here]

## 👥 Contributors

- Milenka-95

## 📞 Support

For issues or questions, please open an issue on GitHub.

---

**Note**: This application was hardened based on OWASP ZAP security scanner recommendations. Regular security audits are recommended.
