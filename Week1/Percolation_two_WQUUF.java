import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // used to accesse surrounding sites
    private static final int[] dx = {0, 1, 0, -1};
    private static final int[] dy = {1, 0, -1, 0};
    private boolean[] grid;
    private int N;  // dimension
    private WeightedQuickUnionUF qufObj, qufObjBackWash;

    public Percolation(int N)               // create N-by-N grid, with all sites blocked
    {
        if(N <= 0)
            throw new java.lang.IllegalArgumentException();
        grid = new boolean[N * N];
        for(int i = 0; i < N * N; ++i)
            grid[i] = false;
        this.N = N;
        // add two virtual site: a virtual top and a virtual bottom
        // percolates iff virtual top site is connected to virtual bottom site.
        // N*N - top; N*N + 1 - bottom
        qufObjBackWash = new WeightedQuickUnionUF(N*N + 2);
        // the second WQUF only has virtual top to get rid of the back wash problem
        // http://coursera.cs.princeton.edu/algs4/checklists/percolation.html
        qufObj = new WeightedQuickUnionUF(N*N + 1);
    }
    public void open(int i, int j)          // open site (row i, column j) if it is not open already
    {
        if (i < 1 || j < 1 || i > N || j > N)
            throw new java.lang.IndexOutOfBoundsException();
        int ni = i - 1, nj = j - 1;
        if(grid[ni*N + nj])
            return;
        grid[ni*N + nj] = true;
        for(int k = 0; k < 4; ++k)
        {//union adjacent sites
            int mi = ni + dx[k], mj = nj + dy[k];
            if(mi >= 0 && mi <= N - 1 && mj >= 0 && mj <= N - 1
                && grid[mi*N + mj])
            {
                qufObj.union(mi*N + mj, ni*N + nj);
                qufObjBackWash.union(mi*N + mj, ni*N + nj);
            }
        }
        if(i == 1)  // virtual top site
        {
            qufObj.union(ni*N + nj, N*N);
            qufObjBackWash.union(ni*N + nj, N*N);
        }
        if(i == N)  // virtual bottom site
            qufObjBackWash.union(ni*N + nj, N*N+1);
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
        return grid[ni*N + nj] && qufObj.connected(ni*N + nj, N * N);
    }
    public boolean percolates()             // does the system percolate?
    {// percolate iff virtual top and bottom are connected
        return qufObjBackWash.connected(N * N, N*N + 1);
    }

    public static void main(String[] args)  // test client (optional)
    {}
}
