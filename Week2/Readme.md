## Programming Assignment 2: Randomized Queues and Deques

[Assignment URL](https://class.coursera.org/algs4partI-010/assignment/view?assignment_id=3)

Follow-up challenge:  For *subset* problem, use only one Deque or RandomizedQueue object of maximum size of output string number *K* rather than input string number *N*.

The naive way of solving *subset* problem with space complexity O(N) is very straightforward.
See `Subset_naive.java` for details.

It is a little tricky to comply with the requirement of space complexity O(K) - how can we do K random sampling if the number of input is not known in advance, or more specifically, without maintaining the whole input series?  
To resolve this problem, we need to use an algorithm called [Reservoir Sampling](https://en.wikipedia.org/wiki/Reservoir_sampling).
The main idea of this algorithm is shown below:  
1. We maintain a queue of size K, where K is the number of random sampling.
In this case, K is the number of strings to be printed out.  
2. For the first K input, we simply enqueue each string.
If the number of input N is no larger than K, we stop here and shuffle the queue.  
3. For the j-th input after K, generate a uniformly distributed random number *r* within [1, j].
If *r* is larger than K, we do nothing and continue to next input.
Otherwise, we substitute the r-th element in the queue with the j-th input string.  
4. Repeat step 3 until the last input.
Then shuffle the queue.

This algorithm guarantees the sampling to be uniform, i.e., the probability of each element to be chosen is K/N.
This can be proved as below:  
1. If N is no larger than K, we can use Knuth Shuffling, which automatically ensures the uniform sampling.  
2. If N is larger than K, for the i-th element where i <= K, it resides in the queue at the beginning but may be replaced by the j-th element, where K < j <= N, at the probability of 1/j.
Therefore, the probability of the i-th element remaining in the queue at the end is:  
<center><img src="http://www.sciweavers.org/upload/Tex2Img_1465088811/render.png"></center>  
, which is the probability of any element after the K doesn't replace it.  
3. For the i-th element where i > K, the probability of being enqueued is K/i.
And the probability of not being replaced by the j-th element, where i < j <= N, is 1/j.
Therefore, the probability of the i-th element remaining in the queue at the end is:
<center><img src="http://www.sciweavers.org/upload/Tex2Img_1465089780/render.png"></center>  
, which is the probability of being enqueue and any subsequent element doesn't replace it.  
4. Thus, the probability of any element to be chosen into the queue is K/N.
Then we shuffle the queue to ensure each element can appear in any position.

Tips:  
1. The analysis above uses 1 as initial index while java uses 0.
We need to be careful about this and some touch-ups are needed when writing programs.  
2. Due to the implementation of *RandomizedQueue*, we cannot choose a specific element to be replaced using `dequeue` method.
But don't need to worry about that since `dequeue` will randomly choose an element in the queue, which doesn't affect the final probability.  
3. Remember to set the reference to NULL when dequeuing to avoid loitering.


**Courtesy of [Sigmainfy's blog](http://www.sigmainfy.com/blog/random-generate-subset-with-length-k-by-reservoir-sampling.html).**
