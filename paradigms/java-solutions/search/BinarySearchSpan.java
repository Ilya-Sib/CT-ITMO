package search;


public class BinarySearchSpan {
    // POST: Выводит в консоль r: a[r - 1] > x >= a[r] и длину промежутка
    public static void main(String[] args) {
        // PRE: args != null & args[i] != null ∀ i = 0..args.length - 1 
        // & args[i] >= args[i + 1] ∀ i = 1..args.length - 2 & args.length > 0 
        // & args[i] - число ∀ i = 0..args.length - 1
        int x = Integer.parseInt(args[0]);
        // x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        // x = Integer.parseInt(args[0]) & a = int[args.length - 1]
        int i = 0;
        // x = Integer.parseInt(args[0]) & a = int[args.length - 1] & i = 0
        // INV: i >= 0
        while (i < args.length - 1) {
            // i >= 0
            a[i] = Integer.parseInt(args[i + 1]);
            // i >= 0 & a[i] = Integer.parseInt(args[i + 1])
            i += 1;
            // i > 0 & a[i] = Integer.parseInt(args[i + 1])
        }
        // a != null & a[i] >= a[i + 1] ∀ i = 0..a.length - 2
        int l = recursiveBinarySearch(x, a);
        // a[r - 1] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 0..a.length - 2 & a != null
        int r = iterativeBinarySearch(x, a);
        // a[r - 1] >= x > a[r] & a[i] >= a[i + 1] ∀ i = 0..a.length - 2 & a != null
        System.out.println(l + " " + (r - l));
        // Вывод в консоль значения
    }

    // POST: a[r - 1] >= x > a[r] & a[i] >= a[i + 1] ∀ i = 0..a.length - 2 & a != null
    public static int iterativeBinarySearch(int x, int[] a) {
        // PRE: a != null & a[i] >= a[i + 1] ∀ i = 0..a.length - 2
        // a[-1] = +INF & a[a.length] = -INF
        int l = -1;
        // l = -1
        int r = a.length;
        // l = -1 & r = a.length
        // INV: a[l] >= x > a[r] & r - l >= 1
        // Пояснение: r - l >= 1 т.к l = -1, a r >= 0
        while (r - 1 != l) {
            // a[l] >= x > a[r] & r - l > 1
            int m = (l - r) / 2 + r;
            // a[l] >= x > a[r] & r - l > 1 & l < m = (l + r) / 2 < r
            // Пояснение: m < r т.к l < r из инварината => 2l < l + r < 2r => l < (l + r) / 2 < r
            if (a[m] >= x) {
                // a[l] >= x > a[r] & r - l > 1 & l < m = (l + r) / 2 < r & a[m] > x
                l = m;
                // a[l] >= x > a[r] & r - l >= 1 & a[m] > x & l = m
                // Пояснение: a[m] >= x & l = m => a[l] >= x > a[r]
                // Пояснение: l < m < r & l = m => r - l >= 1 (и промежуток уменьшился)
            } else {
                // a[l] >= x > a[r] & r - l > 1 & l < m = (l + r) / 2 < r & a[m] <= x
                r = m;
                // a[l] >= x > a[r] & r - l >= 1 & a[m] <= x & r = m
                // Пояснение: a[m] < x & r = m => a[l] >= x > a[r]
                // Пояснение: l < m < r & r = m => r - l >= 1 (и промежуток уменьшился)
            }
            // a[l] >= x > a[r] & r - l >= 1 (и промежуток уменьшился)
        }
        // a[l] >= x > a[r] & r - l = 1
        // Пояснение: r - l = 1 => l = r - 1
        // a[r - 1] >= x > a[r] & a[i] >= a[i + 1] ∀ i = 0..a.length - 2 & a != null
        // Пояснение: последние 2 выполнены т.к мы не изменяли массив, я просто не тащил это условие через весь код
        return r;
    }

    // INV: a[l] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 0..a.length - 2 & r - l >= 1
    private static int recursiveBinarySearch(int x, int[] a, int l, int r) {
        // PRE: a[i] >= a[i + 1] ∀ i = 0..a.length - 2 & a != null & -1 <= l < r <= a.length
        if (r - 1 == l) {
            // a[l] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 1..n & r - 1 = l
            return r;
            // a[r - 1] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 1..n & r - l = 1
        }
        int m = (l - r) / 2 + r;
        // a[l] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 1..n & r - l > 1 & l < m = (l + r) / 2 < r
        if (a[m] > x) {
            // a[l] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 1..n & r - l > 1 & l < m = (l + r) / 2 < r & a[m] > x
            return recursiveBinarySearch(x, a, m, r);
            // a[l] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 1..n & r - l >= 1
            // Пояснение: a[m] > x & l = m => a[l] > x >= a[r]
            // Пояснение: l < m < r & l = m => r - l >= 1 (и промежуток уменьшился)
        } else {
            // a[l] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 1..n & r - l > 1 & l < m = (l + r) / 2 < r & a[m] <= x
            return recursiveBinarySearch(x, a, l, m);
            // a[l] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 1..n & r - l >= 1
            // Пояснение: a[m] <= x & r = m => a[l] > x >= a[r]
            // Пояснение: l < m < r & r = m => r - l >= 1 (и промежуток уменьшился)
        }
        // a[l] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 1..n & r - l >= 1
    }

    // POST: a[r - 1] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 0..a.length - 2 & a != null
    public static int recursiveBinarySearch(int x, int[] a) {
        // PRE: a[l] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 0..a.length - 2 & r - l >= 1
        return recursiveBinarySearch(x, a, -1, a.length);
        // a[l] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 0..a.length - 2 & r - l = 1 =>
        // a[r - 1] > x >= a[r] & a[i] >= a[i + 1] ∀ i = 0..a.length - 2 & a != null
    }
}
