## Programming Assignment 3:  Collinear Points

[Assignment URL](https://class.coursera.org/algs4partI-010/assignment/view?assignment_id=4)

There are two methods of solving this problem.

`BruteCollinearPoints.java` traverses all the 4 points combinations and examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments.
To check whether the 4 points p, q, r, and s are collinear, check whether the three slopes between p and q, between p and r, and between p and s are all equal.
So the time complexity is O(N^4).
Then find the endpoints of the line segment by location information with `compareTo()` method in `Point` class.
Note that this program cannot handle 5 points on a line segment.

`FastCollinearPoints.java` uses a clever also faster algorithm, which is described as below:  
1. Given a point *p*, think of p as the origin.   
2. For each other point q, determine the slope it makes with p.  
3. Sort the points according to the slopes they makes with p.  
4. Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p. 
If so, these points, together with p, are collinear.  
5. Applying this method for each of the N points in turn.

The algorithm solves the problem because points that have equal slopes with respect to p are collinear, and sorting brings such points together. 
The algorithm is fast because the bottleneck operation is sorting.
For each point, sort the other points takes O(NlgN), thus, the total time complexity is O(N^2 lgN).

The tricky part is the process above can't handle duplicate line segments.
We need some modifications to get rid of duplications and find endpoints for each segment.
Before sorting by slopes, we first sort by locations to make use of location info.
Since the system sort `Arrays.sort()` uses mergesort for objects, which is stable, points that have equal slopes are also sorted by their locations after two sorting operations.  
It is self-evident that the lowest and the highest point (with regard to `compareTo()` method, which first compares y-coordinate then x-coordinate to break ties) are endpoints. 
Note that after the point array is sorted by slopes with regard to point *p*, the first element of the sorted array must be point *p* since the slope to itself is `NEGATIVE_INFINITY`.
Therefore, we add to segment list if and only if the beginning point of the equal sequence is higher than the first point of the sorted array.
And the endpoints of this segment are the first point of the sorted array(the lower endpoint) and the last point of the equal sequence(the higher endpoint).

Miscellaneous:  
Note that if x is +0.0 and y is -0.0 while (x == y) is guaranteed to be true, `Arrays.sort()` treats negative zero as strictly less than positive zero. 
It may cause some problem when casting to wrapper type Double. 
Therefore, to avoid any potential problem of signed zero, set all horizontal slopes to be +0.0.