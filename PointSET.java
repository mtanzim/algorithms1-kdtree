/* *****************************************************************************
 *  Name: PointSET.java
 *  Date: Apr 15,2019
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
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
    public boolean conatins(Point2D p) {
        return pointSet.contains(p);
    }

    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        return pointSet;
    }

    public Point2D nearest(Point2D p) {
        return new Point2D(0, 0);
    }

    public static void main(String[] args) {
        StdOut.println("Hello!");
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0.1, 0.2));
        ps.insert(new Point2D(0.2, 0.2));
        ps.insert(new Point2D(0.3, 0.2));
        ps.insert(new Point2D(0.4, 0.2));
        ps.insert(new Point2D(0.5, 0.2));
        ps.draw();
    }
}
