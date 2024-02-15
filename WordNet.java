/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class WordNet {

    Map<Integer, List<String>> synsets;
    Map<Integer, List<Integer>> hypernyms;
    Map<String, List<Integer>> nouns;
    Digraph G;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Filename cant be null");
        this.synsets = getSynsets(synsets);
        this.hypernyms = getHypernyms(hypernyms);
        G = new Digraph(this.synsets.size());

        for (int id = 0; id < this.synsets.size(); id++) {
            for (Integer hyperId : this.hypernyms.get(id))
                G.addEdge(id, hyperId);
        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!nouns.containsKey(nounA) || !nouns.containsKey(nounB))
            throw new IllegalArgumentException("Nouns not present");

        List<Integer> idA = nouns.get(nounA);
        List<Integer> idB = nouns.get(nounB);
        ShortestCommonAncestor sap = new ShortestCommonAncestor(G);
        return sap.lengthSubset(idA, idB);

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!nouns.containsKey(nounA) || !nouns.containsKey(nounB))
            throw new IllegalArgumentException("Nouns not present");

        List<Integer> idA = nouns.get(nounA);
        List<Integer> idB = nouns.get(nounB);

        ShortestCommonAncestor sap = new ShortestCommonAncestor(G);
        Integer ancestorId = sap.ancestorSubset(idA, idB);
        return synsets.get(ancestorId).toString();
    }

    private Map<Integer, List<String>> getSynsets(String fileName) {

        Map<Integer, List<String>> synsets = new HashMap<>();
        // pairs = new ArrayList<Pair>();
        nouns = new HashMap<>();

        File file = new File(fileName);
        Scanner scanner;
        try {

            scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                int id = Integer.parseInt(data[0]);
                List<String> nounList = Arrays.asList(data[1].split(" "));
                synsets.put(id, nounList);

                for (String noun : nounList) {
                    if (!nouns.containsKey(noun)) {
                        nouns.put(noun, new ArrayList<>());
                    }
                    nouns.get(noun).add(id);
                }
            }
            scanner.close();

        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return synsets;
    }

    private Map<Integer, List<Integer>> getHypernyms(String fileName) {

        Map<Integer, List<Integer>> hypernyms = new HashMap<>();
        File file = new File(fileName);
        Scanner scanner;
        try {

            scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                int id = Integer.parseInt(data[0]);
                List<Integer> hyperId = new ArrayList<>();
                for (int i = 1; i < data.length; i++) {
                    hyperId.add(Integer.parseInt(data[i]));
                }
                hypernyms.put(id, hyperId);
            }
            scanner.close();

        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return hypernyms;
    }

    // do unit testing of this class
    public static void main(String[] args) {


        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");

        System.out.println(wordNet.sap("heroin", "cocaine"));
        System.out.println(wordNet.sap("second", "minute"));
        System.out.println(wordNet.sap("difference", "protest"));
    }
}