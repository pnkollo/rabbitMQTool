package com.example.rabbitshell.services;

import org.springframework.stereotype.Service;

@Service
public class HelpOptionsService {

    public String getHelpOptions(){
        return "the most popular commands are \n" +
                "mqtool        as the key word to beginn a command \n" +
                "push          to send a message on a particular queue on rabbitMQ \n" +
                "-m            to precise the content of the message" +
                "-q            to precise the name of the queue \n" +
                "pull          to consume a message from one particular queue \n" +
                "create        to create a particular queue \n" +
                "move-message  to move a particular message of one queue \n" +
                "-s            for the source queue \n" +
                "-t            for the target queue \n" +
                "-ls           to have the list of names of all the queues on rabbitMQ \n" +
                "consume       to consume a message";
    }
}
