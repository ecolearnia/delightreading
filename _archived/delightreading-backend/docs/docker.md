# Docker knowledge base

## Running postgres with persistent database
docker run -v C:\workspace\projects\pgdata:/bitnami  --name postgresql -p 5432:5432 bitnami/postgresql:latest

NOTE: This works when run within `bash` console. It hangs when run under Winnows `CMD` console

For more detail on running bitnami's postgres see 
[link](https://github.com/bitnami/bitnami-docker-postgresql)