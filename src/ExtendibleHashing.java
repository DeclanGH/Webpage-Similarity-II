import java.util.HashMap;
import java.util.Map;

public class ExtendibleHashing {
    private int globalDepth;
    private Map<Integer, Map<String, String>> directory;

    public ExtendibleHashing() {
        this.globalDepth = 1;
        this.directory = new HashMap<>();
        directory.put(0, new HashMap<>());
    }

    public void insert(String key, String value) {
        int hashCode = key.hashCode();
        int localDepth = Integer.toBinaryString(hashCode).length();
        int index = hashCode & ((1 << globalDepth) - 1);

        Map<String, String> bucket = directory.get(index);

        if (bucket.size() >= 2) {
            if (localDepth == globalDepth) {
                // Rule: if bucket overflow, and localDepth equals globalDepth, Double size
                int newGlobalDepth = globalDepth + 1;
                for (int i = 0; i < (1 << globalDepth); i++) {
                    directory.put(i, directory.get(i & ((1 << globalDepth) - 1)));
                }
                globalDepth = newGlobalDepth;
            }

            // Rule: if bucket overflow, and localDepth does not equal globalDepth, split the bucket
            Map<String, String> newBucket = new HashMap<>();
            for (String k : bucket.keySet()) {
                int newBucketIndex = k.hashCode() & ((1 << globalDepth) - 1);
                if (newBucketIndex != index) {
                    newBucket.put(k, bucket.get(k));
                }
            }

            directory.put(index, newBucket);
            index = hashCode & ((1 << globalDepth) - 1);
            bucket = directory.get(index);
        }

        // Rule: Normal insertion if no overflow
        bucket.put(key, value);
    }

    public String find(String key) {
        int hashCode = key.hashCode();
        int index = hashCode & ((1 << globalDepth) - 1);
        Map<String, String> bucket = directory.get(index);

        return bucket.get(key);
    }
}