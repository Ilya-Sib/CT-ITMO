package queue;

// Model: [a[1]..a[size]] - elements of Deque

// INV: size >= 0 & elements[i] != null ∀ i = 1..size

// Immutable: size = size' & a[i] = a'[i] ∀ i = 1..size
public class ArrayQueueModule {
    private static int head = 0, size = 0;
    private static final int DEFAULT_CAPACITY = 10;
    private static Object[] elements = new Object[DEFAULT_CAPACITY];

    // PRE: obj != null
    // POST: size = size' + 1 & a[size] = obj & a[i] = a'[i] ∀ i = 1..size'
    public static void enqueue(Object obj) {
        assert obj != null;
        ensureCapacity(size + 1);
        elements[getTail()] = obj;
        size++;
    }

    private static int getTail() {
        return (head + size) % elements.length;
    }

    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] temp = new Object[capacity * 2];
            System.arraycopy(elements, head, temp, 0, elements.length - head);
            System.arraycopy(elements, 0, temp, elements.length - head, head);
            head = 0;
            elements = temp;
        }
    }

    // PRE: size > 0
    // POST: R = a[1] & Immutable
    public static Object element() {
        assert size > 0;
        return elements[head];
    }

    // PRE: size > 0
    // POST: R = a'[1] & size = size' - 1 & a[i] = a'[i + 1] ∀ i = 1..size
    public static Object dequeue() {
        assert size > 0;
        Object res = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return res;
    }

    // PRE: true
    // POST: R = size & Immutable
    public static int size() {
        return size;
    }

    // PRE: true
    // POST: R = (size == 0) & Immutable
    public static boolean isEmpty() {
        return size == 0;
    }

    // PRE: true
    // POST: size = 0
    public static void clear() {
        if (!isEmpty()) {
            head = 0;
            size = 0;
            elements = new Object[DEFAULT_CAPACITY];
        }
    }

    // PRE: obj != null
    // POST: size = size' + 1 & a[1] = obj & a[i + 1] = a'[i] ∀ i = 1..size'
    public static void push(Object obj) {
        assert obj != null;
        ensureCapacity(size() + 1);
        head = (elements.length + head - 1) % elements.length;
        elements[head] = obj;
        size++;
    }

    // PRE: size > 0
    // POST: R = elements[size'] & size = size' - 1 & a[i] = a'[i] ∀ i = 1..size
    public static Object remove() {
        assert size > 0;
        size--;
        Object res = elements[getTail()];
        elements[getTail()] = null;
        return res;
    }

    // PRE: size > 0
    // POST: R = a[size] & Immutable
    public static Object peek() {
        assert size > 0;
        return elements[(head + size - 1) % elements.length];
    }
}
