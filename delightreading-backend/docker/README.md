# docker folder

To build docker image of the server, execute the following from the `server` folder

  ```
  docker build -t delightreading-backend -f ./docker/Dockerfile .
  ```

To start the delightreading server in a conatiner with the dependencies (mongo & elasticsearch)
  ```
  docker-compose -f ./docker/docker-compose.yml up
  ```
To stop
  ```
  docker-compose -f ./docker/docker-compose.yml down
  ```

The option `-d` runs in detached mode (background).

When files are modified, you may need to re-build
  ```
  docker-compose build --no-cache
  ```

# Environment
Notice that the docker-compose.yml is setting `ENV_NAME=.env.docker`. This will
configure the database url accordingly.

# Running Postgres alone from docker
@see: https://github.com/bitnami/bitnami-docker-postgresql
TL;DR;

```
$ docker run --name postgresql bitnami/postgresql:latest
```

## Persisting your database

```
$ docker run \
    -v /path/to/postgresql-persistence:/bitnami \
    bitnami/postgresql:latest
```
