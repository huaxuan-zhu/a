import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
// import edu.princeton.cs.algs4.StdDraw;
import java.util.TreeSet;
import java.util.Iterator;

public class PointSET {
    private TreeSet<Point2D> pointSet;
    public PointSET() // construct an empty set of points
    {
        pointSet = new TreeSet<Point2D>();
    }
    public boolean isEmpty() // is the set empty?
    {
        return pointSet.isEmpty();
    }
    public int size() // number of points in the set
    {
        return pointSet.size();
    }
    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new java.lang.NullPointerException();
        pointSet.add(p);
        return;
    }
    public boolean contains(Point2D p) // does the set contain point p?
    {
        if (p == null) throw new java.lang.NullPointerException();
        return pointSet.contains(p);
    }
    public void draw() // draw all points to standard draw
    {
        Iterator<Point2D> pIterator = pointSet.iterator();
        while (pIterator.hasNext())
        {
            Point2D p = pIterator.next();
            p.draw();
        }
        return;
    }
    public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle
    {
        if (rect == null) throw new java.lang.NullPointerException();
        // if (pointSet.isEmpty()) return null;
        TreeSet<Point2D> inside = new TreeSet<Point2D>();
        Iterator<Point2D> pIterator = pointSet.iterator();
        while (pIterator.hasNext())
        {
            Point2D p = pIterator.next();
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax() &&
                p.y() >= rect.ymin() && p.y() <= rect.ymax())
                inside.add(p);
        }
        return inside;
    }
    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new java.lang.NullPointerException();
        if (pointSet.isEmpty()) return null;
        Point2D pNearest = null;
        double minSquaredDist = -1;
        Iterator<Point2D> pIterator = pointSet.iterator();
        while (pIterator.hasNext())
        {
            Point2D q = pIterator.next();
            if ((pNearest == null && minSquaredDist == -1) ||
                q.distanceSquaredTo(p) < minSquaredDist)
            {
                pNearest = q;
                minSquaredDist = q.distanceSquaredTo(p);
                continue;
            }
        }
        return pNearest;
    }
    public static void main(String[] args) // unit testing of the methods (optional)
    {}
}
