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
server.error.include-message=never
server.error.include-binding-errors=never
server.error.include-stacktrace=never
server.error.include-exception=false
```

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
