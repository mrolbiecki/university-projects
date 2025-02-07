#include <bits/stdc++.h>
using namespace std;

const int maxN = 2e5+2;
const int inf = 2e9;

pair<int, int> sortedX[2][maxN];
vector<pair<int, int>> edges[maxN];
int x[2][maxN];
int sortedXpos[2][maxN];
int dist[maxN];
int n, m;

void dijkstra(int r) {
	fill(dist + 2, dist + 1 + n, inf);
	priority_queue<pair<int, int>> Q;
	Q.push({0, r});
	
	while (!Q.empty()) {
		auto p = Q.top();
		Q.pop();
		int d = -p.first, v = p.second;
		
		if (dist[v] < d) {
			continue;
		}
		
		for (auto edge : edges[v]) {
			if (dist[edge.first] > d + edge.second) {
				dist[edge.first] = d + edge.second;
				Q.push({-dist[edge.first], edge.first});
			}
		}
	}
}

int main () {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	
	cin >> n;
	for (int i = 1; i <= n; i++) {
		for (int dim = 0; dim < 2; dim++) {
			cin >> x[dim][i];
			sortedX[dim][i] = {x[dim][i], i};
		}
	}
	
	for (int dim = 0; dim < 2; dim++) {
		sort(sortedX[dim] + 1, sortedX[dim] + n + 1);
	}
	
	for (int dim = 0; dim < 2; dim++) {
		for (int i = 1; i <= n; i++) {
			sortedXpos[dim][sortedX[dim][i].second] = i;
		}
	}
	
	for (int v = 1; v <= n; v++) {
		for (int dim = 0; dim < 2; dim++) {
			int pos = sortedXpos[dim][v];
			if (pos != 1) {
				int u = sortedX[dim][pos - 1].second;
				edges[v].push_back({u, min(
					abs(x[dim][u] - x[dim][v]),
					abs(x[1 - dim][u] - x[1 - dim][v])
				)});
			}
			if (pos != n) {
				int u = sortedX[dim][pos + 1].second;
				edges[v].push_back({u, min(
					abs(x[dim][u] - x[dim][v]),
					abs(x[1 - dim][u] - x[1 - dim][v])
				)});
			}	
		}
	}
	
	dijkstra(1);
	cout << dist[n];
}
