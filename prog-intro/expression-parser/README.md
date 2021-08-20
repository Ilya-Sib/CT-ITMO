# [Expression-parser](https://www.kgeorgiy.info/courses/prog-intro/homeworks.html#homework-10)  
  
Задания 10-12  
**Модификации**:  
* *Double* (36, 37)
    * Дополнительно реализуйте интерфейс [DoubleExpression](java/expression/DoubleExpression.java)
    * [Исходный код тестов](java/expression/DoubleExpressionTest.java)
  
* *NotCount* (36, 37)
    * Реализуйте операции из модификации *Bitwise*.
    * Дополнительно реализуйте унарные операции (приоритет как у унарного минуса):
        * `~` – побитное отрицание, `~-5` равно 4;
        * `count` – число установленных битов, `count -5` равно 31.
    * [Исходный код тестов](java/expression/parser/ParserNotCountTest.java)
  
* *MinMax* (36, 37)
    * Реализуйте операции модификации *AbsSqrt*.
    * Дополнительно реализуйте бинарные операции (минимальный приоритет):
        * `min` – минимум, `2 min 3` равно 2;
        * `max` – максимум, `2 max 3` равно 3.
    * [Исходный код тестов](java/expression/exceptions/ExceptionsMinMaxTest.java)