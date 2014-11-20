import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
      X = x;
      Y = y;
      for (int i = 0; i < Y; i++)
         for (int j = 0; j < X; j++)
            // Will run into null pointers in the future
            /*if (array[i][j] == null)
               throw new IllegalArgumentException();*/
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
            findPath(j, i, new Orb[Y][X], numOfMoves, (Orb) mArr[i][j].clone(),
                  "X " + i + "Y " + j + "|");
      for (Map.Entry<ArrayList<ArrayList<Orb>>, String> entry : map.entrySet()) {
         // loops through each possibility w/ minimal moves
         Orb[][] sequence = mergeArrs(populateArray(entry.getKey()));
         int combos = 0;
         for(int i = 0; i < Y; i ++){
            for(int j = 0; j < X; j ++)
               System.out.print(sequence[i][j]);
            System.out.println();
         }
         System.out.println(sequence);
         while (true) {
            combos += countCombos(sequence); // gives blank map to fill in
            if (!skyFall(sequence)) // means there are skyfalls
               break;
            System.out.println("counting");
         }
         if (combos > maxCombos) {
            System.out.println("Hey there was something that beat it!");
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

      // advance orb positions - remember to clone, not modify initial instance
      Orb next = arr[y][x] = (arr[y][x] != null) ? arr[y][x] : (Orb) mArr[y][x].clone();

      Orb temp = next;
      swap(prevY, prevX, y, x, arr);
      prev = temp;

      counter--;
      if (y > 0) // can go up
         findPath(x, y - 1, arr, counter, prev, dir + "U");
      if (x > 0) // can go left
         findPath(x - 1, y, arr, counter, prev, dir + "L");
      if (y < Y-1)
         findPath(x, y + 1, arr, counter, prev, dir + "D");
      if (x < X-1)
         findPath(x + 1, y, arr, counter, prev, dir + "R");
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
      for (int i = 0; i < Y; i++)
         for (int z = 0; z < X; z++) {
            Orb o = arr[i][z];
            int horCount = 0;
            int verCount = 0;
            
            if(o == null)
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
               System.out.println("Found a combo!");
               for(int a = 0; a < Y; a++){
                  for(int b = 0; b < X; b++){
                     System.out.print(arr[a][b]);
                  }
                  System.out.println();
               }
               removeComboFromBoard(i, z, arr, o.color);
            }
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
      try{
         if(arr[y][x] != null && color == arr[y][x].color)
            arr[y][x] = null;
         else
            return;
      }
      catch(Exception e){
         System.out.println(" x is " + x + " y is " + y);
         e.printStackTrace();
      }
      if (x < (X-2))
         removeComboFromBoard(x+1, y, arr, color);
      if (x > 0)
         removeComboFromBoard(x-1, y, arr, color);
      if (y < (Y-2))
         removeComboFromBoard(x, y+1, arr, color);
      if (y > 0)
         removeComboFromBoard(x, y-1, arr, color);
   }

   public boolean skyFall(Orb[][] arr) {
      boolean flag = false;
      int rowU, rowD, counterD;
      // Y-2 because there are no gaps beneath
      for (int i = Y - 2; i >= 0; i--) {
         for (int j = X - 1; j >= 0; j--) {
            Orb orb = arr[i][j];
            rowU = rowD = i;
            counterD = 0;

            if (arr[rowD + 1][j] == null) {// there's a gap
               flag = true;// there are skyfalls
               while (rowD+1 < Y && arr[++rowD][j] == null); //only for incrementing RowD until it reaches the end
               System.out.println("Swapping " + arr[rowD][j] + " with " + arr[i][j]);
               swap(i, j, rowD, j, arr);
               System.out.println("Swapped " + arr[rowD][j] + " with " + arr[i][j]);
            }
         }
      }
      return flag;
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
