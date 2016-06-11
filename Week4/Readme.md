## Programming Assignment 4: 8 Puzzle

[Assignment URL](https://class.coursera.org/algs4partI-010/assignment/view?assignment_id=5)

Key points:  
1. For storing the grid, use a 1D (1 by N^2) short array instead of a 2D (N by N) int array.
  * If using a 2D int array, Java would uses 32 + 32N + 4N^2 bytes. Because there are N^2 int, each of which uses 4 bytes. A int[N][N] array consists of N int[N] array, each of which uses 8 bytes as the object reference and 24 bytes as array overheads. And the constant 32 bytes is for the 1D array of N int[N] arrays.
  * If using a 1D short array, Java would only uses 32 + 2N^2 bytes. Because there are N^2 short, each of which uses only 2 bytes. And space used for array overhead and object reference is 32.  

Therefore, when N is large or a lot of grids need to be stored, we can reduce the amount of memory significantly by using a 1D short array.

2. 