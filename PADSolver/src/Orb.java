
public class Orb implements Cloneable{
   public final static int R = 0;
   public final static int G = 0;
   public final static int B = 0;
   public final static int L = 0;
   public final static int D = 0;
   //poison orb
   public final static int P = 0;
   //blocker orb
   public final static int X = 0;
   public int color;
   
   public Object clone(){
      return (Object)this.clone();
   }
}
