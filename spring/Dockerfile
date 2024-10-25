# mvn package

# docker build --no-cache -t spring/spring-jvm .

# docker run -i --rm -p 8080:8080 spring/spring-jvm


FROM openjdk:17-alpine

ADD target/springboot.jar springboot.jar

ENTRYPOINT ["java", "-jar", "springboot.jar"]