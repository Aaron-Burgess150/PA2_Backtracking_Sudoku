/*  Aaron Burgess
    Mystic Sudoku
    COP3503 Computer Science 2
    SudokuSolver.java
*/

public class SudokuSolver {
    public SudokuSolver() {
    }

    public int solve(int[][] board, int[][] forbiddenPairs) {
        int[][] solvedBoard = new int[board.length][board[0].length];
        int solutions = solveSudokuUtil(board, forbiddenPairs, solvedBoard, 0, 0, (board[8][8] != 0));

        //copy the board back in in case of multiple solutions
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                board[i][j] = solvedBoard[i][j];

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

        int solutions = 0;

        if ((x == 8 && y == 8 && board[x][y] != 0) && lastFilled) //solved the puzzle
        {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    solvedBoard[i][j] = board[i][j];

            return 1;
        }

        //need to fill in cell
        if (board[x][y] == 0) {
            for (int i = 1; i <= 9; i++) //check a number in a cell
            {
                if (goodCell(board, forbiddenPairs, i, x, y)) //if i works
                {
                    board[x][y] = i;

                    if (x == 8 && y == 8) //done with the puzzle
                    {
                        //save solved board
                        for (int j = 0; j < 9; j++)
                            for (int k = 0; k < 9; k++)
                                solvedBoard[j][k] = board[j][k];

                        //revert board back to original
                        solutions += 1;
                        board[x][y] = 0;
                    } else //not done with the puzzle
                    {
                        //if it works on the next iteration, return | else backtrack
                        int number;
                        if (y == 8) //bottom of column
                        {
                            number = solveSudokuUtil(board, forbiddenPairs, solvedBoard, x + 1, 0, lastFilled);
                            solutions += number;
                            board[x][y] = 0;
                        } else //not bottom on column
                        {
                            number = solveSudokuUtil(board, forbiddenPairs, solvedBoard, x, y + 1, lastFilled);
                            solutions += number;
                            board[x][y] = 0;
                        }
                    }
                }
            }
        } else //alr filled, go to next unempty cell
        {
            if (y == 8)
                return solveSudokuUtil(board, forbiddenPairs, solvedBoard, x + 1, 0, lastFilled);
            else
                return solveSudokuUtil(board, forbiddenPairs, solvedBoard, x, y + 1, lastFilled);
        }

        return solutions;
    }

    public boolean goodCell(int[][] board, int[][] forbiddenPairs, int i, int x, int y) {
        //check row
        for (int j = 0; j < 9; j++)
            if (board[x][j] == i)
                return false;


        //check column
        for (int j = 0; j < 9; j++)
            if (board[j][y] == i)
                return false;

        //check subgrid
        //get starting and ending coordinates of the box
        int startingX = (x / 3) * 3;
        int startingY = (y / 3) * 3;
        int maxX = startingX + 2;
        int maxY = startingY + 2;
        if (!(goodMiniGrid(startingX, startingY, x, y, i, maxX, maxY, board)))
            return false;

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
                return false;

        //check forbidden pairs
        int[][] orthogonal = {{correctedCoordinate(x - 1), y}, {correctedCoordinate(x + 1), y},
                {x, correctedCoordinate(y - 1)}, {x, correctedCoordinate(y + 1)}};

        for (int j = 0; j < 4; j++)
            if (!(forbiddenPairsChecker(forbiddenPairs, board, orthogonal[j][0], orthogonal[j][1], i)))
                return false;

        //all constraints checked and hasn't returned false yet? wow
        return true;
    }

    public boolean goodMiniGrid(int startingX, int startingY, int x, int y, int i, int maxX, int maxY, int[][] board) {
        int temp = startingY;

        while (startingX <= maxX) //"for" loop with changing starting value
        {
            startingY = temp;
            while (startingY <= maxY) //"for" loop with changing starting value
            {
                if (startingX == x && startingY == y) //skip same cell bc obvi equal
                {
                    startingY++;
                    continue;
                }

                if (board[startingX][startingY] == i) //i in the minigrid
                    return false;

                startingY++;
            }
            startingX++;
        }
        return true;
    }

    public int correctedCoordinate(int coordinate) //see multiline comment for check knight moves
    {
        if (coordinate < 0)
            coordinate = 9 + coordinate;
        else if (coordinate > 8)
            coordinate = coordinate - 9;

        return coordinate;
    }

    public boolean forbiddenPairsChecker(int[][] forbiddenPairs, int[][] board, int newX, int newY, int i) {
        //either x or y will be unchanged
        int borderingNumber = board[newX][newY];
        if (borderingNumber == 0) return true;

        //go through every row of forbiddenPairs
        for (int j = 0; j < forbiddenPairs.length; j++) {
            int pair1 = forbiddenPairs[j][0];
            int pair2 = forbiddenPairs[j][1];

            if ((pair1 == borderingNumber && pair2 == i) || (pair2 == borderingNumber && pair1 == i))
                return false;
        }
        return true;
    }
}