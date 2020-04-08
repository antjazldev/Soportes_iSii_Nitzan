package com.example.soportesnitzan;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MovilizacionActivity extends AppCompatActivity {
    Button btn_llegue;
    TextView Tv_tarifa;
    private int numeroMinutosMov=0;
    private double costomov;
    Soporte soporte;
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movilizacion);
        Intent i = getIntent();
        btn_llegue = findViewById(R.id.btn_llegue);
        soporte = (Soporte)i.getSerializableExtra("Soporte");
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Tiempo: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        Tv_tarifa = findViewById(R.id.tvTarifamov);

        btn_llegue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses butt

                final Intent i = new Intent(MovilizacionActivity.this, TimerActivity.class);

                i.putExtra("Soporte", soporte);
                //   i.putExtra("arrSoportes",arrSoportes);
                startActivity(i);
            }
        });

        //Chrono Methods
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            private long mTicks = 0;
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (mTicks!=0 && (mTicks % 61 ==0)) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    TypedValue typedValue = new TypedValue();
                    getResources().getValue(R.dimen.tarifaminuto, typedValue, true);
                    float tarifa = typedValue.getFloat();
                    numeroMinutosMov+=1;
                    costomov = numeroMinutosMov*tarifa;
                    String result = df.format(costomov);
                    //Long res = Long.valueOf(result);

                    soporte.setCostomov(result);
                    System.err.println(costomov);
                    Tv_tarifa.setText(result.concat(" $"));
                }
                mTicks++;
            }
        });
    }
    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }
}
