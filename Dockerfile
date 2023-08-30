FROM openjdk:19
EXPOSE 8080

# Set environment variables
#ENV QUEUE_NAME=TestQueue EXCHANGE=TestExchange ROUTING_KEY=fss RABBITMQ_URL=amqps://sdpwqges:GoybPFCeHCxMjtk0eqSeAhEykG335zGL@puffin.rmq2.cloudamqp.com/sdpwqges RABBITMQ_USERNAME=sdpwqges RABBITMQ_PASSWORD=GoybPFCeHCxMjtk0eqSeAhEykG335zGL

ADD target/printapp.jar printapp.jar
ENTRYPOINT ["java","-jar","/printapp.jar"]