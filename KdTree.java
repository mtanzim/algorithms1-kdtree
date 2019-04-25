/* *****************************************************************************
 *  Name: KdTree
 *  Date: Apr 23, 2019
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {

    // private SET<Node> pointNodes;
    private Node root;
    // private boolean curOrientation = false;
    private int size = 0;
    // false is horizontal
    // true is vertical

    private static class Node {
        private Point2D p;
        private RectHV rect;
        // false is horizontal
        // true is vertical
        private boolean orientation;
        private Node lb;
        private Node rt;

        public Node(Point2D _p, boolean _orientation) {
            p = _p;
            // rect = _rect;
            orientation = _orientation;
            // lb = _lb; // left or bottom; < 0
            // rt = _rt; // right or top; > 0
        }

    }

    // construct empty set of points
    public KdTree() {
        root = null;
    }


    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    private String getOrientation(boolean cond) {
        if (cond) return "vertical";
        return "horizontal";
    }


    // does set contain p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to contains() is null");
        return false;
        // return get(p);
    }

    /*private boolean get(Point2D p) {
        return get(root, p) != null;
    }

    private Node get(Node curNode, Point2D p) {
        if (curNode == null) return null;

    }*/

    private int getCmp(Node curNode, Point2D p) {
        int cmp;
        // if true/vertical
        if (curNode.orientation) {
            cmp = Point2D.X_ORDER.compare(p, curNode.p);
        }
        // false/horizontal
        else {
            cmp = Point2D.Y_ORDER.compare(p, curNode.p);

        }
        return cmp;
    }

    private void debugCmp(int cmp, Node curNode) {
        if (cmp < 0) {
            if (getOrientation(curNode.orientation) == "vertical") StdOut.println("Going left");
            else StdOut.println("Going bottom");
        }
        else if (cmp > 0) {
            // StdOut.println("Going right/top");
            if (getOrientation(curNode.orientation) == "vertical") StdOut.println("Going right");
            else StdOut.println("Going top");
        }
    }


    // add point to the set
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calls put() with a null key");

        StdOut.println("\n\n*** Inserting " + p.toString() + " ***\n");
        // root orientation is chosen to be true/vertical

        root = put(root, p, false);
        // size++;
        // curOrientation = !curOrientation;
    }

    private Node put(Node curNode, Point2D p, boolean parentOrientation) {
        // found where to insert
        if (curNode == null) {
            size++;
            // reverse orientation from parent
            StdOut.println("\n=== Inserted " + p.toString() + " to tree" + " with orientation "
                                   + getOrientation(!parentOrientation) + " ===\n");
            // StdOut.println("Current size " + size);

            return new Node(p, !parentOrientation);
        }
        StdOut.println(
                "Currently at Node with point: " + curNode.p.toString() + " with orientation "
                        + getOrientation(curNode.orientation));

        int cmp = getCmp(curNode, p);
        if (cmp < 0) {
            // if (getOrientation(curNode.orientation) == "vertical") StdOut.println("Going left");
            // else StdOut.println("Going bottom");
            debugCmp(cmp, curNode);
            curNode.lb = put(curNode.lb, p, curNode.orientation);

        }
        else if (cmp > 0) {
            // StdOut.println("Going right/top");
            // if (getOrientation(curNode.orientation) == "vertical") StdOut.println("Going right");
            // else StdOut.println("Going top");
            debugCmp(cmp, curNode);
            curNode.rt = put(curNode.rt, p, curNode.orientation);
        }
        // overwrite previously held value
        else curNode.p = p;

        return curNode;
    }


    public void draw() {

    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        points.add(new Point2D(0, 0));
        return points;

    }

    public Point2D nearest(Point2D p) {
        return new Point2D(0, 0);
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        StdOut.println("START: is it empty: " + tree.isEmpty());
        StdOut.println("Current size: " + tree.size());
        tree.insert(new Point2D(0.5, 0.5));
        tree.insert(new Point2D(0.7, 0.5));
        tree.insert(new Point2D(0.3, 0.5));
        tree.insert(new Point2D(0.3, 0.4));
        tree.insert(new Point2D(0.7, 0.6));
        StdOut.println("Current size: " + tree.size());
        StdOut.println("END: is it empty: " + tree.isEmpty());

    }
}
