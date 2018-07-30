/*
 * oauth2-oidc-sdk
 *
 * Copyright 2012-2016, Connect2id Ltd and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.nimbusds.oauth2.sdk.token;


import java.util.Map;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import net.jcip.annotations.Immutable;
import net.minidev.json.JSONObject;

/**
 * HACK to circumvent LinkedIn non-compliance to OAuth - missing token_type
 * Copied from: https://bitbucket.org/connect2id/oauth-2.0-sdk-with-openid-connect-extensions/raw/65d84184001474cb1809995b43bdc8b45a124ccc/src/main/java/com/nimbusds/oauth2/sdk/token/BearerAccessToken.java
 *
 */

/**
 * Bearer access token.
 *
 * <p>Example bearer access token serialised to JSON:
 *
 * <pre>
 * {
 *   "access_token" : "2YotnFZFEjr1zCsicMWpAA",
 *   "token_type"   : "bearer",
 *   "expires_in"   : 3600,
 *   "scope"        : "read write"
 * }
 * </pre>
 *
 * <p>The above example token serialised to a HTTP Authorization header:
 *
 * <pre>
 * Authorization: Bearer 2YotnFZFEjr1zCsicMWpAA
 * </pre>
 *
 * <p>Related specifications:
 *
 * <ul>
 *     <li>OAuth 2.0 (RFC 6749), sections 1.4 and 5.1.
 *     <li>OAuth 2.0 Bearer Token Usage (RFC 6750).
 * </ul>
 */
@Immutable
public class BearerAccessToken extends AccessToken {


    /**
     * Creates a new minimal bearer access token with a randomly generated
     * 256-bit (32-byte) value, Base64URL-encoded. The optional lifetime
     * and scope are left undefined.
     */
    public BearerAccessToken() {

        this(32);
    }


    /**
     * Creates a new minimal bearer access token with a randomly generated
     * value of the specified byte length, Base64URL-encoded. The optional
     * lifetime and scope are left undefined.
     *
     * @param byteLength The byte length of the value to generate. Must be
     *                   greater than one.
     */
    public BearerAccessToken(final int byteLength) {

        this(byteLength, 0L, null);
    }


    /**
     * Creates a new bearer access token with a randomly generated 256-bit
     * (32-byte) value, Base64URL-encoded.
     *
     * @param lifetime The lifetime in seconds, 0 if not specified.
     * @param scope    The scope, {@code null} if not specified.
     */
    public BearerAccessToken(final long lifetime, final Scope scope) {

        this(32, lifetime, scope);
    }


    /**
     * Creates a new bearer access token with a randomly generated value of
     * the specified byte length, Base64URL-encoded.
     *
     * @param byteLength The byte length of the value to generate. Must be
     *                   greater than one.
     * @param lifetime   The lifetime in seconds, 0 if not specified.
     * @param scope      The scope, {@code null} if not specified.
     */
    public BearerAccessToken(final int byteLength, final long lifetime, final Scope scope) {

        super(AccessTokenType.BEARER, byteLength, lifetime, scope);
    }


    /**
     * Creates a new minimal bearer access token with the specified value.
     * The optional lifetime and scope are left undefined.
     *
     * @param value The access token value. Must not be {@code null} or
     *              empty string.
     */
    public BearerAccessToken(final String value) {

        this(value, 0L, null);
    }


    /**
     * Creates a new bearer access token with the specified value and
     * optional lifetime and scope.
     *
     * @param value    The access token value. Must not be {@code null} or
     *                 empty string.
     * @param lifetime The lifetime in seconds, 0 if not specified.
     * @param scope    The scope, {@code null} if not specified.
     */
    public BearerAccessToken(final String value, final long lifetime, final Scope scope) {

        super(AccessTokenType.BEARER, value, lifetime, scope);
    }


    /**
     * Returns the HTTP Authorization header value for this bearer access
     * token.
     *
     * <p>Example:
     *
     * <pre>
     * Authorization: Bearer eyJhbGciOiJIUzI1NiJ9
     * </pre>
     *
     * @return The HTTP Authorization header.
     */
    @Override
    public String toAuthorizationHeader(){

        return "Bearer " + getValue();
    }


    @Override
    public boolean equals(final Object object) {

        return object instanceof BearerAccessToken &&
                this.toString().equals(object.toString());
    }


