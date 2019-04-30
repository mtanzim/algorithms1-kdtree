/* *****************************************************************************
 *  Name: KdTree
 *  Date: Apr 23, 2019
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class KdTree {

    // private SET<Node> pointNodes;
    private Node root;
    // private boolean curOrientation = false;
    private int size = 0;
    // false is horizontal
    // true is vertical
    private static final boolean isDebug = false;

    private boolean ROOT_ORIENTATION = true;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        // false is horizontal
        // true is vertical
        private boolean orientation;
        private Node lb;
        private Node rt;

        public Node(Point2D _p, boolean _orientation, RectHV _rect) {
            p = _p;
            rect = _rect;
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


    private int getCmp(Node curNode, Point2D p) {
        int cmp;
        // if true/vertical
        if (curNode.orientation) {
            cmp = Point2D.X_ORDER.compare(p, curNode.p);
            if (cmp == 0) {
                cmp = Point2D.Y_ORDER.compare(p, curNode.p);

            }
        }
        // false/horizontal
        else {
            cmp = Point2D.Y_ORDER.compare(p, curNode.p);
            if (cmp == 0) {
                cmp = Point2D.X_ORDER.compare(p, curNode.p);

            }
        }
        return cmp;
    }

    private String getOrientation(boolean cond) {
        if (cond) return "vertical";
        return "horizontal";
    }

    private String getDirection(int cmp, Node curNode) {
        if (cmp < 0) {
            if (getOrientation(curNode.orientation) == "vertical") {
                return "left";

            }
            else {
                if (isDebug) StdOut.println("Going bottom");
                return "bottom";

            }
        }
        else if (cmp > 0) {
            // if (isDebug) StdOut.println("Going right/top");
            if (getOrientation(curNode.orientation) == "vertical") {
                if (isDebug) StdOut.println("Going right");
                return "right";

            }
            else {
                if (isDebug) StdOut.println("Going top");
                return "top";

            }
        }

        return "";
    }


    private RectHV debugCmp(int cmp, Node curNode, Node parent, RectHV curRect) {
        // if (isDebug) StdOut.println(curRect.toString());
        RectHV newRect = curRect;
        RectHV outerRect;

        if (parent == null) {
            outerRect = new RectHV(0, 0, 1, 1);
        }
        else {
            // outerRect = parent.rect;
            outerRect = curRect;
        }

        if (isDebug) StdOut.println(
                "\nCurrently at Node with point: " + curNode.p.toString() + " with orientation "
                        + getOrientation(curNode.orientation));


        if (parent != null) {

            if (isDebug) StdOut.println(
                    "Parent Node with point: " + parent.p.toString() + " with orientation "
                            + getOrientation(parent.orientation) + " rectangle " + parent.rect
                            .toString());
        }

        if (isDebug) StdOut.println("Starting rectangle is: " + curRect.toString());

        String curDirection = getDirection(cmp, curNode);
        switch (curDirection) {
            case "left":
                newRect = new RectHV(outerRect.xmin(), outerRect.ymin(), curNode.p.x(),
                                     outerRect.ymax());
                break;
            case "right":
                newRect = new RectHV(curNode.p.x(), outerRect.ymin(),
                                     outerRect.xmax(),
                                     outerRect.ymax());
                break;
            case "bottom":
                newRect = new RectHV(outerRect.xmin(), outerRect.ymin(),
                                     outerRect.xmax(),
                                     curNode.p.y());
                break;
            case "top":
                newRect = new RectHV(outerRect.xmin(), curNode.p.y(),
                                     outerRect.xmax(),
                                     outerRect.ymax());
                break;
            default:
                throw new IllegalArgumentException("Something is wrong!");
        }


        // curRect = newRect;
        if (isDebug) StdOut.println("New rectangle will be: " + newRect.toString());
        return newRect;
    }

    private void debugCmp(int cmp, Node curNode) {
        if (isDebug) StdOut.println(
                "Currently at Node with point: " + curNode.p.toString() + " with orientation "
                        + getOrientation(curNode.orientation));
        if (cmp < 0) {
            if (getOrientation(curNode.orientation) == "vertical") {
                if (isDebug) StdOut.println("Going left");
            }
            else {
                if (isDebug) StdOut.println("Going bottom");
            }
        }
        else if (cmp > 0) {
            // if (isDebug) StdOut.println("Going right/top");
            if (getOrientation(curNode.orientation) == "vertical") StdOut.println("Going right");
            else {
                if (isDebug) StdOut.println("Going top");
            }
        }
    }

    // does set contain p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to contains() is null");
        // return false;
        // StdOut.println("Looking for point " + p.toString());
        if (isDebug) StdOut.println("\n\n*** Searching " + p.toString() + " ***\n");

        return get(p);
    }

    private boolean get(Point2D p) {
        Node found = get(root, p);
        if (found != null) {
            if (isDebug) StdOut.println(
                    "\n=== FOUND " + found.p.toString() + " ===\n");
            return true;
        }

        // StdOut.println("DID NOT FIND point " + p.toString());
        if (isDebug) StdOut.println(
                "\n=== DID NOT FIND " + p.toString() + " ===\n");
        return false;
    }


    private Node get(Node curNode, Point2D p) {
        if (curNode == null) return null;
        int cmp = getCmp(curNode, p);
        if (cmp < 0) {
            debugCmp(cmp, curNode);
            return get(curNode.lb, p);

        }
        else if (cmp > 0) {
            debugCmp(cmp, curNode);
            return get(curNode.rt, p);
        }
        else {
            if (curNode.p.equals(p)) return curNode;
            return null;
        }


    }


    // add point to the set
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calls put() with a null key");

        if (isDebug) StdOut.println("\n\n*** Inserting " + p.toString() + " ***\n");
        // root orientation is chosen to be false/horizontal, so first node will have VERTICAL
        // root = put(root, p, false);
        RectHV curRect;
        // empty tree
        if (root == null) curRect = new RectHV(0, 0, p.x(), 1);
        else curRect = new RectHV(0, 0, 1, 1);
        curRect.draw();
        root = put(root, null, p, curRect);

    }

    private Node put(Node curNode, Node parent, Point2D p, RectHV curRect) {
        // curRect.draw();
        // found where to insert
        if (curNode == null) {
            Node newNode;
            boolean newOrientation;
            RectHV lt;
            RectHV gt;
            size++;

            // when tree is empty
            if (parent != null) {
                newOrientation = !parent.orientation;
            }
            else {
                newOrientation = ROOT_ORIENTATION;
            }

            if (parent != null && getOrientation(newOrientation) == "horizontal") {
                double y_min = Math.min(p.y(), curRect.ymin());
                double y_max = Math.min(p.y(), curRect.ymax());

                curRect = new RectHV(curRect.xmin(), y_min, curRect.xmax(),
                                     y_max);
            }


            if (parent != null && getOrientation(newOrientation) == "vertical") {
                double x_min = Math.min(p.x(), curRect.xmin());
                double x_max = Math.min(p.x(), curRect.xmax());
                curRect = new RectHV(x_min, curRect.ymin(), x_max,
                                     curRect.ymax());
            }

            newNode = new Node(p, newOrientation, curRect);

            // if (isDebug) StdOut.println(curRect);
            // reverse orientation from parent
            if (isDebug) StdOut.println(
                    "\n=== Inserted " + newNode.p.toString() + " to tree" + " with orientation "
                            + getOrientation(newNode.orientation) + " and rectangle: "
                            + newNode.rect
                            .toString() + " ===\n");

            /*StdDraw.setPenColor(Color.BLACK);
            StdDraw.setPenRadius(0.002);
            newNode.rect.draw();
            StdDraw.setPenColor(Color.RED);
            StdDraw.setPenRadius(0.02);
            newNode.p.draw();*/


            return newNode;
        }


        int cmp = getCmp(curNode, p);
        if (cmp < 0) {
            curRect = debugCmp(cmp, curNode, parent, curRect);
            curNode.lb = put(curNode.lb, curNode, p, curRect);

        }
        else if (cmp > 0) {
            curRect = debugCmp(cmp, curNode, parent, curRect);
            curNode.rt = put(curNode.rt, curNode, p, curRect);
        }

        // overwrite previously held value
        else {
            if (isDebug) StdOut.println("Overwriting");
            curNode.p = p;
        }

        return curNode;
    }

    private void inorder(Node curNode) {
        if (curNode == null) return;
        inorder(curNode.lb);
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(Color.RED);
        curNode.p.draw();
        StdDraw.setPenRadius(0.002);
        StdDraw.setPenColor(Color.BLACK);
        curNode.rect.draw();
        inorder(curNode.rt);

    }


    public void draw() {
        inorder(root);

    }

    private void range(Node curNode, Queue<Point2D> q, RectHV rect) {
        if (curNode == null) {
            return;
        }
        if (isDebug) StdOut.println("Checking point: " + curNode.p.toString());
        if (rect.contains(curNode.p)) {

            q.enqueue(curNode.p);
            if (isDebug) StdOut.println("Inserted point: " + curNode.p.toString());
        }

        int cmp = 0;
        double diff_max = 0.0;
        double diff_min = 0.0;
        String curDirection = "";


        if (getOrientation(curNode.orientation) == "vertical") {
            if (curNode.rect.xmax() > rect.xmax()) {
                curDirection = "left";
            }
            else if (curNode.rect.xmax() < rect.xmin()) {
                curDirection = "right";
            }
            else {
                curDirection = "both";
            }

        }
        else if (getOrientation(curNode.orientation) == "horizontal") {
            if (curNode.rect.ymax() > rect.ymax()) {
                curDirection = "bottom";
            }
            else if (curNode.rect.ymax() < rect.ymin()) {
                curDirection = "top";
            }
            else {
                curDirection = "both";
            }
        }


        if (curDirection == "left" || curDirection == "bottom") {

            if (isDebug) StdOut.println(
                    "Direction for point: " + curNode.p.toString() + " is " + curDirection);
            range(curNode.lb, q, rect);

            // if (isDebug) StdOut.println("Traversing point " + curNode.p.toString());

        }
        else if (curDirection == "right" || curDirection == "top") {
            // StdOut.println("Traversing point " + curNode.p.toString());
            if (isDebug) StdOut.println(
                    "Direction for point: " + curNode.p.toString() + " is " + curDirection);
            range(curNode.rt, q, rect);
        }
        else {
            // search both sides
            if (isDebug) StdOut.println(
                    "Direction for point: " + curNode.p.toString() + " is " + curDirection);
            range(curNode.lb, q, rect);
            range(curNode.rt, q, rect);
        }


        // range(curNode.lb, q, rect);
        // if (isDebug) StdOut.println("Traversing point " + curNode.p.toString());
        // range(curNode.rt, q, rect);

        // if (isDebug) StdOut.println("Finished Traversing points for range ");


    }


    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Null data to range!");
        if (isDebug) StdDraw.setPenRadius(0.002);
        if (isDebug) StdDraw.setPenColor(Color.GREEN);
        rect.draw();
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, q, rect);
        if (isDebug) StdOut.println("Done traversal");
        if (isDebug) StdOut.println("Following points in rect");
        if (isDebug) StdDraw.setPenRadius(0.025);
        for (Point2D curP : q) {
            if (isDebug) StdOut.println(curP.toString());
            curP.draw();
        }
        return q;
        /*ArrayList<Point2D> points = new ArrayList<Point2D>();
        points.add(new Point2D(0, 0));
        return points;*/

    }

    //test inorder first by traversing all points
    private void nearest(Node curNode, Point2D p, Node prevNode) {
        if (curNode == null) {

            return;
        }

        double curDistance = p.distanceSquaredTo(curNode.p);
        double prevDistance = curDistance;

        double nowMin;

        if (prevNode != null) {
            prevDistance = p.distanceSquaredTo(prevNode.p);
        }
        StdOut.println("Searching for nearest in " + curNode.p.toString() + " with distance "
                               + curDistance + " and prevDistance " + prevDistance);

        if (curNode.rect.contains(p)) {
            nearest(curNode.lb, p, curNode);

            if (curDistance > prevDistance) {
                nearest(curNode.rt, p, curNode);

            }
            // reached end of subtree
            else {
                StdOut.println("Current champion is " + curDistance);
            }
        }
        else {
            nearest(curNode.rt, p, curNode);
            if (curDistance > prevDistance) nearest(curNode.lb, p, curNode);
            else {
                StdOut.println("Current champion is " + curDistance);
            }
        }


    }


    public Point2D nearest(Point2D p) {
        Point2D nearestPoint = new Point2D(1000, 1000);
        nearest(root, p, null);
        return nearestPoint;
        // return new Point2D(0, 0);
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        StdOut.println("START: is it empty: " + tree.isEmpty());
        StdOut.println("Current size: " + tree.size());

        //test case 1
        /*tree.insert(new Point2D(0.5, 0.5)); // works
        tree.insert(new Point2D(0.7, 0.5));
        tree.insert(new Point2D(0.3, 0.7));
        tree.insert(new Point2D(0.3, 0.4));
        tree.insert(new Point2D(0.8, 0.6));
        tree.insert(new Point2D(0.9, 0.6));
        tree.insert(new Point2D(0.9, 0.8));
        tree.insert(new Point2D(0.9, 0.4));*/

        //Test case on instructions
        tree.insert(new Point2D(0.7, 0.2)); // works
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        tree.draw();
        // tree.range(new RectHV(0.1, 0.35, 0.6, 0.6));
        Point2D query = new Point2D(0.1, 0.9);
        Point2D closest = tree.nearest(query);
        StdDraw.setPenColor(Color.green);
        StdDraw.setPenRadius(0.03);
        closest.draw();
        StdDraw.setPenColor(Color.cyan);
        query.draw();

        Point2D queryB = new Point2D(0.9, 0.9);
        Point2D closestB = tree.nearest(queryB);
        StdDraw.setPenColor(Color.green);
        StdDraw.setPenRadius(0.03);
        closestB.draw();
        StdDraw.setPenColor(Color.cyan);
        queryB.draw();

        //vertical test case
        // tree.insert(new Point2D(0.3, 0.9)); // works
        // tree.insert(new Point2D(0.3, 0.1));
        // tree.insert(new Point2D(0.3, 0.2));
        // tree.insert(new Point2D(0.3, 0.8));
        // tree.insert(new Point2D(0.3, 0.4));
        // tree.insert(new Point2D(0.3, 0.7));
        // tree.insert(new Point2D(0.3, 0.5));
        // tree.draw();
        // tree.range(new RectHV(0.1, 0.35, 0.6, 0.6));
        // tree.nearest(new Point2D(0.1, 0.9));

        // tree.insert(new Point2D(0.9, 0.7));
        // StdOut.println("Current size: " + tree.size());
        // StdOut.println("END: is it empty: " + tree.isEmpty());
        // tree.contains(new Point2D(0.7, 0.6));
        // tree.contains(new Point2D(0.5, 0.5));
        // tree.contains(new Point2D(0.7, 0.5));
        // tree.contains(new Point2D(0.3, 0.5));
        // tree.contains(new Point2D(0.3, 0.4));
        // tree.contains(new Point2D(0.7, 0.6));
        //
        // tree.contains(new Point2D(0.7, 0.9));
        // tree.contains(new Point2D(0.1, 0.9));
        // tree.contains(new Point2D(0.2, 0.9));

    }
}
