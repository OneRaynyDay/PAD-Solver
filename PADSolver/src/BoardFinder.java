import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardFinder {
	private static int width;
	private static int height;
	private static int RES_DOWNSCALE = 2;
	private static Image img;
	
	public static void main(String [] args) throws IOException{
		createAndShowGUI();
	}
	public static void createAndShowGUI() throws IOException{
		img = ImageIO.read(new File("src/HTCOne-M8.png"));
		width = img.getWidth(null);
		height = img.getHeight(null);
		JFrame frame = new JFrame("Screen Capture");
		frame.setPreferredSize(new Dimension(width/RES_DOWNSCALE,height/RES_DOWNSCALE));
		
		JPanel pane = new JPanel(){
			protected void paintComponent(Graphics g){
				//For some reason, panels are a couple pixels smaller than they should be
				g.drawImage(img,0,0,getWidth(),getHeight(),null);
				g.setColor(Color.RED);
				Graphics2D g2 = (Graphics2D) g;
			    g2.setStroke(new BasicStroke(5));
				g.drawRect(0, 0, getWidth(), getHeight()/RES_DOWNSCALE);
			}
		};
		frame.add(pane);
		frame.pack();
		frame.setVisible(true);
	}
}
