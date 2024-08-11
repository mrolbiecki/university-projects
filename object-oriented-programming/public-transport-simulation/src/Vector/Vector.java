package Vector;

public class Vector<T> {
    private int maxSize;
    private int size;
    private Object [] elements;

    public Vector() {
        this.maxSize = 8;
        this.size = 0;
        elements = new Object [maxSize];
    }

    public void resize () {
        maxSize = maxSize * 2;
        Object[] elements2 = new Object [maxSize];
        System.arraycopy(elements, 0, elements2, 0, elements.length);
        elements = elements2;
    }

    public void push_back(T element) {
        if (size == maxSize) {
            resize();
        }
        elements[size++] = element;
    }

    public void pop_back() {
        assert size > 0;
        size--;
    }

    public void insert (T element, int index) {
        assert index >= 0 && index < size;
        if (size == maxSize) {
            resize();
        }
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        size++;
        elements[index] = element;
    }

    public void remove (int index) {
        assert index >= 0 && index < size;
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;
    }

    public T back() {
        assert size > 0;
        return (T) elements[size - 1];
    }

    public T at(int index) {
        assert index >= 0 && index < size;
        return (T) elements[index];
    }

    public void set(int index, T element) {
        assert index >= 0 && index < size;
        elements[index] = element;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size () {
        return size;
    }
}
