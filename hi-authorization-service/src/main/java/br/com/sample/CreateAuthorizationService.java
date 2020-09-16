package br.com.sample;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateAuthorizationService {


    private final Connection connection;

    CreateAuthorizationService() throws SQLException {
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

        var createAuthorizationService = new CreateAuthorizationService();
        try(var kafkaService = new KafkaService(CreateAuthorizationService.class.getSimpleName(),
                "SCHEDULE",
                createAuthorizationService::parse,
                Schedule.class,
                Map.of())) {
            kafkaService.run();
        }

    }

    private void parse(ConsumerRecord<String, Schedule> record) throws InterruptedException, ExecutionException, SQLException {
        System.out.println("----------------------");
        System.out.println("processing new schedule");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        Thread.sleep(3000);

        var number = (Math.random() * (1000 - 1)) + 1;

        if(haveAuthorization(number)){
            System.out.println("scheduled with authorization");
            insertAuthorization(record.value());
        }else{
            System.out.println("discard schedule for authorization");
        }

    }

    private boolean haveAuthorization(double number) {
        return number > 500;
    }

    private void insertAuthorization(Schedule schedule) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("insert into Authorization (uuid, exam) " +
                "values (?, ?)");

        preparedStatement.setString(1, schedule.getUserId());
        preparedStatement.setString(2, schedule.getExamCode());

        preparedStatement.execute();

    }

}
