DelightReading
==============

DelghtReading is a web-based application that manages reading logs.

It consists of three components:
1. DelightReading-server - Java/Spring based RESTful service. Implements all back-end business 
logic.
2. DelightReading-client-web - Single page applicaiton for authenticated access. 
2. DelightReading-server-ui - Nodejs based application for public access pages as well as hosting the client-web.
3. DelightReading-gateway - Gateway

# Deploying the application

1. Buil the server
```
cd delightreading-server
mvn package
```

2. build the client-web

```
cd delightreading-client-web
npm build build; npm build dist 
```

3. build the UI server
```
cd delightreading-server-ui
npm build build
```

# Running the application
```
mvn -f ./delightreading-gateway/pom.xml spring-boot:run
mvn -f ./delightreading-server/pom.xml spring-boot:run
cd delightreading-server-ui;node dist/server.js
```

# Running the application in local dev mode
This runs the web-client in dev mode, meaning that the changes to the code are reflected 
immediately.
The caveat is that the web browser needs to be run with security disabled for cookies to be 
stored.

The gateway is not necessary in this mode as the browser can access the SPA page directly to
the webpack-dev-server.

```
mvn -f ./delightreading-server/pom.xml spring-boot:run
cd delightreading-server-ui;npm start
```