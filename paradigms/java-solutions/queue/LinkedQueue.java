package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head, tail;

    @Override
    protected void enqueueImpl(final Object obj) {
        final Node prevTail = tail;
        tail = new Node(null, obj);
        if (isEmpty()) {
            head = tail;
        } else {
            prevTail.next = tail;
        }
    }

    @Override
    protected Object elementImpl() {
        return head.value;
    }

    @Override
    protected Object dequeueImpl() {
        final Object res = head.value;
        head = head.next;
        if (isEmpty()) {
            tail = null;
        }
        return res;
    }

    @Override
    protected void clearImpl() {
        head = null;
        tail = null;
    }

    private static class Node {
        private Node next;
        private final Object value;

        private Node(final Node next, final Object value) {
            this.next = next;
            this.value = value;
        }
    }
}
