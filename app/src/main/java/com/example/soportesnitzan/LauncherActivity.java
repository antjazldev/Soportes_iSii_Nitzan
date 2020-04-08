package com.example.soportesnitzan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LauncherActivity extends AppCompatActivity {
    private static final String TAG = "LauncherActivity";
    LinearLayout salir;
    LinearLayout soportes,reporte,clientes,servicios;
    ArrayList<Soporte> arrSoportes = new ArrayList<Soporte>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        salir= findViewById(R.id.close);
        soportes = findViewById(R.id.SoportesLink);
        servicios = findViewById(R.id.servicios);
        reporte = findViewById(R.id.reporte);
        clientes = findViewById(R.id.clientes);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        //  String msg = getString(token);
                        //  Log.d(TAG, msg);
                        Toast.makeText(LauncherActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });


        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(LauncherActivity.this, LoginActivity.class);
                startActivity(I);

            }
        });
        soportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(LauncherActivity.this, SoportesActivity.class);
                startActivity(I);

            }
        });
        reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("/Soportes");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arrSoportes.clear();
                        for (DataSnapshot soporteSnapshot : dataSnapshot.getChildren()){
                            Soporte soporte = soporteSnapshot.getValue(Soporte.class);
                            arrSoportes.add(soporte);

                        }
                        saveExcelFile(arrSoportes);
                        Toast.makeText(LauncherActivity.this, "Excel Generado.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });



            }
        });

        clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(LauncherActivity.this, AddClienteActivity.class);
                startActivity(I);


            }
        });

        servicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(LauncherActivity.this, AddServiceActivity.class);
                startActivity(I);


            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseMessaging.getInstance().subscribeToTopic("Soportes")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "msg_subscribe";
                        if (!task.isSuccessful()) {
                            msg = "msg_subscribe_failed";

                        }
                        Log.d(TAG, msg);
                        Toast.makeText(LauncherActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });




    }
    private static boolean saveExcelFile(ArrayList<Soporte> arrSoportes) {

        // check if available and not read only


        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Historico");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Fecha");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Finca");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Detalle del Soporte");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Categoria");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Comentarios de la solicitud");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("Observaciones del servicio");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("Técnico encargado");
        c.setCellStyle(cs);

        c = row.createCell(7);
        c.setCellValue("Persona que Recibe");
        c.setCellStyle(cs);

        c = row.createCell(8);
        c.setCellValue("Soporte Finalizado");
        c.setCellStyle(cs);

        c = row.createCell(9);
        c.setCellValue("Horas del servicio");
        c.setCellStyle(cs);

        c = row.createCell(10);
        c.setCellValue("Valor");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));
        sheet1.setColumnWidth(4, (15 * 500));
        sheet1.setColumnWidth(5, (15 * 500));
        sheet1.setColumnWidth(6, (15 * 500));
        sheet1.setColumnWidth(7, (15 * 500));
        sheet1.setColumnWidth(8, (15 * 500));
        sheet1.setColumnWidth(9, (15 * 500));
        sheet1.setColumnWidth(10, (15 * 500));
        int rownum = 1;
        for (Soporte sop : arrSoportes)
        {
            Row rowr = sheet1.createRow(rownum++);
            createList(sop, rowr);

        }

        // Create a path where we will place our List of objects on external storage
        //File file = new File(context.getExternalFilesDir(null), fileName);
         FileOutputStream os = null;
        try{String directory_path = Environment.getExternalStorageDirectory().getPath() + "/iSiiExcel/";
            File file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String excelName = directory_path + fileName + ".xls";
            File file2 = new File(directory_path, fileName);

        try {
            os = new FileOutputStream(excelName);
            wb.write(os);
            Log.w("FileUtils", "Writing file " + file2);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file2, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return success;
    }catch (Exception e) {
            e.printStackTrace();
        }
        return true;
}
    private static void createList(Soporte soporte, Row row) // creating cells for each row
    {
        Cell cell = row.createCell(0);
        cell.setCellValue(soporte.getFecha());

        cell = row.createCell(1);
        cell.setCellValue(soporte.getFinca());

        cell = row.createCell(2);
        cell.setCellValue(soporte.getDetalle());

        cell = row.createCell(3);
        cell.setCellValue(soporte.getCategoria());

        cell = row.createCell(4);
        cell.setCellValue(soporte.getComentarios());

        cell = row.createCell(5);
        cell.setCellValue(soporte.getComentariost());

        cell = row.createCell(6);
        cell.setCellValue(soporte.getTecnico());

        cell = row.createCell(7);
        cell.setCellValue(soporte.getPersonaRecibe());

        cell = row.createCell(8);
        cell.setCellValue(soporte.getFinalizado());

        cell = row.createCell(9);
        cell.setCellValue(soporte.getHbase());

        cell = row.createCell(10);
        cell.setCellValue(soporte.getCostoTotal());


    }


}
