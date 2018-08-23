package com.delightreading.authsupport;

import com.delightreading.user.UserAuthenticationEntity;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class JwtService {

    String secret = "MySecret that is at least 32 length long";

    public String createToken(UserAuthenticationEntity authentication) {
        try {
            JWSSigner signer = new MACSigner("MySecret that is at least 32 length long");

            // Prepare JWT with claims set
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(authentication.getAccount().getUid())
                    .issuer("https://delightreading.com")
                    .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            // Apply the HMAC protection
            signedJWT.sign(signer);

            // Serialize to compact form, produces something like
            // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
            return signedJWT.serialize();
        } catch (Exception e) {
            log.warn("Could not create token", e);
            throw new IllegalStateException("Could not create token", e);
        }
    }

    public JWT parse(String tokenStr) {
        try {
            // parse the token.
            SignedJWT signedJWT = SignedJWT.parse(tokenStr);
            JWSVerifier verifier = new MACVerifier("MySecret that is at least 32 length long");
            boolean good = signedJWT.verify(verifier);
            //assertTrue(signedJWT.verify(verifier));
            return signedJWT;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing");
        }

    }
}
