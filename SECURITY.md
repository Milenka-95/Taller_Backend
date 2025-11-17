# Security Improvements Documentation

This document describes the security enhancements implemented to address vulnerabilities identified by the ZAP security scanner.

## 1. Anti-CSRF Protection

### Implementation
- **Class**: `CsrfProtectionFilter.java`
- **Pattern**: Double-submit cookie pattern
- **How it works**:
  1. Server generates a random CSRF token for each request
  2. Token is sent as a cookie (`XSRF-TOKEN`) and expected in the `X-CSRF-Token` header
  3. For state-changing operations (POST, PUT, DELETE, PATCH), both must match
  4. Safe methods (GET, HEAD, OPTIONS) and public endpoints are exempt

### Usage
Frontend applications must:
1. Extract the CSRF token from the `XSRF-TOKEN` cookie
2. Include it in the `X-CSRF-Token` header for all state-changing requests

Example JavaScript:
```javascript
function getCsrfToken() {
    const name = 'XSRF-TOKEN=';
    const cookies = document.cookie.split(';');
    for (let cookie of cookies) {
        cookie = cookie.trim();
        if (cookie.startsWith(name)) {
            return cookie.substring(name.length);
        }
    }
    return null;
}

// Add to fetch requests
fetch('/api/productos', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
        'X-CSRF-Token': getCsrfToken()
    },
    credentials: 'include',
    body: JSON.stringify(data)
});
```

### Public Endpoints (No CSRF Required)
- `/api/auth/login`
- `/api/auth/register`
- `/api/productos` (GET only)
- `/api/catalogo`
- Swagger/OpenAPI endpoints

## 2. Security HTTP Headers

### Implementation
- **Class**: `SecurityHeadersFilter.java`
- **Headers Added**:

| Header | Value | Purpose |
|--------|-------|---------|
| `X-Frame-Options` | `DENY` | Prevents clickjacking attacks |
| `X-Content-Type-Options` | `nosniff` | Prevents MIME type sniffing |
| `Referrer-Policy` | `no-referrer` | Controls referrer information |
| `Content-Security-Policy` | Custom policy | Restricts resource loading |
| `Permissions-Policy` | Restrictive | Disables unnecessary browser features |

### HTTPS/HSTS
The `Strict-Transport-Security` header is commented out in development but should be enabled in production:
```java
httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
```

## 3. CORS Configuration

### Implementation
- **Class**: `WebConfig.java`
- **Configuration**: `application.properties`

### Changes Made
- ❌ Removed wildcard patterns (`*`)
- ✅ Specific origins only (configurable)
- ✅ Explicit header specification
- ✅ Proper credentials handling

### Configuration
Edit `application.properties`:
```properties
# Single origin
cors.allowed.origins=http://localhost:3000

# Multiple origins (comma-separated)
cors.allowed.origins=http://localhost:3000,https://example.com
```

### Allowed Methods
- GET, POST, PUT, DELETE, OPTIONS, PATCH

### Allowed Headers
- Authorization
- Content-Type
- X-CSRF-Token
- Accept

## 4. Server Information Hiding

### Configuration
In `application.properties`:
```properties
# Hide server header
server.server-header=

# Disable error details
server.error.include-message=never
server.error.include-binding-errors=never
server.error.include-stacktrace=never
server.error.include-exception=false
```

## 5. Content-Type Headers

### Implementation
- **Class**: `ContentTypeConfig.java`
- **Default**: All API responses default to `application/json`
- **Controller Level**: Explicit `produces` and `consumes` annotations

### Example
```java
@RestController
@RequestMapping(value = "/api/productos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductoController {
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Producto registrar(@RequestBody Producto producto) {
        // ...
    }
}
```

## 6. Authentication & Password Security

### Changes
- ✅ **BCryptPasswordEncoder**: Replaces insecure NoOpPasswordEncoder
- ✅ **Re-enabled Authorization**: Proper role-based access control
- ✅ **JWT Authentication**: Stateless token-based auth

### Authorization Rules
```
Public:
- /api/auth/** (login, register)
- /api/productos/** (read-only)
- /api/catalogo/**

Admin Only:
- /api/usuarios/**

Admin & Employee:
- /api/ventas/**
- /api/inventario/**

All Other Endpoints:
- Require authentication
```

## 7. Filter Execution Order

Security filters are executed in this order:
1. `SecurityHeadersFilter` - Adds security headers
2. `CsrfProtectionFilter` - Validates CSRF tokens
3. `JwtRequestFilter` - Authenticates JWT tokens

## Testing the Security Improvements

### 1. Verify Security Headers
```bash
curl -I http://localhost:8080/api/productos
```

Expected headers:
- X-Frame-Options: DENY
- X-Content-Type-Options: nosniff
- Referrer-Policy: no-referrer
- Content-Security-Policy: ...
- Content-Type: application/json

### 2. Test CSRF Protection
```bash
# Should fail without CSRF token
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"name":"Test"}'

# Should succeed with valid CSRF token
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -H "X-CSRF-Token: <token-from-cookie>" \
  -H "Cookie: XSRF-TOKEN=<token>" \
  -d '{"name":"Test"}'
```

### 3. Verify CORS Configuration
```bash
curl -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type" \
  -X OPTIONS http://localhost:8080/api/productos
```

Expected: CORS headers allowing the origin

### 4. Test Authentication
```bash
# Login should work
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"correo":"user@example.com","password":"password"}'

# Protected endpoints should require JWT
curl http://localhost:8080/api/usuarios
# Expected: 401 Unauthorized
```

## Production Deployment Checklist

- [ ] Enable HTTPS
- [ ] Uncomment `Strict-Transport-Security` header in `SecurityHeadersFilter`
- [ ] Update `cors.allowed.origins` in `application.properties` with production URLs
- [ ] Set secure cookie flags in `CsrfProtectionFilter` (set `secure=true`)
- [ ] Review and adjust `Content-Security-Policy` based on actual resource needs
- [ ] Ensure JWT secret key is strong and properly secured
- [ ] Remove or properly secure any development/debug endpoints
- [ ] Configure proper logging without exposing sensitive information

## Additional Security Recommendations

1. **Database Security**: Use encrypted connections to the database
2. **Secrets Management**: Use environment variables or a secrets manager for sensitive configuration
3. **Rate Limiting**: Implement rate limiting to prevent brute force attacks
4. **Input Validation**: Ensure all user inputs are properly validated and sanitized
5. **Dependency Updates**: Regularly update dependencies to patch security vulnerabilities
6. **Security Audits**: Perform regular security scans and penetration tests
