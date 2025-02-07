#include <bits/stdc++.h>
using namespace std;

vector<pair<int, int>> p;
int n, ans;

int main () {
	std::ios_base::sync_with_stdio(false);
	std::cin.tie(NULL);
	
	cin >> n;
	for (int i = 1; i <= n; i++) {
		int a, b; cin >> a >> b;
		p.push_back({a, 1});
		p.push_back({b, -1});
	}
	sort(p.begin(), p.end());
	
	int bal = 0, prev = -1;
	for (auto pp : p) {
		if (bal >= 2 && pp.first != prev)
			ans += pp.first - prev;
		bal += pp.second;
		prev = pp.first;
	}
	
	cout << ans;
}
