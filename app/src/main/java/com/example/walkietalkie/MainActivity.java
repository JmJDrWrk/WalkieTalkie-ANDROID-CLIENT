package com.example.walkietalkie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errors = (TextView) findViewById(R.id.errort);

    }
    TextView errors ;
    //graphical items
        //Pseudo-Console
    //TextView errors = (TextView) findViewById(R.id.errort);

        //Msg from user
    //EditText msgtext = (EditText) findViewById(R.id.msgt);

    //server conn
    public String host;
    public int port;
    //conn channels
    public static DataInputStream in;
    public DataOutputStream out;
    //self client
    public MainActivity(){
        System.out.println("main activity object created");
    }
    public void reload(){

    }
    public void changeValue(String value){
        errors.setText(value);
    }
    public void makeCONN(View view) throws IOException {
        EditText porttext = (EditText) findViewById(R.id.portt);
        EditText hosttext = (EditText) findViewById(R.id.hostt);
        TextView errors = (TextView) findViewById(R.id.errort);

        host = hosttext.getText().toString();
        port = Integer.parseInt(porttext.getText().toString());
        try {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

            }
            System.out.println("Attempting connection at host " + host + " port " + port);
            errors.setText("Attempting connection at host " + host + " port " + port);




            //Creo el socket para conectarme con el cliente
            Socket sc = new Socket(host, 6112);

            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            //Verifico la conexion
           errors.setText("Conexión sin errores");

            //Enciendo el proceso de recepcion

            RECV_THREAD receiver = new RECV_THREAD(errors.getContext());
            receiver.start();

            //String msg = in.readUTF();
            //errors.setText(errors.getText() + "\n" + msg);


            //Reload content
            //Reload rel = new Reload(view);
           // rel.start();

        }catch(Exception e){
            e.printStackTrace();

            //Pasar excepcion a String
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();

            //pasar a consola
           // errors.setText("Error de conexión\n" + exceptionAsString);
        }
    }

    public void makeSEND(View view) throws IOException {
        try {
            System.out.println("SENDING MESSAGE");
            EditText msgtext = (EditText) findViewById(R.id.msgt);
            String msgstr = msgtext.getText().toString();

            byte[] msg = msgstr.getBytes(StandardCharsets.UTF_8);
            out.writeUTF(msgstr);
            msgtext.setText("");
            Snackbar mySnackbar = Snackbar.make(view, "Mensaje enviado", 3000);
            mySnackbar.show();
        }catch (Exception e){e.printStackTrace();}
    }

    public void makeCLEAR(View view) {
        errors.setText("");
    }

    //TextView errors = (TextView) findViewById(R.id.errort);





/*
    public void makecall2(View view) {

        try {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

            }
            // code block
            //Obtener valores de los campos

            EditText msgtext = (EditText) findViewById(R.id.msgt);
            EditText hosttext = (EditText) findViewById(R.id.hostt);
            EditText porttext = (EditText) findViewById(R.id.portt);


            String host = hosttext.getText().toString();
            String port = porttext.getText().toString();
            String msgstr = msgtext.getText().toString();

            //Intentando crear la conexión con el servidor
            System.out.println("Attempting connection at host " + host + " port " + port);

            //Creando la conexión con el servidor
            //final String HOST = "127.0.0.1";
            //Puerto del servidor
            //final int PUERTO = 5000;
            DataInputStream in;
            DataOutputStream out;


            //Creo el socket para conectarme con el cliente
            Socket sc = new Socket(host, Integer.parseInt(port));

            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            byte[] msg = msgstr.getBytes(StandardCharsets.UTF_8);
            out.write(msg);

            System.out.println("Mensaje enviado");
            //TextView errors = (TextView) findViewById(R.id.error);
            //errors.setText("El mensaje se ha enviado con éxito");

            Snackbar mySnackbar = Snackbar.make(view, "Mensaje enviado", 3000);
            mySnackbar.show();

        } catch (Exception e) {
            System.out.println("Error!!!");
            e.printStackTrace();

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();


            errors.setText(exceptionAsString);
            //Error snack bar
            Snackbar mySnackbar = Snackbar.make(view, "ERROR mensaje NO enviado", 3000);
            mySnackbar.show();
        }

    }
*/

}

class Reload extends Thread{
    View view;
    MainActivity  mn;
    public Reload(View view){
        this.view = view;
        this.mn = new MainActivity();
    }
    @Override
    public void run() {

        try {
            mn.errors.setText(RECV_THREAD.received);
            TimeUnit.SECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(".");
    }
}