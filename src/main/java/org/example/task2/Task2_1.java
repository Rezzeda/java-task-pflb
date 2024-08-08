package org.example.task2;

import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Destination;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Task2_1 {
    public static void main(String[] args) {

        List<String> messages = readMessagesFromFile("src/main/java/org/example/task2/messages.dat");

//        for (String message : messages) {
//            System.out.println(message);
//        }

        try (Connection connection = new ConnectionImpl()) {
            connection.start();

            try (Session session = connection.createSession(true)) {
                Destination destination = session.createDestination("Очередь");
                Producer producer = session.createProducer(destination);
                // отправка сообщений
                for (String message : messages) {
                    producer.send(message);
                    Thread.sleep(2000);
                }
            } catch (DummyException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> readMessagesFromFile(String filePath) {
        List<String> messages = new ArrayList<>();
        File file = new File(filePath);

        // Проверяем существует ли файл
        if (!file.exists()) {
            System.err.println("Файл не найден: " + file.getAbsolutePath());
            return messages;
        }
        // читаем файл построчно и добавляем строки в список
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