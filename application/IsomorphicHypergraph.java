package application;

import algorithms.Aux;
import algorithms.HyperGraph;

import java.util.ArrayList;

/**
 * Author: mi
 * Date: 3/15/13
 * Time: 1:50 PM
 */
public class IsomorphicHypergraph {
    public static void main(String[] args) {
        int dim = 3;
        ArrayList<HyperGraph> col = new ArrayList<HyperGraph>();

l0:     for (int row = 1, max = (1<<(1<<dim)); row < max; row++) {
            /*if (row % 1000 == 0) {
                System.out.println("processing " + row + " of " + max);
            }*/
            HyperGraph u = new HyperGraph(dim, row);

            if (col.size() == 0) {
                col.add(u);
                continue;
            }

            for (HyperGraph v : col) {
                if (u.isIsomorphic(v)) {
                    continue l0;
                }
            }
            col.add(u);
        }

        System.out.println(col.size());
        for (HyperGraph k : col) {
            System.out.println(new StringBuffer(String.format("%"+(1<<dim)+"s", Integer.toBinaryString(k.row)).replace(' ', '0')).reverse().toString());
        }
    }
}
