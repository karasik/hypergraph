package algorithms;

import java.util.ArrayList;

/**
 * Author: mi
 * Date: 3/15/13
 * Time: 5:57 PM
 */

public class Aux {
    public static int numberOfOnes(int n) {
        int pc = 0, dim=30;
        for (int i=0; i<dim; i++) {
            if ((n & (1 << i)) != 0) {
                pc++;
            }
        }
        return pc;
    }
    public static boolean isSubset(ArrayList<Integer> l1, ArrayList<Integer> l2) {
        for (int j : l1) {
            if (!l2.contains(j)) {
                return false;
            }
        }
        return true;
    }

    public static int fact(int n) {
        if (n == 0) return 1;
        return n * fact(n-1);
    }

    public static int[] intToPer(int n, int dim) {
        boolean[] used = new boolean[dim];
        int[] ret = new int[dim];
        for (int i=dim-1; i>=0; i--) {
            int k = 1 + n / fact(i);
            int h=0, u=0;
            while (true) {
                if (!used[h]) {
                    u++;
                }
                if (u == k) {
                    break;
                }
                h++;
            }
            n %= fact(i);
            used[h] = true;
            ret[dim-i-1] = h;
        }
        return ret;
    }
}
