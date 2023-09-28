package com.example.rabbitshell.services;

import com.example.rabbitshell.entities.Message;
import com.example.rabbitshell.repositories.MessageRepository;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Service
public class FromDBToRabbitMQService {

    private Logger logger = LoggerFactory.getLogger(RabbitsMQService.class);

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Autowired
    private MessageRepository repository;

    private final String queues_api_url = "http://localhost:15672/api/queues";

    public String publishMessageOnQueue( Long messageid, String queueName){

        Optional<Message> message = repository.findById(messageid);

        if (!message.isEmpty()){
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost"); // Adresse du serveur RabbitMQ

                // Publier un message dans la queue A
                try (Connection connection = factory.newConnection();
                     Channel channel = connection.createChannel()) {
                    channel.queueDeclare(queueName, false, false, false, null);
                    channel.basicPublish("", queueName, null, message.get().getContent().getBytes());
                    logger.info("Message publié dans la queue A : {}" , message.get().getContent());
                }
            }
            catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }

        return "message published!";
    }

    public String publishAllMessageOnQueue( String queueName){

        List<Message> messages = repository.findAll();

        if (!messages.isEmpty()){
           messages.forEach(
                   message -> {
                       try {
                           ConnectionFactory factory = new ConnectionFactory();
                           factory.setHost("localhost"); // Adresse du serveur RabbitMQ

                           // Publier un message dans la queue A
                           try (Connection connection = factory.newConnection();
                                Channel channel = connection.createChannel()) {
                               channel.queueDeclare(queueName, false, false, false, null);
                               channel.basicPublish("", queueName, null, message.getContent().getBytes());
                               logger.info("Message publié dans la queue A : {}" , message.getContent());
                           }
                       }
                       catch (IOException | TimeoutException e) {
                           e.printStackTrace();
                       }
                   }
           );
        }

        return "message published!";
    }
}
