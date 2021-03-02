FROM python:3
ENV STATIC_URL /static
ENV STATIC_PATH /var/lib/app/static
COPY requirements.txt /var/lib/requirements.txt
RUN pip3 install -r /var/lib/requirements.txt
EXPOSE 5000
