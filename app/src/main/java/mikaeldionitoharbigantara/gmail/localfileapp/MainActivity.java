package mikaeldionitoharbigantara.gmail.localfileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    EditText nama, telepon;
    ListView listView;
    Button tombolInput;
    ArrayAdapter <String> adapter;
    ArrayList <String> arrayKontak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //penghubung antara objek dalam java dengan layout
        nama = (EditText) findViewById(R.id.editNama);
        telepon = (EditText) findViewById(R.id.editTelepon);
        listView = (ListView) findViewById(R.id.listView);
        tombolInput = (Button) findViewById(R.id.buttonInput);
        arrayKontak = new ArrayList<String>();

        //event pada tombol
        tombolInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] bufferNama = new byte[30];
                byte[] bufferTelepon = new byte[15];

                salinData(bufferNama, nama.getText().toString());
                salinData(bufferTelepon, telepon.getText().toString());

                try {
                    FileOutputStream dataFile = openFileOutput("telepon.dat", MODE_APPEND);
                    DataOutputStream output = new DataOutputStream(dataFile);

                    output.write(bufferNama);
                    output.write(bufferTelepon);

                    dataFile.close();

                    Toast.makeText(getBaseContext(), "Data telah disimpan",
                            Toast.LENGTH_LONG).show();

                    nama.setText("");
                    telepon.setText("");
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Kesalahan: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                tampilkanData();
            }
        });
        tampilkanData();
    }

    public void salinData(byte[] buffer, String data) {
        for (int i = 0; i < buffer.length; i++)
            buffer[i] = 0;
        for (int i = 0; i < data.length(); i++)
            buffer[i] = (byte) data.charAt(i);
    }

    public void tampilkanData() {
        try {
            FileInputStream dataFile = openFileInput("telepon.dat");
            DataInputStream input = new DataInputStream(dataFile);

            byte[] bufNama = new byte[30];
            byte[] bufTelepon = new byte[15];
            String infoData = "Data Telepon : \n";
            int no = 1;
            arrayKontak.clear();

            while (input.available() > 0) {
                input.read(bufNama);
                input.read(bufTelepon);

                String dataNama = "";
                for (int i = 0; i < bufNama.length; i++)
                    dataNama = dataNama + (char) bufNama[i];
                String dataTelepon = "";
                for (int i = 0; i < bufTelepon.length; i++)
                    dataTelepon = dataTelepon + (char) bufTelepon[i];

                infoData = no + ". " + dataNama + " - " + dataTelepon;
                arrayKontak.add(infoData);
                no++;
            }
            //adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,infoData);
            //listView.setAdapter(adapter);

            dataFile.close();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Kesalahan: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayKontak);
        listView.setAdapter(adapter);
    }
}
