package com.example.soportesnitzan;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddServiceActivity extends AppCompatActivity {
    EditText codigo,nombre,hbase;
    Button btnAddS;
    Spinner dropdown;
   // FirebaseAuth firebaseAuth;
    Servicio servicio = new Servicio();
    private DatabaseReference mDatabase;// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        codigo = findViewById(R.id.codigo_servicio);
        nombre = findViewById(R.id.nombre_servicio);
        hbase = findViewById(R.id.hbase_servicio);
        btnAddS = findViewById(R.id.btnAgregarServicio);
        dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"electrico","electronico", "hidraulico","mecanico","neumatico"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        //firebaseAuth = FirebaseAuth.getInstance();


        btnAddS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codigo.getText()==null || nombre.getText()==null || hbase.getText()==null)
                {
                    Toast.makeText(AddServiceActivity.this, "Ingrese todos los datos.", Toast.LENGTH_SHORT).show();
                }
                else{
                servicio.setCodigo(String.valueOf(codigo.getText()));
                servicio.setNombre(String.valueOf(nombre.getText()));
                servicio.setHbase(Integer.parseInt(String.valueOf(hbase.getText())));
                servicio.setCategoria(dropdown.getSelectedItem().toString());


                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("servicios").child(servicio.getCodigo()).setValue(servicio);
                    Toast.makeText(AddServiceActivity.this, "Servicio Creado Exitosamente.", Toast.LENGTH_SHORT).show();

            }
            }
        });
    }
}
