package ru.relex.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.service.ConsumerService;
import ru.relex.service.MainService;
import ru.relex.service.ProducerService;

import static ru.relex.model.RabbitQueue.*;

@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {
    private final ProducerService producerService;
    private final MainService mainService;

    public ConsumerServiceImpl(ProducerService producerService,
                               MainService mainService){
        this.producerService = producerService;
        this.mainService = mainService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    //указываем очередь, которую метод будет слушать,
    //а потом "протолкнем" сообщение в брокер
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: Text message received");

        mainService.processTextMessage(update);
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
