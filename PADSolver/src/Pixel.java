import java.awt.Color;

public class Pixel {
	private Color color;
	private int red;
	private int blue;
	private int green;
	
	
	public Pixel(int unformattedColor){
		color = new Color(unformattedColor,true);
		createRGB();
	}
	//Breaks the unformatted color down into separate RGB values
	public void createRGB(){
		red = color.getRed();
		blue = color.getBlue();
		green = color.getGreen();
	}
	//methods to return RGB values of the pixel
	public int getRED(){
		return red;
	}
	public int getBLUE(){
		return blue;
	}
	public int getGREEN(){
		return green;
	}
	public String toString(){
		return "(" + red+"," + green + "," + blue + ")";
		
	}
}
