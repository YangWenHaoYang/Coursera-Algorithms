/******************************************************************************
 *  Name:    Yang Wenhao
 *  NetID:   yangwh
 *  Precept: P01
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 * 
 *  Description:  Modeling Percolation using an N-by-N grid and Union-Find data 
 *                structures to determine the threshold. woot. woot.
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
// import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code PercolationStats} class determines the threshold 
 * of {@code Percolation} model by Monte Carlo simulation.
 * 
 * @author Yang Wenhao
 */
public class PercolationStats {
    private static final double CONST = 1.96;
    // recording the number of open sites when system Percolats
    private final double[] trials;
    private final int numberOfTrials;
    
    
    /**
     * Perform {@code T} independent experiments on
     * {@code N} by {@code N} grid.
     * 
     * @param N the number of sites in one row of grid
     * @param T the number of independent experiments
     * @throws IllegalArgumentException if {@code N <= 0} 
     * or {@code T <= 0}
     */
    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0)
            throw new IllegalArgumentException("argument is illegal!");
        trials = new double[t];
        numberOfTrials = t;
        for (int i = 0; i < t; i++) {
             Percolation perc = new Percolation(n);
             while (!perc.percolates()) {
                 int posX, posY;
                 do {
                     posX = StdRandom.uniform(n) + 1;
                     posY = StdRandom.uniform(n) + 1;
                 } while (perc.isOpen(posX, posY));
                 perc.open(posX, posY);
             }
             trials[i] =  perc.numberOfOpenSites() / (double) (n*n);
        }
    }
    
    /**
     * Returns sample mean of percolation threshold
     * 
     * @return sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(trials);
    }
    
    /**
     * Returns sample standard deviation of percolation threshold
     * 
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        if (numberOfTrials == 1)
            return Double.NaN;
        else
           return StdStats.stddev(trials);
    }
    
    /**
     * Returns low endpoint of 95% confidence interval
     * 
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        double confidenceLow = mean() - CONST * stddev() / Math.sqrt(numberOfTrials);
        return confidenceLow;
    }
    
    /**
     * Returns high endpoint of 95% confident interval
     * 
     * @return high endpoint of 95% confident interval
     */
    public double confidenceHi() {
        double confidenceHigh = mean() + CONST * stddev() / Math.sqrt(numberOfTrials);
        return confidenceHigh;
    }
    
    /**
     * Read two integer from standard input, where
     * 1st interger represents the size of grid and
     * 2nd integer represents the number of independent
     * experiments. Print the mean, standard deviation 
     * and confident interval (95% confidence) of sample.
     * 
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // int n = Integer.parseInt(args[0]);
        // int numberOfTrials = Integer.parseInt(args[1]);
        
        // PercolationStats percolationStats = new PercolationStats(n, numberOfTrials);
        // StdOut.println("sample mean of percolation threshold is " + mean());
        // StdOut.println("stdDev of percolation threshold is " + stdDev());
        // StdOut.println("95% confident interval is")
        // StdOut.println("[" + confidenceLo() + ", " + confidenceHi() + "]");
    }    
}
