package com.example.soportesnitzan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SoportesActivity extends AppCompatActivity {
    ArrayList<Soporte> arrSoportes = new ArrayList<Soporte>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soportes);
    }
    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.activity_soportes);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/Soportes");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrSoportes.clear();
                for (DataSnapshot soporteSnapshot : dataSnapshot.getChildren()){


                    Soporte soporte = soporteSnapshot.getValue(Soporte.class);
                 //   System.out.println(soporte);
                    if(soporte.getFinalizado().toString().equals("no")){
                        arrSoportes.add(soporte);}

                }
                ////////ONLCIK RECYCLERVIEW

                RecVAdapter adaptadorRecyclerView = new RecVAdapter(new InterfazClickRecyclerView() {
                    @Override
                    public void onClick(View v, Soporte soporte) {
                        final Intent i = new Intent(SoportesActivity.this, MovilizacionActivity.class);

                        i.putExtra("Soporte", soporte);
                     //   i.putExtra("arrSoportes",arrSoportes);
                        startActivity(i);




                       // Toast.makeText(SoportesActivity.this, soporte.getCategoria(), Toast.LENGTH_SHORT).show();
                       // Toast.makeText(SoportesActivity.this, String.valueOf(soporte.getHbase()), Toast.LENGTH_SHORT).show();//
                        // Toast.makeText(SoportesActivity.this, soporte.getFinca(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SoportesActivity.this, soporte.getDetalle(), Toast.LENGTH_SHORT).show();
                    }
                });
                adaptadorRecyclerView.setSoportes(arrSoportes);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SoportesActivity.this);
                RecyclerView recyclerViewSoportes = findViewById(R.id.recview);
                recyclerViewSoportes.setLayoutManager(linearLayoutManager);
// La línea que divide los elementos
                recyclerViewSoportes.addItemDecoration(new DividerItemDecoration(SoportesActivity.this, LinearLayoutManager.VERTICAL));
// El adaptador que se encarga de toda la lógica
                recyclerViewSoportes.setAdapter(adaptadorRecyclerView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }}
