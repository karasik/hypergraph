package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Author: mi
 * Date: 3/15/13
 * Time: 1:53 PM
 */

public class HyperGraph {
    public int dim, row;
    public ArrayList<Integer> vertex;
    public ArrayList<ArrayList<Integer>> edges;

    public HyperGraph(int dim, ArrayList<Integer> vertex, ArrayList<ArrayList<Integer>> edges) {
        this.dim = dim;
        this.vertex = vertex;
        this.edges = edges;
    }

    public HyperGraph(int dim, int row) {
        this.dim = dim;
        this.row = row;
        // add vertexes from mask to the list
        vertex = new ArrayList<Integer>();
        for (int i=0; i<(1<<dim); i++) {
            if ((row & (1 << i)) != 0) {
                vertex.add(i);
            }
        }
        // make edges
        edges = new ArrayList<ArrayList<Integer>>();
l0:     for (int fix = 0; fix < (1<<dim); fix++) {
            int pc = Aux.numberOfOnes(fix);
l1:         for (int val=0; val<(1 << pc); val++) {
                ArrayList<Integer> edge = new ArrayList<Integer>();
                for (int p=0; p<(1<<dim); p++) {
                    if (correspond(p, fix, val)) {
                        if (!vertex.contains(p)) continue l1;
                        edge.add(p);
                    }
                }
                for (ArrayList<Integer> e : edges) {
                    if (Aux.isSubset(edge, e)) continue l1;
                }
                Collections.sort(edge);
                edges.add(edge);
//                System.out.println(edge);
            }
        }
        Collections.sort(edges, new Comparator<ArrayList<Integer>>() {
            @Override
            public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
                return o1.size() > o2.size() ? 1 : o1.size() == o2.size() ? 0 : -1;
            }
        });
    }

    private boolean correspond(int p, int fix, int val) {
        for (int i=0, nu=0; i<dim; i++) {
            if ((fix & (1 << i)) != 0) {
                if ((p & (1 << i)) == 0 && (val & (1 << nu)) != 0 ||
                    (p & (1 << i)) != 0 && (val & (1 << nu)) == 0) {
                    return false;
                }
                nu++;
            }
        }

        return true;
    }

    public boolean isIsomorphic(HyperGraph v) {
        if (vertex.size() != v.vertex.size()) return false;
        if (edges.size() != v.edges.size()) return false;
        for (int i=0; i<edges.size(); i++) {
            if (edges.get(i).size() != v.edges.get(i).size()) {
                return false;
            }
        }
        int[] deg1 = new int[vertex.size()], deg2 = new int[vertex.size()];
        for (int i=0; i<vertex.size(); i++) {
            for (ArrayList<Integer> e : edges) {
                if (e.contains(vertex.get(i))) deg1[i]++;
            }
        }
        for (int i=0; i<v.vertex.size(); i++) {
            for (ArrayList<Integer> e : v.edges) {
                if (e.contains(v.vertex.get(i))) deg2[i]++;
            }
        }
        Arrays.sort(deg1);
        Arrays.sort(deg2);
        for (int i=0; i<deg1.length; i++) {
            if (deg1[i] != deg2[i]) return false;
        }


        int n = vertex.size(), e = edges.size();
        HyperGraph a = this, b = v;

        if (n > e) {
            a = a.dual();
            b = b.dual();

            n = a.vertex.size();
            e = a.edges.size();
        }


        for (int i=0, f = Aux.fact(n); i<f; i++) {
            int[] per = Aux.intToPer(i, n);

            if (checkEdges(per, a.vertex, b.vertex, a.edges, b.edges)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkEdges(int[] per, ArrayList<Integer> v1, ArrayList<Integer> v2, ArrayList<ArrayList<Integer>> ea, ArrayList<ArrayList<Integer>> eb1) {
        int[] map1 = new int[1 + (1 << dim)];
        int[] map2 = new int[1 + (1 << dim)];

        for (int i=0; i<v1.size(); i++) {
            map1[v1.get(i)] = i;
        }
        for (int i=0; i<v2.size(); i++) {
            map2[v2.get(i)] = i;
        }

        ArrayList<ArrayList<Integer>> eb = new ArrayList<ArrayList<Integer>>();
        for (ArrayList<Integer> e : eb1) {
            eb.add(e);
        }

l0:     for (ArrayList<Integer> e : ea) {
            ArrayList<Integer> e0 = new ArrayList<Integer>();
            for (int i=0; i<e.size(); i++) {
                e0.add(per[map1[e.get(i)]]);
            }

            Collections.sort(e0);
l1:         for (int i=0; i<eb.size(); i++) {
                ArrayList<Integer> e1 = eb.get(i);

                // not necessary
                //Collections.sort(e1);

                if (e0.size() != e1.size()) continue;

                for (int j=0; j<e0.size(); j++) {
                    if (e0.get(j) != map2[e1.get(j)]) {
                        continue l1;
                    }
                }

                eb.remove(i);
                continue l0;
            }
            return false;
        }

        return true;
    }

    public HyperGraph dual() {
        ArrayList<Integer> nvertex = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> nedges = new ArrayList<ArrayList<Integer>>();

        for (int i=0; i<edges.size(); i++) {
            nvertex.add(i);
        }

        for (int i=0; i<vertex.size(); i++) {
            ArrayList<Integer> newedge = new ArrayList<Integer>();
            for (int h = 0; h < edges.size(); h++) {
                if (edges.get(h).contains(vertex.get(i))) {
                    newedge.add(h);
                }
            }
            nedges.add(newedge);
        }

        return new HyperGraph(dim, nvertex, nedges);
    }
}