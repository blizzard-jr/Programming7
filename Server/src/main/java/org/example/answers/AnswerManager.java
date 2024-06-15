package org.example.answers;

import org.island.commands.Message;
import org.island.details.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class AnswerManager {
    private OutputStream ou;
    private final Logger logger = LoggerFactory.getLogger(AnswerManager.class);

    public void flush(Message msg, Socket socket){
        try {
            ou = socket.getOutputStream();
        } catch (IOException e) {
            logger.error("Не получилось достать поток из клиента");
        }
        logger.info("Отправка ответа пользователю");
        byte[] data = Serialization.SerializeObject(msg);
        try {
            if(data != null){
                ou.write(data);
            }
        } catch (IOException e) {
            logger.error("Не получилось отправить сообщение клиенту");
        }
    }
}
