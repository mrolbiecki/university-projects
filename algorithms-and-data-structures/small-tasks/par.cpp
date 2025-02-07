#include <bits/stdc++.h>
using namespace std;

const int maxN = 5e5+2, maxL = 20;

int c[maxN][2];
int f[maxN];
int jump[maxL][maxN];
int depth[maxN];

int n, m;

pair<int, int> deep_below[maxN];
pair<int, int> deep_above[maxN];

int go_up(int a, int d) {
	for (int i = maxL - 1; i >= 0; i--) {
		if ((1 << i) <= d && jump[i][a] != -1) {
			d -= (1 << i);
			a = jump[i][a];
		}
	}
	return a;
}


void dfs_deep_below(int v, int d) {
	depth[v] = d;
	
	for (int j = 0; j < 2; j++) {
		if (c[v][j] != -1) {
			dfs_deep_below(c[v][j], d + 1);
		}
	}
	
	deep_below[v] = {v, 0};
	
	for (int j = 0; j < 2; j++) {
		if (c[v][j] != -1 && deep_below[ c[v][j] ].second + 1 > deep_below[v].second) {
			deep_below[v] = {deep_below[ c[v][j] ].first, 
								deep_below[ c[v][j] ].second + 1}; 
		}
	}
}

void dfs_deep_above(int v) {
	if (f[v] != -1) {
		deep_above[v] = {deep_above[ f[v] ].first, 
							deep_above[ f[v] ].second + 1};
		int other = c[ f[v] ][(c[ f[v] ][0] == v ? 1 : 0)];
		if (other != -1) {
			if (deep_above[v].second < deep_below[other].second + 2) {
				deep_above[v] = {deep_below[other].first, 
									deep_below[other].second + 2};
			}
		}
	}
	
	for (int j = 0; j < 2; j++) {
		if (c[v][j] != -1) {
			dfs_deep_above(c[v][j]);
		}
	}
}

void precompute() {
	for (int v = 1; v <= n; v++) {
		jump[0][v] = f[v];
	}
	for (int i = 1; i < maxL; i++) {
		for (int v = 1; v <= n; v++) {
			if (jump[i - 1][v] != -1) {
				jump[i][v] = jump[i - 1][ jump[i - 1][v] ];
			} else {
				jump[i][v] = -1;
			}
		}
	}
	
	dfs_deep_below(1, 0);
	deep_above[1] = {1, 0};
	dfs_deep_above(1);
}

int main () {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	
	cin >> n;
	f[1] = -1;
	for (int i = 1; i <= n; i++) {
		for (int j = 0; j < 2; j++) {
			cin >> c[i][j];
			if (c[i][j] != -1) {
				f[ c[i][j] ] = i;
			}
		}
	}
	
	precompute();
/*
	cout << "deep_below: " << endl;
	for (int v = 1; v <= n; v++) {
		cout << v << ": " << deep_below[v].first << ' ' << deep_below[v].second << endl;
	}
	
	cout << "deep_above: " << endl;
	for (int v = 1; v <= n; v++) {
		cout << v << ": " << deep_above[v].first << ' ' << deep_above[v].second << endl;
	}
	
	for (int v = 1; v <= n; v++) {
		cout << "jump " << v << ": ";
		for (int i = 0; i < 5; i++) {
			cout << jump[i][v] << ' ';
		}
		cout << endl;
	}
*/

	cin >> m;
	for (int i = 1; i <= m; i++) {
		int a, d; cin >> a >> d;
		
		if (d <= depth[a]) {
			a = go_up(a, d);
		} else if (d <= deep_below[a].second) {
			a = go_up(deep_below[a].first, deep_below[a].second - d);
		} else if (d <= deep_above[a].second) {
			a = go_up(deep_above[a].first, deep_above[a].second - d);
		} else {
			a = -1;
		}
		
		cout << a << '\n';
	}
}
