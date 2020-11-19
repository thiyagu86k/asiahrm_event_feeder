package com.fiable.hellorest;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
/**
 *
 * @author Thiyagaraja Kalidoss
 */
public class App {
    public static Properties prop=null;
    static{
        prop=new Properties();
        try {
            prop.load(new FileReader("./config/asiahrmevents.prop"));
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath(prop.getProperty("ContextPath"));
        int portNo=Integer.parseInt(prop.getProperty("Port"));
        Server jettyServer = new Server(portNo);
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
       // jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", RestEx.class.getCanonicalName());
       jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.fiable.service");
        
        try {
            jettyServer.start();
            // jettyServer.dumpStdErr();
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                jettyServer.stop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

}
