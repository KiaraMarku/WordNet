/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Outcast {
    WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int max = 0;
        int maxIndex = 0;
        int[] dist = new int[nouns.length];
        for (int i = 0; i < dist.length; i++) {
            dist[i] = 0;
            for (int j = 0; j < dist.length; j++)
                dist[i] += wordNet.distance(nouns[i], nouns[j]);
            //  System.out.println(dist[i]);
            if (dist[i] > max) {
                max = dist[i];
                maxIndex = i;
            }
        }
        return nouns[maxIndex];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);

        String[] files = { "outcast5a.txt", "outcast8a.txt", "outcast12a.txt", "outcast10a.txt" };
        for (int t = 0; t < files.length; t++) {
            In in = new In(files[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(Arrays.toString(nouns));
            StdOut.println(files[t] + ": " + outcast.outcast(nouns));
        }
    }
}
