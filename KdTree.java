/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;

public class KdTree {

    // construct empty set of points
    public KdTree() {

    }

    public boolean isEmpty() {
        return true;
    }

    // number of points in the set
    public int size() {
        return 0;
    }

    // add point to the set
    public void insert(Point2D p) {

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

    }
}
