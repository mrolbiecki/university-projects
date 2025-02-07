#include <bits/stdc++.h>
using namespace std;

const int maxN = 1e5+2;

int n, m, k;
bool vis[maxN];
int p[maxN];

vector<int> order;
vector<int> G [maxN];
vector<int> Gr [maxN];

void toposort(int v) {
	vis[v] = 1;
	for (int u : G[v]) {
		if (!vis[u]) {
			toposort(u);
		}
	}
	order.push_back(v);
}

int main() {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	
	cin >> n >> m >> k;
	for (int i = 1; i <= n; i++) {
		cin >> p[i];
	}
	for (int i = 1; i <= m; i++) {
		int a, b; cin >> a >> b;
		G[b].push_back(a);
		Gr[a].push_back(b);
	}
	
	for (int v = 1; v <= n; v++) {
		if (!vis[v]) {
			toposort(v);
		}
	}
	reverse(order.begin(), order.end());
	
	for (int v : order) {
		for (int u : Gr[v]) {
			p[v] = max(p[v], p[u]);
		}
	}
	sort(p+1, p+n+1);
	
	cout << p[k];
	
	return 0;
}
