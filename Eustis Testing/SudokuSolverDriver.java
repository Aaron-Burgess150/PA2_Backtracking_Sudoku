/*	INSERT STUDENT NAME
	Solving Sudoku!
	COP3503 Computer Science 2
	SudokuSolverDriver.java
	Compile: javac SudokuSolverDriver.java
	Run: java SudokuSolverDriver [CASE]
*/

import java.io.*;
import java.util.*;

public class SudokuSolverDriver 
{
    public static void main(String[] args) 
	{
        if (args.length == 0) 
		{
            System.err.println("Usage: java SudokuSolverDriver <CASE>");
            return;
        }

        int testCase = Integer.parseInt(args[0]);
        int boardSize = 9;

        int forbiddenPairsCount;

        try(Scanner sc = new Scanner(new File("inputset" + testCase + ".in")))
        {
            //Read number of puzzles
            int n = sc.nextInt();

            //Read board; 9 rows of 9 space separated integers (0-9), for each puzzle
            for (int k = 1; k <= n; k++) 
			{
                int[][] board = new int[boardSize][boardSize];
				
                for (int i = 0; i < boardSize; i++)
                    for (int j = 0; j < boardSize; j++)
                        board[i][j] = sc.nextInt();

                forbiddenPairsCount = sc.nextInt();
				
                int[][] forbiddenPairs = new int[forbiddenPairsCount][2];
				
                for(int i=0; i < forbiddenPairsCount; i++)
                    for(int j=0; j < 2; j++)
                        forbiddenPairs[i][j] = sc.nextInt();

                SudokuSolver solver = new SudokuSolver();
                System.out.println("Puzzle " + k + ":");
                int solutionCount = solver.solve(board, forbiddenPairs);
                if (solutionCount == 1) 
				{
					for (int[] row : board) 
					{
                        for (int num : row)
                            System.out.print(num + " ");
						
                        System.out.println();
                    }
                }
                else if(solutionCount > 1)
				{
                    System.out.println(solutionCount + " solutions found.");
                } 
                else 
				{
                    System.out.println("No solution possible.");
                }
				
                System.out.println();
				
            }
            sc.close();
        } 
        catch (FileNotFoundException e) 
        {
            System.err.println("Could not open file \"inputset" + testCase + ".in\"");
            return;
        }
    }
}
