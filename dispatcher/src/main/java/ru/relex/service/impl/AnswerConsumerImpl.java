package ru.relex.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.relex.controller.UpdateProcessor;
import ru.relex.service.AnswerConsumer;

import static ru.relex.model.RabbitQueue.ANSWER_MESSAGE;

@Service
@Log4j
public class AnswerConsumerImpl implements AnswerConsumer {

    private final UpdateProcessor updateProcessor;

    public AnswerConsumerImpl(UpdateProcessor updateProcessor){
        this.updateProcessor = updateProcessor;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateProcessor.setView(sendMessage);
    }
}
