package club.hcmiuiot.vnlpr;

import java.util.ArrayList;

import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import org.opencv.ml.*;
import org.opencv.videoio.VideoCapture;

public class LPRengine {
	
	private static int scale = 2;
	
	public static void main(String[] args) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		VideoCapture vc = new VideoCapture(0);
		
		boolean b = true;
		
		while (b == true) {
			Mat a = new Mat();
			Mat c = new Mat();
			vc.read(a);
			//Mat gra = new Mat();
			//Imgproc.cvtColor(a,gra,Imgproc.COLOR_BGR2GRAY);
			ArrayList<Rect> faces = FaceDetect.regFace(a);
			for (int i=0;i<faces.toArray().length;i++) {
				Imgproc.rectangle(a, faces.get(i).tl(), faces.get(i).br(), new Scalar(0,0,255));
				c = new Mat(a, faces.get(i));
				//Imgproc.pyrUp(c, c, 3);
				Debug.imshow("Face" + i, c);
			}
			
			Debug.imshow("Video", a);
		}
		
		Mat src = Imgcodecs.imread("img/car/5.jpg");
		//Mat src = new Mat();
		//Debug.imshow("Source", src);
		
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		//Debug.imshow("Gray", gray);
		
		Mat mini_src = new Mat();
		mini_src = gray;
		//Imgproc.pyrDown(gray, mini_src, new Size(src.cols() / scale, src.rows() / scale));
		//Debug.imshow("PyDowned", mini_src);
		//Imgproc.blur(mini_src, mini_src, new Size(1,1));
		//Debug.imshow("Mini Src Blured", mini_src);
		
		Imgproc.adaptiveThreshold(mini_src, mini_src, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 51, -10);
		Debug.imshow("AdaptiveThreshold", mini_src);
		
		//Imgproc.threshold(mini_src, mini_src, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
		//Imgproc.erode(mini_src, mini_src, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));
		Mat se1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2));
		Mat se2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
		
		Mat dilated = new Mat();
		Mat eroded = new Mat(); 
		Imgproc.erode(mini_src, eroded, se1);
		Debug.imshow("Erode", eroded);
		Imgproc.dilate(eroded, dilated, se2);
		Debug.imshow("Dilate", dilated);
		//Imgproc.erode(dilated, dilated, se2);
		//Debug.imshow("Erode2", dilated);
		
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierachy = new Mat();
		
		Imgproc.findContours(dilated, contours, hierachy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		
		for (int i = 0; i < contours.size(); i++) {
			//hasChild
			Rect rec = Imgproc.boundingRect(contours.get(i));
			//RotatedRect rotatedRect = Imgproc.fitEllipse((MatOfPoint2f)(contours.get[i]));
			float ratio = 1.0f * rec.width / rec.height;
			//hierachy.
			//if (hierachy.get(i, 3)[1] != -1) {
				//Imgproc.rectangle(src, rec.tl(), rec.br(), new Scalar(255,0,0));
			//}
				//Mat roi = new Mat(src, rec);
				//if (ratio >= 0.42 && ratio <= 0.49 && rec.area() > 500) {
			if (rec.area()>200)
					Imgproc.rectangle(src, rec.tl(), rec.br(), new Scalar(255,0,0));
					//Debug.imshow(""+i, sr);
				//}
				
			//Imgproc.drawContours(src, contours, i, new Scalar(0,0,255));
			//if (ratio >= 1.3f && ratio <= 1.7f)
				//Imgproc.rectangle(src, rec.tl(), rec.br(), new Scalar(255,0,0));
		}
		Debug.imshow("Contours", src);
		
	//	Imgproc.rectangle(mini_src, new Point(0, 0), new Point(32*5, 32), new Scalar(255, 255, 255));	
		//Debug.imshow("Mini source", mini_src);
		
	
		
	
		
		
	}
}
