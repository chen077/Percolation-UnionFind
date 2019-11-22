/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF full;
    private int N;
    private int top;
    private int bottom;
    private boolean[] openNodes;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int N){
        if(N <= 0) throw new IllegalArgumentException();
        this.N = N;

        grid = new WeightedQuickUnionUF(N*N +2);
        full = new WeightedQuickUnionUF(N*N +1);

        top = getSingleArrayIdx(N, N) + 1;
        bottom = getSingleArrayIdx(N, N) + 2;

        openNodes = new boolean[N * N];


    }

    private int getSingleArrayIdx(int i, int j) {
        doOutOfBoundsCheck(i, j);

        return (N * (i - 1) + j) - 1;
    }

    private boolean isValid(int i, int j) {
        return i > 0
                && j > 0
                && i <= N
                && j <= N;
    }

    private void doOutOfBoundsCheck(int i, int j) {
        if (!isValid(i, j)) {
            throw new IndexOutOfBoundsException("Boo! Values are out of bounds");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        doOutOfBoundsCheck(row, col);

        if (isOpen(row, col)) {
            // No need to open this again as it's already open
            return;
        }

        int idx = getSingleArrayIdx(row, col);
        openNodes[idx] = true;


        // Node is in the top row. Union node in `grid` and `full` to the virtual top row.
        if (row == 1) {
            grid.union(top, idx);
            full.union(top, idx);
        }

        // Node is in the bottom row. Only union the node in `grid` to avoid backwash issue.
        if (row == N) {
            grid.union(bottom, idx);
        }

        // Union with the node above the given node if it is already open
        if (isValid(row - 1, col) && isOpen(row - 1, col)) {
            grid.union(getSingleArrayIdx(row - 1, col), idx);
            full.union(getSingleArrayIdx(row - 1, col), idx);
        }

        // Union with the node to the right of the given node if it is already open
        if (isValid(row, col + 1) && isOpen(row, col + 1)) {
            grid.union(getSingleArrayIdx(row, col + 1), idx);
            full.union(getSingleArrayIdx(row, col + 1), idx);
        }

        // Union with the node below the given node if it is already open
        if (isValid(row + 1, col) && isOpen(row + 1, col)) {
            grid.union(getSingleArrayIdx(row + 1, col), idx);
            full.union(getSingleArrayIdx(row + 1, col), idx);

        }

        // Union with the node to the left of the given node if it is already open
        if (isValid(row, col - 1) && isOpen(row, col - 1)) {
            grid.union(getSingleArrayIdx(row, col - 1), idx);
            full.union(getSingleArrayIdx(row, col - 1), idx);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        doOutOfBoundsCheck(row, col);
        int index = getSingleArrayIdx(row, col);
        return openNodes[index];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        doOutOfBoundsCheck(row, col);
        return full.connected(getSingleArrayIdx(row, col),top);
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        int count = 0;
        for(int i=0; i<openNodes.length; i++){
            if(openNodes[i] == true){
                count++;
            }
        }
        return count;
    }

    // does the system percolate?
    public boolean percolates(){
        return grid.connected(top,bottom);
    }

    // union(), find(), connected(), and count()
    // test client (optional)
    public static void main(String[] args) {
        // BufferedReader br = new BufferedReader(new FileReader("input3.txt"));
        // String st;
        // int n = 0;
        // if((st = br.readLine()) != null){
        //     n =Integer.parseInt(st);
        // }
        // Percolation pcl = new Percolation(n);
        //
        // while ((st = br.readLine()) != null){
        //     st = st.replaceFirst("\\s","");
        //     String[] s = st.split("\\s", 2);
        //     int row = Integer.parseInt(s[0]);
        //     int col = Integer.parseInt(s[1]);
        //     pcl.open(row,col);
        // }

    }
}

