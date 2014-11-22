import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardFinder {
	private static int width;
	private static int height;
	private static int RES_DOWNSCALE = 24; //eventually going to have to deal with aspect ratios
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
	}
	public static void parseImage(){
		//create pixels 
		board = new Pixel[height/RES_DOWNSCALE][width/RES_DOWNSCALE]; //arbitrarily only checks every 10 pixels for testing
		
		//fills 2D array of pixels
		for(int i = 0; i < height; i+=RES_DOWNSCALE){
			for(int j = 0; j < width; j+=RES_DOWNSCALE){
				Pixel pixel = new Pixel(img.getRGB(j,i));
				board[i/RES_DOWNSCALE][j/RES_DOWNSCALE] = pixel;
			}
			System.out.println();
		}
		//prints array of pixels
		for(int k = 0; k < board.length;k++){
			for(int l = 0; l < board[k].length;l++){
				System.out.print(board[k][l]);
			}
			System.out.println();
		}
		showData();
		fillBoard();
	}
	public static void showData(){
		JFrame frame = new JFrame("What Computer Sees (don't worry about cutoff)");
		//I believe cutoff has to deal with fitting jpanels in jframes
		
		frame.setPreferredSize(new Dimension(width/2,height/2));
		JPanel pane = new JPanel(){
			protected void paintComponent(Graphics g){
				g.drawRect(0, 0, getWidth(), getHeight()/RES_DOWNSCALE);
				
				//draws the array of pixels that the computer will use for board creation
				for(int i = 0; i < board.length;i++){
					for(int j = 0; j < board[i].length;j++){
						Color temp = ((Pixel)board[i][j]).getColor();
						g.setColor(temp);
						g.fillRect(j*RES_DOWNSCALE/2,i*RES_DOWNSCALE/2,RES_DOWNSCALE/2,RES_DOWNSCALE/2);
					}
				}

			}
		};
		frame.add(pane);
		frame.pack();
		frame.setVisible(true);
	}
	public static void fillBoard(){
		//take the array of pixels and somehow generate a 5x6 board of color-coded orbs
	}
}
