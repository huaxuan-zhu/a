import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // used to accesse surrounding sites
    private static final int[] dx = {0, 1, 0, -1};
    private static final int[] dy = {1, 0, -1, 0};
    // status for each site: blocked, open, connected to the top and to the bottom
    // a site can maintain manifold statuses using bit operation
    private static final byte BLOCKED = 0b000;
    private static final byte OPEN    = 0b001;
    private static final byte TOTOP   = 0b010;
    private static final byte TOBTTM  = 0b100;

    private boolean[] grid;
    private int N;  // dimension
    private WeightedQuickUnionUF qufObj;
    private byte[] status;
    private boolean isPercolate = false;
    public Percolation(int N)               // create N-by-N grid, with all sites blocked
    {
        if(N <= 0)
            throw new java.lang.IllegalArgumentException();
        grid = new boolean[N * N];
        status = new byte[N * N];
        for(int i = 0; i < N * N; ++i)
        {
            grid[i] = false;
            if(i < N)        // every site on top is connected to top
                status[i] |= TOTOP;
            if(i >= (N-1)*N) // every site on bottom is connected to bottom
                status[i] |= TOBTTM;
        }
        this.N = N;
        qufObj = new WeightedQuickUnionUF(N*N);
    }

    public void open(int i, int j)          // open site (row i, column j) if it is not open already
    {
        if (i < 1 || j < 1 || i > N || j > N)
            throw new java.lang.IndexOutOfBoundsException();
        int ni = i - 1, nj = j - 1;
        if(grid[ni*N + nj])  // if already open
            return;
        grid[ni*N + nj] = true;
        status[ni*N + nj] |= OPEN;
        byte statusTemp = BLOCKED;  // status for the roots of neighbors
        for(int k = 0; k < 4; ++k)
        {//union adjacent sites
            int mi = ni + dx[k], mj = nj + dy[k];  // index of neighbor site
            if(mi >= 0 && mi <= N - 1 && mj >= 0 && mj <= N - 1
                && grid[mi*N + mj])
            {
                int neighborRoot = qufObj.find(mi*N + mj);
                statusTemp |= status[neighborRoot];
                qufObj.union(mi*N + mj, ni*N + nj);
            }
        }
        statusTemp |= status[ni*N + nj];
        int newRoot = qufObj.find(ni*N + nj); // new root after union operations
        // status of newRoot is determined by the statuses of the roots of four
        // neighbors and the status of newly opened site
        status[newRoot] |= statusTemp;
        if(status[newRoot] == (TOTOP | TOBTTM | OPEN)) isPercolate = true;
    }
    public boolean isOpen(int i, int j)     // is site (row i, column j) open?
    {
        if (i < 1 || j < 1 || i > N || j > N)
            throw new java.lang.IndexOutOfBoundsException();
        int ni = i - 1, nj = j - 1;
        return grid[ni*N + nj];
    }
    public boolean isFull(int i, int j)     // is site (row i, column j) full?
    {
        if (i < 1 || j < 1 || i > N || j > N)
            throw new java.lang.IndexOutOfBoundsException();
        int ni = i - 1, nj = j - 1;
        int root = qufObj.find(ni*N + nj);
        return grid[ni*N + nj] &&
            (status[root] & TOTOP) > 0;
    }
    public boolean percolates()             // does the system percolate?
    {// percolate iff virtual top and bottom are connected
        return isPercolate;
    }

    public static void main(String[] args)  // test client (optional)
    {}
}
