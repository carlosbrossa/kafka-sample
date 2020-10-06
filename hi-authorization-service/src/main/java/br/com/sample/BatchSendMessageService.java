package br.com.sample;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BatchSendMessageService {

    private final Connection connection;

    BatchSendMessageService() throws SQLException {
        String url = "jdbc:sqlite:authorizations_database.db";
        this.connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("create table Authorization ( " +
                    "uuid varchar(200) primary key," +
                    "exam varchar(200))");
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException, SQLException {

        var batchSendMessageService = new BatchSendMessageService();
        try(var kafkaService = new KafkaService(BatchSendMessageService.class.getSimpleName(),
                "SEND_MESSAGE_TO_ALL_PACIENTS",
                batchSendMessageService::parse,
                String.class,
                Map.of())) {
            kafkaService.run();
        }

    }

    private final KafkaDispatcher batchDispatcher = new KafkaDispatcher<Pacient>();


    private void parse(ConsumerRecord<String, String> record) throws InterruptedException, ExecutionException, SQLException {
        System.out.println("----------------------");
        System.out.println("processing new batch");
        System.out.println("Topic: " + record.value());

        for(Pacient pacient : getAllPacients()){
            batchDispatcher.send(record.value(), pacient.getUuid(), pacient);
        }


    }

    private List<Pacient> getAllPacients() throws SQLException {
        ResultSet resultSet = connection.prepareStatement("select uuid from Authorization").executeQuery();
        List<Pacient> pacientList = new ArrayList<Pacient>();
        while (resultSet.next()){
            pacientList.add(new Pacient(resultSet.getString(1)));
        }
        return pacientList;
    }

}
