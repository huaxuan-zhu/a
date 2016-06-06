import java.util.ArrayList;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
    private int pnum;
    private ArrayList<LineSegment> segList;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null)
            throw new java.lang.NullPointerException("empty point array");
        pnum = points.length;
        segList = new ArrayList<LineSegment>();
        for (int i = 0; i < pnum; ++i)
        {
            if (points[i] == null)
                throw new java.lang.NullPointerException("null point");
            for (int j = i + 1; j < pnum; ++j)
            {
                if (points[j] == null)
                    throw new java.lang.NullPointerException("null point");
                if (points[i].compareTo(points[j]) == 0)
                    throw new java.lang.IllegalArgumentException("duplicate points");
            }
        }

        for (int i = 0; i < pnum; ++i)
        {
            for (int j = i + 1; j < pnum; ++j)
            {
                for (int k = j + 1; k < pnum; ++k)
                {
                    for (int l = k + 1; l < pnum; ++l)
                    {
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]) &&
                            points[i].slopeTo(points[k]) == points[i].slopeTo(points[l]))
                        {
                            Point low, high;
                            low = high = points[i];
                            int[] traversal = {j, k, l};
                            for (int r : traversal)
                            {
                                if (low.compareTo(points[r]) > 0)
                                    low = points[r];
                                if (high.compareTo(points[r]) < 0)
                                    high = points[r];
                            }
                            segList.add(new LineSegment(low, high));
                        }
                    }
                }
            }
        }
    }
    public int numberOfSegments()                  // the number of line segments
    {
        return segList.size();
    }
    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] segArr = new LineSegment[segList.size()];
        segArr = segList.toArray(segArr);
        return segArr;
    }
    public static void main(String[] args)
    {
        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
