package br.com.sample;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ReadingReportService {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) throws InterruptedException {

        var readingReportService = new ReadingReportService();
        try(var kafkaService = new KafkaService(ReadingReportService.class.getSimpleName(),
                "PACIENT_GENERATE_READING_REPORT",
                readingReportService::parse,
                Pacient.class,
                Map.of())) {
            kafkaService.run();
        }

    }

    private void parse(ConsumerRecord<String, Pacient> record) throws IOException {
        System.out.println("----------------------");
        System.out.println("processing report schedule for " + record.value());
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        var pacient = record.value();
        var target = new File(pacient.getReportPath());

        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + pacient.getUuid());

        System.out.println("File created: " + target.getAbsolutePath());

    }

}
