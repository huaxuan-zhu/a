import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int capacity, size;
    public RandomizedQueue()                 // construct an empty randomized queue
    {
        capacity = 4;
        size = 0;
        queue = (Item[]) new Object[capacity];
    }
    public boolean isEmpty()                 // is the queue empty?
    {
        return size == 0;
    }
    public int size()                        // return the number of items on the queue
    {
        return size;
    }
    private void resize(int newCap)
    {
        if (newCap <= 0) throw new java.lang.IndexOutOfBoundsException();
        if (newCap == capacity) return;
        int oldCap = capacity;
        Item[] newQueue = (Item[]) new Object[newCap];
        int N = (newCap < oldCap) ? newCap : oldCap;
        for (int i = 0; i < N; ++i)
            newQueue[i] = queue[i];
        queue = newQueue;
        capacity = newCap;
    }
    public void enqueue(Item item)           // add the item
    {
        if (item == null) throw new java.lang.NullPointerException();
        if(size + 1 > capacity)
        resize(capacity * 2);
        queue[size++] = item;
    }
    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int idx = StdRandom.uniform(size);
        Item returned = queue[idx];
        queue[idx] = queue[--size];
        queue[size] = null; // avoid loitering
        if(capacity > 4 && (size * 4 < capacity))  // avoid resizing back and forth
        resize(capacity / 2);
        return returned;
    }
    public Item sample()                     // return (but do not remove) a random item
    {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int idx = StdRandom.uniform(size);
        return queue[idx];
    }
    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new QueueIterator();
    }
    private class QueueIterator implements Iterator<Item>
    {
        private int[] shuffledIdx;
        int current;
        public QueueIterator()
        {
            shuffledIdx = new int[size];
            current = 0;
            for (int i = 0; i < size; ++i)
            shuffledIdx[i] = i;
            StdRandom.shuffle(shuffledIdx);
        }
        public boolean hasNext()
        {
            return current < size;
        }
        public Item next()
        {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return queue[shuffledIdx[current++]];
        }
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
    public static void main(String[] args)   // unit testing
    {}
}
