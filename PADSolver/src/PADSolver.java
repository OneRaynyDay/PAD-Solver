import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PADSolver {
   /**
    * variable stubs - we will extend for extra calculations
    */
   private static double SINGLE_ADD = 0.25;
   private static double COMBO_MULT = 0.25;

   private static int X;
   private static int Y;
   private Orb[][] mArr;
   // map that includes path and starting position
   // use regex to extract String directions
   private HashMap<ArrayList<ArrayList<Orb>>, String> map;

   /**
    * 
    * @param array
    *           the array is positioned to be like this: [0,0] [0,1] [0,2] [1,0]
    *           [1,1] [1,2] [2,0] [2,1] [2,2] Thus we use y as outer loop for
    *           all algorithms
    * @param x
    * @param y
    */
   public PADSolver(Orb[][] array, int x, int y) {
      X = x; //Width
      Y = y; //Height
      for (int i = 0; i < Y; i++)
         for (int j = 0; j < X; j++)
            // Will run into null pointers in the future
            if (array[i][j] == null)
               throw new IllegalArgumentException();
      mArr = array;
      map = new HashMap<ArrayList<ArrayList<Orb>>, String>();
   }

   /**
    * driver method
    */
   public void findSolutions(int numOfMoves) {
      int maxCombos = 0;
      ArrayList<ArrayList<Orb>> maxSequence = null;
      for (int i = 0; i < Y; i++)
         for (int j = 0; j < X; j++)
            // populates hashmap
            findPath(j, i, new Orb[Y][X], numOfMoves, j, i,
                  "Y " + i + "X " + j + "|");
      for (Map.Entry<ArrayList<ArrayList<Orb>>, String> entry : map.entrySet()) {
         // loops through each possibility w/ minimal moves
         Orb[][] sequence = mergeArrs(populateArray(entry.getKey()));
         int combos = 0;

         while (true) {
            combos += countCombos(sequence); // gives blank map to fill in
            if (!skyFall(sequence)) // means there are skyfalls
               break;
         }
         if (combos > maxCombos) {
            System.out.println("Found big combo : " + combos);
            maxSequence = entry.getKey();
            maxCombos = combos;
         }
      }
      /**
       * stub needs modification - gives simple result for now
       */
      System.out.println("The best result is : " + map.get(maxSequence));
   }

   /**
    * recursively populating using heuristics and path finding
    */
   public void findPath(int x, int y, Orb[][] arr, int counter, int prevX, int prevY,
         String dir) {
      // end of operations if last turn (base case)
      if (counter == 0){
         return;
      }
      
      arr[y][x] = (arr[y][x] != null) ? arr[y][x] : (Orb) mArr[y][x].clone();
      int nextY = y, nextX = x;
      swap(nextY, nextX, prevY, prevX, arr);
      prevY = nextY;
      prevX = nextX;

      counter--;
      if (y > 0) // can go up
         findPath(x, y - 1, arr, counter, prevX, prevY , dir + "U");
      if (x > 0) // can go left
         findPath(x - 1, y, arr, counter, prevX, prevY, dir + "L");
      if (y < Y-1)
         findPath(x, y + 1, arr, counter, prevX, prevY, dir + "D");
      if (x < X-1)
         findPath(x + 1, y, arr, counter, prevX, prevY, dir + "R");
      /*
       * There is a problem in using arrays as hashmaps each array's address is
       * different, so we must put the array in a wrapper class - a kind of
       * ArrayList w/ its own equals
       */
      ArrayList<ArrayList<Orb>> list = populateList(arr);
      // There already is an arraylist w/ same directions but longer length - we
      // override
      if(map.get(list) == null)
         map.put(list, dir);
      if (map.get(list) != null && (map.get(list).length() > dir.length()))
         map.put(list, dir);
   }

   public Orb[][] mergeArrs(Orb[][] arr) {
      Orb[][] newArr = new Orb[Y][X];
      for (int i = 0; i < Y; i++) {
         for(int j = 0; j < X; j++){
            Orb mo = mArr[i][j];
            Orb o = arr[i][j];
            if (o == null)
               newArr[i][j] = mo;
            else
               newArr[i][j] = o;
         }
      }
      return newArr;
   }

   /**
    * precondition: array has all cells complete
    * 
    * @return
    */
   public int countCombos(Orb[][] arr) {
      int combos = 0;
      for (int i = 0; i < Y; i++) {
         for (int z = 0; z < X; z++) {
            Orb o = arr[i][z];
            int horCount = 0;
            int verCount = 0;
            
            if(o == null || o.delete)
               continue;
            
            // scan right
            for (int j = 1; ((z+j) < X && arr[i][z+j] != null) && o.color == arr[i][z + j].color; j++)
               horCount++;
            // scan left
            for (int j = -1; ((z+j) >= 0 && arr[i][z+j] != null) && o.color == arr[i][z + j].color; j--)
               horCount++;
            for (int j = 1; ((i+j) < Y && arr[i+j][z] != null) && o.color == arr[i + j][z].color; j++)
               verCount++;
            for (int j = -1; ((i+j) >= 0 && arr[i+j][z] != null) && o.color == arr[i + j][z].color; j--)
               verCount++;

            if (Math.max(horCount, verCount) > 2) {
               combos++; // is combo?
               System.out.println("Found a combo! With color " + o.color + " At: " + i + ", " + z);
               removeComboFromBoard(z, i, arr, o.color);
            }
            System.out.println("x = " + z);
         }
         System.out.println("y = " + i);
      }
      wipeCombosFromBoard(arr);
      System.out.println("After removing: ");
      for(int a = 0; a < Y; a++){
         for(int b = 0; b < X; b++)
            System.out.print(arr[a][b]);
         System.out.println();
      }
      return combos;
   }

   /**
    * precondition: array has no nulls
    * @param x, y coordinates
    * @param arr, array that is being referenced
    * @param orig, only for its color
    */
   public void removeComboFromBoard(int x, int y, Orb[][] arr, int color) {
      if((y > Y-1 || y < 0) || (x > X-1 || x < 0)){
         return;
      }
      
      if(arr[y][x] != null && color == arr[y][x].color){
         arr[y][x].delete = true;
      }
      else
         return;
      
      int horCount = 0;
      int verCount = 0;
      
      for (int j = 1; ((x+j) < X && arr[y][x+j] != null) && color == arr[y][x + j].color; j++)
         horCount++;
      // scan left
      for (int j = -1; ((x+j) >= 0 && arr[y][x+j] != null) && color == arr[y][x + j].color; j--)
         horCount++;
      for (int j = 1; ((y+j) < Y && arr[y+j][x] != null) && color == arr[y + j][x].color; j++)
         verCount++;
      for (int j = -1; ((y+j) >= 0 && arr[y+j][x] != null) && color == arr[y + j][x].color; j--)
         verCount++;
      
      if (horCount > 2) {
         if (x < (X-2) && !arr[y][x+1].delete)
            removeComboFromBoard(x+1, y, arr, color);
         if (x > 0 && !arr[y][x-1].delete)
            removeComboFromBoard(x-1, y, arr, color);
      }
      if(verCount > 2) {
         if (y < (Y-2) && !arr[y+1][x].delete)
            removeComboFromBoard(x, y+1, arr, color);
         if (y > 0 && !arr[y-1][x].delete)
            removeComboFromBoard(x, y-1, arr, color);
      }
      
   }
   
   public void wipeCombosFromBoard(Orb[][] arr){
      for(int j = 0; j < Y; j++)
         for(int i = 0; i < X; i++)
            if(arr[j][i].delete)
               arr[j][i] = null;
   }

   public boolean skyFall(Orb[][] arr) {
      boolean flag = false;
      int rowU, rowD;
      // Y-2 because there are no gaps beneath
      for (int i = Y - 2; i >= 0; i--) {
         for (int j = X - 1; j >= 0; j--) {
            Orb orb = arr[i][j];
            rowU = rowD = i;
            if(orb == null)
               continue;
            
            if (arr[rowD + 1][j] == null) {// there's a gap
               //checks to see if the entire row is empty
               if(isRowEmpty(j, arr))
                  continue;
               flag = true;// there are skyfalls
               rowD++;
               swap(i, j, rowD, j, arr);
            }
         }
      }
      return flag;
   }
   
   public static boolean isRowEmpty(int x, Orb[][] arr){
      for(int check = 0; check < Y; check++)
         if(arr[check][x] != null)
            return false;
      return true;
   }

   public static ArrayList<ArrayList<Orb>> populateList(Orb[][] arr) {
      ArrayList<ArrayList<Orb>> list = new ArrayList<ArrayList<Orb>>();
      for (int i = 0; i < Y; i++){
         list.add(new ArrayList<Orb>());
         for (int j = 0; j < X; j++)
            list.get(i).add(arr[i][j]);
      }
      return list;
   }

   public static Orb[][] populateArray(ArrayList<ArrayList<Orb>> list) {
      Orb[][] arr = new Orb[Y][X];
      for (int i = 0; i < Y; i++)
         for (int j = 0; j < X; j++)
            arr[i][j] = list.get(i).get(j);
      return arr;
   }

   public static void swap(int y1, int x1, int y2, int x2, Orb[][] arr) {
      Orb temp, o1, o2;
      temp = o1 = arr[y1][x1];
      o2 = arr[y2][x2];
      arr[y1][x1] = o2;
      arr[y2][x2] = temp;
   }
}
