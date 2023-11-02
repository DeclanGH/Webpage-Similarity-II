import HashClasses.CustomHashTable;
import HashClasses.ExtendibleHashing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

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

        HashSet<String> centroids = new HashSet<>();

        while(centroids.size() != k){
            centroids.add("yes");
        }
        System.out.print(k);
    }
}
