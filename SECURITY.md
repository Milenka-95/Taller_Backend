<<<<<<< HEAD
# Security Documentation

This document describes the security measures implemented in the Taller Backend application to address vulnerabilities identified during security scanning.

## Security Measures Implemented

### 1. CSRF Protection

**Problem:** Cross-Site Request Forgery (CSRF) attacks allow malicious sites to perform unauthorized actions on behalf of authenticated users.

**Solution:**
- Enabled CSRF protection using cookie-based tokens
- CSRF token cookie name: `XSRF-TOKEN`
- Token must be included in requests via header: `X-XSRF-TOKEN`
- Public read-only endpoints are exempted from CSRF validation
- Token endpoint available at: `GET /api/csrf/token`

**Frontend Integration:**
```javascript
// Get CSRF token
const response = await fetch('/api/csrf/token', { credentials: 'include' });
const csrfToken = await response.json();

// Include in requests
fetch('/api/endpoint', {
  method: 'POST',
  headers: {
    'X-XSRF-TOKEN': csrfToken.token,
    'Content-Type': 'application/json'
  },
  credentials: 'include',
  body: JSON.stringify(data)
});
```

### 2. Security Headers

**Implemented Headers:**

#### X-Content-Type-Options: nosniff
Prevents browsers from MIME-sniffing responses, reducing risk of content-type confusion attacks.

#### X-Frame-Options: DENY
Prevents the application from being embedded in iframes, protecting against clickjacking attacks.

#### Strict-Transport-Security (HSTS)
Forces HTTPS connections for 1 year (31536000 seconds) including subdomains.
- **Note:** Only effective when application is served over HTTPS
- Configuration: `max-age=31536000; includeSubDomains`

#### Content-Security-Policy (CSP)
Mitigates XSS and code injection attacks by controlling resource loading:
- `default-src 'self'` - Only load resources from same origin
- `script-src 'self'` - Only execute scripts from same origin
- `style-src 'self' 'unsafe-inline'` - Allow inline styles (adjust as needed)
- `img-src 'self' data: https:` - Images from same origin, data URLs, and HTTPS
- `frame-ancestors 'none'` - Cannot be framed
- `form-action 'self'` - Forms can only submit to same origin

#### Referrer-Policy: strict-origin-when-cross-origin
Controls referrer information sent with requests, protecting user privacy.

#### X-Permitted-Cross-Domain-Policies: none
Restricts cross-domain policy files.

#### Cache-Control Headers
Prevents caching of sensitive data:
- `Cache-Control: no-cache, no-store, must-revalidate`
- `Pragma: no-cache`
- `Expires: 0`

### 3. CORS Configuration

**Problem:** Overly permissive CORS allows any origin to access the API.

**Solution:**
- CORS restricted to specific origins defined in `application.properties`
- Default: `http://localhost:3000,http://localhost:4200`
- Credentials enabled for CSRF token support
- Allowed methods restricted to: GET, POST, PUT, DELETE, OPTIONS
- Allowed headers explicitly defined

**Configuration:**
```properties
cors.allowed.origins=http://localhost:3000,http://localhost:4200
```

### 4. Password Security

**Problem:** NoOpPasswordEncoder stores passwords in plain text.

**Solution:**
- Replaced with BCryptPasswordEncoder
- Passwords are hashed using BCrypt algorithm
- Each password gets a unique salt
- **Important:** Existing plain text passwords need to be migrated

**Password Migration:**
Existing users with plain text passwords will need to reset their passwords or have them re-hashed.

### 5. Session Security

**Configurations:**
- Session timeout: 30 minutes
- Cookie settings:
  - `HttpOnly: true` - JavaScript cannot access session cookies
  - `Secure: false` - Set to `true` in production with HTTPS
  - `SameSite: Strict` - Prevents CSRF via cookie restrictions
- Session fixation protection enabled via `migrateSession()`

**Application Properties:**
```properties
server.servlet.session.timeout=30m
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=false
server.servlet.session.cookie.same-site=strict
```

### 6. Information Disclosure Prevention

**Server Header Hiding:**
- `Server` header removed to prevent fingerprinting
- `X-Powered-By` header removed
- Error details, stack traces, and binding errors hidden in responses

**Configuration:**
```properties
=======
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
>>>>>>> 4d7155184e972130e5b28f8041b811db7e5d9748
server.error.include-message=never
server.error.include-binding-errors=never
server.error.include-stacktrace=never
server.error.include-exception=false
```

<<<<<<< HEAD
### 7. Administrative Interface Security

**Recommendations:**
- Remove or restrict access to phpMyAdmin and similar tools in production
- Use IP whitelisting for administrative interfaces
- Require strong authentication
- Never expose phpinfo.php or similar diagnostic pages

## Production Deployment Checklist

Before deploying to production:

- [ ] Update CORS origins to production domain(s)
- [ ] Set `server.servlet.session.cookie.secure=true` (requires HTTPS)
- [ ] Configure HTTPS/TLS on server
- [ ] Verify HSTS is working with HTTPS
- [ ] Remove or secure administrative tools (phpMyAdmin, etc.)
- [ ] Review and adjust CSP policy based on actual resource needs
- [ ] Test CSRF protection with frontend application
- [ ] Migrate existing user passwords to BCrypt
- [ ] Configure proper logging and monitoring
- [ ] Set up security headers at reverse proxy (Nginx/Apache) level
- [ ] Disable server tokens in Nginx: `server_tokens off;`
- [ ] Review and test all endpoints with authentication

## Testing Security Headers

You can verify security headers using curl:

```bash
curl -I https://your-domain.com/api/csrf/token
```

Or use online tools:
- [Security Headers](https://securityheaders.com/)
- [Mozilla Observatory](https://observatory.mozilla.org/)

## API Endpoints

### Public Endpoints (No CSRF Required)
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration  
- `GET /api/productos/listar` - List products
- `GET /api/catalogo/**` - Catalog endpoints

### Protected Endpoints (CSRF Required)
All other POST, PUT, DELETE operations require:
1. Valid JWT token in Authorization header
2. CSRF token in X-XSRF-TOKEN header

## Known Limitations

1. **CSP Policy:** Current policy allows `'unsafe-inline'` for styles. This should be removed and replaced with nonces or hashes in a future update.

2. **HTTPS:** HSTS and secure cookies are only effective when application is served over HTTPS. Local development uses HTTP.

3. **Password Migration:** Existing users with plain text passwords need migration strategy.

## Future Improvements

1. Implement CSP with nonces instead of `'unsafe-inline'`
2. Add rate limiting to prevent brute force attacks
3. Implement account lockout mechanism
4. Add security audit logging
5. Implement Content-Security-Policy-Report-Only for monitoring
6. Add automated security scanning in CI/CD pipeline
7. Implement API key rotation mechanism

## References

- [OWASP CSRF Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.html)
- [OWASP Secure Headers Project](https://owasp.org/www-project-secure-headers/)
- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/index.html)
- [Content Security Policy Reference](https://content-security-policy.com/)
=======
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
>>>>>>> 4d7155184e972130e5b28f8041b811db7e5d9748
