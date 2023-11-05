import HashClasses.CustomHashTable;
import HashClasses.ExtendibleHashing;

import java.io.ByteArrayInputStream;
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

    boolean isEmpty() {
        return wikiLinks.isEmpty();
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

        int iterationLimit = 1000;

        HashSet<String> centroidSet = new HashSet<>(); // the k selected centroids

        int centroidSelector; // generates random points to get centroids


        HashMap<String,Cluster> finalState = new HashMap<>();

        double variance = -1;

        for(int i=0; i<iterationLimit; i++){

            HashMap<String,Cluster> initialState = new HashMap<>();

            // select new centroids
            while(centroidSet.size() != k){
                centroidSelector = rand.nextInt(0,200);
                centroidSet.add(myUrls[centroidSelector]);
            }

            // populate initial state/clustering
            for(String s : centroidSet){
                initialState.put(s, new Cluster(s));
            }

            // assign data-points(wiki documents) to their most similar centroid
            for(String s : myUrls){
                if(!centroidSet.contains(s)){
                    ArrayList<String> centroidAndScore = getClosestCentroid(s,centroidSet,urlsMappedToObject);
                    String closestCentroid = centroidAndScore.get(0);
                    double similarityScore = Double.parseDouble(centroidAndScore.get(1));
                    initialState.get(closestCentroid).addCluster(s,similarityScore);
                }
            }

            // check if a centroid has nothing close to it. if true, abandon iteration.
            boolean hasEmptyCentroid = false;
            for(Map.Entry<String, Cluster> entry : initialState.entrySet()){
                if(entry.getValue().isEmpty()){
                    hasEmptyCentroid = true;
                    break;
                }
            }

            double varianceScore = 0;

            if(hasEmptyCentroid){
                continue;
            } else{

                for(String centroid : centroidSet){
                    if(initialState.get(centroid).hasGoodVariance()){
                        varianceScore += 1;
                    }
                }

                varianceScore /= k;
            }

            if(varianceScore == 1){
                finalState = initialState;
                System.out.println("Perfect spread!");
                break;
            }else if (varianceScore > variance) {
                variance = varianceScore;
                finalState = initialState;
            }

            if (i == 999){
                System.out.println("Reached Iteration Limit!");
            }
        }

        loadJsonOutput(finalState);
    }

    private static ArrayList<String> getClosestCentroid
            (String s, HashSet<String> centroidSet, ExtendibleHashing urlsMappedToObject)
            throws IOException, ClassNotFoundException {

        ArrayList<String> centroidAndScore = new ArrayList<>();

        String closestCentroid = "";
        double similarityScore1;
        double similarityScore2 = -1; // initialize with a negative score

        ByteArrayInputStream bis = new ByteArrayInputStream(urlsMappedToObject.find(s));
        ObjectInputStream ois = new ObjectInputStream(bis);
        CustomHashTable ht1 = (CustomHashTable) ois.readObject();
        String[] ht1KeyList = ht1.toKeyList();

        for(String centroid : centroidSet){
            ByteArrayInputStream bis1 = new ByteArrayInputStream(urlsMappedToObject.find(centroid));
            ObjectInputStream ois1 = new ObjectInputStream(bis1);
            CustomHashTable ht2 = (CustomHashTable) ois1.readObject();

            similarityScore1 = SimilarityAlgorithm.doCosineSimilarity(ht1,ht2,ht1KeyList);

            if(similarityScore1 >= similarityScore2){
                closestCentroid = centroid;
                similarityScore2 = similarityScore1;
            }
        }

        // Add centroid and score in that order
        centroidAndScore.add(closestCentroid);
        centroidAndScore.add(String.valueOf(similarityScore2));

        return centroidAndScore;
    }

    private static void loadJsonOutput(HashMap<String,Cluster> finalState) {
    }

}