%node(L, K, V, H, R)
height(null, -1) :- !.
height(node(_, _, _, H, _), H).

diff(node(L, _, _, _, R), Diff) :-
    height(L, HL),
    height(R, HR),
    Diff is HR - HL.

update(node(L, K, V, _, R), node(L, K, V, NewH, R)) :-
    height(L, HL),
    height(R, HR),
    HL > HR,
    NewH is HL + 1, !.
update(node(L, K, V, _, R), node(L, K, V, NewH, R)) :-
		height(R, HR),
    NewH is HR + 1.

rotate_left(node(L, K, V, H, node(RL, RK, RV, _, RR)), NewNode) :- 
    update(node(L, K, V, H, RL), NewL),
    update(node(NewL, RK, RV, H, RR), NewNode).
                                                                                        
rotate_right(node(node(LL, LK, LV, _, LR), K, V, H, R), NewNode) :- 
    update(node(LR, K, V, H, R), NewR),  
    update(node(LL, LK, LV, H, NewR), NewNode).

balance_node(Node, NewNode) :-
    update(Node, UpNode), 
	  diff(UpNode, Diff),
	  balance(Diff, UpNode, NewNode).

balance(Diff, Node, NewNode) :- 
    abs(Diff) < 2, 
    update(Node, NewNode), !.
  
balance(Diff, Node, NewNode) :-
	  Diff is 2,
	  update(Node, node(L, K, V, H, R)),
	  diff(R, DiffR),
	  DiffR < 0,
	  rotate_right(R, NewR),
	  update(node(L, K, V, H, NewR), UpNode),
	  rotate_left(UpNode, NewNode), !.

balance(Diff, Node, NewNode) :-
	  Diff is 2,
	  rotate_left(Node, NewNode), !.

balance(_, Node, NewNode) :-
	  update(Node, node(L, K, V, H, R)),
	  diff(L, DiffL),
	  DiffL > 0,
	  rotate_left(L, NewL),
	  update(node(NewL, K, V, H, R), UpNode),
	  rotate_right(UpNode, NewNode), !.

balance(_, Node, NewNode) :-
    rotate_right(Node, NewNode).

map_put(null, Key, Value, node(null, Key, Value, 0, null)) :- !.
map_put(node(L, Key, _, Height, R), Key, Value, node(L, Key, Value, Height, R)) :- !.
map_put(node(L, K, V, H, R), Key, Value, NewNode) :-
	  Key < K,
	  map_put(L, Key, Value, NewL),
	  balance_node(node(NewL, K, V, H, R), NewNode).
map_put(node(L, K, V, H, R), Key, Value, NewNode) :-
	  Key > K,
	  map_put(R, Key, Value, NewR),
	  balance_node(node(L, K, V, H, NewR), NewNode).

find_min(node(null, K, V, _, _), K, V).
find_min(node(L, _, _, _, _), MinK, V) :- 
    find_min(L, MinK, V).

map_remove(null, _, null) :- !.
map_remove(node(null, Key, _, _, null), Key, null) :- !.
map_remove(node(L, Key, _, _, null), Key, L) :- !.
map_remove(node(L, Key, _, _, R), Key, NewNode) :-
	  find_min(R, MinK, V),
	  map_remove(R, MinK, NewR),
	  balance_node(node(L, MinK, V, _, NewR), NewNode), !.

map_remove(node(L, K, V, _, R), Key, NewNode) :-
	  Key < K,
	  map_remove(L, Key, NewL),
	  balance_node(node(NewL, K, V, _, R), NewNode).
map_remove(node(L, K, V, _, R), Key, NewNode) :-
	  Key > K,
	  map_remove(R, Key, NewR),
	  balance_node(node(L, K, V, _, NewR), NewNode).

map_get(null, _, _) :- fail, !.
map_get(node(_, Key, Value, _, _), Key, Value) :- !.
map_get(node(L, K, _, _, _), Key, Value) :-
    Key < K,
    map_get(L, Key, Value).
map_get(node(_, K, _, _, R), Key, Value) :- 
    Key > K,
    map_get(R, Key, Value).

map_build([], null).
map_build([(K, V) | Tail], TreeMap) :-
    map_build(Tail, Tree),
    map_put(Tree, K, V, TreeMap).

map_getLast(node(_, K, V, _, null), (K, V)) :- !.
map_getLast(node(_, _, _, _, R), (MaxK, V)) :- 
    map_getLast(R, (MaxK, V)).
    
map_removeLast(null, null) :- !.
map_removeLast(node(L, K, _, _, null), Result) :- 
    map_remove(node(L, K, _, _, null), K, Result), !.
map_removeLast(node(L, K, V, H, R), Result) :- 
    map_removeLast(R, NewR),
    balance_node(node(L, K, V, H, NewR), Result).
 