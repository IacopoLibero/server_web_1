package com.example;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) 
    {
        try 
        {
            // Creazione del socket del server sulla porta 3000
            ServerSocket serverSocket = new ServerSocket(3000);
            System.out.println("Server in avvio...");

            while (true) 
            {
                // Accettazione della connessione del client
                Socket clientSocket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                System.out.println("Client connesso: ");

                // Lettura della richiesta inviata dal client
                String line= reader.readLine();
                String[] requestParts = line.split(" ");
                String path =requestParts[1].substring(1);
                File file = new File(path);

                // Lettura delle linee successive della richiesta
                while (!line.isEmpty()) 
                {
                    System.out.println("Linea ricevuta: " + line);
                    line = reader.readLine();
                }

                // Ricerca del file sul disco
                if (file.exists()) 
                {
                    String mess="file esiste";
                    System.out.println("File trovato");

                    // Invio della risposta al client
                    out.writeBytes("HTTP/1.1 200 OK\n");
                    out.writeBytes("Content-lenght: "+mess.length()+"\n");
                    out.writeBytes("\n");
                    out.writeBytes(mess);
                } 
                else 
                {
                    String mess="file non esiste";
                    System.out.println("File non trovato");

                    // Invio della risposta al client
                    out.writeBytes("HTTP/1.1 404 not found\n");
                    out.writeBytes("Content-lenght: "+mess.length()+"\n");
                    out.writeBytes("\n");
                    out.writeBytes(mess);
                }

                // Chiusura della connessione con il client
                clientSocket.close();
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
