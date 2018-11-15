# delightreading-client-web

> A Vue.js project

## Build Setup

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report

# run unit tests
npm run unit

# run e2e tests
npm run e2e

# run all tests
npm test

# run UI using webpack-dev-server
npm start
```

** NOTE: using the webpack-dev-server ignores cookie =(
    Using the 

For a detailed explanation on how things work, check out the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).

## OAuth

### GOogle Oauth
To modify project's credentials
https://console.developers.google.com/apis/credentials

## Proxy configuration
Running the app from  webpack-dev-server and making AJAX call directly to the API server 
with a different port will cause CORS error.

The workaround is to enable proxy and have the SPA application call the dev-server.
TO configure proxy, add the following code in the config/dev.env.js 
```
proxyTable: {
      '/api': 'http://localhost:9090'
    },
```