import edu.princeton.cs.algs4.Queue;

public class Board {
    private static final int BLANK = 0;
    private int[][] blocks;
    private int N; // dimension

    public Board(int[][] blocks)           // construct a board from an N-by-N array of blocks
    {                                      // (where blocks[i][j] = block in row i, column j)
        N = blocks.length;
        // System.out.println(N);
        this.blocks = new int[N][N];
        // deep copy
        for (int i = 0; i < N; ++i)
        {
            this.blocks[i] = blocks[i].clone();
        }
    }

    public int dimension()                 // board dimension N
    {
        return N;
    }

    public int hamming()                   // number of blocks out of place
    {
        int dist = 0;
        for (int i = 0; i < N; ++i)
        {
            for (int j = 0; j < N; ++j)
            {
                if (blocks[i][j] == BLANK || blocks[i][j] == i*N + (j+1))
                    continue;
                else
                    dist++;
            }
        }
        return dist;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int dist = 0;
        // the value at [i, j] and its original coordinate
        int value, origin_i, origin_j;
        for (int i = 0; i < N; ++i)
        {
            for (int j = 0; j < N; ++j)
            {
                value = blocks[i][j];
                if (value == BLANK) continue; // encounter blank block
                origin_i = value / N;
                origin_j = value % N - 1; // range from -1 to N-2, where -1 shold be N-1
                if (origin_j == -1)
                {
                    origin_i -= 1;
                    origin_j = N - 1;
                }
                dist += Math.abs(i - origin_i) + Math.abs(j - origin_j);
            }
        }
        return dist;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        for (int i = 0; i < N; ++i)
        {
            for (int j = 0; j < N; ++j)
            {
                if (blocks[i][j] == BLANK || blocks[i][j] == i*N + (j+1))
                    continue;
                else
                    return false;
            }
        }
        return true;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        Board twin = new Board(blocks);    // Note this is a deep copy within constructor
        // find two blocks to swap, neither of which is blank square
        int i1 = 0, j1 = 0, i2 = 0, j2 = 0;
        outerloop:
        for (int i = 0; i < N; ++i)
        {
            for (int j = 0; j < N; ++j)
            {
                if (blocks[i][j] != BLANK)
                {
                    i1 = i;
                    j1 = j;
                    break outerloop;
                }
            }
        }
        outerloop:
        for (int i = N-1; i >= 0; --i)
        {
            for (int j = N-1; j >= 0; --j)
            {
                if (blocks[i][j] != BLANK)
                {
                    i2 = i;
                    j2 = j;
                    break outerloop;
                }
            }
        }
        twin.swapBlock(i1, j1, i2, j2);
        return twin;
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == null) return false;
        if (y == this) return true;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < N; ++i)
        {
            for (int j = 0; j < N; ++j)
            {
                if (this.blocks[i][j] != that.blocks[i][j]) return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Queue<Board> neighbors = new Queue<Board>();
        // find position of blank square
        int blank_i = 0, blank_j = 0;
        outerloop:
        for (int i = 0; i < N; ++i)
        {
            for (int j = 0; j < N; ++j)
            {
                if (blocks[i][j] == BLANK)
                {
                    blank_i = i;
                    blank_j = j;
                    break outerloop;
                }
            }
        }
        // swap along directions: up, right, down, left
        if (blank_i - 1 >= 0)
        {// up
            Board newBoard = new Board(blocks);
            newBoard.swapBlock(blank_i, blank_j, blank_i - 1, blank_j);
            neighbors.enqueue(newBoard);
        }
        if (blank_j + 1 < N)
        {// right
            Board newBoard = new Board(blocks);
            newBoard.swapBlock(blank_i, blank_j, blank_i, blank_j + 1);
            neighbors.enqueue(newBoard);
        }
        if (blank_i + 1 < N)
        {// down
            Board newBoard = new Board(blocks);
            newBoard.swapBlock(blank_i, blank_j, blank_i + 1, blank_j);
            neighbors.enqueue(newBoard);
        }
        if (blank_j - 1 >= 0)
        {// left
            Board newBoard = new Board(blocks);
            newBoard.swapBlock(blank_i, blank_j, blank_i, blank_j - 1);
            neighbors.enqueue(newBoard);
        }
        return neighbors;
    }

    // swap two blocks residing at [i1, j1] and [i1, j2]
    private void swapBlock(int i1, int j1, int i2, int j2)
    {
        int temp = blocks[i1][j1];
        blocks[i1][j1] = blocks[i2][j2];
        blocks[i2][j2] = temp;
        return;
    }

    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    {}
}
