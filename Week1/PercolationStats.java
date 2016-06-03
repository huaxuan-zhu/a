/*----------------------------------------------------------------
 *  Author:        hzhu007
 *
 *  Compilation:   javac-algs4 PercolationStats.java
 *  Execution:     java-algs4 PercolationStats
 *
 *  Perform Monte Carlo simulation to estimate
 *  the percolation threshold.
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double[] thresh;  // result for each iteration
    private double mean, std;
    private double ciL, ciH;  // confidence interval
    public PercolationStats(int N, int T)     // perform T independent experiments on an N-by-N grid
    {
        if(N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException();
        thresh = new double[T];
        for(int t = 0; t < T; ++t)
        {
            Percolation percObj = new Percolation(N);
            int count = 0;
            while(!percObj.percolates())
            {
                double rand = StdRandom.uniform() * (N*N);
                int i = (int)rand / N + 1;
                int j = (int)rand % N + 1;
                if(percObj.isOpen(i, j)) continue;
                percObj.open(i, j);
                ++count;
            }
            thresh[t] = count;
        }
        mean = StdStats.mean(thresh) / (N*N);
        std = StdStats.stddev(thresh) / (N*N);
        ciL = mean - 1.96 * std / Math.sqrt(T);
        ciH = mean + 1.96 * std / Math.sqrt(T);
    }
    public double mean()                      // sample mean of percolation threshold
    {
        return mean;
    }
    public double stddev()                    // sample standard deviation of percolation threshold
    {
        return std;
    }
    public double confidenceLo()              // low  endpoint of 95% confidence interval
    {
        return ciL;
    }
    public double confidenceHi()              // high endpoint of 95% confidence interval
    {
        return ciH;
    }
    public static void main(String[] args)    // test client (described below)
    {
        if(args.length != 2)
        {
            System.err.println("Wrong parameters.");
            System.exit(1);
        }
        int N = Integer.parseInt(args[0]), T = Integer.parseInt(args[1]);
        PercolationStats simProc = new PercolationStats(N, T);
        System.out.println("mean                    = " + simProc.mean());
        System.out.println("stddev                  = " + simProc.stddev());
        System.out.println("95% confidence interval = " +
                           simProc.confidenceLo() + ", " +
                           simProc.confidenceHi());
    }
}
