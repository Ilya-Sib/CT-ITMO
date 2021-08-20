# Prolog
  
## [Primes](https://www.kgeorgiy.info/courses/paradigms/homeworks.html#homework-prolog-primes) 
**Модификация**:  
* *Gcd* (36-37)
    * Добавьте правило `gcd(A, B, GCD)`,
      подсчитывающее НОД(`A`, `B`) через разложение на простые множители:
      `gcd(4, 6, 2)`.
  
## [Search Tree](https://www.kgeorgiy.info/courses/paradigms/homeworks.html#homework-prolog-map)
**Модификация**:  
* *Last* (36-37)
    * Добавьте правила:
        * `map_getLast(Map, (Key, Value))`, возвращающее максимальную пару;
        * `map_removeLast(Map, Result)`, удаляющее максимальную пару.
  
## [Parser](https://www.kgeorgiy.info/courses/paradigms/homeworks.html#homework-prolog-expression-parsing)
**Модификация**:  
* *VarSinhCosh* (36-37). Сделать модификацию *Variables* и дополнительно реализовать поддержку:
    * унарных операций:
        * `op_sinh` (`sinh`) – гиперболический синус, `sinh(3)` немного больше 10;
        * `op_cosh` (`cosh`) – гиперболический косинус, `cosh(3)` немного меньше 10.