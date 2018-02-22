# Backend

## Running
Run server expecting client-ui to be in different port 8080.
```
$ts-node src/server.ts
```
Or to with with demo environment that runs the SPA client-ui from same express server
```
$APP_ENV=.env.demo ts-node src/server.ts
```

## Testing
npm test
./node_modules/jest/bin/jest.js -t <test name>

# Reference
The Social Network Auth is based on [sahat's hackaton starter](https://github.com/sahat/hackathon-starter/) 