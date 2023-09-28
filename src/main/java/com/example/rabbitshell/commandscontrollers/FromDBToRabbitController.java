package com.example.rabbitshell.commandscontrollers;

import com.example.rabbitshell.services.FromDBToRabbitMQService;
import com.example.rabbitshell.services.RabbitsMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class FromDBToRabbitController {

    @Autowired
    private RabbitsMQService rabbitsMQService;

    @Autowired
    private FromDBToRabbitMQService service;

    @ShellMethod(key = "mqtool db push", value = "me message")
    public String publishMessageFromDBOnQueue(@ShellOption(value = "-i")Long id, @ShellOption(value = "-q") String queueName){
        return service.publishMessageOnQueue(id, queueName);
    }

    @ShellMethod(key = "mqtool db push -a", value = "me message")
    public String publishAllMessagesFromDBOnQueue( @ShellOption(value = "-q") String queueName){
        return service.publishAllMessageOnQueue(queueName);
    }

    //TODO
    @ShellMethod(key = "mqtool delete -a", value = "me message")
    public String deleteAllMessagesOnQueue( @ShellOption(value = "-q") String queueName){
        return service.publishAllMessageOnQueue(queueName);
    }
}
