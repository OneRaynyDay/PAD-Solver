import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardFinder {
	private static int width;
	private static int height;
	private static int RES_DOWNSCALE = 2;
	private static BufferedImage img;
	private static Pixel [][] board;
	
	public static void main(String [] args) throws IOException{
		createAndShowGUI();
	}
	public static void createAndShowGUI() throws IOException{
		img = ImageIO.read(new File("src/HTCOne-M8.png"));
		width = img.getWidth(null);
		height = img.getHeight(null);
		JFrame frame = new JFrame("Screen Capture");
		frame.setPreferredSize(new Dimension(width/RES_DOWNSCALE,height/RES_DOWNSCALE));
		
		parseImage();
		
		JPanel pane = new JPanel(){
			protected void paintComponent(Graphics g){
				//For some reason, panels are a couple pixels smaller than they should be
				g.drawImage(img,0,0,getWidth(),getHeight(),null);
				g.setColor(Color.RED);
				Graphics2D g2 = (Graphics2D) g;
				//Allows for more visibility in the grid
			    g2.setStroke(new BasicStroke(5));
				g.drawRect(0, 0, getWidth(), getHeight()/RES_DOWNSCALE);
			}
		};
		frame.add(pane);
		frame.pack();
		frame.setVisible(true);
	}
	public static void parseImage(){
		//create pixels 
		board = new Pixel[height/10][width/10]; //arbitrarily only checks every 10 pixels for testing

		for(int i = 0; i < height-10; i+=10){
			for(int j = 0; j < width-10; j+=10){
				System.out.print(i+","+j+"|");
				Pixel pixel = new Pixel(img.getRGB(i,j));
			}
			System.out.println();
		}
		for(int k = 0; k < board.length;k++){
			for(int l = 0; l < board[k].length;l++){
				//System.out.print(board[k][l]);
			}
			//System.out.println();
		}
	}
}
