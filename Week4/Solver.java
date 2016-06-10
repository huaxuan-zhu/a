import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private int totalMoves; // minimum moves to reach goal
    private Node current;   // current node in priority queue

    public Solver(Board initial) // find a solution to the initial board (using the A* algorithm)
    {
        MinPQ<Node> nodePq = new MinPQ<Node>();
        Node initNode, initNodeTwin;
        initNode = new Node(initial, 0);
        initNodeTwin = new Node(initial.twin(), 0); // note twin() do deep copy and swap
        initNodeTwin.setTwin();
        nodePq.insert(initNode);
        nodePq.insert(initNodeTwin);
        current = nodePq.delMin();
        while (!current.isGoal())
        {
            for (Board b : current.neighbors())
            {
                Node currPrev = current.getPrev();
                // get rid of previous node
                if (currPrev != null && b.equals(currPrev.getBoard())) continue;
                Node newNode = new Node(b, current.getMoves() + 1);
                newNode.setPrev(current);
                if (current.isTwin()) newNode.setTwin();
                nodePq.insert(newNode);
            }
            current = nodePq.delMin();
        }
        // if goal is hitted from twin node, original node is unsolvable
        if (current.isTwin())
            totalMoves = -1;
        else
            totalMoves = current.getMoves();
    }

    public boolean isSolvable() // is the initial board solvable?
    {
        return totalMoves != -1;
    }

    public int moves() // min number of moves to solve initial board; -1 if unsolvable
    {
        return totalMoves;
    }

    public Iterable<Board> solution() // sequence of boards in a shortest solution; null if unsolvable
    {
        if (totalMoves == -1) return null;
        Stack<Board> solStack = new Stack<Board>();
        Node traceBack = current;
        while (traceBack != null)
        {
            solStack.push(traceBack.getBoard());
            traceBack = traceBack.getPrev();
        }
        return solStack;
    }

    private class Node implements Comparable<Node>
    {
        // Since protected access modifier is not allowed in this assign
        // inheritance cannot used here. Thus, we have to contain Board in Node
        private Board board;
        private int moves;      // how many moves before hitting this node
        private int manhattan;  // manhattan distance
        private int priority;   // manhattan priority
        private boolean isTwin; // is this node stems from the twin node
        // privious node, used for tracing back and implementing tree-like structure
        private Node previous;

        public Node(Board initial, int moves)
        {
            this.board = initial; // shallow copy
            this.moves = moves;
            manhattan = initial.manhattan();
            priority = manhattan + this.moves;
            isTwin = false;
            previous = null;
        }

        public int compareTo(Node that)
        {
            if (this.priority - that.priority != 0)
                return this.priority - that.priority;
            else // use manhattan distance to break ties
                return this.manhattan - that.manhattan;
        }

        public int getPriority()
        {
            return priority;
        }

        public Board getBoard()
        {
            return this.board;
        }

        public void setTwin()
        {
            this.isTwin = true;
            return;
        }

        public boolean isTwin()
        {
            return this.isTwin;
        }

        public void setPrev(Node prev)
        {
            this.previous = prev;
            return;
        }

        public Node getPrev()
        {
            return this.previous;
        }

        public int getMoves()
        {
            return this.moves;
        }

        public boolean isGoal()
        {
            return this.board.isGoal();
        }

        public Iterable<Board> neighbors()
        {
            return this.board.neighbors();
        }
    }

    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
        StdOut.println("No solution possible");
        else
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
