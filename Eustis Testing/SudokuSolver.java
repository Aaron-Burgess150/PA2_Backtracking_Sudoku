/*  Aaron Burgess
    Mystic Sudoku
    COP3503 Computer Science 2
    SudokuSolver.java
*/

public class SudokuSolver {
    public SudokuSolver() {

    }

    public int solve(int[][] board, int[][] forbiddenPairs) {
        int[][] solvedBoard = new int[board.length][board[0].length]; // empty 9x9 array

        //System.out.println("Start: Inside solve method, going ot Util method");
        int solutions = solveSudokuUtil(board, forbiddenPairs, solvedBoard, 0, 0, (board[8][8] != 0));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = solvedBoard[i][j];
            }
        }
        //System.out.println("Util returned " + solutions + " solutions!");
        return solutions;
    }

    public int solveSudokuUtil(int[][] board, int[][] forbiddenPairs, int[][] solvedBoard, int x, int y, boolean lastFilled) {
        /*
            Things to have in backtracking algo:
                Base case
                Valid move
                Recursive calls
                Backtracking

            Sudoku grid solving:
                go down to bottom of column
                then next row at the top
        */

        int solutions = 0; // number of solutions

        if ((x == 8 && y == 8 && board[x][y] != 0) && lastFilled) {
            //System.out.println("We found the solution");
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    solvedBoard[i][j] = board[i][j];
                }
            }
            return 1;
        }
        
        //need to fill in cell
        if (board[x][y] == 0) // empty slot
        {
            //System.out.println("Empty slot in board[" + x + "][" + y + "]");
            for (int i = 1; i <= 9; i++) //check a number in a cell
            {
                //System.out.println("Trying " + i + " in cell");
                if (goodCell(board, forbiddenPairs, i, x, y))
                {
                    //System.out.println(i + " works in cell, placing in board");
                    board[x][y] = i;

                    if (x == 8 && y == 8) //done with the puzzle
                    {
                        //System.out.println("End of puzzle, saving solved board");
                        //save solved board
                        for (int j = 0; j < 9; j++)
                            for (int k = 0; k < 9; k++)
                                solvedBoard[j][k] = board[j][k];


                        //revert board back to original
                        //System.out.println("Backtracking after finding final solution");
                        solutions += 1;
                        board[x][y] = 0;
                    }
                    else //not done with the puzzle
                    {
                        //System.out.println("Not done with puzzle, keep going to next cell");
                        //if it works on the next iteration, return | else backtrack
                        int number;

                        if (y == 8) //bottom of column
                        {
                            //System.out.println("At the bottom of the column, go across and up");
                            number = solveSudokuUtil(board, forbiddenPairs, solvedBoard, x + 1, 0, lastFilled);
                            solutions += number;
                            board[x][y] = 0;
                        }
                        else //not bottom on column
                        {
                            //System.out.println("Not at bottom of column, go down");
                            number = solveSudokuUtil(board, forbiddenPairs, solvedBoard, x, y + 1, lastFilled);
                            solutions += number;
                            board[x][y] = 0;
                        }
                    }
                }
            }
        }
        else //alr filled, go to next cell. unempty slot
        {
            //System.out.println("Not empty slot in board[" + x + "][" + y + "]");
            if (y == 8) {
                //System.out.println("At the bottom of the column, go across and up");
                return solveSudokuUtil(board, forbiddenPairs, solvedBoard, x + 1, 0, lastFilled);
            }
            else
            {
                //System.out.println("Not at the bottom of the column, go down");
                return solveSudokuUtil(board, forbiddenPairs, solvedBoard, x, y + 1, lastFilled);
            }
        }

        //System.out.println("Returning " + solutions + " solutions");
        return solutions;
    }

    public boolean goodCell(int[][] board, int[][] forbiddenPairs, int i, int x, int y) {
        //System.out.println("In goodCell method to see if "+ i + " works in board[" + x + "][" + y + "]");

        //check row
        //System.out.println("Checking row of board[" + x + "][" + y + "] to see if " + i + " works");
        for (int j = 0; j < 9; j++)

                if (board[x][j] == i){
                    //System.out.println(i + " doesn't work in the row, returning false");
                    return false;
                }

        //check column
        //System.out.println("Checking column of board[" + x + "][" + y + "] to see if " + i + " works");
        for (int j = 0; j < 9; j++)

                if (board[j][y] == i)
                {
                    //System.out.println(i + " doesn't work in the column, returning false");
                    return false;
                }

        //check subgrid
        //System.out.println("Checking subgrid of board[" + x + "][" + y + "] to see if " + i + " works");
        int startingX = (x / 3) * 3;
        int startingY = (y / 3) * 3;
        int maxX = startingX + 2;
        int maxY = startingY + 2;
        //System.out.println("Subgrid starting coordinates are (" + startingX + ", " + startingY + ")");
        //System.out.println("Max coordinates of subgrid are (" + maxX + ", " + maxY + ")");
        if (!(goodMiniGrid(startingX, startingY, x, y, i, maxX, maxY, board)))
        {
            //System.out.println(i + " doesn't work in the minigrid, returning false");
            return false;
        }

        //check knight moves
        /*
            knight has 8 possible moves
            (+2, +1) | (+2, -1) | (-2, +1) | (-2, -1)
            (+1, +2) | (+1, -2) | (-1, +2) | (-1, -2)

            if there is a negative coordinate:
                real = 9 + (negative coordinate)

            if there is a coordinate over 8:
                real = (big coordinate) - 9
        */

        //System.out.println("Checking knight moves of board[" + x + "][" + y + "] to see if " + i + " works");
        //k[0] is the x coordinate, k[1] is the y coordinate
        int[][] k = {{correctedCoordinate(x + 2), correctedCoordinate(y + 1)},
                     {correctedCoordinate(x + 2), correctedCoordinate(y - 1)},
                     {correctedCoordinate(x - 2), correctedCoordinate(y + 1)},
                     {correctedCoordinate(x - 2), correctedCoordinate(y - 1)},
                     {correctedCoordinate(x + 1), correctedCoordinate(y + 2)},
                     {correctedCoordinate(x + 1), correctedCoordinate(y - 2)},
                     {correctedCoordinate(x - 1), correctedCoordinate(y + 2)},
                     {correctedCoordinate(x - 1), correctedCoordinate(y - 2)}};
        for (int j = 0; j < 8; j++)
            if (board[k[j][0]][k[j][1]] == i)
            {
                //System.out.println(i + " is at one of the knight moves, returning false");
                return false;
            }

        //check forbidden pairs
        //System.out.println("Checking forbidden pairs of board[" + x + "][" + y + "] to see if " + i + " works");
        int[][] orthogonal = {{correctedCoordinate(x - 1), y}, {correctedCoordinate(x + 1), y},
                              {x, correctedCoordinate(y - 1)}, {x, correctedCoordinate(y + 1)}};

        for (int j = 0; j < 4; j++)
            if (!(forbiddenPairsChecker(forbiddenPairs, board, orthogonal[j][0], orthogonal[j][1], i)))
            {
                //System.out.println(i + " is a forbidden pair, returning false");
                return false;
            }

        //all constraints checked and hasn't returned false yet? wow
        //System.out.println(i + " works in board[" + x + "][" + y + "] so returning true");
        return true;
    }

    public boolean goodMiniGrid(int startingX, int startingY, int x, int y, int i, int maxX, int maxY, int[][] board) {
        //System.out.println("In goodMiniGrid method to see if " + i + " works in subgrid of board[" + x + "][" + y + "]");
        //System.out.println("maxX: " + maxX + "; maxY: " + maxY);

        int temp = startingY;

        while (startingX <= maxX) //"for" loop with changing starting value
        {
            startingY = temp;
            //System.out.println("startingX: " + startingX);
            while (startingY <= maxY) //"for" loop with changing starting value
            {
                //System.out.println("startingY: " + startingY);
                if (startingX == x && startingY == y) //skip same cell bc obvi equal
                {
                    //System.out.println(i + " at board[" + x + "][" + y + "] so skipping");
                    startingY++;
                    continue;
                }

                if (board[startingX][startingY] == i)
                {
                    //System.out.println(i + " at board[" + startingX + "][" + startingY + "] so return false");
                    return false;
                }

                startingY++;
            }
            startingX++;
        }
        //System.out.println(i + " not in subgrid so returning true");
        return true;
    }

    public int correctedCoordinate(int coordinate) //see multiline comment for check knight moves
    {
        //System.out.println("correctedCoordinate method called");
        if (coordinate < 0)
            coordinate = 9 + coordinate;
        else if (coordinate > 8)
            coordinate = coordinate - 9;

        return coordinate;
    }

    public boolean forbiddenPairsChecker(int[][] forbiddenPairs, int[][] board, int newX, int newY, int numberBeingChecked) {
        //System.out.println("forbiddenPairsChecker method called");

        int borderingNumber = board[newX][newY];

        if (borderingNumber == 0) return true;

        for (int i = 0; i < forbiddenPairs.length; i++)
        {
            int pair1 = forbiddenPairs[i][0];
            int pair2 = forbiddenPairs[i][1];
            
            if ((pair1 == borderingNumber && pair2 == numberBeingChecked) || (pair2 == borderingNumber && pair1 == numberBeingChecked))
            {
                return false;
            }
        }
        return true;
    }
}
