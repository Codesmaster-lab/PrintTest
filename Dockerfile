FROM openjdk:19
EXPOSE 8080

# Set environment variables
ENV QUEUE_NAME=TestQueue EXCHANGE=TestExchange ROUTING_KEY=fss

ADD target/printapp.jar printapp.jar
ENTRYPOINT ["java","-jar","/printapp.jar"]