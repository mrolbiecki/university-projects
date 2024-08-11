package MinQueue;

public interface MinQueue<T extends Comparable<T>> {
    void insert (T element);
    T top ();
    void pop ();
    boolean isEmpty ();
}
