import java.util.List;

/**
 * Implementation of a BST based String Set.
 * @author sahil
 */
public class BSTStringSet implements StringSet {
    /** Creates a new empty set. */
    public BSTStringSet() {
        root = null;
    }


    private Node put(String s, Node node) {
        if (node == null) {
            return new Node(s);
        }
        if (s.compareTo(node.s) == 1) {
            node.right = put(s, node.right);
        }
        if (s.compareTo(node.s) == -1) {
            node.left = put(s, node.left);
        }

        return node;
    }

    @Override
    public void put(String s) {
        root = put(s, root);

    }

    private boolean contains(String s, Node node) {
        if (node == null) {
            return false;
        }
        if (s.compareTo(node.s) == 1) {
            return contains(s, node.right);
        }
        return (s.compareTo(node.s) == -1 || contains(s, node.left));
        //return true;
    }


    @Override
    public boolean contains(String s) {
        return contains(s, root); // FIXME
    }

    @Override

    public List<String> asList() {
        return null; // FIXME
    }

    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        public Node(String sp) {
            s = sp;
        }
    }

    /** Root node of the tree. */
    private Node root;
}
