package com.example;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
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
                File file;
                if(path.equals(""))
                {
                    file = new File("html/index.html");
                }
                else
                {
                    file = new File("html/"+path);
                }


                // Lettura delle linee successive della richiesta
                while (!line.isEmpty()) 
                {
                    System.out.println("Linea ricevuta: " + line);
                    line = reader.readLine();
                }
                // Ricerca del file sul disco
                if (file.exists()) 
                {
                    sendBinaryFile(clientSocket, file);
                } 
                else 
                {
                    String mess="file non esiste";
                    System.out.println("File non trovato");

                    // Invio della risposta al client
                    out.writeBytes("HTTP/1.1 404 not found\n");
                    out.writeBytes("Content-lenght: "+mess.length()+"\n");
                    out.writeBytes("Content-type: text/plain\n");
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

    
    private static void sendBinaryFile(Socket socket, File file) throws IOException
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeBytes("HTTP/1.1 200 OK\n");
            out.writeBytes("Content-lenght: "+file.length()+"\n");

            //funzione per scegliere estensione
            out.writeBytes("Content-type:"+getContnentType(file)+"\n");

            out.writeBytes("\n");
            InputStream in = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int n;
            while((n=in.read(buffer))>0)
            {
                out.write(buffer,0,n);
            }
            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private static String getContnentType(File file) 
    {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        fileName= fileName.substring(dotIndex + 1);
        switch (fileName) 
        {
            case "html":
            {
                return "text/html";
            }
            case "jpeg":
            {
                return "image/jpeg";
            }
            case "jpg":
            {
                return "image/jpeg";
            }
            case "png":
            {
                return "image/png";
            }
            case "css":
            {
                return "text/css";
            }
            case "js":
            {
                return "text/javascript";
            }
            default:
            {
                return "text/plain";
            }
        }
    }

}