
public class Orb implements Cloneable{
   public final static int R = 0;
   public final static int G = 1;
   public final static int B = 2;
   public final static int L = 3;
   public final static int D = 4;
   //poison orb
   public final static int P = 5;
   //blocker orb
   public final static int X = 6;
   public int color;
   
   public Orb(int c){
      color = c;
   }
   
   public Object clone(){
      try {
         return (Object)super.clone();
      } catch (CloneNotSupportedException e) {
         e.printStackTrace();
      }
      return null;
   }
   
   public String toString(){
      if(color == 0)
         return "R";
      if(color == 1)
         return "G";
      if(color == 2)
         return "B";
      if(color == 3)
         return "L";
      if(color == 4)
         return "D";
      if(color == 5)
         return "P";
      if(color == 6)
         return "X";
      return "Not initialized with a color";
   }
}
