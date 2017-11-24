package club.hcmiuiot.vnlpr;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;

public class Debug {

	private static ArrayList<JframeObj> jList = new ArrayList<JframeObj>();
	
	private static Image toBufferedImage(Mat m){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);  
        return image;
    }

	private static void displayImage(Image img2, String title) {
		Collections.sort(jList);
		JframeObj frame;
		int findIdx = Collections.binarySearch(jList, new JframeObj(title));
		if (findIdx >= 0)
			frame = jList.get(findIdx);
		else {
			frame = new JframeObj(title);
			jList.add(frame);
		}
		frame.updateImg(img2);
	}
	
	public static void imshow(String title, Mat m) {
		displayImage(toBufferedImage(m), title);
	}
}
