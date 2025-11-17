# Manual Testing Guide for Security Improvements

This guide provides instructions for manually testing the security improvements implemented in this project.

## Prerequisites

- MySQL database running on `localhost:3306` with database name `taller`
- Java 17 installed
- Maven installed (or use the included mvnw wrapper)

## Starting the Application

```bash
# Navigate to the project directory
cd /path/to/Taller_Backend

# Build the project
./mvnw clean package -DskipTests

# Run the application
./mvnw spring-boot:run
```

The application should start on `http://localhost:8080`

## Test 1: Verify Security Headers

Test that security headers are properly configured on all endpoints.

### Using curl:

```bash
curl -I http://localhost:8080/api/productos
```

### Expected Response Headers:

```
HTTP/1.1 200 OK
X-Frame-Options: DENY
X-Content-Type-Options: nosniff
Referrer-Policy: no-referrer
Content-Security-Policy: default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data: https:; font-src 'self' data:;
Permissions-Policy: geolocation=(), microphone=(), camera=()
Content-Type: application/json
```

✅ **Pass Criteria**: All security headers are present in the response.

## Test 2: Verify CSRF Protection

Test that CSRF protection is working for state-changing operations.

### Step 1: Get CSRF Token

```bash
# Make a GET request to obtain CSRF token
curl -v http://localhost:8080/api/productos -c cookies.txt

# The response will include a Set-Cookie header with XSRF-TOKEN
```

### Step 2: Try POST without CSRF Token (Should Fail)

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Test Product","precio":100}'
```

✅ **Expected**: `403 Forbidden` with error message `{"error": "CSRF token validation failed"}`

### Step 3: Try POST with CSRF Token (Should Succeed if authenticated)

```bash
# Extract token from cookies
CSRF_TOKEN=$(grep XSRF-TOKEN cookies.txt | awk '{print $7}')

# Make POST request with CSRF token
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -H "X-CSRF-Token: $CSRF_TOKEN" \
  -b cookies.txt \
  -d '{"nombre":"Test Product","precio":100}'
```

✅ **Pass Criteria**: Request is accepted (note: authentication may still be required)

## Test 3: Verify CORS Configuration

Test that CORS is properly configured with specific origins only.

### Allowed Origin Test:

```bash
curl -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type" \
  -X OPTIONS http://localhost:8080/api/productos -v
```

✅ **Expected**: Response includes CORS headers allowing the origin:
```
Access-Control-Allow-Origin: http://localhost:3000
Access-Control-Allow-Methods: GET,POST,PUT,DELETE,OPTIONS,PATCH
Access-Control-Allow-Credentials: true
```

### Disallowed Origin Test:

```bash
curl -H "Origin: http://evil-site.com" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type" \
  -X OPTIONS http://localhost:8080/api/productos -v
```

✅ **Expected**: No CORS headers in response (request blocked by browser)

## Test 4: Verify Content-Type Headers

Test that all API endpoints return proper Content-Type headers.

```bash
# Test various endpoints
curl -I http://localhost:8080/api/productos
curl -I http://localhost:8080/api/usuarios
curl -I http://localhost:8080/api/clientes
```

✅ **Expected**: All responses include `Content-Type: application/json`

## Test 5: Verify Server Information Hiding

Test that server information is not exposed.

```bash
curl -I http://localhost:8080/api/productos
```

✅ **Expected**: 
- No `Server` header in response (or minimal information)
- No `X-Powered-By` header in response

## Test 6: Test Authentication and Authorization

### Step 1: Login and Get JWT Token

```bash
# Register a new user (if needed)
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "correo": "admin@example.com",
    "password": "securePassword123",
    "nombre": "Admin User",
    "rol": "ADMIN"
  }'

# Login
JWT_TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "correo": "admin@example.com",
    "password": "securePassword123"
  }' | tr -d '"')

echo "JWT Token: $JWT_TOKEN"
```

### Step 2: Access Protected Endpoint Without Token (Should Fail)

```bash
curl http://localhost:8080/api/usuarios
```

✅ **Expected**: `401 Unauthorized`

### Step 3: Access Protected Endpoint With Token (Should Succeed)

```bash
curl http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer $JWT_TOKEN"
```

✅ **Expected**: `200 OK` with list of users

## Test 7: Test BCrypt Password Encoding

Verify that passwords are stored encrypted (not plain text).

### Step 1: Register a User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "correo": "testuser@example.com",
    "password": "myPassword123",
    "nombre": "Test User"
  }'
```

### Step 2: Verify in Database

Connect to your MySQL database and check:

```sql
SELECT correo, password FROM usuario WHERE correo = 'testuser@example.com';
```

✅ **Expected**: Password should be a BCrypt hash (starts with `$2a$` or `$2b$`) and NOT plain text

Example: `$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy`

## Test 8: Browser Testing with Developer Tools

1. Open browser Developer Tools (F12)
2. Navigate to `http://localhost:3000` (or your frontend URL)
3. Make requests to the API
4. Check the Network tab:
   - Verify security headers are present in all responses
   - Verify CSRF tokens are being sent/received
   - Verify JWT tokens are in Authorization headers

## Expected Security Improvements Summary

After implementing all security fixes, your application should:

✅ Have Anti-CSRF protection for all state-changing operations
✅ Include security headers on all responses (X-Frame-Options, X-Content-Type-Options, etc.)
✅ Use specific CORS origins (no wildcards)
✅ Hide server information
✅ Return proper Content-Type headers
✅ Use BCrypt for password hashing
✅ Implement proper JWT authentication
✅ Enforce role-based access control

## Troubleshooting

### Issue: "Failed to load ApplicationContext" in tests
**Cause**: Tests require a database connection
**Solution**: Either configure a test database or use `@DataJpaTest` with H2 in-memory database

### Issue: CORS errors in browser
**Cause**: Frontend origin not in allowed list
**Solution**: Update `cors.allowed.origins` in `application.properties`

### Issue: 401 Unauthorized on all endpoints
**Cause**: JWT filter requiring authentication on public endpoints
**Solution**: Verify `SecurityConfig.java` has proper `permitAll()` for public endpoints

### Issue: CSRF validation always fails
**Cause**: Cookie not being sent or token mismatch
**Solution**: Ensure `credentials: 'include'` in frontend fetch requests and extract token from cookie

## Automated Testing (Future Enhancement)

For automated testing, consider:
1. Using H2 in-memory database for tests
2. Creating test profiles with `@ActiveProfiles("test")`
3. Mocking external dependencies
4. Using `@WebMvcTest` for controller tests
5. Using TestContainers for integration tests with MySQL
