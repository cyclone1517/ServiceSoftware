package team.hnuwt.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReactorTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        try {
            Socket socket = new Socket("115.157.192.49", 8080);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            
            for (int i = 0; i < 100; i++)
            {
                printWriter.write("AA00002B0002050202A28DB910"
                        + reverse(String.format("%08x", i))
                        + "72060000000000000000000011071C00570100000000000000000020E837003CFF");
                printWriter.flush();
            }
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
    
    private String reverse(String s)
    {
        StringBuilder sb = new StringBuilder(s);
        StringBuilder result = new StringBuilder();
        for (int i = sb.length() - 1; i > 0; i -= 2)
        {
            result.append(sb.substring(i - 1, i + 1));
        }
        return result.toString();
    }

}
