/**
 * Author: Declan Onunkwo
 * Date: 29-oct-2023
 *
 * Description: Maps a string (URL in our case) to a byte array (which is a persistent object)
 */

package HashClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Bucket implements Serializable {
    Map<String, byte[]> entries;
    private int localDepth;
    private int capacity;

    public Bucket(int depth, int capacity) {
        this.entries = new HashMap<>();
        this.localDepth = depth;
        this.capacity = capacity;
    }

    public int getLocalDepth() {
        return localDepth;
    }

    public boolean isFull() {
        return entries.size() >= capacity;
    }

    public void insert(String key, byte[] value) {
        entries.put(key, value);
    }

    public byte[] get(String key) {
        return entries.get(key);
    }
}

public class Directory implements Serializable {
    private Bucket[] buckets;
    private int globalDepth;
    private int bucketCapacity;

    public Directory(int depth, int bucketCapacity) {
        this.globalDepth = depth;
        this.bucketCapacity = bucketCapacity;
        this.buckets = new Bucket[(int) Math.pow(2, depth)];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new Bucket(depth, bucketCapacity);
        }
    }

    public byte[] find(String key) {
        int bucketIndex = getBucketIndex(hash(key));
        return buckets[bucketIndex].get(key);
    }

    public void insert(String key, byte[] value) {
        int bucketIndex = getBucketIndex(hash(key));
        Bucket bucket = buckets[bucketIndex];
        if (bucket.isFull()) {
            if (bucket.getLocalDepth() == globalDepth) {
                doubleDirectory();
                // Re-calculate bucketIndex after directory doubling
                bucketIndex = getBucketIndex(hash(key));
                bucket = buckets[bucketIndex];
            }
            if (bucket.isFull()) {
                // Split the bucket
                splitBucket(bucketIndex);
                // After splitting, recalculate bucketIndex
                bucketIndex = getBucketIndex(hash(key));
                bucket = buckets[bucketIndex];
            }
        }
        // Insert the key and value
        bucket.insert(key, value);
    }

    /*public void insert(String key, byte[] value) {
        int bucketIndex = getBucketIndex(hash(key));
        Bucket bucket = buckets[bucketIndex];
        if (bucket.isFull()) {
            if (bucket.getLocalDepth() == globalDepth) {
                doubleDirectory();
                bucketIndex = getBucketIndex(hash(key)); // Re-calculate bucketIndex
                bucket = buckets[bucketIndex];
            }
            splitBucket(bucketIndex);
        }
        bucket.insert(key, value);
    }*/

    private void splitBucket(int bucketIndex) {
        Bucket bucket = buckets[bucketIndex];
        int localDepth = bucket.getLocalDepth();
        int newLocalDepth = localDepth + 1;
        Bucket newBucket = new Bucket(newLocalDepth, bucketCapacity);
        int mask = (1 << localDepth) - 1;
        List<String> toMove = new ArrayList<>();
        for (Map.Entry<String, byte[]> entry : bucket.entries.entrySet()) {
            String key = entry.getKey();
            int newIndex = hash(key) & mask;
            if (newIndex != bucketIndex) {
                newBucket.insert(key, entry.getValue());
                toMove.add(key);
            }
        }
        for (String key : toMove) {
            bucket.entries.remove(key);
        }
        /*int mask = (1 << localDepth) - 1;
        for (Map.Entry<String, byte[]> entry : bucket.entries.entrySet()) {
            String key = entry.getKey();
            int newIndex = hash(key) & mask;
            if (newIndex != bucketIndex) {
                newBucket.insert(key, entry.getValue());
            }
        }
        bucket.entries.clear(); // Clear the original bucket
        buckets[bucketIndex] = newBucket;*/
    }

    private void doubleDirectory() {
        int oldSize = buckets.length;
        int newSize = oldSize * 2;
        Bucket[] newBuckets = new Bucket[newSize];
        for (int i = 0; i < oldSize; i++) {
            newBuckets[i] = buckets[i];
            newBuckets[i + oldSize] = buckets[i];
        }
        buckets = newBuckets;
        globalDepth++;
    }

    private int hash(String key) {
        return key.hashCode();
    }

    private int getBucketIndex(int hash) {
        return hash & ((1 << globalDepth) - 1);
    }
}
