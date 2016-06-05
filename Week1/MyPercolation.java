public class MyPercolation {
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

    public MyPercolation(int N)               // create N-by-N grid, with all sites blocked
    {
        if(N <= 0)
            throw new java.lang.IllegalArgumentException();
        grid = new boolean[N * N];
        status = new byte[N * N];
        for(int i = 0; i < N * N; ++i)
        {
            grid[i] = false;
            if(i < N)           // every site on top is connected to top
                status[i] |= TOTOP;
            if(i >= (N-1)*N)    // every site on bottom is connected to bottom
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
    {
        return isPercolate;
    }
    private class WeightedQuickUnionUF
    {
        private int[] rootId; // the root of the node
        private int[] count;  // the number of nodes of the tree, which the node is in

        public WeightedQuickUnionUF()
        {
            rootId = count = null;
        }
        public WeightedQuickUnionUF(int N)
        {
            rootId = new int[N];
            for(int i = 0; i < N; ++i)
            {
                rootId[i] = i;
                count[i] = 1;
            }
        }
        public int find(int p)
        {
            int root = p;
            // Make every other node in the path point to its grandparent node.
            // If a node only has a parent node, doesn't change.
            // So that flaten the tree
            while(root != rootId[root])
            {
                rootId[root] = rootId[rootId[root]];
                root = rootId[root];
            }
            return root;
        }
        public boolean connected(int p, int q)
        {
            return find(p) == find(q);
        }
        public void union(int p, int q)
        {
            int rootP, rootQ;
            rootP = find(p);
            rootQ = find(q);
            // link root of smaller tree to root of larger tree
            if(count[rootP] > count[rootQ])
            {
                count[rootP] += count[rootQ];
                rootId[rootQ] = rootP;
            }
            else
            {
                count[rootQ] += count[rootP];
                rootId[rootP] = rootQ;
            }
        }
    }
    public static void main(String[] args)  // test client (optional)
    {}
}
