import HashClasses.CustomHashTable;
import HashClasses.ExtendibleHashing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

class Cluster {
    String centroid;
    HashMap<String,Double> wikiLinks;

    public Cluster(String centroid) {
        this.centroid = centroid;
        this.wikiLinks = new HashMap<>();
    }

    void clearCluster() {
        wikiLinks.clear();
    }

    void addCluster(String wikiLink, double similarityScore) {
        wikiLinks.put(wikiLink, similarityScore);
    }

    boolean hasGoodVariance(){
        ArrayList<Double> listOfScores = new ArrayList<>();

        double mean = 0;

        for(Map.Entry<String, Double> entry : wikiLinks.entrySet()) {
            listOfScores.add(entry.getValue());
            mean += entry.getValue();
        }
        mean /= wikiLinks.size();

        for(int i=0; i<listOfScores.size(); i++){
            double d = listOfScores.get(i);
            d -= mean;
            d *= d;
            listOfScores.set(i,d);
        }

        double totalSum = 0;

        for(Double d : listOfScores){
            totalSum += d;
        }

        double variance = totalSum / (listOfScores.size() - 1);

        return (variance * 100) <= 1;
    }

    String getCentroid() {
        return centroid;
    }
}

public class ClusteringAlgorithm {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Deserialize Extensible hashing class
        FileInputStream fis = new FileInputStream("SerializedExtensibleHashingClass");
        ObjectInputStream ois = new ObjectInputStream(fis);
        ExtendibleHashing urlsMappedToObject = (ExtendibleHashing) ois.readObject();

        // Deserialize myUrl list
        FileInputStream fis2 = new FileInputStream("SerializedUrlList");
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        CustomHashTable deserializedUrlList = (CustomHashTable) ois2.readObject();
        String[] myUrls = deserializedUrlList.toKeyList();

        Random rand = new Random();
        int k = rand.nextInt(5,11); // range of k

        HashSet<String> centroidSet = new HashSet<>();
        int centroidSelector; // generates random points to get centroids

        while(centroidSet.size() != k){
            centroidSelector = rand.nextInt(0,200);
            centroidSet.add(myUrls[centroidSelector]);
        }

        HashMap<String,Cluster> centroidToCluster = new HashMap<>();

        for(String s : centroidSet){
            centroidToCluster.put(s, new Cluster(s));
        }

        for(String s : myUrls){
            if(!centroidSet.contains(s)){
                for(){

                }
            }
        }

        System.out.print(k);
    }
}
