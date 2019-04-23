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
    private boolean curOrientation = false;
    private int size = 0;
    // false is horizontal
    // true is vertical

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D _p, RectHV _rect, Node _lb, Node _rt) {
            p = _p;
            rect = _rect;
            lb = _lb;
            rt = _rt;
        }

    }

    // construct empty set of points
    public KdTree() {
        curOrientation = false;
    }


    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add point to the set
    public void insert(Point2D p) {
        StdOut.println("Flipping");
        size++;
        curOrientation = !curOrientation;
    }

    // does set contain p?
    public boolean conatins(Point2D p) {
        return false;
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
        StdOut.println(tree.isEmpty());
        StdOut.println(tree.size());
        tree.insert(new Point2D(0.5, 0.5));
        StdOut.println(tree.isEmpty());
        StdOut.println(tree.size());

    }
}
