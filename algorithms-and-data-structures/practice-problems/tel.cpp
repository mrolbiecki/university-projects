#include <bits/stdc++.h>
using namespace std;

const int maxN = 602;
const int inf = 1e9;

int n, m;
pair<int, int> s, f;
char grid[maxN][maxN];
int dist[maxN][maxN];

vector <pair<int, int>> moves = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
bool teleports;

bool check(pair<int, int> pos) {
	return pos.first <= n && pos.second <= m && 
			grid[pos.first][pos.second] != '#';
}

void bfs() {
	queue<pair<int, int>> Q;
	dist[s.first][s.second] = 0;
	Q.push(s);
	while (!Q.empty()) {
		auto t = Q.front(); Q.pop();
		int d = dist[t.first][t.second];
		
		if (grid[t.first][t.second] == '*' && !teleports) {
			teleports = true;
			for (int i = 1; i <= n; i++)
				for (int j = 1; j <= n; j++)
					if (grid[i][j] == '*') {
						dist[i][j] = d + 1;
						Q.push({i, j});
					}
		}
		
		for (auto move : moves) {
			pair<int, int> pos = {t.first + move.first, 
									t.second + move.second};
			if (!check(pos))
				continue;
			
			if (dist[pos.first][pos.second] == inf) {
				dist[pos.first][pos.second] = d + 1;
				Q.push(pos);
			}
		}
	}
}

int main () {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	
	cin >> n >> m;
	for (int i = 1; i <= n; i++)
		for (int j = 1; j <= m; j++) {
			cin >> grid[i][j];
			
			if (grid[i][j] == 'S')
				s = {i, j};
				
			if (grid[i][j] == 'M')
				f = {i, j};
				
			dist[i][j] = inf;
		}
		
	bfs();
	cout << (dist[f.first][f.second] == inf ? -1 : dist[f.first][f.second]);
}
