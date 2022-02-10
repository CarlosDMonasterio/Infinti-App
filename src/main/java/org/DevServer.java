package org;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.predicate.PredicatesHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.builder.PredicatedHandlersParser;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.util.HttpString;
import org.glassfish.jersey.servlet.ServletContainer;
import org.ih.servlet.IHServletContextListener;

/**
 * Embedded (Undertow) server for development. Uses the settings from <code>web.xml</code>
 *
 * @author Hector Plahar
 */
public class DevServer {

    public static void main(String[] args) throws Exception {
        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(ClassLoader.getSystemClassLoader())
                .addListener(Servlets.listener(IHServletContextListener.class))
                .setContextPath("/")
                .setDeploymentName("Infinity Health Nurse Data Management System")
//                .setResourceManager(new FileResourceManager(new File("src/main/webapp"), 10)) //.addWelcomePage("index.htm")
                .addServlets(
                        Servlets.servlet("Jersey REST Servlet", ServletContainer.class)
                                .addInitParam("jersey.config.server.provider.packages", "org.ih.service.rest")
                                .addInitParam("jersey.config.server.provider.scanning.recursive", "false")
                                .addInitParam("javax.ws.rs.Application", "org.ih.service.rest.multipart.InfinitiHealthApplication")
                                .setAsyncSupported(true)
                                .setEnabled(true)
                                .addMapping("/rest/*")
                );

        // deploy servlet
        DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();
        HttpHandler servletHandler = manager.start();

        PredicatesHandler handler = Handlers.predicates(PredicatedHandlersParser.parse(
                "path-prefix('districts') or " +
                        "path-prefix('forgotPassword') or " +
                        "path-prefix('patients') or " +
                        "path-prefix('schools') or " +
                        "path-prefix('incidents') or " +
                        "path-prefix('login') or " +
                        "path-prefix('users') or " +
                        "path-prefix('files') or " +
                        "path-prefix('passes') or " +
                        "path-prefix('reports') or " +
                        "path-prefix('admin') and regex('/.+') -> rewrite('/')",
                ClassLoader.getSystemClassLoader()), servletHandler);

        PathHandler path = Handlers.path(Handlers.redirect("/"))
                .addPrefixPath("/", handler);

        Undertow server = Undertow.builder()
                .addHttpListener(8081, "localhost")
                .setHandler(exchange -> {
                    exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Origin"), "*");
                    exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Methods"), "GET,PUT,POST,DELETE,OPTIONS");
                    exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Headers"), "Content-Type, X-IH-Authentication-SessionId");

                    path.handleRequest(exchange);
                })
                .build();
        server.start();
    }
}
