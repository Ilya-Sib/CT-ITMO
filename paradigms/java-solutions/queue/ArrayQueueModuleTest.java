package queue;

public class ArrayQueueModuleTest {
    public static void main(String[] args) {
        fill(15);
        printParameters();
        dump(5);
        printParameters();
        dump(9);
        printParameters();
    }

    private static void dumpRemove(int x) {
        for (int i = 0; i < x; i++) {
            try {
                System.out.print(ArrayQueueModule.remove() + " ");
            } catch (AssertionError e) {
                System.out.print("can't remove, queue is empty");
                break;
            }
        }
        System.out.println();
    }

    private static void fillPush(int x) {
        for (int i = 0; i < x; i++) {
            ArrayQueueModule.push(10 * i);
        }
    }

    private static void printParameters() {
        System.out.println("size = " + ArrayQueueModule.size());
        try {
            System.out.println("first element = " + ArrayQueueModule.element());
        } catch (AssertionError e) {
            System.out.println("can't print first element, queue is empty");
        }
        try {
            System.out.println("last element = " + ArrayQueueModule.peek());
        } catch (AssertionError e) {
            System.out.println("can't print last element, queue is empty");
        }
        System.out.println("queue" + (ArrayQueueModule.isEmpty() ? " is " : " isn't ") + "empty");
        System.out.println();
    }

    private static void dump(int x) {
        for (int i = 0; i < x; i++) {
            try {
                System.out.print(ArrayQueueModule.dequeue() + " ");
            } catch (AssertionError e) {
                System.out.print("can't dequeue, queue is empty");
                break;
            }
        }
        System.out.println();
    }

    private static void fill(int x) {
        for (int i = 0; i < x; i++) {
            ArrayQueueModule.enqueue(10 * i);
        }
    }
}
