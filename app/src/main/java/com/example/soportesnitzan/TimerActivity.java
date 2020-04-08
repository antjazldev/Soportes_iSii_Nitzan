package com.example.soportesnitzan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    boolean esGarantia=false;
    ArrayList<Soporte> arrSoportes = new ArrayList<Soporte>();
    ArrayList<Long> arrContador = new ArrayList<Long>();
    double res;
    TextView firma;
    EditText personaQueFirma,comentatiosTecnico;
    Boolean firmaTecnico = false;
    Boolean firmaCliente = false;
    String nombreTecnico="";
    String nombreQuienRecibe="";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
    File file;
    boolean permited = false;
    TextView tvFincaSoporte,tvPrecioSoporte,tvTipoSoporte,tvDetalleSoporte,tvComentariosSoporte,tvSolicitanteSoporte,tvTelefonoSoporte,tvCostoMov,tvCostoTotal;
    ImageView imageView;
    Button mClear, mGetSign, mCancel,btnpdf;
    View view;String DIRECTORY,pic_name,StoredPath;
    LinearLayout mContent;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    static Bitmap bitmap,bitmapfirma,bitmaplogo, bitmap2,bitmapCliente,bitmapfirmaCliente,bitlogo;
    static int numeroHorasSoporte;
    int preciobase;
    signature mSignature;
    static Soporte soporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        firma = findViewById(R.id.tvQnFirna);
        personaQueFirma = findViewById(R.id.etPersonaQueFirma);
        comentatiosTecnico=findViewById(R.id.etComentariosTecnico);
        numeroHorasSoporte = 1;
        preciobase=0;
        Intent i = getIntent();
         soporte = (Soporte)i.getSerializableExtra("Soporte");
        // arrSoportes = i.getExtra("arrSoportes");
       // arrSoportes = (ArrayList<Soporte>) getIntent().getSerializableExtra("arrSoportes");
       imageView=findViewById(R.id.logo);
        tvPrecioSoporte = findViewById(R.id.PrecioSoporte_timer);
        preciobase = soporte.getHbase()* getResources().getInteger(R.integer.horasbase);
        tvPrecioSoporte.setText(String.valueOf(preciobase));
       // imageView.setVisibility(GONE);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap2 = drawable.getBitmap();
        tvFincaSoporte = findViewById(R.id.Cliente_timer);
        tvFincaSoporte.setText(soporte.getFinca());
        tvTipoSoporte = findViewById(R.id.Categoria_timer);
        tvTipoSoporte.setText(soporte.getCategoria());
        tvDetalleSoporte = findViewById(R.id.Detalle_timer);
        tvDetalleSoporte.setText(soporte.getDetalle());

        tvComentariosSoporte = findViewById(R.id.Comentarios_timer);
        tvComentariosSoporte.setText(soporte.getComentarios());
        tvSolicitanteSoporte = findViewById(R.id.Solicitante_timer);
        tvSolicitanteSoporte.setText(soporte.getSolicitante());
        tvTelefonoSoporte = findViewById(R.id.Telefono_timer);
        tvTelefonoSoporte.setText(soporte.getTelefono());
        tvCostoMov = findViewById(R.id.tvMov_timer);
        tvCostoMov.setText(String.valueOf(soporte.getCostomov()));
        tvCostoTotal = findViewById(R.id.PrecioTotal_timer);
        double ps = Double.valueOf(tvPrecioSoporte.getText().toString().replace(",", "."));
        double  cm = Double.valueOf(tvCostoMov.getText().toString().replace(",", "."));
        res = ps + cm;
        String sres = String.valueOf(res);
        soporte.setCostoTotal(sres);
        tvCostoTotal.setText(String.valueOf(res));
