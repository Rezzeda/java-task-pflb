package org.example.task1;

import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Destination;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // создаем списка сообщений
        List<String> messages = Arrays.asList("Четыре", "Пять", "Шесть");
        // создаем и запускаем соединения
        try (Connection connection = new ConnectionImpl()) {
            connection.start();

            // создаем сессии
            try (Session session = connection.createSession(true)) {
                // создаем очередь
                Destination destination = session.createDestination("Очередь");
                // создаем продюсер
                Producer producer = session.createProducer(destination);

                // проходим по списку сообщений и отправляем
                Iterator<String> iterator = messages.iterator();
                while (iterator.hasNext()) {
                    String message = iterator.next();
                    producer.send(message);
                    Thread.sleep(2000); // задержка 2 секунды
                }
            } catch (DummyException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}