    /**
     * Parses a bearer access token from a JSON object access token
     * response.
     *
     * @param jsonObject The JSON object to parse. Must not be
     *                   {@code null}.
     *
     * @return The bearer access token.
     *
     * @throws ParseException If the JSON object couldn't be parsed to a
     *                        bearer access token.
     */
    public static BearerAccessToken parse(final JSONObject jsonObject)
            throws ParseException {

        // Parse and verify type
        // Original: AccessTokenType tokenType = new AccessTokenType(JSONObjectUtils.getString(jsonObject, "token_type"));
        // Parse and verify type
        AccessTokenType tokenType;
        try {
            tokenType = new AccessTokenType(JSONObjectUtils.getString(jsonObject, "token_type"));
        } catch (ParseException ex) {
            tokenType = AccessTokenType.BEARER;
        }

        if (! tokenType.equals(AccessTokenType.BEARER))
            throw new ParseException("Token type must be \"Bearer\"");


        // Parse value
        String accessTokenValue = JSONObjectUtils.getString(jsonObject, "access_token");


        // Parse lifetime
        long lifetime = 0;

        if (jsonObject.containsKey("expires_in")) {

            // Lifetime can be a JSON number or string

            if (jsonObject.get("expires_in") instanceof Number) {

                lifetime = JSONObjectUtils.getLong(jsonObject, "expires_in");
            }
            else {
                String lifetimeStr = JSONObjectUtils.getString(jsonObject, "expires_in");

                try {
                    lifetime = new Long(lifetimeStr);

                } catch (NumberFormatException e) {

                    throw new ParseException("Invalid \"expires_in\" parameter, must be integer");
                }
            }
        }


        // Parse scope
        Scope scope = null;

        if (jsonObject.containsKey("scope"))
            scope = Scope.parse(JSONObjectUtils.getString(jsonObject, "scope"));


        return new BearerAccessToken(accessTokenValue, lifetime, scope);
    }


    /**
     * Parses an HTTP Authorization header for a bearer access token.
     *
     * @param header The HTTP Authorization header value to parse. May be
     *               {@code null} if the header is missing, in which case
     *               an exception will be thrown.
     *
     * @return The bearer access token.
     *
     * @throws ParseException If the HTTP Authorization header value
     *                        couldn't be parsed to a bearer access token.
     */
    public static BearerAccessToken parse(final String header)
            throws ParseException {

        if (StringUtils.isBlank(header))
            throw new ParseException("Missing HTTP Authorization header", BearerTokenError.MISSING_TOKEN);

        String[] parts = header.split("\\s", 2);

        if (parts.length != 2)
            throw new ParseException("Invalid HTTP Authorization header value", BearerTokenError.INVALID_REQUEST);

        if (! parts[0].equals("Bearer"))
            throw new ParseException("Token type must be \"Bearer\"", BearerTokenError.INVALID_REQUEST);

        try {
            return new BearerAccessToken(parts[1]);

        } catch (IllegalArgumentException e) {

            throw new ParseException(e.getMessage(), BearerTokenError.INVALID_REQUEST);
        }
    }


    /**
     * Parses a query or form parameters map for a bearer access token.
     *
     * @param parameters The query parameters. Must not be {@code null}.
     *
     * @return The bearer access token.
     *
     * @throws ParseException If a bearer access token wasn't found in the
     *                        parameters.
     */
    public static BearerAccessToken parse(final Map<String,String> parameters)
            throws ParseException {

        if (! parameters.containsKey("access_token")) {
            throw new ParseException("Missing access token parameter", BearerTokenError.MISSING_TOKEN);
        }

        String accessTokenValue = parameters.get("access_token");

        if (StringUtils.isBlank(accessTokenValue)) {
            throw new ParseException("Blank / empty access token", BearerTokenError.INVALID_REQUEST);
        }

        return new BearerAccessToken(accessTokenValue);
    }



    /**
     * Parses an HTTP request for a bearer access token.
     *
     * @param request The HTTP request to parse. Must not be {@code null}.
     *
     * @return The bearer access token.
     *
     * @throws ParseException If a bearer access token wasn't found in the
     *                        HTTP request.
     */
    public static BearerAccessToken parse(final HTTPRequest request)
            throws ParseException {

        // See http://tools.ietf.org/html/rfc6750#section-2

        String authzHeader = request.getAuthorization();

        if (authzHeader != null) {

            return parse(authzHeader);
        }

        // Try alternative token locations, form and query string are
        // parameters are not differentiated here

        Map<String,String> params = request.getQueryParameters();

        return parse(params);
    }
}