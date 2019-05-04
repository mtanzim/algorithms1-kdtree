/* *****************************************************************************
 *  Name: KdTree
 *  Date: Apr 23, 2019
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
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
        // if true/vertical
        if (curNode.orientation) {
            if (p.x() < curNode.p.x()) return -1;
            if (p.x() > curNode.p.x()) return 1;
            if (p.y() < curNode.p.y()) return -1;
            if (p.y() > curNode.p.y()) return 1;
            return 0;
        }
        // false/horizontal
        else {
            if (p.y() < curNode.p.y()) return -1;
            if (p.y() > curNode.p.y()) return 1;
            if (p.x() < curNode.p.x()) return -1;
            if (p.x() > curNode.p.x()) return 1;
            return 0;
        }
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
        else if (cmp >= 0) {
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




    /*private void debugCmp(int cmp, Node curNode) {
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
            if (getOrientation(curNode.orientation) == "vertical")
                if (isDebug) StdOut.println("Going right");
                else {
                    if (isDebug) StdOut.println("Going top");
                }
        }
    }*/

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
            // debugCmp(cmp, curNode);
            return get(curNode.lb, p);

        }
        else if (cmp > 0) {
            // debugCmp(cmp, curNode);
            return get(curNode.rt, p);
        }
        else {
            if (curNode.p.equals(p)) return curNode;
            return null;
        }


    }

    private double[] prepareRect(int cmp, Node curNode, double[] curCoords) {
        // if (isDebug) StdOut.println(curRect.toString());
        double[] outerCoords = curCoords;
        double[] newCoords = outerCoords.clone();


        if (isDebug) StdOut.println(
                "\nCurrently at Node with point: " + curNode.p.toString() + " with orientation "
                        + getOrientation(curNode.orientation));


        if (isDebug) StdOut.println("Starting rectangle is: " + curCoords.toString());

        String curDirection = getDirection(cmp, curNode);
        switch (curDirection) {
            case "left":
                newCoords[2] = curNode.p.x();
                break;
            case "right":
                newCoords[0] = curNode.p.x();
                break;
            case "bottom":
                newCoords[3] = curNode.p.y();
                break;
            case "top":
                newCoords[1] = curNode.p.y();
                break;
            default:
                throw new IllegalArgumentException("Something is wrong!");
        }

        // curRect = newRect;
        if (isDebug) StdOut.println("New rectangle will be: " + newCoords.toString());
        return newCoords;
    }


    private Node put(Node curNode, Node parent, Point2D p, double[] curCoords) {
        // found where to insert
        if (curNode == null) {
            Node newNode;
            boolean newOrientation;

            // when tree is empty
            if (parent != null) {
                newOrientation = !parent.orientation;
                if (getOrientation(newOrientation) == "horizontal") {
                    curCoords[1] = Math.min(p.y(), curCoords[1]);
                    curCoords[3] = Math.min(p.y(), curCoords[3]);
                }
                else if (getOrientation(newOrientation) == "vertical") {
                    curCoords[0] = Math.min(p.x(), curCoords[0]);
                    curCoords[2] = Math.min(p.x(), curCoords[2]);
                }
            }
            else {
                newOrientation = ROOT_ORIENTATION;
            }


            newNode = new Node(p, newOrientation,
                               new RectHV(curCoords[0], curCoords[1], curCoords[2], curCoords[3]));

            // if (isDebug) StdOut.println(curRect);
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
            size++;
            return newNode;
        }


        int cmp = getCmp(curNode, p);
        curCoords = prepareRect(cmp, curNode, curCoords);
        if (cmp < 0) {
            curNode.lb = put(curNode.lb, curNode, p, curCoords);
        }
        else if (cmp > 0) {
            curNode.rt = put(curNode.rt, curNode, p, curCoords);
        }
        // overwrite previously held value
        else {
            if (isDebug) StdOut.println("Overwriting");
            curNode.p = p;
        }
        return curNode;
    }

    // add point to the set
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calls put() with a null key");
        if (isDebug) StdOut.println("\n\n*** Inserting " + p.toString() + " ***\n");
        double[] curCoords = { 0, 0, 1, 1 };
        if (root == null) curCoords[2] = p.x();
        root = put(root, null, p, curCoords);
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

    }


    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Null data to range!");
        Queue<Point2D> q = new Queue<Point2D>();
        if (isEmpty()) return q;
        range(root, q, rect);
        return q;
    }

    private Point2D nearest(Point2D p, Node curNode, Node prevNode, double curMin,
                            Point2D curChamp) {


        if (curNode == null) return curChamp;
        if (isEmpty()) return null;

        if (isDebug) StdOut.println("Traversing point " + curNode.p.toString());

        double curDistance = curNode.p.distanceSquaredTo(p);
        if (curDistance < curMin) {
            curMin = curDistance;
            curChamp = curNode.p;
        }

        if (curNode.rect.contains(p)) {
            if (isDebug) StdOut.println("Going " + getDirection(-1, curNode));
            // get champion of left subtree
            Point2D immediateChamp = nearest(p, curNode.lb, curNode, curMin, curChamp);
            curMin = p.distanceSquaredTo(immediateChamp);
            curChamp = immediateChamp;

            if (isDebug) StdOut.println("Back to point " + curNode.p.toString());

            // also go right
            if (curMin <= curDistance) {
                if (isDebug) StdOut.println("Going " + getDirection(-1, curNode));
                return nearest(p, curNode.rt, curNode, curMin, curChamp);
            }
        }
        else {
            if (isDebug) StdOut.println("Going " + getDirection(1, curNode));
            Point2D immediateChamp = nearest(p, curNode.rt, curNode, curMin, curChamp);
            curMin = p.distanceSquaredTo(immediateChamp);
            curChamp = immediateChamp;
            // also go left
            if (isDebug) StdOut.println("Back to point " + curNode.p.toString());
            if (curMin <= curDistance) {
                if (isDebug) StdOut.println("Going " + getDirection(-1, curNode));
                return nearest(p, curNode.lb, curNode, curMin, curChamp);
            }
        }
        return curChamp;


    }


    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null point provided");
        if (isEmpty()) return null;
        Point2D nearestPoint = nearest(p, root, null, 10.0, root.p);
        if (isDebug) StdOut.println("\nFinal champion is " + nearestPoint.toString());
        return nearestPoint;
    }

    public static void main(String[] args) {

        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        kdtree.draw();
        // Point2D query = new Point2D(0.393, 0.955);
        Point2D query = new Point2D(0.205, 0.407);

        StdDraw.setPenColor(Color.cyan);
        StdDraw.setPenRadius(0.03);
        query.draw();
        Point2D nearest = kdtree.nearest(query);
        StdDraw.setPenColor(Color.GREEN);
        nearest.draw();
        StdOut.println(nearest.toString());


        // StdOut.println("START: is it empty: " + tree.isEmpty());
        // StdOut.println("Current size: " + tree.size());

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
        // tree.insert(new Point2D(0.7, 0.2)); // works
        // tree.insert(new Point2D(0.5, 0.4));
        // tree.insert(new Point2D(0.2, 0.3));
        // tree.insert(new Point2D(0.4, 0.7));
        // tree.insert(new Point2D(0.9, 0.6));
        // tree.draw();
        // tree.range(new RectHV(0.1, 0.35, 0.6, 0.6));
        // Point2D query = new Point2D(0.1, 0.9);
        // StdDraw.setPenColor(Color.cyan);
        // StdDraw.setPenRadius(0.03);
        // query.draw();
        // Point2D closest = tree.nearest(query);
        // StdDraw.setPenColor(Color.green);
        // closest.draw();


        // Point2D queryB = new Point2D(0.9, 0.9);
        // Point2D closestB = tree.nearest(queryB);
        // StdDraw.setPenColor(Color.green);
        // StdDraw.setPenRadius(0.03);
        // closestB.draw();
        // StdDraw.setPenColor(Color.cyan);
        // queryB.draw();


        // Point2D queryC = new Point2D(0.2, 0.45);
        // Point2D closestC = tree.nearest(queryC);
        // StdDraw.setPenColor(Color.green);
        // StdDraw.setPenRadius(0.03);
        // closestC.draw();
        // StdDraw.setPenColor(Color.cyan);
        // queryC.draw();

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
