prime(N) :- primes_table(N).
composite(N) :- not primes_table(N).

primes_table(2).
init(MAX_N) :- eratosthenes_sieve(3, MAX_N).

mark_as_composite(L, R, S) :- L =< R,
								assert(composite_table(L)),
								L1 is L + 2 * S,
								mark_as_composite(L1, R, S).

eratosthenes_sieve(L, R) :- L =< R,
															not composite_table(L),
															assert(primes_table(L)),
															L1 is L * L,
															L1 =< R, 
															mark_as_composite(L1, R, L).
eratosthenes_sieve(L, R) :- L =< R,
															L1 is L + 2,
															eratosthenes_sieve(L1, R).

min_divisor(N, R) :- min_divisor(N, 2, R).
min_divisor(N, L, L) :- 0 is mod(N, L), !.
min_divisor(N, L, R) :-  M is mod(N, L), 
														 M \= 0,
                           	 L1 is L + 1, 
                           	 min_divisor(N, L1, R).

prime_divisors(1, []) :- !.
prime_divisors(N, [N]) :- prime(N), !.
prime_divisors(N, [H | T]) :- number(N),
															 min_divisor(N, H),
                              N1 is div(N, H), 
                              prime_divisors(N1, T).
prime_divisors(N, [H | T]) :- var(N),
																prime_divisors(N, T, H).
prime_divisors(N, [], N) :- prime(N), !.
prime_divisors(N, [H | T], L) :- L =< H,
																	prime(L), 
																	prime_divisors(N1, T, H), 
																	N is N1 * L.

gcd(A, B, GCD) :- prime_divisors(A, AD),
										prime_divisors(B, BD),
										intersect(AD, BD, R),
										prime_divisors(GCD, R).

intersect([], B, []) :- !.
intersect(A, [], []) :- !.
intersect([H | AT], [H | BT], [H | RT]) :- intersect(AT, BT, RT), !.
intersect([AH | AT], [BH | BT], R) :- AH < BH,
																			intersect(AT, [BH | BT], R).
intersect([AH | AT], [BH | BT], R) :- AH > BH,
																			intersect([AH | AT], BT, R).


										