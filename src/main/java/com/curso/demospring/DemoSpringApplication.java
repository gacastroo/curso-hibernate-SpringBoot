package com.curso.demospring;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.h2.tools.Server;


@SpringBootApplication
public class DemoSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringApplication.class, args);
    }

    /**
     * Levanta un servidor TCP de H2 en el puerto 9092
     * para conectarse desde DBeaver o cualquier cliente SQL.
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws Exception {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
}
