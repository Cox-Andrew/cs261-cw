docker rm -f backend database web pythonapp

docker network create mood-net

docker build -t database containers/mod-postgres
docker build -t pythonapp containers/mod-python

docker run -p 8001:8080 -v "%CD%\warfiles:/var/lib/jetty/webapps" --name backend -d jetty
docker run -p 8000:80 -v "%CD%\php:/var/www/html" --name web -d php:7.2-apache
docker run -p 8432:5432 -v "%CD%\containers\mod-postgres:/var/local" --name database -e POSTGRES_PASSWORD=password -d database
docker run -p 8002:80 -v "%CD%\pythonapp:/app" --name pythonapp -d pythonapp

docker network connect mood-net backend
docker network connect mood-net web
docker network connect mood-net database
docker network connect mood-net pythonapp
