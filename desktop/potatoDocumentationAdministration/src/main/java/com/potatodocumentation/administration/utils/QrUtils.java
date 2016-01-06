/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.utils;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

/**
 *
 * @author Marcel W
 */
public class QrUtils {

    /**
     * The Path where all QR codes from ParcelBox are going to be safed
     */
    public static String qrPath = "C:\\QR\\";

    /**
     * Generates an QR-Code in PNG format of the given string <i>input</i> in
     * the given <i>path</i>. If path is null @link{#qrPath} is going to be
     * used. The filename of the generated file will be <i>[input].png</i>.
     *
     * @param input the
     * @param path
     * @return true if the QR-Code was generated, false otherwise.
     */
    public static boolean generateQrFromString(String input, String path, 
            String header) {

        if (path == null) {
            path = new String(qrPath);
        }

        QRCode qrCode = QRCode.from(input);

        qrCode = (QRCode) qrCode.to(ImageType.PNG);
        qrCode = (QRCode) qrCode.withErrorCorrection(ErrorCorrectionLevel.H);
        qrCode = (QRCode) qrCode.withSize(1024, 1024);
        qrCode = (QRCode) qrCode.withCharset("UTF-8");

        File qrFile = new File(path + "\\" + input + ".png");

        System.out.println(qrFile.getPath());

        try {
            qrFile.mkdirs();
            qrFile.delete();
            OutputStream out = new FileOutputStream(qrFile, false);
            qrCode.writeTo(out);
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        return qrFile.exists() && addHeaderToQrCode(qrFile, header);
    }

    public static boolean addHeaderToQrCode(File qrFile, String header) {
        try {
            final BufferedImage image = ImageIO.read(qrFile);

            double height = image.getHeight();

            //prepare Graphics
            Graphics g = image.getGraphics();
            g.setColor(Color.BLACK);
            g.setFont(g.getFont().deriveFont(new Float(height * 0.09)));

            //Center horizontally
            double stringWidth = g.getFontMetrics().getStringBounds(header, g)
                    .getWidth();
            double startX = height / 2 - stringWidth / 2;

            //Center vertically. the border of the Code is going to be approx.
            // 9,28% of its size, so centering is done by adding half of the difference
            // of border and font-height to the font-height
            double stringHeight = g.getFontMetrics().getStringBounds(header, g)
                    .getHeight();
            double heightDiff = ((height * 0.0928) - stringHeight);
            double startY = stringHeight + (heightDiff / 2);

            //Draw string
            g.drawString(header, (int) startX, (int) startY);
            g.dispose();

            ImageIO.write(image, "png", qrFile);
        } catch (IOException ex) {
            return false;
        }

        return true;
    }

}
