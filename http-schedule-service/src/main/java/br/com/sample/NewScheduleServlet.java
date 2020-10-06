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

public class NewScheduleServlet extends HttpServlet {

    private final KafkaDispatcher scheduleKafkaDispatcher = new KafkaDispatcher<Schedule>();
    private final KafkaDispatcher emailKafkaDispatcher = new KafkaDispatcher<Email>();

    @Override
    public void destroy() {
        super.destroy();
        scheduleKafkaDispatcher.close();
        emailKafkaDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String userId = UUID.randomUUID().toString();
            String scheduleId = UUID.randomUUID().toString();

            var email = req.getParameter("email");
            var exam = req.getParameter("exam");
            var schedule = new Schedule(userId, scheduleId, exam, email);

            scheduleKafkaDispatcher.send("SCHEDULE", email, schedule);

            var emailCode = new Email(email,"Your exam is scheduled");
            emailKafkaDispatcher.send("SCHEDULE_SEND_EMAIL", email, emailCode);

            resp.getWriter().println("Exam scheduled");

        } catch (ExecutionException e) {
            throw new ServletException();
        } catch (InterruptedException e) {
            throw new ServletException();
        }
    }

}
