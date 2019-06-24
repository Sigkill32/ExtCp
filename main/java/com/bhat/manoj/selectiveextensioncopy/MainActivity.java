package com.bhat.manoj.selectiveextensioncopy;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText source,destination;
    String src,dest;
    ProgressBar progressBar;
    Thread thread;
    Handler handler = new Handler();
    int stat=0;
    long sSize,tSize=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.status);
        source = (EditText) findViewById(R.id.source);
        destination = (EditText) findViewById(R.id.destination);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void cpy(View view) {
        src = source.getText().toString();
        dest = destination.getText().toString();
        sSize = new File(src).length();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = new FileInputStream(src);
                    OutputStream outputStream = new FileOutputStream(dest);
                    int len;
                    byte[] buffer = new byte[1024];
                    while ((len = inputStream.read(buffer))>0) {
                        outputStream.write(buffer,0,len);
                        tSize+=len;
                        stat = (int) ((tSize*100)/sSize);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(""+tSize+" Bytes copied");
                                progressBar.setProgress(stat);
                            }
                        });
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void clr(View view) {
        progressBar.setProgress(0);
        source.setText("");
        destination.setText("");
        textView.setText("");
    }
}


