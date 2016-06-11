import edu.princeton.cs.algs4.Queue;

public class Board {
    private static final short BLANK = 0; // blank square
    private short[] blocks; // compress 2d int blocks to 1d short block
    private final int N;    // dimension
    public Board(int[][] blocks)           // construct a board from an N-by-N array of blocks
    {
        N = blocks.length;
        this.blocks = new short[N*N];
        // deep copy
        for (int i = 0; i < N; ++i)
        {
            for (int j = 0; j < N; ++j)
            {
                this.blocks[i*N + j] = (short) blocks[i][j];
            }
        }
    }

    public int dimension()                 // board dimension N
    {
        return N;
    }

    public int hamming()                   // number of blocks out of place
    {
        int dist = 0;
        for (int i = 0; i < N * N; ++i)
        {
            if (blocks[i] != BLANK && blocks[i] != i + 1)
                dist++;
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
                value = blocks[i*N + j];
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
        for (int i = 0; i < N * N; ++i)
        {
            if (blocks[i] != BLANK && blocks[i] != i + 1)
                return false;
        }
        return true;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        int[][] blocks2D = new int[N][N];
        for (int i = 0; i < N; ++i)
        {
            for (int j = 0; j < N; ++j)
            {
                blocks2D[i][j] = blocks[i*N + j];
            }
        }
        Board twin = new Board(blocks2D);    // Note this is a deep copy within constructor
        // find two blocks to swap, neither of which is blank square
        int i1, i2;
        for (i1 = 0; i1 < N * N; ++i1)
        {
            if (blocks[i1] != BLANK)
                break;
        }
        for (i2 = N*N - 1; i2 >= 0; --i2)
        {
            if (blocks[i2] != BLANK)
                break;
        }
        twin.swapBlock(i1, i2);
        return twin;
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == null) return false;
        if (y == this) return true;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < N * N; ++i)
        {
            if (this.blocks[i] != that.blocks[i]) return false;
        }
        return true;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Queue<Board> neighbors = new Queue<Board>();
        // find position of blank square
        int blankPos, blank_i, blank_j;
        for (blankPos = 0; blankPos < N * N; ++blankPos)
        {
            if (blocks[blankPos] == BLANK) break;
        }
        blank_i = blankPos / N;
        blank_j = blankPos % N;
        int[][] blocks2D = new int[N][N];
        for (int i = 0; i < N; ++i)
        {
            for (int j = 0; j < N; ++j)
            {
                blocks2D[i][j] = blocks[i*N + j];
            }
        }
        // swap along directions: up, right, down, left
        if (blank_i - 1 >= 0)
        {// up
            Board newBoard = new Board(blocks2D);
            newBoard.swapBlock(blankPos, blankPos - N);
            neighbors.enqueue(newBoard);
        }
        if (blank_j + 1 < N)
        {// right
            Board newBoard = new Board(blocks2D);
            newBoard.swapBlock(blankPos, blankPos + 1);
            neighbors.enqueue(newBoard);
        }
        if (blank_i + 1 < N)
        {// down
            Board newBoard = new Board(blocks2D);
            newBoard.swapBlock(blankPos, blankPos + N);
            neighbors.enqueue(newBoard);
        }
        if (blank_j - 1 >= 0)
        {// left
            Board newBoard = new Board(blocks2D);
            newBoard.swapBlock(blankPos, blankPos - 1);
            neighbors.enqueue(newBoard);
        }
        return neighbors;
    }

    // swap two blocks residing at [i1, j1] and [i1, j2]
    private void swapBlock(int i1, int i2)
    {
        short temp = blocks[i1];
        blocks[i1] = blocks[i2];
        blocks[i2] = temp;
        return;
    }

    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N*N; i++) {
            s.append(String.format("%2d ", blocks[i]));
            if (i % N  == N - 1)
                s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    {}
}
