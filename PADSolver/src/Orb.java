
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
   //heart orb
   public final static int H = 7;
   public boolean delete;
   public int color;
   
   //c = R,G,B,L(ight),D(ark)
   public Orb(int c){
      color = c;
      delete = false;
   }
   
   public Object clone() throws CloneNotSupportedException{
      return (Object)super.clone();
   }
   
   //Prints colors of the orbs
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
      if(color == 7)
         return "H";
      return "Not initialized with a color";
   }
}
