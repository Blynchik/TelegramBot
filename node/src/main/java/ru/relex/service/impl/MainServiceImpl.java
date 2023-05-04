package ru.relex.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.RawDataDAO;
import ru.relex.entity.RawData;
import ru.relex.service.MainService;
import ru.relex.service.ProducerService;

@Service
public class MainServiceImpl implements MainService {

    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;

    public MainServiceImpl(RawDataDAO rawDataDAO,
                           ProducerService producerService) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        //тестовая отправка сообщения в брокер
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        //получим id чата из сообщения
        //и отправим по нему свое сообщение
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello from  NODE");
        producerService.produceAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        //к классу применили @Builder из Lombok
        RawData rawData = RawData.builder()
                .event(update)
                .build();

        rawDataDAO.save(rawData);
    }
}
