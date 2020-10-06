package br.com.sample;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpScheduleService {

    public static void main(String[] args) throws Exception {
        var server = new Server(8080);

        var context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new NewScheduleServlet()), "/new");
        context.addServlet(new ServletHolder(new GenerateScheduleServlet()), "/generate");


        server.setHandler(context);

        server.start();
        server.join();
    }

}
