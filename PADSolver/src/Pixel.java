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
	public int getRed(){
		return red;
	}
	public int getBlue(){
		return blue;
	}
	public int getGreen(){
		return green;
	}
	public Color getColor(){
		return color;
	}
	public String toString(){
		return "(" + red+"," + green + "," + blue + ")";
		
	}
}
