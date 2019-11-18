import java.util.*;

public class Trie {
    // Number of Symbols Represented by ASCII Values
    private static final int AV = 256;
    private Node root;
    private int k;

    private static class Node {
        private Node[] next = new Node[AV];
        private boolean isString;
    }

    public Trie() {

    }

    public int size() {
        return k;
    }

    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void add(String key) {
        if (key == null)
            throw new IllegalArgumentException("'add' cannot be null!");
        root = add(root, key, 0);
    }

    // Iterate Through Character Positions to Find Which Portion of the Prefixes
    // Havent Been Added
    private Node add(Node n, String key, int pos) {
        if (n == null)
            n = new Node();
        if (pos == key.length()) {
            if (!n.isString) {
                // Increment Trie Size
                k++;
            }
            n.isString = true;
        } else {
            char ch = key.charAt(pos);
            n.next[ch] = add(n.next[ch], key, pos + 1);
        }
        return n;
    }

    public boolean contains(String key) {
        if (key == null)
            throw new IllegalArgumentException("'contains' cannot be null!");
        Node n = get(root, key, 0);
        if (n == null)
            return false;
        return n.isString;
    }

    private Node get(Node n, String key, int pos) {
        if (n == null)
            return null;
        if (pos == key.length())
            return n;
        char ch = key.charAt(pos);
        return get(n.next[ch], key, pos + 1);
    }

    public Iterator<String> iterator() {
        return keysWithPrefix("").iterator();
    }

    public Iterable<String> keysWithPrefix(String pre) {
        Queue<String> results = new LinkedList<String>();
        Node n = get(root, pre, 0);
        collect(n, new StringBuilder(pre), results);
        return results;
    }

    private void collect(Node n, StringBuilder pre, Queue<String> results) {
        if (n == null) {
            return;
        }
        if (n.isString) {
            results.add(pre.toString());
        }
        for (char ch = 0; ch < AV; ch++) {
            pre.append(ch);
            collect(n.next[ch], pre, results);
            pre.deleteCharAt(pre.length() - 1);
        }
    }

}
