# Clojure 
  
## [Linear](https://www.kgeorgiy.info/courses/paradigms/homeworks.html#homework-clojure-linear) 
**Модификация**:  
* *Simplex* (36-37)
    * Назовем _симплексом_ многомерную таблицу чисел, 
      такую что для некоторого `n` в ней существуют все значения
      с суммой индексов не превышающей `n` и только эти значения.
    * Добавьте операции поэлементного сложения (`x+`),
        вычитания (`x-`) и умножения (`x*`) симплексов.
        Например, `(x+ [[1 2] [3]] [[5 6] [7]])` должно быть равно `[[6 8] [10]]`.
  
## [Functional](https://www.kgeorgiy.info/courses/paradigms/homeworks.html#homework-clojure-functional-expressions) 
**Модификация**:  
* *SumAvg* (36-37). Дополнительно реализовать поддержку:
    * операций произвольного числа аргументов:
        * `sum` – сумма, `(sum 1 2 3)` равно 6;
        * `avg` – среднее, `(avg 1 2 3)` равно 2;
  
## [Object](https://www.kgeorgiy.info/courses/paradigms/homeworks.html#homework-clojure-object-expressions)
**Модификация**:  
* *SumAvg* (36-37). Дополнительно реализовать поддержку:
    * операций произвольного числа аргументов:
        * `Sum` (`sum`) – сумма, `(sum 1 2 3)` равно 6;
        * `Avg` (`avg`) – арифметическое среднее, `(avg 1 2 3)` равно 2;
  
## [Сombinatorial Parser](https://www.kgeorgiy.info/courses/paradigms/homeworks.html#homework-clojure-expression-parsing) 
**Модификация**:  
* *PowLog* (36-37). Сделать модификацию *Variables* и дополнительно реализовать поддержку:
    * Бинарных правоассоциативных операций максимального приоритета:
        * `IPow` (`**`) – возведения в степень:
            `4 ** 3 ** 2` равно `4 ** (3 ** 2)` равно 262144
        * `ILog` (`//`) – взятия логарифма:
            `8 // 9 // 3` равно `8 // (9 // 3)` равно 3