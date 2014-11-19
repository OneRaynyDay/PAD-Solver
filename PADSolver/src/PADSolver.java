import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PADSolver {
   private static double SINGLE_ADD = 0.25;
   private static double COMBO_MULT = 0.25;
   private static int X;
   private static int Y;
   private Orb[][] mArr;
   //map that includes path and starting position
   //use regex to extract String directions
   private HashMap<Orb[][], String> map;
   /**
    * 
    * @param array
    *    the array is positioned to be like this:
    *    [0,0] [0,1] [0,2] 
    *    [1,0] [1,1] [1,2] 
    *    [2,0] [2,1] [2,2]
    *    Thus we use y as outer loop for all algorithms
    * @param x
    * @param y
    */
   public PADSolver(Orb[][] array, int x, int y){
      X = x;
      Y = y;
      for(int i = 0; i < Y; i++)
         for(int j = 0; j < X; j++)
            //Will run into null pointers in the future
            if(array[i][j] == null)
               throw new IllegalArgumentException();
      mArr = array;
   }
   /**
    * driver method stub
    */
   public void findSolutions(){
      
   }
   
   /**
    * recursively populating using heuristics and path finding
    */
   public void findPath(int x, int y, Orb[][] arr, int counter, Orb prev, String dir){
    //end of operations if last turn (base case)
      if(counter == 0)
         return;
      
      //advance orb positions - remember to clone, not modify initial instance
      Orb next = (arr[y][x] != null) ? arr[y][x] : (Orb)mArr[y][x].clone();
      Orb temp = next;
      swap(next, prev);
      prev = temp;      
      
      if(y > 0) //can go up
         findPath(x, y-1, arr, --counter, prev, dir+"U");
     if(x > 0) //can go left
         findPath(x-1, y, arr, --counter, prev, dir+"L");
     if(y < Y)
         findPath(x, y+1, arr, --counter, prev, dir+"D");
     if(x < X)
         findPath(x+1, y, arr, --counter, prev, dir+"R");
     /*
      * There is a problem in using arrays as hashmaps 
      * each array's address is different, so we must put the array in
      * a wrapper class - a kind of ArrayList w/ its own equals
      */
     ArrayList<ArrayList<Orb>> list = populateList(arr);
     //There already is an arraylist w/ same directions but longer length - we override
     if(map.get(list).length() > dir.length())
        map.put(arr, dir);
     
   }
   public static ArrayList<ArrayList<Orb>> populateList(Orb[][] arr){
      ArrayList<ArrayList<Orb>> list = new ArrayList<ArrayList<Orb>>();
      for(int i = 0; i < Y; i++)
         for(int j = 0; j < X; j++)
            list.get(i).set(j, arr[i][j]);
      return list;
   }
   public static void swap(Orb o1, Orb o2){
      if(o1 == null){
         o1 = o2;
         o2 = null;
         return;
      }
      if(o2 == null){
         o2 = o1;
         o1 = null;
         return;
      }
      //just swapping colors
      int temp = o1.color;
      o1.color = o2.color;
      o2.color = temp;
   }
}
