import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;

public class Subset {
    public static void main(String[] args)
    {
        if (args.length != 1) throw new java.lang.IllegalArgumentException();
        final int K = Integer.parseInt(args[0]);
        RandomizedQueue<String> rqObj = new RandomizedQueue<String>();
        int k = K;
        while (!StdIn.isEmpty())
        {
            rqObj.enqueue(StdIn.readString());
        }
        Iterator<String> i = rqObj.iterator();
        k = K;
        while (i.hasNext() && k > 0)
        {
            System.out.println(i.next());
            k--;
        }
    }
}
