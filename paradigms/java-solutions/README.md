# Java
  
## [Binary Search with contracts](https://www.kgeorgiy.info/courses/paradigms/homeworks.html#homework-java-binary-search) 
**Модификация**:  
* *Span* (36-37)
    * Требуется вывести два числа: начало и длину диапазона элементов,
      равных `x`. Если таких элементов нет, то следует вывести
      пустой диапазон, у которого левая граница совпадает с местом
      вставки элемента `x`.
    * Не допускается использование типов `long` и `BigInteger`.
    * Класс должен иметь имя `BinarySearchSpan`
  
## [Array Queue](https://www.kgeorgiy.info/courses/paradigms/homeworks.html#homework-java-array-queue)
**Модификация**:  
* *Deque* (36-37)
    * Реализовать методы
        * `push` – добавить элемент в начало очереди
        * `peek` – вернуть последний элемент в очереди
        * `remove` – вернуть и удалить последний элемент из очереди
  
## [Queues](https://www.kgeorgiy.info/courses/paradigms/homeworks.html#homework-java-queues)
**Модификация**:  
* *Contains* (36-37)
    * Добавить в интерфейс очереди и реализовать методы
        * `contains(element)` – проверяет, содержится ли элемент в очереди
        * `removeFirstOccurrence(element)` – удаляет первое вхождение элемента в очередь 
            и возвращает было ли такое
    * Дублирования кода быть не должно
  
## [Generic Tubulator](https://www.kgeorgiy.info/courses/paradigms/homeworks.html#homework-java-tabulator)
**Модификация**:  
* *AsmUls* (36-7)
    * Реализовать режимы из модификации *Uls*.
    * Дополнительно реализовать унарные операции:
        * `abs` – модуль числа, `abs -5` равно 5;
        * `square` – возведение в квадрат, `square 5` равно 25.
    * Дополнительно реализовать бинарную операцию (максимальный приоритет):
        * `mod` – взятие по модулю, приоритет как у умножения (`1 + 5 mod 3` равно `1 + (5 mod 3)` равно `3`).