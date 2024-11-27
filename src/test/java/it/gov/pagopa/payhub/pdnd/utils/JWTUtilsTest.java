package it.gov.pagopa.payhub.pdnd.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import java.util.Date;
import org.junit.jupiter.api.Test;

class JWTUtilsTest {

  @Test
  void givenValidTokenWhenIsJWTExpiredThenTokenNotExpired() {
    // Given
    Date futureDate = new Date(System.currentTimeMillis() + 3600 * 1000); // 1 hour from now
    String token = JWT.create()
        .withExpiresAt(futureDate)
        .sign(com.auth0.jwt.algorithms.Algorithm.HMAC256("secret"));

    // Then
    assertFalse(JWTUtils.isJWTExpired(token));
  }

  @Test
  void givenExpiredTokenWhenIsJWTExpiredThenTokenExpired() {
    // Given
    Date pastDate = new Date(System.currentTimeMillis() - 3600 * 1000); // 1 hour ago
    String token = JWT.create()
        .withExpiresAt(pastDate)
        .sign(com.auth0.jwt.algorithms.Algorithm.HMAC256("secret"));
    // Then
    assertTrue(JWTUtils.isJWTExpired(token));
  }

  @Test
  void givenInvalidTokenWhenIsJWTExpiredThenException() {
    // Given
    String invalidtoken = "INVALIDTOKEN";
    // Then
    assertThrows(JWTDecodeException.class, () -> JWTUtils.isJWTExpired(invalidtoken));
  }
}