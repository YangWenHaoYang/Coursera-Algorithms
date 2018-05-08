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

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
    
/**  
 * The {@code Percolation} class represents a 
 * percolation model. 
 * It supports the open operation, along with 
 * methods for determining whether a site is open,  
 * full, the system percolates and the number 
 * of open sites.
 * This implementation relays on weighted quick union 
 * method (implemented)
 * 
 * @author Yang Wenhao
 */
public class Percolation {
    private static final int NUMBER_OF_DIRECTIONS = 4;
    // 1D mapping grid containing 2 virual node mapped by 2D grid 
    private final WeightedQuickUnionUF mappingGrid;  
    // 1D mapping grid containing 1 virual node(top) mapped by 2D grid 
    private final WeightedQuickUnionUF mappingGridTop;
    // an array record if the site is open
    private boolean[] ifopen; 
    // the length of percolation model
    private final int length;   
    // number of open sites
    private int numberOfOpenSites;   
 
    
    /**
     * Initializes two empty WeightedQuickUnionUF data 
     * structure, one with {@code n*n+2} sites , the other with
     * {@code n*n+1} sites
     * In order to reduce the complexity of method of percolates, 
     * we add extra 2 virtual node on two sides (above and below) 
     * in the grid to help us determine whether this system 
     * percolates.
     * To avoid arising backwash, we introduce the other grid 
     * which contains only one virtual node above.
     * 
     * @param n the number of sites in one row
     * @throws IllegalArgumentException if {@code n < 1}
     */
    public Percolation(int n) {  
        if (n < 1) 
            throw new IllegalArgumentException("number " + n + " is less than 1 ");
        length = n;
        mappingGrid = new WeightedQuickUnionUF(n*n+2);
        mappingGridTop = new WeightedQuickUnionUF(n*n+1);
        ifopen = new boolean[n*n+2];
        for (int i = 0; i < n*n+2; i++)
            ifopen[i] = false;
        ifopen[0] = true;    // set state of virual site (above) to be open
        ifopen[n*n+1] = true;    // set state of virtual site (below) to be open
        numberOfOpenSites = 0;    // initialize the number of open sites to 0
    }
    

     // Returns an index corrsponding to 2D coordinates in percolation
     private int coordinateMapping(int row, int col) {
        int index;    // index corrsponding to 2D coordinates 
        index = (row - 1) * length + col;
        return index;
    }
     
    // validate that p is a valid index 
    private void validate(int row, int col) {
        if ((row < 1 || row > length) || (col < 1 || col > length)) {
            throw new IllegalArgumentException("input row or col is not illegal!");
        }
    }
    
    /**
     * Opens the site determined by {@code row} and {@code col}.
     * 
     * @param row the integer representing row coordinate
     * @param column the integer representing column coordinate
     * @throws IllegalArgumentException unless
     *         both {@code 1 <= row <= n} and {@code 1 <= col <= n}
     */
    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col))
            return;
        int index = coordinateMapping(row, col);
        int adjacentIndex;
        int posX, posY;
        int[] directionX = {0, 1, 0, -1};
        int[] directionY = {1, 0, -1, 0};
        ifopen[index] = true;
        if (row == 1) {
            // merges site determined by index with virtual site (above)
            mappingGrid.union(0, index); 
            mappingGridTop.union(0, index);  
        } 
        if (row == length) {
            // merges site determined by index with virtual site (below)
            mappingGrid.union(length*length+1, index);    
        } 
        for (int i = 0; i < NUMBER_OF_DIRECTIONS; i++) {
            posX = row + directionX[i];
            posY = col + directionY[i];
            if (posX >= 1 && posX <= length && posY >= 1 && posY <= length) {
                // merges site with adjacent site if it is open
                if (isOpen(posX, posY)) {
                    adjacentIndex = coordinateMapping(posX, posY);
                    mappingGrid.union(adjacentIndex, index); 
                    mappingGridTop.union(adjacentIndex, index); 
                }
            }
        }
        numberOfOpenSites++;      
    }
    
    /**
     * Returns true if the state of site which is determined by {@code row} and 
     * {@code col} is already open.
     * @param row the integer representing row coordinate
     * @param column the integer representing column coordinate
     * @return {@code true} if the site state is open;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless
     *         both {@code 1 <= row <= n} and {@code 1 <= col <= n}
     */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        int index = coordinateMapping(row, col);
        return ifopen[index];
    }
    
    /**
     * Returns true if the site determined by {@code row} and {@code col}
     * is full.
     * @param row the integer representing row coordinate
     * @param column the integer representing column coordinate
     * @return {@code true} if the site is full;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless
     *         both {@code 1 <= row <= n} and {@code 1 <= col <= n}
     */
    public boolean isFull(int row, int col) {
        validate(row, col);
        int index = coordinateMapping(row, col);
        if (isOpen(row, col) && mappingGridTop.connected(0, index))
            return true;
        return false;
    }
    
    /**
     * Returns the number of open sites
     * 
     * @return the number of open sites (between {@code 1} and {code n*n})
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }
    
    /**
     * Returns true if this system percolates
     * 
     * @return {@code true} if this system percolates
     *         {@code false} otherwise
     */
    public boolean percolates() {
        return mappingGrid.connected(0, length*length+1);
    }
    
    public static void main(String[] args) {
        // test client (optional)
   }
}