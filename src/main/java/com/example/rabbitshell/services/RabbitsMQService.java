package com.example.rabbitshell.services;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RabbitsMQService {

    private Logger logger = LoggerFactory.getLogger(RabbitsMQService.class);

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    private final String queues_api_url = "http://localhost:15672/api/queues";

    public String publishMessageOnQueue( String message, String queueName){

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); // Adresse du serveur RabbitMQ

            // Publier un message dans la queue A
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(queueName, false, false, false, null);
                channel.basicPublish("", queueName, null, message.getBytes());
                logger.info("Message publié dans la queue A : {}" , message);
            }
        }
        catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return "message published!";
    }

    public String createQueue( String queueName){

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); // Adresse du serveur RabbitMQ

            // Publier un message dans la queue A
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(queueName, false, false, false, null);
                logger.info("Queue {} created on the rabbitMQ server", queueName);
            }
        }
        catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return "new queue created !";
    }

    public String movingFromSrcQueueToTargetQueue( String srcQueue, String targetQueue){

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); // Adresse du serveur RabbitMQ


            // Transférer un message de la queue A à la queue B
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(targetQueue, false, false, false, null);

                // Obtenir le message de la queue A
                GetResponse response = channel.basicGet(srcQueue, false);
                if (response != null) {
                    String message = new String(response.getBody());

                    // Publier le message dans la queue B
                    channel.basicPublish("", targetQueue, null, message.getBytes());
                    logger.info("message moved from queue {}, to queue {} !", srcQueue, targetQueue);

                    // Acknowledge (marquer comme consommé) le message dans la queue A
                    channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
                }
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return "message moved !";
    }

    public ResponseEntity<List> getAllQueueNames() throws IOException, InterruptedException, URISyntaxException, JSONException {
        HttpClient client = HttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password.toCharArray());
                    }
                }).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(queues_api_url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray queues = new JSONArray(response.body());
        List<String> queueNames=new ArrayList<>();
        for( int i=0;i<queues.length();i++){
            queueNames.add(queues.getJSONObject(i).getString("name"));
        }
        return ResponseEntity.ok(queueNames);
    }


    public void consumeFromParticularQueue(String queueName) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Adresse du serveur RabbitMQ

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(queueName, false, false, false, null);

            System.out.println("Waiting for messages... To exit, press CTRL+X then CTRL+CTRL+V");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Message received : " + message);
                logger.info("consumed message :  ->  {}", message);
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }


    public String moveMessageWithRegexFromOneQueueToAnother(String srcQueue, String targetQueue, String regex) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(srcQueue, false, false, false, null);
            channel.queueDeclare(targetQueue, false, false, false, null);

            // Déclaration d'un consommateur pour la queue source
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");

                // Vérifie si le message correspond à l'expression régulière
                if (matchesRegex(regex, message)) {
                    // Si oui, envoie le message à la queue de destination
                    channel.basicPublish("pat", targetQueue, null, message.getBytes("UTF-8"));
                    System.out.println("Message moved! : " + message);
                }
            };

            // Commence à consommer des messages de la queue source
            channel.basicConsume(srcQueue, true, deliverCallback, consumerTag -> {
            });

            // Maintenant, le programme attendra des messages de la queue source
            System.out.println("En attente de messages. Appuyez sur Ctrl+C pour quitter.");
            Thread.sleep(Long.MAX_VALUE);
        }
        return "message with regex moved!";
    }

    private static boolean matchesRegex(String regex, String message) {
        // Utilisez l'expression régulière fournie pour vérifier si le message correspond
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        return matcher.matches();
    }

}
