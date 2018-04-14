docker build -t delightreading-app:0.2 -f ./docker/Dockerfile .
docker tag delightreading-app:0.2 registry.heroku.com/delightreading/web
docker push registry.heroku.com/delightreading/web