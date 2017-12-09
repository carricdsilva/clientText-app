package com.example.carricdesktop.clienttext;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by CarricDesktop
 */

public class ClientThread extends Thread
{
    public int portNumber = 10000;
    public Socket clientSocket1;
    BufferedReader in;
    BufferedWriter out;
    WriterThread writeTh;
    TextView textView;

    public void run()
    {
        System.out.println("Communicator thread started...");

        try
        {
            clientSocket1 = new Socket("localhost", portNumber);
            in  = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));

            writeTh.clientSocket1 = clientSocket1;
            writeTh.start();

            while(true)
            {
                System.out.println("Waiting for message");
                String msg = in.readLine();
                if(null != msg)
                {
                    System.out.println("Got message from remote: length[" + msg.length() + "] msg: [" + msg + "]");
                    msg = "\n" + msg;
                    textView.append(msg);
                }
                else
                {
                    //Communication closed
                    System.out.println("Communication terminated. Cleaning up sockets");
                    in.close();
                    clientSocket1.close();
                    break;
                }
            }

        }
        catch(Exception exp)
        {
            System.out.println("Caught exception: " + exp.toString());
        }

        writeTh.writerLock.lock();
        writeTh.toExit = true;
        writeTh.writerCond.signalAll();
        writeTh.writerLock.unlock();

        System.out.println("Exiting Communicator/Reader Thread");
}
}
