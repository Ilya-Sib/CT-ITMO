package queue;

public class ArrayQueue extends AbstractQueue {
    private int head = 0;
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements = new Object[DEFAULT_CAPACITY];

    @Override
    protected void enqueueImpl(Object obj) {
        ensureCapacity(size + 1);
        elements[getTail()] = obj;
    }

    @Override
    public Object elementImpl() {
        return elements[head];
    }

    @Override
    protected Object dequeueImpl() {
        Object res = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return res;
    }

    @Override
    protected void clearImpl() {
        head = 0;
        elements = new Object[DEFAULT_CAPACITY];
    }

    // PRE: obj != null
    // POST: size = size' + 1 & a[1] = obj & a[i + 1] = a'[i] ∀ i = 1..size'
    public void push(Object obj) {
        assert obj != null;
        ensureCapacity(size + 1);
        head = (elements.length + head - 1) % elements.length;
        elements[head] = obj;
        size++;
    }

    // PRE: size > 0
    // POST: R = elements[size'] & size = size' - 1 & a[i] = a'[i] ∀ i = 1..size
    public Object remove() {
        assert size > 0;
        size--;
        Object res = elements[getTail()];
        elements[getTail()] = null;
        return res;
    }

    // PRE: size > 0
    // POST: R = a[size] & Immutable
    public Object peek() {
        assert size > 0;
        return elements[(head + size - 1) % elements.length];
    }

    private int getTail() {
        return (head + size) % elements.length;
    }

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] temp = new Object[capacity * 2];
            System.arraycopy(elements, head, temp, 0, elements.length - head);
            System.arraycopy(elements, 0, temp, elements.length - head, head);
            head = 0;
            elements = temp;
        }
    }
}