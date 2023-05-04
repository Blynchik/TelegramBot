package ru.relex.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.service.ConsumerService;
import ru.relex.service.ProducerService;

import static ru.relex.model.RabbitQueue.*;

@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {
    private final ProducerService producerService;

    public ConsumerServiceImpl(ProducerService producerService){
        this.producerService = producerService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    //указываем очередь, которую метод будет слушать,
    //а потом "протолкнем" сообщение в брокер
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: Text message received");

        //тестовая отправка сообщения в брокер
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        //получим id чата из сообщения
        //и отправим по нему свое сообщение
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello from  NODE");
        producerService.produceAnswer(sendMessage);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdates(Update update) {
        log.debug("NODE: Doc message received");
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdates(Update update) {
        log.debug("NODE: Photo message received");

    }
}