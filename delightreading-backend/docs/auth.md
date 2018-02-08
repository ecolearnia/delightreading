Create a google project

Manage API credentials at [Google Dev Console](https://console.developers.google.com/apis/credentials?project=delightreading-test&authuser=0)


Tutorial at [Google Dev site](https://developers.google.com/identity/sign-in/web/sign-in)



Trouble shooting Google 

When getting error
500 TokenError: Bad Request
   at Strategy.OAuth2Strategy.parseErrorResponse ..

1. Go API & auth > APIs and enable Google+ API.
2. Then click on Explore this API and enable Authorize requests using OAuth 2.0 

Also `callbackURL` must be "/auth/google/callback", instead of "auth/google/callback"


Another sample of [SPA with Social Auth](https://www.sitepoint.com/spa-social-login-google-facebook/) 