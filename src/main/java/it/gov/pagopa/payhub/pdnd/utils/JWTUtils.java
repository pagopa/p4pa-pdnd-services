package it.gov.pagopa.payhub.pdnd.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

public class JWTUtils {
  private JWTUtils() {
  }

  public static boolean isJWTExpired(String token) {
    try {
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expiresAt = decodedJWT.getExpiresAt();
        return expiresAt.before(new Date());
    } catch (JWTDecodeException e) {
        throw new JWTDecodeException(e.getMessage());
    }
  }
}
