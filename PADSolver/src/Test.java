import java.util.ArrayList;


public class Test {
   public static void main(String[] args){
      ArrayList<Orb[][]> list = new ArrayList<Orb[][]>();
      int x = 6;
      int y = 5;
      
      //Creates a randomly generated board
      for(int a = 0; a < 100; a++){
         Orb[][] orbArray = new Orb[5][6];
         for(int i = 0; i < y ; i++){
            for(int j = 0; j < x; j++){
               int num = (int) (Math.random()*5);
               Orb orb = new Orb(num);
               orbArray[i][j] = orb;
            }
         }
         list.add(orbArray);
      }
      //print the original random board
      for(int c = 0; c < 100; c++){
         Orb[][] arr = list.get(c);
         for(int i = 0; i < y; i++){
            for(int j = 0; j < x; j++)
               System.out.print(arr[i][j]);
            //System.out.println();
         }
         System.out.println();
      }
      PADSolver padsolver = new PADSolver("GRBDHRRBRRDRGGLDHGRDGDDLLDBRLG", x, y);
      padsolver.findSolutions(9);
      /*int combos = 0;
      for(int b = 0; b < 100; b++){
         combos = 0;
         PADSolver padsolver = new PADSolver(list.get(b), x, y);
         int count = 0;
         do{ //counts the number of combos 
            combos += padsolver.countCombos(list.get(b)); // gives blank map to fill in
            System.out.println("After:");
            for(int i = 0; i < y; i++){
               for(int j = 0; j < x; j++)
                  System.out.print(list.get(b)[i][j]);
               System.out.println();
            }
            count++;
         }while (padsolver.skyFall(list.get(b)));
         System.out.println("combos = " + combos);
      }*/
      /*int combos = 0;
      Orb[][] sequence = padsolver.mArr;
      do { //counts the number of combos
         combos += padsolver.countCombos(sequence); // gives blank map to fill in
      }while (padsolver.skyFall(sequence));
      //System.out.println("After");
      //System.out.println("\n" + padsolver.countCombos(arr));
      //padsolver.skyFall(arr);
      System.out.println("After, combo : " + combos + ":");
      for(int i = 0; i < y; i++){
         for(int j = 0; j < x; j++)
            System.out.print(padsolver.mArr[i][j]);
         System.out.println();
      }*/
   }
}
