package com.example.personal.databasepgas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BuatBiodataActivity extends AppCompatActivity {
    protected Cursor cursor;
    DataHelper dbHelper;
    Button ton1, ton2;
    EditText text1, edtIP, editGedung;
    ListView listPing;
    private CountDownTimer countDownTimer;
    private final long startTime = 10 * 1000;
    private final long interval = 1 * 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_biodata);

        dbHelper = new DataHelper(this);

        text1 = (EditText) findViewById(R.id.editText1);
        editGedung = (EditText) findViewById(R.id.editText2);
        edtIP = (EditText) findViewById(R.id.ip);
        listPing = (ListView) findViewById(R.id.listbuat);
        ton1 = (Button) findViewById(R.id.button1);
        ton2 = (Button) findViewById(R.id.button2);
        countDownTimer = new MyCountDownTimer(startTime, interval);
    }

    public void simpan(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into tbl_gedung(no, gedung, ip) values('" +
                text1.getText().toString() + "','" +
                editGedung.getText().toString() + "','" +
                edtIP.getText().toString() + "')");
        Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
        MainActivity.ma.RefreshList();
        finish();
    }

    public void kembali(View view) {

        finish();
        countDownTimer.cancel();
    }


    public void ping (){
        Editable host = edtIP.getText();
        List<String> listResponstPing = new ArrayList<String>();
        ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                listResponstPing);

        try {
            String cmdPing = "ping -c 4 " + host;
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(cmdPing);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                listResponstPing.add(inputLine);
                listPing.setAdapter(adapterList);
            }

        } catch (Exception e) {
            Toast.makeText(this, "GAGAL: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();

        }

    }

    public void onclick(View view) {
        countDownTimer.start();
        ping();
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            countDownTimer.start();
            ping();

        }
    }
}
//testjuga