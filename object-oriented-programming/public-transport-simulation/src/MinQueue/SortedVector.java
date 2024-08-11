package MinQueue;
import Vector.Vector;

public class SortedVector<T extends Comparable<T>> implements MinQueue<T> {
    private final Vector<T> elements;

    public SortedVector() {
        elements = new Vector<T>();
    }

    public void insert (T element) {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.at(i).compareTo(element) <= 0) {
                elements.insert(element, i);
                return;
            }
        }
        elements.push_back(element);
    }

    public T top () {
        return elements.back();
    }

    public void pop () {
        elements.pop_back();
    }

    public boolean isEmpty () {
        return elements.isEmpty();
    }
}
