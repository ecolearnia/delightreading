Deploying NodeJS App in Heroku
==============================

# About
This document guides throught the installation and deployment of NodeJS application
in Heroku using docker.
It assumes the database is already provisioned elsewhere, in this case we are using
PostgreSQL in ElephantDB.

# Pre-requisites
Install docker
Install heroku-cli
```
heroku plugins:install heroku-container-registry
```

# Project setup

## Create an application in Heroku
```
$heroku create <myapp>
```

## Set an environment variable in Heroku application
```
$heroku config:set --app delightreading DATABASE_URL=postgres://ltpqympm:ucxkt4PVqv_Dy1vDy1VsHr1L3YC9qWh4@baasu.db.elephantsql.com:5432/ltpqympm
$heroku config:set --app delightreading TRUST_PROXY=true
$heroku config:set --app delightreading UI_BASE_URL=
$heroku config:set --app delightreading SERVER_BASE_URL=
$heroku config:set --app delightreading SERVER_BASE_URL=
```

# Build docker image and push to heroku docker registry
Build docker image 
```
$docker build -t delightreading-app:0.2 -f ./docker/Dockerfile .
```

Login to heroku's registry
```
$heroku container:login
// Alternatively
$docker login --username=_ --password=$(heroku auth:token) registry.heroku.com
```

Tag image and push it to heorku docker registry, pushing automatically deploys the app
docker tag <image> registry.heroku.com/<app>/<process-type>
```
$docker tag delightreading-app:0.2 registry.heroku.com/delightreading/web
$docker push registry.heroku.com/delightreading/web
```
## Run the application, view log
Open the browser
```
$heroku open --app delightreading
```

Display the log of the deployed app
```
heroku logs --tail --app delightreading
```

# References
- [Getting Started on Heroku with Node.js](https://devcenter.heroku.com/articles/getting-started-with-nodejs)
- [How to Run Dockerized Apps on Heroku… and it’s pretty sweet](https://medium.com/travis-on-docker/how-to-run-dockerized-apps-on-heroku-and-its-pretty-great-76e07e610e22)
- [Container Registry & Runtime (Docker Deploys)](https://devcenter.heroku.com/articles/container-registry-and-runtime#pushing-an-image-s)


