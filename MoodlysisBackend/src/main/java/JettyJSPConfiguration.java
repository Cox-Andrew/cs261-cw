package main.java;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
 
public class JettyJSPConfiguration {
 
    public static void main(String[] args) {
         
        Server server = new Server(8580);
        WebAppContext ctx = new WebAppContext();
        ctx.setResourceBase("src/main/webapp");
        ctx.setContextPath("/jettyjspconfiguration-example");
        server.setHandler(ctx);
        try {
            server.start();
            System.out.println("Hellooooo? Is this code being executed? Anyone??");
            server.join();
        } catch (Exception e) {         
            e.printStackTrace();
        }
    }
 
}