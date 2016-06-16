import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.TreeSet;

public class KdTree {
    private static final byte VERTICAL   = 0;
    private static final byte HORIZONTAL = 1;
    private static final double MINX     = 0.0;
    private static final double MINY     = 0.0;
    private static final double MAXX     = 1.0;
    private static final double MAXY     = 1.0;
    private static class Node
    {
        private Point2D point;    // the point
        private RectHV rect;      // the axis-aligned rectangle corresponding to this node
        private Node left_bottom; // the left/bottom subtree
        private Node right_top;   // the right/top subtree
        private byte direction;   // direction of this node

        public Node(Point2D p, byte d)
        {
            this.point = p;
            if (d == VERTICAL)
                this.rect = new RectHV(this.point.x(), 0, this.point.x(), 1);
            else // if (d == HORIZONTAL)
                this.rect = new RectHV(0, this.point.y(), 1, this.point.y());
            this.left_bottom = null;
            this.right_top = null;
            this.direction = d;
        }

        public double x()
        {
            return this.point.x();
        }

        public double y()
        {
            return this.point.y();
        }

        public void draw()
        {
            this.point.draw();
            return;
        }

        public boolean equals(Point2D p)
        {
            if (p == null) throw new java.lang.NullPointerException();
            return this.point.equals(p);
        }
    }

    private int count;
    private Node root;

    public KdTree() // construct an empty set of points
    {
        this.count = 0;
        this.root = null;
    }

    public boolean isEmpty() // is the set empty?
    {
        return this.count == 0;
    }

