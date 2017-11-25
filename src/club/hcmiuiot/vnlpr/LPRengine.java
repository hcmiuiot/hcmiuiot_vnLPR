package club.hcmiuiot.vnlpr;

import java.util.ArrayList;
import java.util.Scanner;

import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import org.opencv.ml.*;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

public class LPRengine {
	
	private static int scale = 2;
	private static Integer val1 = new Integer(3);
	
	
	public static void setVal1(Integer val1) {
		LPRengine.val1 = val1;
	}


	public static void main(String[] args) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
//		VideoCapture vc = new VideoCapture(0);
//		BackgroundSubtractor bs = Video.createBackgroundSubtractorMOG2();
	
		boolean b = true;
			
		Mat src = Imgcodecs.imread("img/bike/0.jpg");
		//Debug.imshow("Source", src);
		
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		//Debug.imshow("Gray", gray);
		
		Mat mini_src = new Mat();
		mini_src = gray.clone();
		//Imgproc.pyrDown(gray, mini_src, new Size(src.cols() / scale, src.rows() / scale));
		//Debug.imshow("PyDowned", mini_src);
		//Imgproc.blur(mini_src, mini_src, new Size(1,1));
		//Debug.imshow("Mini Src Blured", mini_src);
		//Imgproc.morphologyEx(mini_src, mini_src, Imgproc.MORPH_BLACKHAT, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(20,20)));
		//Debug.imshow("Normalized", mini_src);
		//Core.normalize(mini_src, mini_src, 3, 255, Core.NORM_HAMMING);
		//Imgproc.threshold(mini_src, mini_src, Core.mean(mini_src).val[0]*10, 255, Imgproc.THRESH_BINARY);
		//Imgproc.adaptiveThreshold(mini_src, mini_src, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 51, -10);
		
		Debug.imshow("Normalized2", mini_src);

		Mat debug = mini_src.clone();
		//Core.normalize(mini_src, mini_src, 0, 255, Core.NORM_MINMAX);
		
		Thread slider1 = new Thread(new Slider(val1));
		slider1.start();
		
		while (b) {
			if (val1>1 && val1%2==1) {
				Imgproc.adaptiveThreshold(mini_src, debug, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, val1, -10);
				Imgproc.erode(debug, debug, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));
				Imgproc.dilate(debug, debug, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1,1)));
			}
			Debug.imshow("AdaptiveThreshold", debug);
			ArrayList<MatOfPoint> contours = new ArrayList<>();
			Mat hierarchy = new Mat();
			Mat cp = src.clone();
			Imgproc.findContours(debug, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
			for (int i=0; i<contours.size(); i++)
				if (Imgproc.boundingRect(contours.get(i)).area() > 50) {
					Rect rec = Imgproc.boundingRect(contours.get(i));
					Imgproc.rectangle(cp, rec.tl(), rec.br(), new Scalar(0,0,255));
				}
			Debug.imshow("SRC", cp);
		}
			
		//Imgproc.adaptiveThreshold(mini_src, mini_src, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 201, -50);
		//Debug.imshow("AdaptiveThreshold", mini_src);
			
		//Imgproc.threshold(mini_src, mini_src, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
		//Imgproc.erode(mini_src, mini_src, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));
//		Mat se1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2));
//		Mat se2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
//		
//		Mat dilated = new Mat();
//		Mat eroded = new Mat(); 
//		Imgproc.erode(mini_src, eroded, se1);
//		Debug.imshow("Erode", eroded);
//		Imgproc.dilate(eroded, dilated, se2);
//		Debug.imshow("Dilate", dilated);
//		//Imgproc.erode(dilated, dilated, se2);
//		//Debug.imshow("Erode2", dilated);
//		
//		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//		Mat hierachy = new Mat();
//		
//		Imgproc.findContours(dilated, contours, hierachy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//		
//		for (int i = 0; i < contours.size(); i++) {
//			//hasChild
//			Rect rec = Imgproc.boundingRect(contours.get(i));
//			//RotatedRect rotatedRect = Imgproc.fitEllipse((MatOfPoint2f)(contours.get[i]));
//			float ratio = 1.0f * rec.width / rec.height;
//			//hierachy.
//			//if (hierachy.get(i, 3)[1] != -1) {
//				//Imgproc.rectangle(src, rec.tl(), rec.br(), new Scalar(255,0,0));
//			//}
//				//Mat roi = new Mat(src, rec);
//				//if (ratio >= 0.42 && ratio <= 0.49 && rec.area() > 500) {
//			if (rec.area()>200)
//					Imgproc.rectangle(src, rec.tl(), rec.br(), new Scalar(255,0,0));
//					//Debug.imshow(""+i, sr);
//				//}
//				
//			//Imgproc.drawContours(src, contours, i, new Scalar(0,0,255));
//			//if (ratio >= 1.3f && ratio <= 1.7f)
//				//Imgproc.rectangle(src, rec.tl(), rec.br(), new Scalar(255,0,0));
//		}
//		Debug.imshow("Contours", src);
		
	//	Imgproc.rectangle(mini_src, new Point(0, 0), new Point(32*5, 32), new Scalar(255, 255, 255));	
		//Debug.imshow("Mini source", mini_src);
		
	
		
	
		
		
	}
}
