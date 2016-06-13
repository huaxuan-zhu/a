## Programming Assignment 4: 8 Puzzle

[Assignment URL](https://class.coursera.org/algs4partI-010/assignment/view?assignment_id=5)

Key points:  
1. For storing the grid, use a 1D (1 by N^2) short array instead of a 2D (N by N) int array.
  * If using a 2D int array, Java would uses 32 + 32N + 4N^2 bytes. Because there are N^2 int, each of which uses 4 bytes. A int[N][N] array consists of N int[N] array, each of which uses 8 bytes as the object reference and 24 bytes as array overheads. And the constant 32 bytes is for the 1D array of N int[N] arrays.
  * If using a 1D short array, Java would only uses 32 + 2N^2 bytes. Because there are N^2 short, each of which uses only 2 bytes. And space used for array overhead and object reference is 32.

2. Use Manhattan distance over Hamming distance. Because many different grids may have the same Hamming distance while Manhattan distance is more specific. We can expect that we have to traverse much more blocks and occupying a lot more memory if using Hamming distance. 

3. Cache distance or priority when initializing a node. 
We need to calculate priority with distance and moves when initializing a node and we also need priority in `compareTo()` to implement the priority queue. 
Therefore,  we could save a lot of computation time if we cache the distance/priority at the initializing phase.

4. There are actually two process trees rooted at initial node and initial twin node. 
A boolean variable `Node.isTwin` is used to distinguish two kinds of nodes.
But we put all nodes in the same priority queue. 
We examine whether a problem is solvable by checking in which tree the final node that hits the goal is. 

5. We may exploit the fact that the difference in Manhattan distance between a board and a neighbor is either +1, -1, or 0, based on the direction that the block moves. 
But so far I haven't figure it out.

6. Exploit parity to prove the correctness of this algorithm. I didn't know the details. Maybe try to figure it out in the future.