docker network create mood-net || true

docker build -t database containers/mod-postgres
docker build -t pythonapp containers/mod-python

WARPATH=$HOME/Documents/CS261/coursework/cs261-cw/docker/warfiles
PHPAPP=$HOME/Documents/CS261/coursework/cs261-cw/docker/php
DBPATH=$HOME/Documents/CS261/coursework/cs261-cw/docker/containers/mod-postgres
PYPATH=$HOME/Documents/CS261/coursework/cs261-cw/docker/pythonapp

docker run -p 8001:8080 -v "$WARPATH:/var/lib/jetty/webapps" --name backend -d jetty
docker run -p 8000:80 -v "$PHPAPP:/var/www/html" --name web -d php:7.2-apache
docker run -p 8432:5432 -v "$DBPATH:/var/local" --name database -e POSTGRES_PASSWORD=password -d database
docker run -idt -p 8002:5000 -v "$PYPATH:/var/lib/pythonapp" --name pythonapp -d python:3

docker network connect mood-net backend
docker network connect mood-net web
docker network connect mood-net database
docker network connect mood-net pythonapp
