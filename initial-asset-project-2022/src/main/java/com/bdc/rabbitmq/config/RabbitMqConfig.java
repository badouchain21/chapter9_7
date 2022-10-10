package com.bdc.rabbitmq.config;


import com.bdc.api.intermanage.interfacelogger.service.IInterFaceLoggerService;
import com.bdc.rabbitmq.handler.ReceiveHandler;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author lipeishan@badousoft.com
 * @version 1.0.0
 * @ClassName RabbitMqConfig.java
 * @Description RabbitMqConfig
 * @createTime 2020年12月07日 16:42:00
 */
@Configuration
public class RabbitMqConfig {
        /**
         * 队列
         */
        @Value("${rabbit.queue-inform-log}")
        public String queueInformLog;
        /**
         * 消费者数量
         */
        @Value("${rabbit.concurrent-consumers}")
        private Integer concurrentConsumers;
        /**
         * 交换机
         */
        @Value("${rabbit.exchange-topics-inform}")
        private String exchangeTopicsInform;
        /**
         * 路由键
         */
        @Value("${rabbit.routingkey}")
        private String routingKey;

        @Bean
        public ReceiveHandler receiveHandler(IInterFaceLoggerService interFaceLoggerService){
            ReceiveHandler receiveHandler = new ReceiveHandler(interFaceLoggerService);
            return receiveHandler;
        }

        @Bean
        public RabbitAdmin rabbitAdmin (ConnectionFactory connectionFactory){
            RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
            Queue queue = new Queue(queueInformLog);
            Exchange exchange = ExchangeBuilder.topicExchange(exchangeTopicsInform).durable(true).build();

            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareExchange(exchange);
            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs());
            return rabbitAdmin;
        }

        @Bean
        public RabbitTemplate rabbitTemplate (ConnectionFactory connectionFactory){
            RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
            rabbitTemplate.setQueue(queueInformLog);
            rabbitTemplate.setExchange(exchangeTopicsInform);
            rabbitTemplate.setRoutingKey(routingKey);
            return rabbitTemplate;
        }

        @Bean
        public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,ReceiveHandler receiveHandler) {
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            container.setMessageListener(receiveHandler);
            container.setConcurrentConsumers(1);
            container.setQueueNames(queueInformLog);
            return container;
        }

}

