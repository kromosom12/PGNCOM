package com.example.personal.databasepgas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LihatBiodataActivity extends AppCompatActivity {
    protected Cursor cursor;
    DataHelper dbHelper;
    TextView text1, text2;
    EditText text3;
    ListView  listPing;

    private CountDownTimer countDownTimer;

    private final long startTime = 6 * 1000;
    private final long interval = 1 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_biodata);

        dbHelper = new DataHelper(this);
        text1 = (TextView) findViewById(R.id.textView1);
        text2 = (TextView) findViewById(R.id.textView2);
        text3 = (EditText) findViewById(R.id.ipGedung);
        listPing = (ListView) findViewById(R.id.Lihat_ping);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tbl_gedung WHERE gedung = '" +
                getIntent().getStringExtra("gedung") + "'",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            text1.setText(cursor.getString(0).toString());
            text2.setText(cursor.getString(1).toString());
            text3.setText(cursor.getString(2).toString());
        }

    }

    public void pinglihat(){
        Editable host = text3.getText();
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

    public void lihatping(View view) {
        countDownTimer.start();
        pinglihat();
    }

    public void kembal(View view) {
        finish();
        countDownTimer.cancel();
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
            pinglihat();

        }
    }

}
