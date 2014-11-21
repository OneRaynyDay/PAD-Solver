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
               //throw new IllegalArgumentException(); //comment out if need testing
      mArr = array; //master array?
      map = new HashMap<ArrayList<ArrayList<Orb>>, String>();
   }

   /**
    * driver method
    */
   public void findSolutions(int numOfMoves) {
      int maxCombos = 0;
      ArrayList<ArrayList<Orb>> maxSequence = null;
      for (int i = 0; i < Y; i++){
         for (int j = 0; j < X; j++){
            // populates hashmap
            findPath(j, i, new Orb[Y][X], numOfMoves, j, i,
                  "Y " + i + "X " + j + "|");
         }
      }
      for (Map.Entry<ArrayList<ArrayList<Orb>>, String> entry : map.entrySet()) {
         // loops through each possibility w/ minimal moves
         Orb[][] sequence = mergeArrs(populateArray(entry.getKey()));
         int combos = 0;
         System.out.println("new entry");
         while (true) { //counts the number of combos 
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
    * @param int x - current column position
    * @param int y - current row position
    * @param Orb [][] arr - current board
    * @param int counter - number of moves remaining
    * @param int prevX - previous column position
    * @param int prevY - previous row position 
    * @param String dir - keeps track of orb path
    */
   public void findPath(int x, int y, Orb[][] arr, int counter, int prevX, int prevY,
         String dir) {
      // end of operations if last turn (base case)
      if (counter == 0){
         return;
      }
      
      //makes necessary changes to the board after each iteration
      arr[y][x] = (arr[y][x] != null) ? arr[y][x] : (Orb) mArr[y][x].clone();
      int nextY = y, nextX = x;
      swap(nextY, nextX, prevY, prevX, arr);
      prevY = nextY;
      prevX = nextX;

      counter--; //decrements the number of moves left
      if (y > 0) // can go up
         findPath(x, y - 1, arr, counter, prevX, prevY, dir + "U");
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
   
   //updates the master array
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
    * @param Orb[][] arr - current board state to analyze for combos
    * @return
    */
   public int countCombos(Orb[][] arr) {
      int combos = 0;
      for (int i = 0; i < Y; i++) { //rows
         for (int z = 0; z < X; z++) { //columns
            Orb o = arr[i][z];
            
            if(o == null || o.delete)
               continue;
            removeComboFromBoard(z, i, arr, o.color);
         }
      }
      combos = wipeCombosFromBoard(arr);
      System.out.println("After removing: ");
      for(int a = 0; a < Y; a++){
         for(int b = 0; b < X; b++)
            System.out.print(arr[a][b]);
         System.out.println();
      }
      System.out.println("combos amount : " + combos);
      return combos;
   }

   /**
    * precondition: array has no nulls
    * @param x, y coordinates
    * @param arr, array that is being referenced
    * @param orig, only for its color
    */
   public void removeComboFromBoard(int x, int y, Orb[][] arr, int color) {
      //check for cases
      if((y > Y-1 || y < 0) || (x > X-1 || x < 0) || arr[y][x] == null){
         return;
      }
      boolean flag = (isHorizontalCombo(x, y, arr) || isVerticalCombo(x, y, arr));
      if(flag && !arr[y][x].delete)
         arr[y][x].delete = true;
      else
         return;
      if(isHorizontalCombo(x, y, arr)){
         flag = true;
            removeComboFromBoard(x+1, y, arr, color);
            removeComboFromBoard(x-1, y, arr, color);
      }
      if(isVerticalCombo(x, y, arr)){
         flag = true;
            removeComboFromBoard(x, y+1, arr, color);
            removeComboFromBoard(x, y-1, arr, color);
      }
   }
   public boolean isHorizontalCombo(int x, int y, Orb[][] arr){
      int horCount = 0;
      for (int j = 1; ((x+j) < X && arr[y][x+j] != null) && arr[y][x].color == arr[y][x + j].color; j++)
         horCount++;
      // scan left
      for (int j = -1; ((x+j) >= 0 && arr[y][x+j] != null) && arr[y][x].color == arr[y][x + j].color; j--)
         horCount++;
      if(horCount >= 2)
         return true;
      else
         return false;
   }
   
   public boolean isVerticalCombo(int x, int y, Orb[][] arr){
      int verCount = 0;
      for (int j = 1; ((y+j) < Y && arr[y+j][x] != null) && arr[y][x].color == arr[y + j][x].color; j++)
         verCount++;
      for (int j = -1; ((y+j) >= 0 && arr[y+j][x] != null) && arr[y][x].color == arr[y + j][x].color; j--)
         verCount++;
      if(verCount >= 2)
         return true;
      else
         return false;
   }
   
   //run through the board the remove combos which have been marked for deletion
   public int wipeCombosFromBoard(Orb[][] arr){
      int combos = 0;
      for(int j = 0; j < Y; j++)
         for(int i = 0; i < X; i++)
            if(arr[j][i] != null && arr[j][i].delete){
               deleteCountCombo(j, i, arr, arr[j][i].color);
               combos++;
            }
      return combos;
   }
   public void deleteCountCombo(int y, int x, Orb[][] arr, int color){
      if(((y > Y-1 || y < 0) || (x > X-1 || x < 0)) || (arr[y][x] == null || arr[y][x].color != color)){
         return;
      }
      arr[y][x] = null;
      if((y < Y-1 && arr[y+1][x] != null) && (arr[y+1][x].color == color && arr[y+1][x].delete))//means we need to delete
         deleteCountCombo(y+1, x, arr, color);
      if((y > 0 && arr[y-1][x] != null) && (arr[y-1][x].color == color &&arr[y-1][x].delete))
         deleteCountCombo(y-1, x, arr, color);
      if((x < X-1 && arr[y][x+1] != null) && (arr[y][x+1].color == color &&arr[y][x+1].delete))
         deleteCountCombo(y, x+1, arr, color);
      if((x > 0 && arr[y][x-1] != null) && (arr[y][x-1].color == color &&arr[y][x-1].delete))
         deleteCountCombo(y, x-1, arr, color);
      
      
   }
   public boolean skyFall(Orb[][] arr) {
      boolean flag = false;
      int rowD;
      // Y-2 because there are no gaps beneath
      for (int i = Y - 2; i >= 0; i--) {
         for (int j = X - 1; j >= 0; j--) {
            Orb orb = arr[i][j];
            rowD = i;
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
   //populates an arraylist of arraylists given a 2D array
   public static ArrayList<ArrayList<Orb>> populateList(Orb[][] arr) {
      ArrayList<ArrayList<Orb>> list = new ArrayList<ArrayList<Orb>>();
      for (int i = 0; i < Y; i++){
         list.add(new ArrayList<Orb>());
         for (int j = 0; j < X; j++)
            list.get(i).add(arr[i][j]);
      }
      return list;
   }
   //populates a 2D array given an arraylist of arraylists
   public static Orb[][] populateArray(ArrayList<ArrayList<Orb>> list) {
      Orb[][] arr = new Orb[Y][X];
      for (int i = 0; i < Y; i++)
         for (int j = 0; j < X; j++)
            arr[i][j] = list.get(i).get(j);
      return arr;
   }
   //swaps two orbs given both x-y positions
   public static void swap(int y1, int x1, int y2, int x2, Orb[][] arr) {
      Orb temp, o1, o2;
      temp = o1 = arr[y1][x1];
      o2 = arr[y2][x2];
      arr[y1][x1] = o2;
      arr[y2][x2] = temp;
   }
}
