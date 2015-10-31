/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.utils;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

/**
 *
 * @author Marcel W
 */
public class QrUtils {
    
    //Outputstream must choosen either on the Controlpanel or if it always equal
    // as a local variable here. For now it will be choosen on execute.
    public static void QRGenerator(String s, OutputStream outputStream) {
        
        File file = QRCode.from(s).file();

        // get QR stream from text using defaults
        ByteArrayOutputStream stream = QRCode.from(s).stream();

        // override the image type to be JPG
        QRCode.from(s).to(ImageType.JPG).file();
        QRCode.from(s).to(ImageType.JPG).stream();

        // override image size to be 250x250
        QRCode.from(s).withSize(250, 250).file();
        QRCode.from(s).withSize(250, 250).stream();

        // override size and image type
        QRCode.from(s).to(ImageType.GIF).withSize(250, 250).file();
        QRCode.from(s).to(ImageType.GIF).withSize(250, 250).stream();

        // override default colors (black on white)
        // notice that the color format is "0x(alpha: 1 byte)(RGB: 3 bytes)"
        // so in the example below it's red for foreground and yellowish for background, both 100% alpha (FF).
       // QRCode.from(s).withColor(0xFFFF0000, 0xFFFFFFAA).file();

        // supply own outputstream
        QRCode.from(s).to(ImageType.PNG).writeTo(outputStream);

        // supply own file name
        QRCode.from(s).file("QRCode");

        // supply charset hint to ZXING
        QRCode.from(s).withCharset("UTF-8");

        // supply error correction level hint to ZXING
        QRCode.from(s).withErrorCorrection(ErrorCorrectionLevel.L);

        // supply any hint to ZXING
        QRCode.from(s).withHint(EncodeHintType.CHARACTER_SET, "UTF-8");

        
    
    }
    
}
