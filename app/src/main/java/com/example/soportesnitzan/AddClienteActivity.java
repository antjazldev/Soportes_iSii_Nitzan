package com.example.soportesnitzan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddClienteActivity extends AppCompatActivity {

    Finca finca = new Finca();
    EditText email,password,nombre;
    Button btnAddC;
    FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente);

        email = findViewById(R.id.addcliente_email);
        password = findViewById(R.id.addcliene_password);
        btnAddC = findViewById(R.id.btnAgregarCliente);
        nombre = findViewById(R.id.addcliente_nombre);

        btnAddC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailID = email.getText().toString();
                String paswd = password.getText().toString();
                final String nombreFinca = nombre.getText().toString();
                firebaseAuth = FirebaseAuth.getInstance();
                if (emailID.isEmpty()) {
                    email.setError("Ingrese el correo primero!");
                    email.requestFocus();
                } else if (paswd.isEmpty()) {
                    password.setError("Ingrese el password");
                    password.requestFocus();
                } else if (emailID.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(AddClienteActivity.this, "Campos vacios !!!", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(AddClienteActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(AddClienteActivity.this.getApplicationContext(),
                                        "Error: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                              finca.setCorreo(emailID);
                              finca.setNombre(nombreFinca);
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Fincas").child(finca.getNombre()).setValue(finca);
                                Toast.makeText(AddClienteActivity.this, "Servicio Creado Exitosamente.", Toast.LENGTH_SHORT).show();





                                startActivity(new Intent(AddClienteActivity.this, LauncherActivity.class));
                            }
                        }
                    });



                } else {
                    Toast.makeText(AddClienteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
