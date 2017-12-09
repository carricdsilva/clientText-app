package com.example.carricdesktop.clienttext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by CarricDesktop
 */

public class WriterThread extends Thread
{
    public Socket clientSocket1 = null;
    public boolean       toExit = false;
    public Lock writerLock = new ReentrantLock();
    Condition writerCond = writerLock.newCondition();
    String            msgToSend = "";

    public void run()
    {
        System.out.println("Writer thread started.");
        try
        {
            if(null == clientSocket1)
            {
                System.out.println("Unexpected: socket is null");
            }

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket1.getOutputStream()));

            while(true)
            {
                writerLock.lock();
                writerCond.await();

                if(true == toExit)
                {
                    writerLock.unlock();
                    break;
                }

                // Send the message
                System.out.println("Sending to remote: length[" + msgToSend.length() + "] msg: [" + msgToSend + "]");
                out.write(msgToSend);
                out.newLine();
                out.flush();
                writerLock.unlock();

            }

        }
        catch(Exception exp)
        {
            System.out.println("Caught exception: " + exp.toString());
        }

        System.out.println("Exiting Writer Thread");
    }
}
