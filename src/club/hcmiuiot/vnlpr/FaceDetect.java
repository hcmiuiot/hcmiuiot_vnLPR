package club.hcmiuiot.vnlpr;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.*;

public class FaceDetect {

	private static CascadeClassifier cas = new CascadeClassifier("haarcascade_frontalface_default.xml");
	private static CascadeClassifier eye = new CascadeClassifier("haarcascade_eye.xml");
	
	public FaceDetect() {
		this.cas = new CascadeClassifier("haarcascade_frontalface_default.xml");
	}
	
//	public FaceDetect(CascadeClassifier cas) {
//		this.cas = cas;
//	}

	public static ArrayList<Rect> regFace(Mat img) {
		MatOfRect res = new MatOfRect();
		ArrayList<Rect> faces = new ArrayList<Rect>();
		cas.detectMultiScale(img, res);
		for (int i=0; i<res.toArray().length; i++) {
			Mat face = new Mat(img, res.toArray()[i]);
			MatOfRect eyes = new MatOfRect();
			eye.detectMultiScale(face, eyes);
			if (eyes.toArray().length!=0)
				faces.add(res.toList().get(i));
		}
		return faces;
	}
	
	
}
