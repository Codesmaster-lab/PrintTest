FROM openjdk:19
EXPOSE 8080
ADD target/printapp.jar printapp.jar
ENTRYPOINT ["java","-jar","/core.jar"]