//2024-01-17
//author: Marcin Rolbiecki
#include <bits/stdc++.h>
#include "wys.h"
using namespace std;

const int inf = 1e6;
typedef vector <int> state;

int n, k, g;
map<state, int> eval;
map<state, int> best_move;

void update_state (state &S, int move, bool answer)
{
	if (answer)
	{
		for (int i = move; i < n; i++)
		{
			S[i] = min(S[i] + 1, k + 1);
		}
	}
	else
	{	
		for (int i = 0; i < move; i++)
		{
			S[i] = min(S[i] + 1, k + 1);
		}
	}
}

int get_evaluation (state &S)
{
	if (eval.count(S)) return eval[S];
	
	eval[S] = inf;
	int _eval = inf, _best_move = -1;
	
	for (int move = 1; move < n; move++)
	{
		state yes = S, no = S;
		update_state(yes, move, true);
		update_state(no, move, false);
				
		int maxi = max(get_evaluation(yes), get_evaluation(no));
		
		if (maxi < _eval)
		{
			_eval = maxi;
			_best_move = move;
		}
	}
	
	best_move[S] = _best_move;
	return eval[S] = _eval + 1;
}

int get_answer (state &S)
{
	for (int i = 0; i < n; i++)
	{
		if (S[i] <= k) return i + 1;
	}
	assert(false);
	return -1;
}

void solve ()
{
	for (int i = 0; i < n; i++)
	{
		for (int j = 0; j <= k; j++)
		{
			state v (n, k + 1);
			v[i] = j;
			eval[v] = 0;
		}
	}
	
	state S (n, 0);
	get_evaluation(S);
}

void play ()
{
	state S (n, 0);
	
	while (eval[S])
	{
		int move = best_move[S];
		int answer = mniejszaNiz(move + 1);
		update_state(S, move, answer);
	}
	
	odpowiedz(get_answer(S));
}

int main ()
{
	dajParametry(n, k, g);
	solve();
	while (g--) play();
	
	return 0;
}
