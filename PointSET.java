/* *****************************************************************************
 *  Name: PointSET.java
 *  Date: Apr 15,2019
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {

    private SET<Point2D> pointSet;

    // construct empty set of points
    public PointSET() {
        pointSet = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add point to the set
    public void insert(Point2D p) {
        pointSet.add(p);
    }

    // does set contain p?
    public boolean contains(Point2D p) {
        return pointSet.contains(p);
    }

    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    /*public String toString() {
        return pointSet.toString();
    }*/

    public Iterable<Point2D> range(RectHV rect) {
        // copy over every other point to test clients

        // StdOut.println(rect.toString());
        SET testPointSet = new SET<Point2D>();
        int i = 0;
        for (Point2D p : pointSet) {
            // if (i % 2 == 0) testPointSet.add(p);
            // i++;
            if (rect.contains(p)) testPointSet.add(p);
        }
        return testPointSet;


    }


    public Point2D nearest(Point2D p) {

        class Node implements Comparable<Node> {
            private Point2D myCurP;
            private double myDistanceSq;

            public Node(Point2D curP, double distanceSq) {
                myCurP = curP;
                myDistanceSq = distanceSq;
            }

            public Point2D getCurP() {
                return myCurP;
            }

            public int compareTo(Node that) {
                if (myDistanceSq < that.myDistanceSq) return -1;
                if (myDistanceSq > that.myDistanceSq) return 1;
                return 0;
            }

        }
        SET<Node> nearestPointSet = new SET<Node>();
        for (Point2D curPointInSet : pointSet) {
            if (curPointInSet.equals(p)) continue;
            Node curNode = new Node(curPointInSet, curPointInSet.distanceSquaredTo(p));
            nearestPointSet.add(curNode);
        }
        // for (Node curNode : nearestPointSet) {
        //     curNode.myCurP.toString();
        // }
        // send point in the middle to test clients
        return nearestPointSet.min().getCurP();
    }

    public static void main(String[] args) {
        StdOut.println("Hello!");
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0.1, 0.2));
        ps.insert(new Point2D(0.2, 0.2));
        ps.insert(new Point2D(0.3, 0.2));
        ps.insert(new Point2D(0.4, 0.2));
        ps.insert(new Point2D(0.5, 0.2));
        ps.insert(new Point2D(0.9, 0.3));
        ps.insert(new Point2D(0.8, 0.4));
        ps.insert(new Point2D(0.95, 0.5));
        RectHV searchRect = new RectHV(0.10, 0.15, 0.55, 0.55);
        ps.range(searchRect);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.RED);
        ps.draw();
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        searchRect.draw();
        // StdOut.println(ps.toString());
        StdDraw.setPenColor(StdDraw.GREEN);
        for (Point2D insideP : ps.range(searchRect)) {
            insideP.draw();
        }
        Point2D checkNeighborPoint = new Point2D(0.98, 0.5);
        StdDraw.setPenColor(StdDraw.BLACK);
        checkNeighborPoint.draw();
        Point2D nearestPoint = ps.nearest(checkNeighborPoint);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        nearestPoint.draw();


    }
}
