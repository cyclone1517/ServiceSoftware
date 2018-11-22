package team.hnuwt.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Send implements Runnable {
    private String pkg;
    private Socket socket;
    
    public Send(String pkg, Socket socket)
    {
        this.pkg = pkg;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            
            
            printWriter.write(pkg);
            printWriter.flush();
            
            printWriter.close();
            outputStream.close();
            inputStream.close();
            socket.close();
            
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    

}
