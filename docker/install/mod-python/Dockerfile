FROM tiangolo/uwsgi-nginx-flask:python3.6-alpine3.7
ENV STATIC_URL /static
ENV STATIC_PATH /var/www/app/static
COPY pythonapp/ /app
COPY requirements.txt /var/www/requirements.txt
RUN pip install -r /var/www/requirements.txt
EXPOSE 80
