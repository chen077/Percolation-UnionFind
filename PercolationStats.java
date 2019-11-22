/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    Stopwatch sw = new Stopwatch();
    private int n;
    private int trials;
    private double[] thresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if(n <=0 || trials <= 0) throw  new IllegalArgumentException();

        this.n = n;
        this.trials = trials;
        thresholds = new double[trials];

        // Create T instances of new Percolation objects of size N.
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            // Keep count of how many nodes we have opened
            int openCount = 0;

            // Continue to open nodes until the grid percolates
            while (!p.percolates()) {
                openRandomNode(p);
                openCount++;
            }

            // And store the result in our array to run queries over it later. Convert to double
            thresholds[i] = (double) openCount / (n * n);
        }
    }

    private void openRandomNode(Percolation p) {
        boolean openNode = true;
        int row = 0;
        int col = 0;

        // Repeat until we randomly find a closed node
        while (openNode) {
            // Generate a random index between 1 and N + 1 (1-based grid, remember)
            row = StdRandom.uniform(1, n + 1);
            col = StdRandom.uniform(1, n + 1);

            openNode = p.isOpen(row, col);
        }

        // If we reach here then we know that p[row, col] is an open node
        p.open(row, col);
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean() - ((1.96 * stddev()) / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean() + ((1.96 * stddev()) / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[1]);

        PercolationStats s = new PercolationStats(n, trails);

        System.out.println("mean:\t\t\t\t = " + s.mean());
        System.out.println("stddev:\t\t\t\t = " + s.stddev());
        System.out.println("95% confidence interval:\t = " + s.confidenceLo() + ", " + s.confidenceHi());
    }
}
