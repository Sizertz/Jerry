package siz.terry.reader.test;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.io.File;

import javax.imageio.ImageIO;

public class ReaderTest {
	public static void main(String[] args) {
		BufferedImage image;
		int width;
		int height;
		try {
			File input = new File("lf_heights.tif");
			image = ImageIO.read(input);
			System.out.println(image);
			width = image.getWidth();
			System.out.println("width " + width);
			height = image.getHeight();
			System.out.println("height " + height);

			Raster raster = image.getData();
			System.out.println(raster);

			System.out.println(raster.getTransferType());
			System.out.println(DataBuffer.TYPE_USHORT);

			short[] pixels = new short[width * height];
			pixels = (short[]) raster.getDataElements(0, 0, width, height, pixels);
			double coeff = 3000d / (Short.MAX_VALUE - Short.MIN_VALUE);
			System.out.println(coeff);
			double centre = pixels[255 * 512 + 255] * coeff;
			System.out.println(centre);
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					System.out.print(pixels[(252 + i) * 512 + j + 252] + " ");
				}
				System.out.println("");
			}
			System.out.println((pixels[255 * 512 + 255] * coeff - centre));
			System.out.println((pixels[255 * 512 + 255 + 1] * coeff - centre));
			System.out.println((pixels[255 * 512 + 255 + 3] * coeff - centre));
			System.out.println((pixels[255 * 512 + 255 + 10] * coeff - centre));

			short[] data = new short[1];
			centre = ((short[]) raster.getDataElements(255, 255, data))[0] * coeff;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					System.out.print(
							((short[]) raster.getDataElements(250 + i, 250 + j, data))[0] * coeff - centre + " ");
				}
				System.out.println("");
			}
			System.out.println(((short[]) raster.getDataElements(0, 0, data))[0] * coeff - centre + " ");

			centre = ((short[]) raster.getDataElements(320, 320, data))[0];
			for (int i = width/2-40; i < width/2+41; i++) {
				for (int j = height/2-40; j < height/2+41; j++) {
					System.out.print(getOffset(i,j,raster)+" ");
				}
				System.out.println("");
			}
			System.out.println(getOffset(0,0,raster));

	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final double coeff  = 3000d / (Short.MAX_VALUE - Short.MIN_VALUE);
	
	public static double getOffset(int x, int y, Raster raster) {
		short centre = ((short[]) raster.getDataElements(raster.getWidth()/2, raster.getHeight()/2, null))[0];
		short s = ((short[]) raster.getDataElements(x, y, null))[0];
		s = (short) (s - centre);
		s = (short) ((int) s > 0 ? s : -s);
		
		return ((int)(s*coeff*1000))/1000d;
	}
	
}
