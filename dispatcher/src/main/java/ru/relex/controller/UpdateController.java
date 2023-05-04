package ru.relex.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.service.AnswerConsumer;
import ru.relex.service.UpdateProducer;
import ru.relex.util.MessageUtils;

import static ru.relex.model.RabbitQueue.*;

@Component
@Log4j
public class UpdateController {

    private TelegramBot telegramBot;
    private MessageUtils messageUtils;
    private final UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils,
                            UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update){

        // если сообщение пустое, то записываем в лог и ничего не делаем
        if(update == null){
            log.error("Received update is null");
            return;
        }

        //если сообщения разных типов(сообщения приватных чатов, отредактированные сообщения
        //сообщения из каналов или групп и др.)
        //бдуем обрабатывать только первичные неизмененные сообщения
        //если приходит иной тип сообщения, то записываем в лог
        if(update.getMessage() != null){
            distributeMessagesByType(update);
        } else {
            log.error("Unsupported message type received" + update);
        }
    }

    //этот метод распределяет принятые сообщения, прошедшие первую проверку
    //по очередям брокера RabbitMQ
    //будь то текст, картинка или документ
    //иначе сообщение, что формат не поддерживается
    private void distributeMessagesByType(Update update) {
        var message = update.getMessage();

        if(message.getText()!=null){
            processTextMessage(update);
        } else if (message.getDocument()!=null){
            processDocMessage(update);
        } else if (message.getPhoto() != null){
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        //т.к. обраотка больших файлов может занять
        //много времени, чтобы пользователь не переживал
        //сообщим ему, что контент в обработке
        setFileReceivedView(update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileReceivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void setFileReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Файл получен! Обрабатывается...");
        setView(sendMessage);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Неподдерживаемы тип сообщения!");
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }
}
