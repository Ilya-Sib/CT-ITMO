package queue;

public class ArrayQueueADTTest {
    public static void main(String[] args) {
        ArrayQueueADT q = new ArrayQueueADT();
        fillEnqueue(q, 15);
        printParameters(q);
        dumpDequeue(q, 5);
        printParameters(q);
    }

    private static void dumpRemove(ArrayQueueADT q, int x) {
        for (int i = 0; i < x; i++) {
            try {
                System.out.print(ArrayQueueADT.remove(q) + " ");
            } catch (AssertionError e) {
                System.out.print("can't remove, queue is empty");
                break;
            }
        }
        System.out.println();
    }
	

    private static void fillPush(ArrayQueueADT q, int x) {
        for (int i = 0; i < x; i++) {
            ArrayQueueADT.push(q, 10 * i);
        }
    }

    private static void printParameters(ArrayQueueADT q) {
        System.out.println("size = " + ArrayQueueADT.size(q));
        try {
            System.out.println("first element = " + ArrayQueueADT.element(q));
        } catch (AssertionError e) {
            System.out.println("can't print first element, queue is empty");
        }
        try {
            System.out.println("last element = " + ArrayQueueADT.peek(q));
        } catch (AssertionError e) {
            System.out.println("can't print last element, queue is empty");
        }
        System.out.println("queue" + (ArrayQueueADT.isEmpty(q) ? " is " : " isn't ") + "empty");
        System.out.println();
    }

    private static void dumpDequeue(ArrayQueueADT q, int x) {
        for (int i = 0; i < x; i++) {
            try {
                System.out.print(ArrayQueueADT.dequeue(q) + " ");
            } catch (AssertionError e) {
                System.out.print("can't dequeue, queue is empty");
                break;
            }
        }
        System.out.println();
    }

    private static void fillEnqueue(ArrayQueueADT q, int x) {
        for (int i = 0; i < x; i++) {
            ArrayQueueADT.enqueue(q, 10 * i);
        }
    }
}
