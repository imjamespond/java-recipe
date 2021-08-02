import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import qrcode.QREncode;
import qrcode.QRReader;

import java.io.File;
import java.io.IOException;

public class Test {

    static final String QRTEST = System.getenv("HOME") + "/Downloads/qrtest.png";
    static final String LOGO_FILE = System.getenv("HOME") + "/Downloads/logo.png";

    public static void main(String[] args) {
        new Test().test();
    }

    @org.junit.jupiter.api.Test
    void test() {
        try {
            QREncode.Encode("http://trueviewar.com/", LOGO_FILE, QRTEST);

            QRReader.Scan(new File(QRTEST));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void test1() {
        try {
            QRReader.Scan(new File(System.getenv("HOME") + "/Downloads/test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

}