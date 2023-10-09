package com.example.rabbitshell.commandscontrollers;

import com.example.rabbitshell.services.RabbitsMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@ShellComponent
public class RabbitMqCommandsController {

    @Autowired
    private RabbitsMQService rabbitsMQService;

    @ShellMethod(key = "mqtool push", value = "me message")
    public String publishMessageOnQueue(@ShellOption(value = "-m") String message, @ShellOption(value = "-q") String queueName){
        return rabbitsMQService.publishMessageOnQueue(message, queueName);
    }

    @ShellMethod(key = "mqtool create", value = "pat")
    public String createQueue( @ShellOption(value = "-q") String queueName){
        return rabbitsMQService.createQueue(queueName);
    }

    @ShellMethod(key = "mqtool move-message", value = "me message")
    public String movingFromSrcQueueToTargetQueue(@ShellOption(value = "-s") String srcQueue, @ShellOption(value = "-t") String targetQueue){
        return rabbitsMQService.movingFromSrcQueueToTargetQueue(srcQueue, targetQueue);
    }

    @ShellMethod(key = "mqtool -ls", value = "me message")
    public ResponseEntity<List> getAllQueueNames() throws IOException, JSONException, URISyntaxException, InterruptedException {
        return rabbitsMQService.getAllQueueNames();
    }

    @ShellMethod(key = "mqtool consume", value = "me message")
    public void movingFromSrcQueueToTargetQueue(@ShellOption(value = "-q") String srcQueue){
         rabbitsMQService.consumeFromParticularQueue(srcQueue);
    }

    @ShellMethod(key = "mqtool move", value = "me message")
    public String moveMessageWithRegexFromOneQueueToAnother(@ShellOption(value = "-s") String srcQueue, @ShellOption(value = "-t")
    String targetQueue, @ShellOption(value = "-re") String regex) throws Exception {
        return rabbitsMQService.moveMessageWithRegexFromOneQueueToAnother( srcQueue, targetQueue, regex);
    }
}
