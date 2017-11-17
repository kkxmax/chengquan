package com.chengxin.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageCropper {
	public static String ImageCrop(String absoluteDir, String fileName, double sw, double sh, double x, double y, double w, double h) {
		try {
			String extension = "";
			String tmpFileName = "";
			if (fileName.lastIndexOf(".") > -1) {
				extension = fileName.substring(fileName.lastIndexOf(".") + 1);
				tmpFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "-1";
			}

			File file = new File(absoluteDir + "\\" + fileName);

			BufferedImage originalImage = ImageIO.read(file);
			int oriWidth = originalImage.getWidth();
			int oriHeight = originalImage.getHeight();

			double x1 = oriWidth * x / sw;
			double y1 = oriHeight * y / sh;
			double w1 = oriWidth * w / sw;
			double h1 = oriHeight * h / sh;

			BufferedImage SubImage = originalImage.getSubimage((int)x1, (int)y1, (int)w1, (int)h1);

			File outputfile = new File(absoluteDir + "\\" + tmpFileName + "." + extension);

			ImageIO.write(SubImage, extension, outputfile);

			file.delete();

			return tmpFileName + "." + extension;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}