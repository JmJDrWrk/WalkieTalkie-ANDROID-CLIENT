package com.example.walkietalkie;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RECV_THREAD extends Thread {
    Context ct;
    public static String received = "mensajes:\n";
    //DataInputStream dtin;

    public RECV_THREAD(Context ct){
        this.ct = ct;

    }
    @Override
    public void run() {
        /*
        try{ct.errors.setText("readingUTF");}catch(Exception e){
            System.out.println("error on console");
        }*/
        while(true){
            try {
                String msg = MainActivity.in.readUTF();
                System.out.println("REcibido: " + msg);
                received = msg + "\n";
                try {
                    TextView txtView = (TextView) ((Activity) ct).findViewById(R.id.errort);
                    txtView.setText(txtView.getText() + "\n" + msg);
                }catch(Exception e){
                    System.out.println("Error setting console text");
                }
                //mn.changeValue(msg);
                //mn.errors.setText(mn.errors.getText().toString() + "\n" + msg);}

            } catch (IOException e) {
                e.printStackTrace();
            }/*
            try {
                //TimeUnit.SECONDS.sleep();
            } catch (InterruptedException e) {
                //MainActivity.errors.setText("Error on sleep");
                e.printStackTrace();
            }*/
        }
    }


}