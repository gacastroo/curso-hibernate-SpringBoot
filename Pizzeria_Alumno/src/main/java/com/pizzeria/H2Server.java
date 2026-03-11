package com.pizzeria;

import org.h2.tools.Server;

public class H2Server {

    public static void main(String[] args) {

        boolean enableH2 = false;
        for (String arg : args) {
            if ("--enable-h2".equalsIgnoreCase(arg)) {
                enableH2 = true;
                break;
            }
        }

        Server webServer = null;

        if (enableH2) {
            try {
                // Inicia la consola web de H2
                webServer = Server.createWebServer(
                        "-web",
                        "-webPort", "8082",
                        "-tcpAllowOthers" // CORRECCIÓN
                ).start();

                System.out.println("=================================");
                System.out.println("H2 Console iniciada");
                System.out.println("Abrir en navegador:");
                System.out.println("http://localhost:8082");
                System.out.println("=================================");

                // Hook para detener la consola al cerrar la app
                final Server finalWebServer = webServer;
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    System.out.println("Deteniendo H2 Console...");
                    finalWebServer.stop();
                }));

            } catch (Exception e) {
                System.err.println("Error iniciando H2: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("H2 Console no habilitada. Usar --enable-h2 para activarla.");
        }

        // Ejecutar la aplicación principal
        PizzeriaApp.main(args);
    }
}