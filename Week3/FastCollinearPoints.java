import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    private int pnum;
    private ArrayList<LineSegment> segList;

    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null)
            throw new java.lang.NullPointerException("empty point array");
        pnum = points.length;
        segList = new ArrayList<LineSegment>();

        for (int i = 0; i < pnum; ++i)
        {// check null point
            if (points[i] == null)
                throw new java.lang.NullPointerException("null point");
        }

        // default sort based on compareTo method, which uses position
        Point[] pointsPos = points.clone();
        Arrays.sort(pointsPos);

        for (int i = 0; i < pnum - 1; ++i)
        {// check duplicate point
         // in sorted arrays duplicate elements are always adjacent
            if (pointsPos[i].compareTo(pointsPos[i+1]) == 0)
                throw new java.lang.IllegalArgumentException("duplicate points");
        }

        Point[] pointsSlp;
        for (int i = 0; i < pnum; i++)
        {
            // restore the order, so that we can traverse correctly
            pointsSlp = pointsPos.clone();
            Comparator<Point> BY_SLOPE = pointsSlp[i].slopeOrder();
            // sorting by slope, the first point must be pointsPos[i] (NEGATIVE_INFINITY)
            // since system sort over objects is stable, colinear points are
            // also sorted by position
            Arrays.sort(pointsSlp, BY_SLOPE);

            for (int begin = 1, end = 2; end < pnum; end++)
            {// begin indicates the starting index of equal series
             // and end indecates the ending index
                while (end < pnum &&
                    pointsSlp[0].slopeTo(pointsSlp[begin]) ==
                        pointsSlp[0].slopeTo(pointsSlp[end]))
                {
                    end++;
                }
                // if there are at least 3 points having the same slope to pointsPos[i]
                // and the first point is above pointsPos[i], which means this is
                // not a duplicate segment
                if (end - begin >= 3 && pointsSlp[0].compareTo(pointsSlp[begin]) < 0)
                {
                    // The breaking rule of while loop is pointsSlp[end]
                    // is no longer in the equal series. Thus, the last element
                    // of the equal series is pointsSlp[end - 1].
                    // Note the equal series is also sorted by position. This
                    // will ensure the points to be endpoints.
                    segList.add(new LineSegment(pointsSlp[0], pointsSlp[end - 1]));
                }
                begin = end;
            }
        }
    }

    public int numberOfSegments()                  // the number of line segments
    {
        return segList.size();
    }

    public LineSegment[] segments()                // the line segments
    {
        return segList.toArray(new LineSegment[segList.size()]);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
