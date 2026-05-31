Rules
• Each row must contain the digits 1 through 9 exactly once.
• Each column must contain the digits 1 through 9 exactly once.
• Each 3x3 subgrid must contain the digits 1 through 9 exactly once. Subgrids do not wrap
around the edges.
• No two identical digits may be placed in cells that are a chess knight’s move apart (i.e.,
offsets (±2,±1) and (±1,±2)).
• The board wraps at the edges. When checking neighbors (including knight moves and
orthogonal adjacency), row/column indices must be wrapped around the edges. For ex-
ample, from (0,0), a knight move of (-2,-1) refers to cell (7,8). Wrap-around applies only to
neighborhood checks (knight moves and orthogonal adjacency). Row, column, and 3×3
subgrid checks do not wrap.
• After each 9×9 grid, K pairs of digits (unordered) will be given. For any pair (a,b), no
cell containing a can be orthogonally adjacent (up, down, left, right) to a cell containing b,
and vice versa. Orthogonal adjacency checks also wrap around the edges. Treat each
forbidden pair as unordered: if (a,b) is listed, then a cannot be orthogonally adjacent to b,
and b cannot be orthogonally adjacent to a. Note: Orthogonal adjacency refers only to
the four neighbors: up, down, left, and right (with wrap-around). Diagonal neighbors do
not count.

Inputs
The input files provided with this assignment contain the inputs. Each input file contains the
following information:
• The first line contains a positive integer n, the number of puzzles.
• Each puzzle consists of:
– 9 lines, each with 9 space-separated integers in [0-9], 0 represents an empty cell.
– 1 line describing forbidden pairs: the first integer is K (0 ≤ K ≤ 20), followed by 2 · K
integers representing the unordered pairs (a1, b1), . . . , (aK , bK ), where each digit is
in [1–9].
The input from the files will be handled by the provided driver file, and only a two-dimensional
integer array board[][] of size 9 × 9, representing the Sudoku puzzle, and a two-dimensional
integer array forbiddenPairs[][] of size K × 2, will be provided to your methods to solve.

Output
For this assignment, the Driver file will handle the output process. Your task is to return the
number of solution exists through the “solve” function. For each puzzle, “Puzzle k:” (without the
quotes) will be printed where k is the puzzle number. Then,
• If a UNIQUE solution is found, the completed grid in 9 lines will be printed. Each line will
contain space-separated 9 numbers.
• If more than one solution is found, the number of solutions found will be printed.
• If no solution exists, “No solution possible.” (without the quotes) will be printed.

Commands to compile and test code:
javac SudokuSolver.java SudokuSolverDriver.java
java SudokuSolverDriver test-case-number
