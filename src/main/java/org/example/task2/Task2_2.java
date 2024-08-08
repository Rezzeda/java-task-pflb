package org.example.task2;

import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Destination;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Task2_2 {
    public static void main(String[] args) {

        // Проверка наличия аргумента командной строки
        if (args.length == 0) {
            System.err.println("Необходимо указать путь к файлу с сообщениями в качестве аргумента командной строки.");
            return;
        }

        String filePath = args[0];

        List<String> messages = readMessagesFromFile(filePath);

        try (Connection connection = new ConnectionImpl()) {
            connection.start();

            try (Session session = connection.createSession(true)) {
                Destination destination = session.createDestination("Очередь");
                Producer producer = session.createProducer(destination);

                for (String message : messages) {
                    producer.send(message);
                    Thread.sleep(2000); // Задержка 2 секунды
                }
            } catch (DummyException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> readMessagesFromFile(String filePath) {
        List<String> messages = new ArrayList<>();
        File file = new File(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                messages.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }
}