package com.shashi.practise.ds.graph;

import java.util.Scanner; 

public class Islands {
    
    static int ROW, COL;
    
    public static void main (String[] args) {
        
        Scanner scan = new Scanner(System.in); 
        System.out.println("Please enter number of Rows :");
        ROW = scan.nextInt();
        System.out.println("Please enter number of Columns :");
        COL = scan.nextInt();
        
        int[][] mat = new int[ROW][COL];
        
        System.out.println("Enter the matrix elements (Right to Left, Top to bottom) :");
        
        for(int i=0; i<ROW; i++){
            for(int j=0; j<COL; j++){
                mat[i][j]=scan.nextInt();
            }
        }
        
        scan.close();
        
       //Alternative Testing
       /*ROW = 4;
       COL = 5;
       int mat[][]=  new int[][] {{1, 1, 0, 0, 0},
                                 {1, 1, 0, 0, 0},
                                 {0, 0, 1, 0, 0},
                                 {0, 0, 0, 1, 1}
                                };*/
                                
        Islands islands = new Islands();
        System.out.println("Number of islands : "+ islands.countIslands(mat));
 
   }
   
    // This function counts the number of islands in the given matrix
    // by utilising DFS traversal algorithm
    int countIslands(int mat[][])
    {
        boolean visited[][] = new boolean[ROW][COL];
        int count = 0;
        
        for (int i = 0; i < ROW; ++i)
            for (int j = 0; j < COL; ++j)
                if (mat[i][j]==1 && !visited[i][j]) {
                    DFS(mat, i, j, visited);
                    ++count;
                }
        return count;
    }
 
 
    // Recursive DFS 
    void DFS(int mat[][], int row, int col, boolean visited[][])
    {
        int rowNo[] = new int[] {-1, 0, 0, 1};
        int colNo[] = new int[] {0, -1, 1, 0};
        visited[row][col] = true;
 
        // recursive call for all neighbors with value 1
        for (int k = 0; k < 4; ++k){
            int nbrRow = row+rowNo[k];		//neighbor row index
            int nbrCol = col+colNo[k];		//neighbor col index
            if(nbrRow>=0 && nbrRow<ROW && nbrCol>=0 && nbrCol<COL 
                                && mat[nbrRow][nbrCol]==1 && !visited[nbrRow][nbrCol])
                DFS(mat, nbrRow, nbrCol, visited);
        }
    }
  
}
