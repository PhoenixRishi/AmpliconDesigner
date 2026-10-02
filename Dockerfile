FROM python:3.11-slim

RUN apt-get update && \
    apt-get install -y --no-install-recommends default-jdk-headless && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY . .

RUN pip install -r requirements.txt

RUN javac AmpliconDesigner.java

CMD ["gunicorn", "--bind", "0.0.0.0:10000", "app:app"]