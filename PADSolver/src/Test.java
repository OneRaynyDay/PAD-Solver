
public class Test {
   public static void main(String[] args){
      Orb[][] arr = new Orb[5][6];
      int x = 6;
      int y = 5;
      
      //Creates a randomly generated board
      for(int i = 0; i < y-1 ; i++){
         if(i == 3)
            continue;
         for(int j = 0; j < x; j++){
            int num = (int) (Math.random()*5);
            Orb orb = new Orb(num);
            arr[i][j] = orb;
         }
      }
      //print the original random board
      for(int i = 0; i < y; i++){
         for(int j = 0; j < x; j++)
            System.out.print(arr[i][j]);
         System.out.println();
      }
      System.out.println();
      PADSolver padsolver = new PADSolver(arr, x, y);
      //padsolver.findSolutions(5);
      //System.out.println("\n" + padsolver.countCombos(arr));
      padsolver.skyFall(arr);
      System.out.println("After:");
      for(int i = 0; i < y; i++){
         for(int j = 0; j < x; j++)
            System.out.print(arr[i][j]);
         System.out.println();
      }
      
   }
   
}
