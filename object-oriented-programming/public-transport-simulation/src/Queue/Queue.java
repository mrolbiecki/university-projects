package Queue;

import Vector.Vector;

public class Queue<T> {
    private final Vector<T> V;
    private int frontIndex;

    public Queue() {
        V = new Vector<T>();
        frontIndex = 0;
    }

    public void push(T x) {
        V.push_back(x);
    }

    public T front () {
        return V.at(frontIndex);
    }

    public void pop () {
        assert frontIndex != V.size();
        frontIndex++;
    }

    public int size () {
        return V.size() - frontIndex;
    }

    public boolean isEmpty () {
        return frontIndex == V.size();
    }
}
