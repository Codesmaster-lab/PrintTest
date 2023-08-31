package com.sdec.printtest.config;

import org.apache.logging.log4j.simple.SimpleLogger;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MessagingConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MessagingConfig.class);

    // public static final String QUEUE = "TestQueue";
   public static final String QUEUE = System.getenv("QUEUE_NAME");

    //public static final String EXCHANGE = "TestExchange";
    public static final String EXCHANGE = System.getenv("EXCHANGE");
//    public static final String ROUTING_KEY = "fss";
public static final String ROUTING_KEY = System.getenv("ROUTING_KEY");

    @Bean
    public Queue getQueue()
    {
        Queue queue =new Queue(QUEUE);
        return queue;
    }
    @Bean
    public TopicExchange getTopicExchange()
    {
        TopicExchange exchange =new TopicExchange(EXCHANGE);
        return exchange ;
    }
    @Bean
    public String getRoutingKey()
    {
        return ROUTING_KEY ;
    }

    @Bean
    public RabbitTemplate setupConnectionAndGetaTemplate(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("puffin.rmq2.cloudamqp.com");
        connectionFactory.setUri(System.getenv("RABBITMQ_URL"));
        connectionFactory.setUsername(System.getenv("RABBITMQ_USERNAME"));
        connectionFactory.setPassword(System.getenv("RABBITMQ_PASSWORD"));
        connectionFactory.setVirtualHost(System.getenv("RABBITMQ_USERNAME"));

        connectionFactory.setRequestedHeartBeat(30);
        connectionFactory.setConnectionTimeout(30000);

        RabbitAdmin admin= new RabbitAdmin(connectionFactory);
        admin.declareQueue(getQueue());
        admin.declareExchange(getTopicExchange());
        admin.declareBinding(BindingBuilder.bind(getQueue()).to(getTopicExchange()).with(getRoutingKey()));

        LOG.info("Connection established");
        RabbitTemplate template =new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());

        return template;

    }

}
