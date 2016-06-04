import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
    public static void main(String[] args)
    {
        if (args.length != 1) throw new java.lang.IllegalArgumentException(
            "There should be one and only one argument indicating number K.");
        final int K = Integer.parseInt(args[0]);
        RandomizedQueue<String> rqObj = new RandomizedQueue<String>();
        int count = 0; // count number of string input
        while (!StdIn.isEmpty())
        {// implement reservoir sampling
            if(count < K)
                rqObj.enqueue(StdIn.readString());
            else
            {
                String temp = StdIn.readString();
                // uniform is right-open and rand should be in [0, count]
                int rand = StdRandom.uniform(count + 1);
                if (rand < K)
                {
                    rqObj.dequeue();
                    rqObj.enqueue(temp);
                }
            }
            count++;
        }
        Iterator<String> i = rqObj.iterator();
        while (i.hasNext())
            System.out.println(i.next());
    }
}
