package club.hcmiuiot.vnlpr;

import java.util.ArrayList;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class LPRengine {
	
	private static int scale = 2;
	
	public static void main(String[] args) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		Mat src = Imgcodecs.imread("img/bike/7.jpg");
		//Debug.imshow("Source", src);
		
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		//Debug.imshow("Gray", gray);
		
		Mat mini_src = new Mat();
		mini_src = gray;
		//Imgproc.pyrDown(gray, mini_src, new Size(src.cols() / scale, src.rows() / scale));
		//Debug.imshow("PyDowned", mini_src);
		
		Imgproc.adaptiveThreshold(mini_src, mini_src, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 51, -10);
		Debug.imshow("AdaptiveThreshold", mini_src);
		
		//Imgproc.threshold(mini_src, mini_src, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
		//Imgproc.erode(mini_src, mini_src, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));
		Mat se1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2));
		Mat se2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
		
		Mat dilated = new Mat();
		Mat eroded = new Mat(); 
		Imgproc.erode(mini_src, eroded, se2);
		Debug.imshow("Erode", eroded);
		Imgproc.dilate(eroded, dilated, se1);
		Debug.imshow("Dilate", dilated);
		//Imgproc.erode(dilated, dilated, se2);
		//Debug.imshow("Erode2", dilated);
		
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierachy = new Mat();
		
		Imgproc.findContours(dilated, contours, hierachy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		
		for (int i = 0; i < contours.size(); i++) {
			Rect rec = Imgproc.boundingRect(contours.get(i));
			float ratio = 1.0f * rec.width / rec.height;
			if (ratio >= 1.3f && ratio <= 1.7f)
				Imgproc.rectangle(src, rec.tl(), rec.br(), new Scalar(255,0,0));
		}
		Debug.imshow("Contours", src);
		
	//	Imgproc.rectangle(mini_src, new Point(0, 0), new Point(32*5, 32), new Scalar(255, 255, 255));	
		//Debug.imshow("Mini source", mini_src);
		
	
		
	
		
		
	}
}
