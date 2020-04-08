import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

public class comprobantePDF {
    private static String pdfFILE = Environment.getExternalStorageDirectory().getPath() + "/iSiiPDF/";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
}
