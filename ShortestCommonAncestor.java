import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.List;

public class ShortestCommonAncestor {
    BreadthFirstDirectedPaths pathsV;
    BreadthFirstDirectedPaths pathsW;
    Digraph G;
    int root;

    // constructor takes a digraph (not necessarily a DAG)
    public ShortestCommonAncestor(Digraph G) {

        DirectedCycle cycle = new DirectedCycle(G);
        root = -1;

        if (cycle.hasCycle())
            throw new IllegalArgumentException("Graph has to be acyclic");


        for (int v = 0; v < G.V(); v++) {
            if (G.outdegree(v) == 0)
                root = v;
        }

        if (root == -1)
            throw new IllegalArgumentException("Graph not rooted");
        for (int v = 0; v < G.V(); v++) {
            if (v != root && G.outdegree(v) == 0)
                throw new IllegalArgumentException("Graph not rooted");
        }
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if ((Integer) v == null || (Integer) w == null)
            throw new IllegalArgumentException("Vertex cant be null");
        pathsV = new BreadthFirstDirectedPaths(G, v);
        pathsW = new BreadthFirstDirectedPaths(G, w);

        int min = G.V();
        int dist = -1;
        for (int x = 0; x <= v && x <= w; x++) {

            if (pathsV.hasPathTo(x) && pathsW.hasPathTo(x)) {
                dist = pathsV.distTo(x) + pathsW.distTo(x);
                if (dist < min)
                    min = dist;
            }
        }
        if (dist == -1) min = -1;
        return min;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {

        if ((Integer) v == null || (Integer) w == null)
            throw new IllegalArgumentException("Vertex cant be null");
        pathsV = new BreadthFirstDirectedPaths(G, v);
        pathsW = new BreadthFirstDirectedPaths(G, w);

        int min = G.V();
        int dist = 0;
        int ancestor = -1;
        for (int x = 0; x <= v && x <= w; x++) {
            if (pathsV.hasPathTo(x) && pathsW.hasPathTo(x)) {
                dist = pathsV.distTo(x) + pathsW.distTo(x);
                if (dist < min) {
                    min = dist;
                    ancestor = x;
                }
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int lengthSubset(Iterable<Integer> v, Iterable<Integer> w) {
        pathsV = new BreadthFirstDirectedPaths(G, v);
        pathsW = new BreadthFirstDirectedPaths(G, w);

        int min = G.V();
        int dist = -1;
        for (int x = 0; x < G.V(); x++) {

            if (pathsV.hasPathTo(x) && pathsW.hasPathTo(x)) {
                dist = pathsV.distTo(x) + pathsW.distTo(x);
                if (dist < min)
                    min = dist;
            }
        }
        if (dist == -1) min = -1;
        return min;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestorSubset(Iterable<Integer> v, Iterable<Integer> w) {
        pathsV = new BreadthFirstDirectedPaths(G, v);
        pathsW = new BreadthFirstDirectedPaths(G, w);

        int min = G.V();
        int dist = 0;
        int ancestor = -1;
        for (int x = 0; x < G.V(); x++) {
            if (pathsV.hasPathTo(x) && pathsW.hasPathTo(x)) {
                dist = pathsV.distTo(x) + pathsW.distTo(x);
                if (dist < min) {
                    min = dist;
                    ancestor = x;
                }
            }
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in1 = new In("digraph1.txt");
        Digraph G1 = new Digraph(in1);
        ShortestCommonAncestor sap1 = new ShortestCommonAncestor(G1);
        int a = 3;
        int b = 10;
        int length1 = sap1.length(a, b);
        int ancestor1 = sap1.ancestor(a, b);
        StdOut.printf("length = %d, ancestor = %d\n", length1, ancestor1);

        In in = new In("digraph25.txt");
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sap = new ShortestCommonAncestor(G);


        List<Integer> v = Arrays.asList(7, 23, 24);
        List<Integer> w = Arrays.asList(9, 16, 17);

        int length = sap.lengthSubset(v, w);
        int ancestor = sap.ancestorSubset(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }

}
