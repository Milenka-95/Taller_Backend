# Password Migration Guide

## Overview

This application has been updated to use BCrypt password hashing for security. Previously, passwords were stored in plain text using `NoOpPasswordEncoder`, which is **highly insecure**.

## Impact

- **New users**: Passwords will be automatically hashed with BCrypt when registering
- **Existing users**: Passwords stored in plain text will **NOT work** after this update

## Migration Options

### Option 1: Password Reset (Recommended for Production)

If you have a password reset mechanism:

1. Force all existing users to reset their passwords
2. New passwords will be automatically hashed with BCrypt

### Option 2: Manual Migration Script (For Development/Testing)

If you need to migrate existing passwords and have access to the database:

```sql
-- WARNING: This is only for development where you know the plain text passwords
-- In production, you should use Option 1 (password reset)

-- For each user, you need to:
-- 1. Get the BCrypt hash of their password using the Spring Boot application
-- 2. Update the database

-- Example: Update a specific user's password
UPDATE usuario SET password = '$2a$10$...' WHERE id = 1;
```

### Option 3: Temporary Backward Compatibility Script (NOT RECOMMENDED)

For testing purposes only, you can create a one-time migration endpoint:

```java
@PostMapping("/admin/migrate-passwords")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> migratePasswords() {
    List<Usuario> users = usuarioRepository.findAll();
    for (Usuario user : users) {
        // Only migrate if password is not already hashed
        if (!user.getPassword().startsWith("$2a$")) {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            usuarioRepository.save(user);
        }
    }
    return ResponseEntity.ok("Passwords migrated");
}
```

**IMPORTANT**: This endpoint should be:
- Protected with admin-only access
- Removed after migration is complete
- Never deployed to production
- Only used in controlled development environments

## Testing Password Encryption

To verify BCrypt is working:

1. Create a new user via `/api/auth/register`
2. Check the database - password should look like: `$2a$10$...` (60 characters)
3. Login with the same user - should work correctly

## Security Notes

- BCrypt hashes are 60 characters long
- Each password gets a unique salt
- The same password will produce different hashes
- BCrypt is intentionally slow to prevent brute force attacks
- **Never store or log plain text passwords**

## Database Considerations

Make sure your database column for passwords can store at least 60 characters:

```sql
-- Check current column size
DESCRIBE usuario;

-- If password column is too small, update it:
ALTER TABLE usuario MODIFY COLUMN password VARCHAR(255);
```

## Recommended Steps for Production Migration

1. **Backup your database** before any changes
2. Update the application with new security code
3. Ensure password column is VARCHAR(255) or larger
4. Implement password reset functionality
5. Send password reset emails to all users
6. Monitor login failures and provide support
7. Remove any temporary migration endpoints

## Support

If users cannot login after the update:
1. Verify the password column size in database (should be VARCHAR(255))
2. Have users reset their password
3. Check application logs for authentication errors
4. Verify BCrypt is properly configured in `SecurityConfig`

## References

- [Spring Security Password Encoding](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html)
- [BCrypt Algorithm](https://en.wikipedia.org/wiki/Bcrypt)
- [OWASP Password Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html)
