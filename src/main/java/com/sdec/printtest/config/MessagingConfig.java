package com.sdec.printtest.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String QUEUE = "TestQueue";
    public static final String EXCHANGE = "TestExchange";
    public static final String ROUTING_KEY = "fss";

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
        connectionFactory.setUri("amqps://sdpwqges:GoybPFCeHCxMjtk0eqSeAhEykG335zGL@puffin.rmq2.cloudamqp.com/sdpwqges");
        connectionFactory.setUsername("sdpwqges");
        connectionFactory.setPassword("GoybPFCeHCxMjtk0eqSeAhEykG335zGL");
        connectionFactory.setVirtualHost("sdpwqges");

        connectionFactory.setRequestedHeartBeat(30);
        connectionFactory.setConnectionTimeout(30000);

        RabbitAdmin admin= new RabbitAdmin(connectionFactory);
        admin.declareQueue(getQueue());
        admin.declareExchange(getTopicExchange());
        admin.declareBinding(BindingBuilder.bind(getQueue()).to(getTopicExchange()).with(getRoutingKey()));

        RabbitTemplate template =new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());

        return template;

    }

}