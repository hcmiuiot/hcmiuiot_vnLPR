package club.hcmiuiot.vnlpr;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
//		Mat frame = new Mat();
//		Mat a = new Mat();
//		long timeStart, timeEnd;
//		double interval;
//		int fps=0;
//		while (b) {
//			timeStart = System.nanoTime();
//			vc.read(frame);
//			Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
//			Imgproc.adaptiveThreshold(frame, a, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 155, -10);
//			timeEnd = System.nanoTime();
//			interval = (timeEnd - timeStart);
//			fps = (int) (1000000000f/interval);
//			Imgproc.putText(frame, "FPS "+ fps, new Point(10,50), 0, 1, new Scalar(255,0,0));
//			Debug.imshow("FPS", a);
//		}
			
		Mat src = Imgcodecs.imread("img/bike/66.jpg");
		//Debug.imshow("Source", src);
		
		Mat gray = new Mat();
		
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		//Debug.imshow("Gray", gray);
		
		Mat mini_src = new Mat();
		mini_src = gray.clone();
		//Imgproc.pyrDown(gray, mini_src, new Size(src.cols() / scale, src.rows() / scale));
		//Debug.imshow("PyDowned", mini_src);
		
		//Debug.imshow("Normalized2", mini_src);

		Mat debug = mini_src.clone();
		
		//Thread slider1 = new Thread(new Slider(val1));
		//slider1.start();
		
		//while (b) {
		
		val1 = 55;
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
			for (int i=0; i<contours.size(); i++) {
				Rect rec = Imgproc.boundingRect(contours.get(i));
				float ratio = 1.0f * rec.width / rec.height;
				if (rec.area()>1000 && (ratio > 1.3f && ratio < 1.6f)) {
					Imgproc.rectangle(cp, rec.tl(), rec.br(), new Scalar(0,0,255));
					Mat plate = new Mat(debug, rec);
						
					Imgproc.dilate(plate, plate, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5,5)));
					Imgproc.erode(plate, plate, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(4,4)));
					//Debug.imshow("Plate "+i, plate);
					
					ArrayList<MatOfPoint> pContours = new ArrayList<>();
					Mat pHierarchy = new Mat();
					Imgproc.findContours(plate, pContours, pHierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
					
					int charFoundNum = 0;
					Mat plateSrc = plate.clone();
					Mat show = new Mat(src, rec);
					for (int j=0; j<pContours.size(); j++) {
						Rect recChar = Imgproc.boundingRect(pContours.get(j));
						float ratioChar = 1f * recChar.width / recChar.height;
						if ((ratioChar > 0.38f && ratioChar < 0.53f) || (ratioChar > 0.28f && ratioChar < 0.35f)) {
							charFoundNum++;
							
							Mat charMat = new Mat(plateSrc, recChar);
							System.out.println("" + recChar.height + " - " + rec.height);
							int nonZero = Core.countNonZero(charMat);
							double area = recChar.area();
							if (1.0f* recChar.height / rec.height < 0.3f) 
								continue;
							
							//saveMat(new Mat(plate, recChar));
							Imgproc.rectangle(show, recChar.tl(), recChar.br(), new Scalar(0,0,255), 2);
						}
							
					}
					System.out.println(charFoundNum);
					if (charFoundNum>=8)
						Debug.imshow("Char"+i, show);
				}
			}
			//Debug.imshow("SRC", cp);
		//}		
	}
	
	private static void saveMat(Mat img) {
		Imgcodecs.imwrite("./img/trainData/"+System.nanoTime()+".jpg", img);
	}
}