    public int size() // number of points in the set
    {
        return this.count;
    }

    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new java.lang.NullPointerException();
        this.root = insert(this.root, p, VERTICAL);
        return;
    }
    private Node insert(Node h, Point2D p, byte direction)
    {// recursive helper function
        if (h == null)
        {
            this.count++;
            // System.out.printf("%f, %f\n", p.x(), p.y());
            return new Node(p, direction);
        }
        if (h.equals(p)) return h; // if inserting an already existing point
        if (direction == VERTICAL)
        {
            if (p.x() <= h.x())
            {
                // if (h.left_bottom == null)
                //     System.out.printf("%f, %f go left, root: %f, %f\n", p.x(), p.y(), h.x(), h.y());
                h.left_bottom = insert(h.left_bottom, p, HORIZONTAL);
            }
            if (p.x() > h.x())
            {
                // if (h.right_top == null)
                    // System.out.printf("%f, %f go right, root: %f, %f\n", p.x(), p.y(), h.x(), h.y());
                h.right_top = insert(h.right_top, p, HORIZONTAL);
            }

        }
        if (direction == HORIZONTAL)
        {
            if (p.y() <= h.y())
            {
                // if (h.left_bottom == null)
                    // System.out.printf("%f, %f go down, root: %f, %f\n", p.x(), p.y(), h.x(), h.y());
                h.left_bottom = insert(h.left_bottom, p, VERTICAL);
            }
            if (p.y() > h.y())
            {
                // if (h.right_top == null)
                    // System.out.printf("%f, %f go up, root: %f, %f\n", p.x(), p.y(), h.x(), h.y());
                h.right_top = insert(h.right_top, p, VERTICAL);
            }
        }
        return h;
    }

    public boolean contains(Point2D p) // does the set contain point p?
    {
        if (p == null) throw new java.lang.NullPointerException();
        return contains(this.root, p);
    }
    private boolean contains(Node h, Point2D p)
    {// recursive helper function
        if (h == null) return false;
        if (h.equals(p)) return true;
        byte direction = h.direction;
        if (direction == VERTICAL)
        {
            if (p.x() <= h.x()) return contains(h.left_bottom, p);
            if (p.x() > h.x()) return contains(h.right_top, p);
        }
        if (direction == HORIZONTAL)
        {
            if (p.y() <= h.y()) return contains(h.left_bottom, p);
            if (p.y() > h.y()) return contains(h.right_top, p);
        }
        return false;
    }

    public void draw() // draw all points to standard draw
    {
        draw(this.root, MINX, MINY, MAXX, MAXY);
    }
    private void draw(Node x, double xmin, double ymin, double xmax, double ymax)
    {// recursive helper function
        if (x == null) return;
        Point2D point = x.point;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        point.draw();
        byte direction = x.direction;
        if (direction == VERTICAL)
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(.001);
            StdDraw.line(point.x(), ymin, point.x(), ymax);
            draw(x.left_bottom, xmin, ymin, point.x(), ymax);
            draw(x.right_top, point.x(), ymin, xmax, ymax);
        }
        if (direction == HORIZONTAL)
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(.001);
            StdDraw.line(xmin, point.y(), xmax, point.y());
            draw(x.left_bottom, xmin, ymin, xmax, point.y());
            draw(x.right_top, xmin, point.y(), xmax, ymax);
        }
        return;
    }

    public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle
    {
        if (rect == null) throw new java.lang.NullPointerException();
        // if (this.isEmpty()) return null;
        TreeSet<Point2D> inside = new TreeSet<Point2D>();
        range(this.root, inside, rect);
        return inside;
    }
    private void range(Node h, TreeSet<Point2D> inside, RectHV target)
    {// recursive helper function
        if (h == null) return;
        Point2D p = h.point;
        if (p.x() >= target.xmin() && p.x() <= target.xmax() &&
            p.y() >= target.ymin() && p.y() <= target.ymax())
            inside.add(p);

        RectHV pointRect = h.rect;
        if (pointRect.intersects(target))
        {
            range(h.left_bottom, inside, target);
            range(h.right_top, inside, target);
        }
        byte direction = h.direction;
        if (direction == VERTICAL)
        {
            if (target.xmax() <= h.x()) range(h.left_bottom, inside, target);
            if (target.xmin() >= h.x()) range(h.right_top, inside, target);
        }
        if (direction == HORIZONTAL)
        {
            if (target.ymax() <= h.y()) range(h.left_bottom, inside, target);
            if (target.ymin() >= h.y()) range(h.right_top, inside, target);
        }
        return;
    }

    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new java.lang.NullPointerException();
        if (this.isEmpty()) return null;
        return nearest(this.root, p, this.root).point;
    }
    private Node nearest(Node h, Point2D p, Node currMin)
    {
        if (h == null) return currMin;
        Point2D hPoint = h.point, cPoint = currMin.point;
        if (hPoint.distanceSquaredTo(p) < cPoint.distanceSquaredTo(p))
            currMin = h;
        byte direction = h.direction;
        if (direction == VERTICAL)
        {
            if (p.x() < hPoint.x())
            {
                currMin = nearest(h.left_bottom, p, currMin);
                Point2D temp = currMin.point;
                if (temp.distanceSquaredTo(p) > Math.pow(hPoint.x() - p.x(), 2))
                    currMin = nearest(h.right_top, p, currMin);
            }
            else if (p.x() > hPoint.x())
            {
                currMin = nearest(h.right_top, p, currMin);
                Point2D temp = currMin.point;
                if (temp.distanceSquaredTo(p) > Math.pow(p.x() - hPoint.x(), 2))
                    currMin = nearest(h.left_bottom, p, currMin);
            }
            else
            {
                currMin = nearest(h.left_bottom, p, currMin);
                currMin = nearest(h.right_top, p, currMin);
            }
        }

        if (direction == HORIZONTAL)
        {
            if (p.y() < hPoint.y())
            {
                currMin = nearest(h.left_bottom, p, currMin);
                Point2D temp = currMin.point;
                if (temp.distanceSquaredTo(p) > Math.pow(hPoint.y() - p.y(), 2))
                    currMin = nearest(h.right_top, p, currMin);
            }
            else if (p.y() > hPoint.y())
            {
                currMin = nearest(h.right_top, p, currMin);
                Point2D temp = currMin.point;
                if (temp.distanceSquaredTo(p) > Math.pow(p.y() - hPoint.y(), 2))
                    currMin = nearest(h.left_bottom, p, currMin);
            }
            else
            {
                currMin = nearest(h.left_bottom, p, currMin);
                currMin = nearest(h.right_top, p, currMin);
            }
        }
        return currMin;
    }
    public static void main(String[] args) // unit testing of the methods (optional)
    {
        KdTree test = new KdTree();
        Point2D p1 = new Point2D(0.5, 0.5);
        System.out.println(test.size() + " - size");
        System.out.println(test.isEmpty() + " - isEmpty");
        test.insert(p1);
        System.out.println(test.size() + " - size");

        Point2D p2 = new Point2D(0.1, 0.1);
        test.insert(p2);
        System.out.println(test.size() + " - size");

        Point2D p3 = new Point2D(0.7, 0.7);
        test.insert(p3);
        System.out.println(test.size() + " - size");

        Point2D p4 = new Point2D(0.7, 0.1);
        test.insert(p4);
        System.out.println(test.size() + " - size");

        Point2D p5 = new Point2D(0.6, 0.2);
        test.insert(p5);
        System.out.println(test.size() + " - size");

        Point2D p6 = new Point2D(0.4, 0.6);
        test.insert(p6);
        System.out.println(test.size() + " - size");

        System.out.println("contains p3? " + test.contains(p3));
        System.out.println("contains new Point 0.2-0.2? " + test.contains(new Point2D(0.2, 0.2)));

        System.out.println("nearest to 0.2-0.2" + test.nearest(new Point2D(0.2, 0.2)));

        // test.draw();
    }
}
