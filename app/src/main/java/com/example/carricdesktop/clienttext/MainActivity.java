package com.example.carricdesktop.clienttext;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    public String user = "XX";
    public ClientThread commTh = new ClientThread();
    public WriterThread writeTh = new WriterThread();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("onCreate method called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        // Starting communicator thread
        commTh.textView = textView;
        commTh.writeTh = writeTh;
        commTh.start();

    }

    /** Called when the user touches the edit box to send a message */
    public void removeText(View view)
    {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText("");
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view)
    {

        try
        {
            EditText editText = (EditText) findViewById(R.id.editText);
            String message = editText.getText().toString();

            System.out.println(message);
            message = user + " : " + message;

            //Instruct the sender thread to send the message
            writeTh.writerLock.lock();
            writeTh.msgToSend = message;
            writeTh.writerCond.signalAll();
            writeTh.writerLock.unlock();

            message = "\n" + message;

            textView.append(message);

            //finally clear the EditText box once the message is sent and reset focus
            editText.setText("");
            hideKeyboard(this, view);
        }
        catch(Exception exp)
        {
            System.out.println("Caught exception: " + exp.toString());
        }


    }

    // Call this function to hide the on screen keyboard
    public static void hideKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}
