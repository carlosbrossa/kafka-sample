package br.com.sample;

import org.eclipse.jetty.servlet.Source;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class GenerateScheduleServlet extends HttpServlet {


    private final KafkaDispatcher batchDispatcher = new KafkaDispatcher<String>();

    @Override
    public void destroy() {
        super.destroy();
        batchDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            batchDispatcher.send(
                    "SCHEDULE_SEND_MESSAGE_TO_ALL_PACIENTS",
                    "SCHEDULE_PACIENT_GENERATE_READING_REPORT",
                     new CorrelationId(GenerateScheduleServlet.class.getSimpleName()),
                    "SCHEDULE_PACIENT_GENERATE_READING_REPORT");

            System.out.println("Sent generate report to all pacients");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Exam scheduled");

        } catch (ExecutionException e) {
            throw new ServletException();
        } catch (InterruptedException e) {
            throw new ServletException();
        }
    }

}
