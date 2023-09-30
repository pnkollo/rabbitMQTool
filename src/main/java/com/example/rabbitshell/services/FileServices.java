
package com.example.rabbitshell.services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.TimeoutException;

@Service
public class FileServices {


    private Logger logger = LoggerFactory.getLogger(RabbitsMQService.class);

    /**
     *
     * @param queueName the name of the queue the message(s) should be sent
     * @param filePath  the (absolute) path of the file (.txt) from where the message(s) come (don't forget the double back slashs)
     * @return    the notification that the message has been sent
     */
    public String readFileInTextFormat(String queueName, String filePath) {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); // Adresse du serveur RabbitMQ

            // Publier un message dans la queue A
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                String message = readTextFile(filePath);
                channel.queueDeclare(queueName, false, false, false, null);
                channel.basicPublish("", queueName, null, message.getBytes());
                logger.info("Message publié dans la queue  : {}" , message);
            }
        }
        catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return "message published!";
    }

    /**
     *
     * @param queueName the name of the queue the message(s) should be sent
     * @param filePath  the (absolute) path of the file (.csv) from where the message(s) come (don't forget the double back slashs)
     * @return    the notification that the message has been sent
     */

    public String readFileInCsvFormat(String queueName, String filePath) {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); // Adresse du serveur RabbitMQ

            // Publier un message dans la queue A
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                String message = readFileInCsvFormat(filePath);
                channel.queueDeclare(queueName, false, false, false, null);
                channel.basicPublish("", queueName, null, message.getBytes());
                logger.info("Message publié dans la queue  : {}" , message);
            }
        }
        catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return "message published!";

    }

    public String readTextFile(String fileTextname) {
        String result = ""; // Remplacez ceci par le nom de votre fichier

        try (BufferedReader br = new BufferedReader(new FileReader(fileTextname))) {
            String ligne;

            while ((ligne = br.readLine()) != null) {
                System.out.println(ligne);
                result = result+ " " +ligne;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String readFileInCsvFormat( String filePath) {

        String result = "";
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

            for (CSVRecord record : csvParser) {
                // Vous pouvez accéder aux champs de chaque ligne ici
                String champ1 = record.get(0); // Le premier champ
                //String champ2 = record.get(1); // Le deuxième champ, etc.

                // Faites ce que vous voulez avec les champs
                System.out.println("Champ 1 : " + champ1);
                //System.out.println("Champ 2 : " + champ2);
                // ...

                result = result +" "+ champ1+ " " + " \n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param queueName the name of the queue the message(s) should be sent
     * @param filePath  the (absolute) path of the file (.csv) from where the message(s) come (don't forget the double back slashs)
     *                  the message(s) are sent individually in this case
     * @return    the notification that the message has been sent
     */
    public String readFileInCsvFormat2(String queueName, String filePath) {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); // Adresse du serveur RabbitMQ

            // Publier un message dans la queue A
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel(); Reader reader = new FileReader(filePath);
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT) ) {

                for (CSVRecord record : csvParser) {
                    // Vous pouvez accéder aux champs de chaque ligne ici
                    String message = record.get(0); // Le premier champ
                    //String champ2 = record.get(1); // Le deuxième champ, etc.

                    channel.queueDeclare(queueName, false, false, false, null);
                    channel.basicPublish("", queueName, null, message.getBytes());
                    logger.info("Message publié dans la queue  : {}" , message);
                }

            }
        }
        catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return "message published!";

    }
}
