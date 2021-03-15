docker build -t web mod-web/
docker save web | gzip > prodweb.img.tgz
docker build -t backend mod-backend/
docker save backend | gzip > prodbackend.img.tgz 
docker build -t database mod-postgres/
docker save database | gzip > proddatabase.img.tgz 
docker build -t pythonapp mod-python/
docker save pythonapp | gzip > prodpythonapp.img.tgz 
