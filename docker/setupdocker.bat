docker network create mood-net || true

docker build -t database containers\mod-postgres



docker run -p 8001:8080 -v "C:\Users\Jude\Documents\CS261\docker\warfiles:/var/lib/jetty/webapps" --name backend -d jetty
docker run -p 8000:80 -v "C:\Users\Jude\Documents\CS261\docker\php:/var/www/html" --name web -d php:7.2-apache
docker run -p 8432:5432 -v "C:\Users\Jude\Documents\CS261\docker\containers\mod-postgres:/var/local" --name database -e POSTGRES_PASSWORD=password -d database

docker network connect mood-net backend
docker network connect mood-net web
docker network connect mood-net database
