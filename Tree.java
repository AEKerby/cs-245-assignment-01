public class Tree<E extends Comparable<? super E>> {

    private class Node {
        Node left;
        Node right;
        Node p;
        E data;
        int h;

        // Node Constructor
        Node(E k) {
            this.data = k;
        }

        public String toString() {
            return "Node<data: " + this.data + ">";
        }
    }

    private Node root;
    private int size;
    private StringBuilder stringBuilder;

    private int height(Node n) {
        return n == null ? -1 : n.h;
    }

    public int height() {
        return height(root);
    }

    // Return Node for Given Data
    private Node find(E data) {
        if (data == null) {
            throw new IllegalArgumentException("'find' cannot be null!");
        }
        Node n = this.root;
        while (n != null) {
            int cmp = data.compareTo(n.data);
            if (cmp < 0) {
                n = n.left;
            } else if (cmp > 0) {
                n = n.right;
            } else {
                return n;
            }
        }
        return null;
    }

    // Throw Exception if Not Found
    private Node findForSure(E data) {
        Node n = this.find(data);
        if (n == null) {
            throw new IllegalArgumentException("Node was not found!");
        }
        return n;
    }

    public void insert(E data) throws IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("'insert' cannot be null!");
        }
        this.root = this.insert(this.root, data);
        this.size += 1;
    }

    private Node insert(Node n, E data) {
        if (n == null) {
            return new Node(data);
        }
        int cmp = data.compareTo(n.data);
        if (cmp < 0) {
            n.left = this.insert(n.left, data);
            n.left.p = n;
        } else if (cmp > 0) {
            n.right = this.insert(n.right, data);
            n.right.p = n;
        } else {
            throw new IllegalArgumentException("Node was a duplicate!");
        }

        if (height(n.left) - height(n.right) > 1) {
            Node temp;
            if (height(n.left.left) >= height(n.left.right)) {
                temp = n.left;
                rightRotate(n); // case 1
            } else {
                temp = n.left.right;
                leftRotate(n.left); // case 2
                rightRotate(n);
            }
            n = temp;
        } else if (height(n.right) - height(n.left) > 1) {
            Node temp;
            if (height(n.right.right) >= height(n.right.left)) {
                temp = n.right;
                leftRotate(n); // case 4
            } else {
                temp = n.right.left;
                rightRotate(n.right); // case 3
                leftRotate(n);
            }
            n = temp;
        }
        n.h = Math.max(height(n.left), height(n.right)) + 1;

        return n;
    }

    // Helper to Balance Nodes
    private void leftRotate(Node x) {
        Node y = x.right;
        if (y == null) {
            return;
        }
        x.right = y.left;
        if (y.left != null) {
            y.left.p = x;
        }
        y.p = x.p;

        if (x.p == null) {
            root = y;
        } else if (x == x.p.left) {
            x.p.left = y;
        } else {
            x.p.right = y;
        }
        y.left = x;
        x.p = y;

        x.h = Math.max(height(x.left), height(x.right)) + 1;
        y.h = Math.max(height(y.right), x.h) + 1;
    }

    // Helper to Balance Nodes
    private void rightRotate(Node y) {
        Node x = y.left;
        if (x == null) {
            return;
        }
        y.left = x.right;
        if (x.right != null) {
            x.right.p = y;
        }
        x.p = y.p;
        if (y.p == null) {
            root = x;
        } else if (y == y.p.left) {
            y.p.left = x;
        } else {
            y.p.right = x;
        }
        x.right = y;
        y.p = x;
        y.h = Math.max(height(y.right), height(y.left)) + 1;
        x.h = Math.max(height(x.left), y.h) + 1;
    }

    private Node max(Node n) {
        while (n.right != null) {
            n = n.right;
        }
        return n;
    }

    private Node min(Node n) {
        while (n.right != null) {
            n = n.left;
        }
        return n;
    }

    public void remove(E data) {
        this.root = this.remove(this.root, data);
        this.size--;
    }

    private Node remove(Node n, E data) {
        if (n == null) {
            throw new IllegalArgumentException("Node to remove not found! ");
        }
        int cmp = data.compareTo(n.data);
        if (cmp < 0) {
            n.left = this.remove(n.left, data);
        } else if (cmp > 0) {
            n.right = this.remove(n.right, data);
        } else if (n.left != null && n.right != null) {
            Node max = this.max(n.left);
            n.data = max.data;
            n.left = remove(n.left, n.data);
        } else {
            n = (n.left != null) ? n.left : n.right;
        }
        return balance(n);
    }

    // Assumes n is Balanced or Within One of Being Balanced
    private Node balance(Node n) {
        if (n == null) {
            return n;
        }
        Node temp;
        if (height(n.left) - height(n.right) > 1) {
            if (height(n.left.left) >= height(n.left.right)) {
                temp = n.left;
                rightRotate(n);
            } else {
                temp = n.left.right;
                leftRotate(n.left);
                rightRotate(n);
            }
            n = temp;
        } else if (height(n.right) - height(n.left) > 1) {
            if (height(n.right.right) >= height(n.right.left)) {
                temp = n.right;
                leftRotate(n);
            } else {
                temp = n.right.left;
                rightRotate(n.right);
                leftRotate(n);
            }
            n = temp;
        }
        n.h = Math.max(height(n.left), height(n.right)) + 1;
        return n;
    }

    public E successor(E data) {
        E ret;
        Node node;
        boolean had = true;
        if (!has(data)) {
            insert(data);
            had = false;
        }
        if (max(root).data.equals(data)) {
            // If Data is Largest in Tree There is no Successor
            return null;
        }
        node = findForSure(data);
        // Has a Right Child, Go Right Then Far Left
        if (node.right != null) {
            ret = getLeftMost(node.right).data;
        } else {
            // Doesn't Have Right Child
            // If Left Child of Parent, Successor is Parent
            if (isLeftChild(node)) {
                ret = node.p.data;
            } else {
                // If Right Child of Parent, Successor is Above
                while (node.data.compareTo(node.p.data) > 0) {
                    node = node.p;
                }
                ret = node.p.data;
            }
        }
        if (!had) {
            remove(data);
        }
        return ret;
    }

    public E predecessor(E data) {
        E ret;
        Node node;
        boolean had = true;
        if (!has(data)) {
            insert(data);
            had = false;
        }
        if (min(root).data.equals(data)) {
            // if data is the smallest in the tree
            // there is no predecessor
            return null;
        }
        node = findForSure(data);
        if (node.left != null) {
            ret = getRightMost(node.left).data;
        } else {
            if (isRightChild(node)) {
                ret = node.p.data;
            } else {
                while (node.data.compareTo(node.p.data) < 0) {
                    node = node.p;
                }
                ret = node.p.data;
            }
        }
        if (!had) {
            remove(data);
        }
        return ret;
    }

    private boolean isLeftChild(Node node) {
        return node == node.p.left;
    }

    private boolean isRightChild(Node node) {
        return node == node.p.right;
    }

    private Node getLeftMost(Node node) {
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node getRightMost(Node node) {
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    public boolean has(E data) {
        if (data == null) {
            return false;
        }
        return this.find(data) != null;
    }

    public int size() {
        return this.size;
    }

    // Create StringBuilder, Else Reset Back to Empty State
    private void setupStringBuilder() {
        if (this.stringBuilder == null) {
            this.stringBuilder = new StringBuilder();
        } else {
            this.stringBuilder.setLength(0);
        }
    }

    // Recursively Append String Representations of data From Subtree Rooted at a
    // Given Node
    private void toStringHelper(Node n, StringBuilder s) {
        if (n == null) {
            return;
        }
        this.toStringHelper(n.left, s);
        s.append(n.data);
        s.append(", ");
        this.toStringHelper(n.right, s);
    }

    @Override
    public String toString() {
        this.setupStringBuilder();
        this.stringBuilder.append("{");
        this.toStringHelper(this.root, this.stringBuilder);
        int length = this.stringBuilder.length();
        if (length > 1) {
            // Remove the Last Comma the toStringHelper Inserted
            this.stringBuilder.setLength(length - 2);
        }
        this.stringBuilder.append("}");
        return this.stringBuilder.toString();
    }

}
