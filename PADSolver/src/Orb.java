
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
      return (Object)this.clone();
   }
}
