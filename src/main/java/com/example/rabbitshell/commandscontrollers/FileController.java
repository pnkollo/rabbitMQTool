

package com.example.rabbitshell.commandscontrollers;

import com.example.rabbitshell.services.FileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class FileController {

    @Autowired
    private FileServices fileServices;
    // mqtool push --source-queue <source_queue_name>[--exhcange <exchange_name>] --number <number_of_msgs> --file-location= </filepath/file.txt>

    @ShellMethod(key = "mqtool -text push" ,  value = "me message")
    public String readFileInTextFormat(@ShellOption("-q") String queueName, @ShellOption("-p") String filePath) throws Exception {
        return fileServices.readFileInTextFormat(queueName, filePath);
    }
    @ShellMethod(key = "mqtool -csv push" ,  value = "me message")
    public void readFileInCsvFormat(@ShellOption("-q") String queueName, @ShellOption("-p") String filePath) throws Exception {
        fileServices.readFileInCsvFormat(queueName, filePath);
    }

}


