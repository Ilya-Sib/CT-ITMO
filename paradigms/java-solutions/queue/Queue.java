package queue;

// Model: [a[1]..a[size]] - elements of Deque

// INV: size >= 0 & ∀ i = 1..size : elements[i] != null

// Immutable: size = size' & ∀ i = 1..size : a[i] = a'[i]

public interface Queue {
    // PRE: obj != null
    // POST: size = size' + 1 & a[size] = obj & ∀ i = 1..size' : a[i] = a'[i]
    void enqueue(Object obj);

    // PRE: size > 0
    // POST: R = a[1] & Immutable
    Object element();

    // PRE: size > 0
    // POST: R = a'[1] & size = size' - 1 & ∀ i = 1..size : a[i] = a'[i + 1]
    Object dequeue();

    // PRE: true
    // POST: R = size & Immutable
    int size();

    // PRE: true
    // POST: R = (size == 0) & Immutable
    boolean isEmpty();

    // PRE: true
    // POST: size = 0
    void clear();

    // PRE: true
    // POST: R = ∃ j : a[j].equals(obj)
    boolean contains(Object obj);

    // :NOTE: Явный min
    // PRE: true
    // POST: Q = {size' + 1} ⋃ {j : 0 < j <= size' & a'[j].equals(obj)} & k : !∃ t < k : t ∈ Q &
    // ∀ i = 1..(k - 1) : a[i] = a'[i] & ∀ i = (k + 1)..size' : a[i - 1] = a'[i] & 
    // size = max(size', k) - 1 & R = |Q| > 1
    boolean removeFirstOccurrence(Object obj);
}
