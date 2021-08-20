package queue;

// Model: [a[1]..a[size]] - elements of Deque q

// INV: size >= 0 & elements[i] != null ∀ i = 1..size

// Immutable: size = size' & a[i] = a'[i] ∀ i = 1..size
public class ArrayQueueADT {
    private int head = 0, size = 0;
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements = new Object[DEFAULT_CAPACITY];


    // PRE: q != null & obj != null
    // POST: size = size' + 1 & a[size] = obj & a[i] = a'[i] ∀ i = 1..size'
    public static void enqueue(ArrayQueueADT q, Object obj) {
        assert q != null && obj != null;
        ensureCapacity(q, q.size + 1);
        q.elements[getTail(q)] = obj;
        q.size++;
    }

    private static int getTail(ArrayQueueADT q) {
        return (q.head + q.size) % q.elements.length;
    }

    private static void ensureCapacity(ArrayQueueADT q, int capacity) {
        if (capacity > q.elements.length) {
            Object[] temp = new Object[capacity * 2];
            System.arraycopy(q.elements, q.head, temp, 0, q.elements.length - q.head);
            System.arraycopy(q.elements, 0, temp, q.elements.length - q.head, q.head);
            q.head = 0;
            q.elements = temp;
        }
    }

    // PRE: q != null & size > 0
    // POST: R = a[1] & Immutable
    public static Object element(ArrayQueueADT q) {
        assert q != null && q.size > 0;
        return q.elements[q.head];
    }

    // PRE: q != null & size > 0
    // POST: R = a'[1] & size = size' - 1 & a[i] = a'[i + 1] ∀ i = 1..size
    public static Object dequeue(ArrayQueueADT q) {
        assert q != null && q.size > 0;
        Object res = q.elements[q.head];
        q.elements[q.head] = null;
        q.head = (q.head + 1) % q.elements.length;
        q.size--;
        return res;
    }

    // PRE: q != null
    // POST: R = size & Immutable
    public static int size(ArrayQueueADT q) {
        assert q != null;
        return q.size;
    }

    // PRE: q != null
    // POST: R = (size == 0) & Immutable
    public static boolean isEmpty(ArrayQueueADT q) {
        assert q != null;
        return q.size == 0;
    }

    // PRE: q != null
    // POST: size = 0
    public static void clear(ArrayQueueADT q) {
        assert q != null;
        if (!isEmpty(q)){
            q.head = 0;
            q.size = 0;
            q.elements = new Object[DEFAULT_CAPACITY];
        }
    }

    // PRE: q != null & obj != null
    // POST: size = size' + 1 & a[1] = obj & a[i + 1] = a'[i] ∀ i = 1..size'
    public static void push(ArrayQueueADT q, Object obj) {
        assert q != null && obj != null;
        ensureCapacity(q, q.size + 1);
        q.head = (q.elements.length + q.head - 1) % q.elements.length;
        q.elements[q.head] = obj;
        q.size++;
    }

    // PRE: q != null & size > 0
    // POST: R = elements[size'] & size = size' - 1 & a[i] = a'[i] ∀ i = 1..size
    public static Object remove(ArrayQueueADT q) {
        assert q != null && q.size > 0;
        q.size--;
        Object res = q.elements[getTail(q)];
        q.elements[getTail(q)] = null;
        return res;
    }

    // PRE: q != null & size > 0
    // POST: R = a[size] & Immutable
    public static Object peek(ArrayQueueADT q) {
        assert q != null && q.size > 0;
        return q.elements[(q.head + q.size - 1) % q.elements.length];
    }
}