//        tvCostoTotal.setText(String.valueOf(Double.valueOf(tvPrecioSoporte.getText().toString())+Double.valueOf(tvCostoMov.getText().toString())));
                mContent = (LinearLayout) findViewById(R.id.canvasLayout);
        view = mContent;
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = (Button) findViewById(R.id.clear);
        mGetSign = (Button) findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = (Button) findViewById(R.id.cancel);
        btnpdf = findViewById(R.id.btnpdf);

        mGetSign.setOnClickListener(onButtonClick);
        mClear.setOnClickListener(onButtonClick);
        mCancel.setOnClickListener(onButtonClick);
        btnpdf.setOnClickListener(onButtonClick);

        // Method to create Directory, if the Directory doesn't exists


        System.out.println(soporte.getComentarios());


        //Setting up Chronomether
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Tiempo: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        //Chrono Methods
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            private long mTicks = 0;
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (mTicks!=0 && (mTicks % 3601 ==0)) {
                   numeroHorasSoporte+=1;
                   if (numeroHorasSoporte>soporte.getHbase())
                    { preciobase += ((numeroHorasSoporte-soporte.getHbase())* 45);
                      res += ((numeroHorasSoporte-soporte.getHbase())* 45);
                      tvPrecioSoporte.setText(String.valueOf(preciobase));
                      tvCostoTotal.setText(String.valueOf(res));
                    }
                }
                mTicks++;
            }
        });
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_facturacion:
                if (checked)
                    // Pirates are the best
                    esGarantia=false;
                    break;
            case R.id.radio_garantias:
                if (checked)
                    // Ninjas rule
                    esGarantia=true;
                    break;
        }
    }

    final Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v == mClear) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
             //   mGetSign.setEnabled(false);
            } else if (v == mGetSign) {
                pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/iSii/";
                //////
                file = new File(DIRECTORY);
                if (!file.exists()) {
                    file.mkdir();
                }
                if (firmaTecnico==false)
                { StoredPath = DIRECTORY + pic_name + "tecnico.png";
                Log.v("log_tag", "Panel Saved");
                if (Build.VERSION.SDK_INT >= 23) {
                    permited = isStoragePermissionGranted();
                    System.out.println("ENTER FLAG IF(SignatureActivity)");
                    if (permited) {
                        System.out.println("IS PERMITED(SignatureActivity)");
                        view.setDrawingCacheEnabled(true);
                        mSignature.save(view, StoredPath);
                        Toast.makeText(getApplicationContext(), "Firma de Tecnico Registrada", Toast.LENGTH_SHORT).show();
                        nombreTecnico = String.valueOf(personaQueFirma.getText());
             }
                }
                  //  firma=findViewById(R.id.tvfirmas);
                    firma.setText("Firma Cliente: ");
                personaQueFirma.setText("");
                    personaQueFirma.setHint("Ingrese el nombre del cliente");
                    mClear.performClick();
                }
                //////////////////////////
                else if (firmaCliente==false && firmaTecnico)
                { StoredPath = DIRECTORY + pic_name + "cliente.png";
                Log.v("log_tag", "Panel Saved");
                    if (Build.VERSION.SDK_INT >= 23) {
                        permited = isStoragePermissionGranted();
                        System.out.println("ENTER FLAG IF(SignatureActivity)");
                        if (permited) {
                            System.out.println("IS PERMITED(SignatureActivity)");
                            view.setDrawingCacheEnabled(true);
                            mSignature.save(view, StoredPath);
                            Toast.makeText(getApplicationContext(), "Firma de cliente registrada.", Toast.LENGTH_SHORT).show();
                            nombreQuienRecibe = String.valueOf(personaQueFirma.getText());


                        }
                    } }










                ////////////////////

            } else if (v == mCancel) {
                Log.v("log_tag", "Panel Canceled");
                // Calling the BillDetailsActivity
                //Intent intent = new Intent(SignatureActivity.this, MainActivity.class);
                //startActivity(intent);
                onBackPressed();
            } else if (v == btnpdf ){

                soporte.setPersonaRecibe(nombreQuienRecibe);
                soporte.setTecnico(nombreTecnico);
                Double d = Double.parseDouble(String.valueOf(res));
                soporte.setValor(d.longValue());
                soporte.setFinalizado("yes");
                soporte.setComentariost(String.valueOf(comentatiosTecnico.getText()));
                if(esGarantia)
                    soporte.setNotadecredito(String.valueOf(res));
                if(!esGarantia)
                    soporte.setNotadecredito("0");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("/Soportes").child(soporte.getId());
                //


                //
                ref.child("personaRecibe").setValue(soporte.getPersonaRecibe());
                ref.child("tecnico").setValue(soporte.getTecnico());
                ref.child("valor").setValue(soporte.getValor());
                ref.child("finalizado").setValue(soporte.getFinalizado());
                ref.child("comentariost").setValue(soporte.getComentariost());
                ref.child("costomov").setValue(soporte.getCostomov());
                ref.child("notadecredito").setValue(soporte.getNotadecredito());
                if(numeroHorasSoporte>soporte.getHbase()) {
                    ref.child("hbase").setValue(numeroHorasSoporte);
                }

                readData(new MyCallBack() {
                    @Override
                    public void onCallback(long value) {
                        try{String directory_path = Environment.getExternalStorageDirectory().getPath() + "/iSiiPDF/";
                            File file = new File(directory_path);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            String pdf_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                            String targetPdf = directory_path+pdf_name +".pdf";
                            Document document = new Document();
                            PdfWriter.getInstance(document, new FileOutputStream(targetPdf));
                            document.open();
                            addMetaData(document);
                            addTitlePage(document,value);
                            // addContent(document);
                            document.close();
                            Toast.makeText(getApplicationContext(), "Soporte Finalizado. PDF GENERADO", Toast.LENGTH_SHORT).show();
                            final Intent i = new Intent(TimerActivity.this, SoportesActivity.class);
                            //   i.putExtra("arrSoportes",arrSoportes);
                            startActivity(i);



                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });





            }
        }

    };
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

    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        preciobase = soporte.getHbase()* getResources().getInteger(R.integer.horasbase);
        tvPrecioSoporte.setText(preciobase+" $");
    }
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }



    public class signature extends View {

        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        @SuppressLint("WrongThread")
        public void save(View v, String StoredPath) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (firmaTecnico==false) {
                if (bitmap == null) {
                    bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
                }
                Canvas canvas = new Canvas(bitmap);
                try {
                    // Output the file
                    FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                    v.draw(canvas);

                    // Convert the output file to Image such as .png
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                    // Intent intent = new Intent(SignatureActivity.this, MainActivity.class);
                    // intent.putExtra("imagePath", StoredPath);
                    // startActivity(intent);
                    //finish();
                    mFileOutStream.flush();
                    mFileOutStream.close();

                } catch (Exception e) {
                    Log.v("log_tag", e.toString());
                }
                firmaTecnico = true;

            }

            else if (firmaCliente==false && firmaTecnico==true) {
                if (bitmapCliente == null) {
                    bitmapCliente = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
                }
                Canvas canvas = new Canvas(bitmapCliente);
                try {
                    // Output the file
                    FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                    v.draw(canvas);

                    // Convert the output file to Image such as .png
                    bitmapCliente.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                    // Intent intent = new Intent(SignatureActivity.this, MainActivity.class);
                    // intent.putExtra("imagePath", StoredPath);
                    // startActivity(intent);
                    //finish();
                    mFileOutStream.flush();
                    mFileOutStream.close();

                } catch (Exception e) {
                    Log.v("log_tag", e.toString());
                }
                firmaCliente = true;
                btnpdf.setEnabled(true);

            }

        }

        public void clear() {
            path.reset();
            invalidate();
            mGetSign.setEnabled(false);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("iSii Nitzan soporte");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Luis Jacho");
        document.addCreator("Luis Jacho");
    }
    private void addTitlePage(Document document, long value) throws DocumentException, IOException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);


        //add header table
        createHeaderTable(bitmaplogo,preface,soporte,value);
        // add a table
        createTable(soporte,preface);
       /* addEmptyLine(preface,3 );
            Anchor anchorObs = new Anchor ("Observaciones del Servicio Realizado:",catFont);
            preface.add(anchorObs);
        preface.add(new Paragraph(soporte.getComentariost(),                smallBold));

            addEmptyLine(preface,2 );*/
        createTableFirmas(soporte,bitmap,bitmapCliente,preface);
        document.add(preface);




        // now add all this to the document


    }
    public static PdfPCell createImageCell(ByteArrayOutputStream path) throws IOException{
        Image img = null;
        try {
            img = Image.getInstance(path.toByteArray());
        } catch (BadElementException e) {
            e.printStackTrace();
        }
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }

    private static void createTable(Soporte soporte, Paragraph paragraph)
            throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        table.setWidths(new int[]{1, 3,1,1});

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("CANTIDAD"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("DESCRIPCION DEL SERVICIO"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("V/UNIT"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("TOTAL"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);


        table.setHeaderRows(1);

        table.addCell("     1");
        table.addCell(soporte.getDetalle());
        table.addCell("   ".concat(soporte.getCostoTotal().concat(" $")));
        table.addCell("   ".concat(soporte.getCostoTotal().concat(" $")));
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");



        paragraph.add(table);
        PdfPTable table2 = new PdfPTable(2);
        table2.setWidthPercentage(100);

        table2.setWidths(new int[]{2,1});
        PdfPCell c7 = new PdfPCell(new Phrase("OBSERVACIONES DEL SERVICIO TÉCNICO:"));
        c7.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.addCell(c7);

        c7 = new PdfPCell(new Phrase("NOTA DE CREDITO:"));
        c7.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.addCell(c7);


        table2.setHeaderRows(1);
        PdfPCell cellcoment = new PdfPCell(new Phrase(soporte.getComentariost()));
        cellcoment.setFixedHeight(45f);
        table2.addCell(cellcoment);
        cellcoment = new PdfPCell(new Phrase(soporte.getNotadecredito()));//Soporte.GetNotaDeCredito
        cellcoment.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.addCell(cellcoment);
        paragraph.add(table2);
    }

    private static void createTableFirmas(Soporte soporte, Bitmap firmatecnico, Bitmap firmaCliente,Paragraph paragraph)
            throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2,2,1,1});

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Firma del Técnico: "));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Firma del Cliente:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("SUBTOTAL "));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        if (soporte.getNotadecredito().length()>1)
        {soporte.setCostoTotal("0");
            c1 = new PdfPCell(new Phrase("0 $"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);}
        if (soporte.getNotadecredito().length()<=1)
        {c1 = new PdfPCell(new Phrase(soporte.getCostoTotal().concat(" $")));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);}


        table.setHeaderRows(1);
        c1 = new PdfPCell(new Phrase(soporte.getTecnico()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);
        c1 = new PdfPCell(new Phrase(soporte.getPersonaRecibe()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("IVA 12%:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);
        DecimalFormat dfd = new DecimalFormat("0.00");

        double costod = Double.parseDouble(soporte.getCostoTotal())*0.12;
        c1 = new PdfPCell(new Phrase(new Phrase(dfd.format(costod))));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapfirma = firmatecnico.createScaledBitmap(bitmap,250,130,false);
        bitmapfirma.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
        Image myImg = null;
        try {
            myImg = Image.getInstance(stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
       // myImg.setAlignment(Image.LEFT);
        table.addCell(myImg);
        ByteArrayOutputStream streamc = new ByteArrayOutputStream();
        bitmapfirmaCliente = firmaCliente.createScaledBitmap(bitmapCliente,250,130,false);
        bitmapfirmaCliente.compress(Bitmap.CompressFormat.JPEG, 100 , streamc);
        Image myImgc = null;
        try {
            myImgc = Image.getInstance(streamc.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // myImg.setAlignment(Image.LEFT);
        table.addCell(myImgc);
        c1 = new PdfPCell(new Phrase("COSTO TOTAL:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);
        DecimalFormat df = new DecimalFormat("0.00");

        double costo = Double.parseDouble(soporte.getCostoTotal())*1.12;
        c1 = new PdfPCell(new Phrase(String.valueOf(df.format(costo))));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);


        paragraph.add(table);

    }
    private void createHeaderTable(Bitmap bitmaplogo, final Paragraph paragraph, Soporte soporte, long value)
            throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 1});

        PdfPTable innerTable = new PdfPTable(1);
        PdfPCell c2;
        innerTable.addCell(new Paragraph("      CARLOS MAURICIO YANEZ BARRAGAN", catFont));
        innerTable.addCell(new Paragraph("      COMPROBANTE DE SERVICIO TECNICO", catFont));
        innerTable.addCell(new Paragraph("                                   RUC:1710207356001",smallBold));
        innerTable.addCell(new Paragraph("         Leonardo Tejada S3-262 y Bernardo de Legarda - Cumbaya",smallBold));
        innerTable.addCell(new Paragraph("                                   Telefono: 0988404805",smallBold));
       // PdfPCell c1 = new PdfPCell(new Phrase("This picture was taken at Java One.\nIt shows the iText crew at Java One in 2013."));
        c2 = new PdfPCell (innerTable);//this line made the difference
        c2.setPadding(0);
        table.addCell(c2);


        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        bitmaplogo = bitmap2.createScaledBitmap(bitmap2,90,90,false);
        bitmaplogo.compress(Bitmap.CompressFormat.JPEG, 100 , stream2);
        Image myImg2 = null;
        try {
            myImg2 = Image.getInstance(stream2.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        myImg2.setAlignment(Image.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell(myImg2,false);
        cell.setPaddingBottom(2);
        cell.setPaddingLeft(2);
        cell.setPaddingTop(2);
        table.addCell(cell);

        paragraph.add(table);

        final PdfPTable tableDatos = new PdfPTable(2);
        tableDatos.setWidthPercentage(100);
        tableDatos.setWidths(new int[]{2, 1});
        PdfPTable innerTable2 = new PdfPTable(1);
        PdfPCell c22;
        innerTable2.addCell(new Paragraph("Fecha: ".concat(new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss", Locale.getDefault()).format(new Date())), smallBold));
        innerTable2.addCell(new Paragraph("Cliente: ".concat(soporte.getFinca()), smallBold));
        innerTable2.addCell(new Paragraph("Duración del servicio: ".concat(String.valueOf(numeroHorasSoporte).concat(" hora(s)")),smallBold));
        innerTable2.addCell(new Paragraph("Tipo de Servicio:",smallBold));
        // PdfPCell c1 = new PdfPCell(new Phrase("This picture was taken at Java One.\nIt shows the iText crew at Java One in 2013."));
        c22 = new PdfPCell (innerTable2);//this line made the difference
        c22.setPadding(0);
        tableDatos.addCell(c22);
        tableDatos.addCell(new Paragraph("Comprobante:2020-00".concat(String.valueOf(value)), smallBold));
        paragraph.add(tableDatos);





    }
    public static void readData(final MyCallBack myCallback){
        final long[] cont = new long[1];
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("/Contador");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    cont[0] = snap.getValue(long.class);

                    System.err.println("AKIAKIAKIAKIAKIAKIAKIAKIAKAIKAIKAIKIAKIAKAIKAIKAIAKIAKAIKAIKAIKAKAIAKIAKIAKIAKIAKIAKIAKIKAIKIAKIAKIKAIK");
                    System.err.println(String.valueOf(cont[0]));
                    System.err.println(cont[0]);
                    Log.d("TAGDENTROonDataChange",String.valueOf(cont[0]));
                    System.err.println("AKIAKIAKIAKIAKIAKIAKIAKIAKAIKAIKAIKIAKIAKAIKAIKAIAKIAKAIKAIKAIKAKAIAKIAKIAKIAKIAKIAKIAKIKAIKIAKIAKIKAIK");

                }
                ref.child("contador").setValue(cont[0]+1);
                Log.d("TAGDOnCallBack",String.valueOf(cont[0]));
                myCallback.onCallback(cont[0]);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});


    }

    public long cont(){
        final long[] c = new long[1];;

        readData(new MyCallBack() {
            @Override
            public void onCallback(long value) {
                Log.d("TAGDENTROcont",String.valueOf(value));
            c[0] = value;
                Log.d("TagContCCCCCCCC",String.valueOf(c[0]));
            }
        });
        Log.d("TAGDreturncont",String.valueOf(c[0]));
       return c[0];
    }



    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}


