package farm.pic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Qrcode  {

	public static BufferedImage gen() throws Exception {
//		String qr = "wxp://f2f0cL69ygoPrp2qOOwjYcAfhHHF49F9p2lE";
		String qr="https://qr.alipay.com/fkx02916kdtaoovuhh6n425";
		int w=40, h=1;
		BitMatrix bitMatrix = new QRCodeWriter().encode(qr, BarcodeFormat.QR_CODE, w, h);
		ByteArrayOutputStream png = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "png", png);
		return ImageIO.read(new ByteArrayInputStream(png.toByteArray()));
	}
}
