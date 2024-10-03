# mvn package

# docker build --no-cache -t spring/spring-jvm .

# docker run -i --rm -p 8080:8080 spring/spring-jvm


FROM openjdk:17-alpine

ADD target/springboot.jar springboot.jar

ENV SPRING_DATASOURCE_URL="jdbc:postgresql://books-db.postgres.database.azure.com:5432/books?sslmode=require"
ENV SPRING_DATASOURCE_USERNAME="kovacevic"
ENV SPRING_DATASOURCE_PASSWORD="!reactive33"
ENV SPRING_JPA_PROPERTIES_HIBERNATE_HBM2DDL_IMPORT_FILES="no-file"

# For images with one thread
# ENV SERVER_TOMCAT_THREADS_MAX=1

ENTRYPOINT ["java", "-jar", "springboot.jar"]