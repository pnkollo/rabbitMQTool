package com.example.rabbitshell.commandscontrollers;

import com.example.rabbitshell.services.HelpOptionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class HelpOptionsControllers {


    private static final Logger logger = LoggerFactory.getLogger(HelpOptionsControllers.class);
    @Autowired
    private HelpOptionsService service;

    @ShellMethod(key = "mqtool -help")
    public String getHelpOptions(){
        return service.getHelpOptions();
    }
}
