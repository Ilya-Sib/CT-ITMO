package queue;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    @Override
    public void enqueue(final Object obj) {
        assert obj != null;
        enqueueImpl(obj);
        size++;
    }

    @Override
    public Object element() {
        assert size > 0;
        return elementImpl();
    }

    @Override
    public Object dequeue() {
        assert size > 0;
        size--;
        return dequeueImpl();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        if (!isEmpty()) {
            size = 0;
            clearImpl();
        }
    }

    @Override
    public boolean contains(final Object obj) {
        return containsAndRemove(obj, false);
    }

    @Override
    public boolean removeFirstOccurrence(final Object obj) {
        return containsAndRemove(obj, true);
    }

    protected boolean containsAndRemove(final Object obj, final boolean remove) {
        boolean found = false;
        final int oldSize = size;
        for (int i = 0; i < oldSize; i++) {
            final Object curr = dequeue();
            if (!found && curr.equals(obj)) {
                found = true;
                if (remove) {
                    continue;
                }
            }
            enqueue(curr);
        }
        return found;
    }

    protected abstract void clearImpl();
    protected abstract void enqueueImpl(Object obj);
    protected abstract Object elementImpl();
    protected abstract Object dequeueImpl();
}
