FROM openjdk:17

WORKDIR /app

COPY . .

RUN apt-get update && \
    apt-get install -y python3 python3-pip

RUN pip3 install -r requirements.txt

RUN javac AmpliconDesigner.java

CMD ["gunicorn", "--bind", "0.0.0.0:10000", "app:app"]