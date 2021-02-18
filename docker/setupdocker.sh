docker network create mood-net || true

docker build -t database containers/mod-postgres

WARPATH=$HOME/Documents/CS261/coursework/cs261-cw/docker/warfiles
PHPAPP=$HOME/Documents/CS261/coursework/cs261-cw/docker/php
DBPATH=$HOME/Documents/CS261/coursework/cs261-cw/docker/containers/mod-postgres

docker run -p 8001:8080 -v "$WARPATH:/var/lib/jetty/webapps" --name backend -d jetty
docker run -p 8000:80 -v "$PHPAPP:/var/www/html" --name web -d php:7.2-apache
docker run -p 8432:5432 -v "$DBPATH:/var/local" --name database -e POSTGRES_PASSWORD=password -d database

docker network connect mood-net backend
docker network connect mood-net web
docker network connect mood-net database
