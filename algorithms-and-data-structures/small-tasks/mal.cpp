#include <bits/stdc++.h>
using namespace std;

const int T = 1<<20;

int tree[T << 1];
int lazy[T << 1];
int n, m;

void push (int id, int lo, int hi) {
	if (lazy[id] == -1) {
		return;
	}
	int mid = (lo + hi) / 2;
	lazy[id * 2] = lazy[id];
	tree[id * 2] = (mid - lo + 1) * lazy[id];
	lazy[id * 2 + 1] = lazy[id];
	tree[id * 2 + 1] = (hi - mid) * lazy[id];
	lazy[id] = -1;
}

void fix (int id) {
	tree[id] = tree[id * 2] + tree[id * 2 + 1];
}

void _set (int id, int l, int r, int lo, int hi, int val) {
	if (r < lo || hi < l) {
		return;
	}
	if (l <= lo && hi <= r) {
		lazy[id] = val;
		tree[id] = (hi - lo + 1) * val;
		return;
	}
	push(id, lo, hi);
	int mid = (lo + hi) / 2;
	_set(id * 2, l, r, lo, mid, val);
	_set(id * 2 + 1, l, r, mid + 1, hi, val);
	fix(id);
}

void tree_init () {
	fill(lazy, lazy + (T << 1), -1);
}

void tree_set (int l, int r, int val) {
	return _set(1, l, r, 0, T - 1, val);
}

int main () {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	tree_init();
	cin >> n >> m;
	for (int i = 1; i <= m; i++) {
		int a, b; char c;
		cin >> a >> b >> c;
		tree_set(a, b, (c == 'B' ? 1 : 0));
		cout << tree[1] << '\n';
	}
}
