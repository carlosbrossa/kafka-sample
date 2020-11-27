package br.com.sample;

import br.com.sample.consumer.ConsumerService;
import br.com.sample.consumer.KafkaService;
import br.com.sample.consumer.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ReadingReportService implements ConsumerService<Pacient> {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new ServiceRunner(ReadingReportService::new).start(5);

    }

    public void parse(ConsumerRecord<String, Message<Pacient>> record)  {
        System.out.println("----------------------");
        System.out.println("processing report schedule for " + record.value());
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        var message = record.value();
        var patient = message.getPayload();
        var target = new File(patient.getReportPath());

        try {
            IO.copyTo(SOURCE, target);
            IO.append(target, "Created for " + patient.getUuid());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File created: " + target.getAbsolutePath());

    }

    @Override
    public String getTopic() {
        return "SCHEDULE_PACIENT_GENERATE_READING_REPORT";
    }

    @Override
    public String getConsumerGroup() {
        return ReadingReportService.class.getSimpleName();
    }

}
