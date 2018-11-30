package com.delightreading.authsupport

import com.delightreading.user.model.UserAuthenticationEntity
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import java.util.*


@Service
@Slf4j
class JwtService {

    internal var secret = "MySecret that is at least 32 length long"

    fun createToken(authentication: UserAuthenticationEntity): String {
        try {
            val signer = MACSigner("MySecret that is at least 32 length long")

            // Prepare JWT with claims set
            val claimsSet = JWTClaimsSet.Builder()
                // The subject is the Authentication's UID, not the account's UID
                .subject(authentication.uid)
                .issuer("https://delightreading.com")
                // TODO: add roles
                //                    .claim("role", authentication.getAccount().getRoles())
                .expirationTime(Date(Date().time + 60 * 1000))
                .build()

            val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claimsSet)

            // Apply the HMAC protection
            signedJWT.sign(signer)

            // Serialize to compact form, produces something like
            // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
            return signedJWT.serialize()
        } catch (e: Exception) {
//            log.warn("Could not create token", e)
            throw IllegalStateException("Could not create token", e)
        }

    }

    fun parse(tokenStr: String): JWT {
        try {
            // parse the token.
            val signedJWT = SignedJWT.parse(tokenStr)
            val verifier = MACVerifier("MySecret that is at least 32 length long")
            val good = signedJWT.verify(verifier)
            //assertTrue(signedJWT.verify(verifier));
            return signedJWT
        } catch (e: Exception) {
            throw IllegalArgumentException("Error parsing")
        }

    }
}