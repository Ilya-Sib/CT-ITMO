package queue;

public class ArrayQueueClassTest {
    public static void main(String[] args) {
        ArrayQueue q = new ArrayQueue();
        fill(q, 29);
        printParameters(q);
        dump(q, 19);
    }

    private static void dumpRemove(ArrayQueue q, int x) {
        for (int i = 0; i < x; i++) {
            try {
                System.out.print(q.remove() + " ");
            } catch (AssertionError e) {
                System.out.print("can't remove, queue is empty");
                break;
            }
        }
        System.out.println();
    }

    private static void fillPush(ArrayQueue q, int x) {
        for (int i = 0; i < x; i++) {
            q.push(10 * i);
        }
    }

    private static void printParameters(ArrayQueue q) {
        System.out.println("size = " + q.size());
        try {
            System.out.println("first element = " + q.element());
        } catch (AssertionError e) {
            System.out.println("can't print first element, queue is empty");
        }
        try {
            System.out.println("last element = " + q.peek());
        } catch (AssertionError e) {
            System.out.println("can't print last element, queue is empty");
        }
        System.out.println("queue" + (q.isEmpty() ? " is " : " isn't ") + "empty");
        System.out.println();
    }

    private static void dump(ArrayQueue q, int x) {
        for (int i = 0; i < x; i++) {
            try {
                System.out.print(q.dequeue() + " ");
            } catch (AssertionError e) {
                System.out.print("can't dequeue, queue is empty");
                break;
            }
        }
        System.out.println();
    }

    private static void fill(ArrayQueue q, int x) {
        for (int i = 0; i < x; i++) {
            q.enqueue(10 * i);
        }
    }
}
