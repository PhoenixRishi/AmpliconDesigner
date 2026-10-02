FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN javac AmpliconDesigner.java
RUN pip install -r requirements.txt

CMD ["gunicorn", "--bind", "0.0.0.0:10000", "app:app"]