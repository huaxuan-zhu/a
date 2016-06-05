## Programming Assignment 1: Percolation
[Assignment URL](https://class.coursera.org/algs4partI-010/assignment/view?assignment_id=1)

Most part of this assignment is pretty straightforward. 
However, [back wash problem](http://coursera.cs.princeton.edu/algs4/checklists/percolation.html) is the tricky part and worth emphasis.

There are two recipes for resolving backwash.  
1. The first one is to use virtual sites and maintain two *WeightedQuickUnionUF* objects. 
One object uses both a virtual top site and a virtual bottom site.
The virtual top site is virtually adjacent to all the sites on top and the similar goes for the virtual bottom site.
Once we detect the virtual top and bottom are connected, at least one site on bottom is connected with at least one site on top. 
Thus, the system is percolated.   
The problem is, however, if we use this WeightedQuickUnionUF object to detect whether a site is full, i.e., connected with any top site, backwash may arise since any site connected with the bottom will connected with the top then via virtual sites.  
Therefore, we need another WeightedQuickUnionUF object with only a virtual top site and we use this WeightedQuickUnionUF object to determine whether a site is full.  
This approach is implemented in [Percolation_two_WQUUF.java](https://github.com/hzhu007/Princeton-Algorithms/blob/master/Week1/Percolation_two_WQUUF.java).  
2. The second approach is to maintain an array of byte indicating the status of each site. 
There are four possible statuses: blocked, open, connected to the top and connected to the bottom.
Each status is represented by a single bit respectively in the 8-bit byte data type.
So a site can have multiple statues using bitwise operations.  
This method doesn't use any virtual site. 
The following is the specific approach:  
(1) At the beginning, all sites' statues are *blocked*. Set the status of each site on top to be *connected to the top* and the similar goes for bottom.  
(2) Every time we open a blocked site, we need to set its status to open and union it to its neighbors.
Before each union call, find the root of the tree of this neighbor and record its status.
After traversal of all its neighbors, we have a new tree consisting of trees which neighbors are originally in and the newly opened site. 
Update the status of the new root based on the statues the roots of original trees and also the status of the newly opened site.  
(3) After each open operation, we check if the new root is connected to both the bottom and top. 
If so, the system is percolated. 
To check if a site is open, simply check if the bit for open is 1 and to check if a site is full, check if the bits for open and connected to the top are both 1.
This approach is implemented in [Percolation.java](https://github.com/hzhu007/Princeton-Algorithms/blob/master/Week1/Percolation.java).

P.S.
1. Maintaining a status array, which is a byte array in Java, has less overhead than maintaining another WeightedQuickUnionUF object. 
So the second approach is more efficient and can earn the bonus credit for only using one WeightedQuickUnionUF object.
Actually, we need only one array in the second method since status can indicate open and connection with top or bottom at the same time.  
2. I also implement my own WeightedQuickUnionUF in [MyPercolation.java](https://github.com/hzhu007/Princeton-Algorithms/blob/master/Week1/MyPercolation.java).
The second method is also used in this file.

**Courtesy of [Sigmainfy's blog](http://www.sigmainfy.com/blog/avoid-backwash-in-percolation.html).**