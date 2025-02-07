// author: Marcin Rolbiecki
// date: 2024-10-14

#include <bits/stdc++.h>
using namespace std;

const int maxN = 4e4+2;
const int maxM = 52;

long long H[maxN];
unordered_map <long long, int> M;
int n, m, ans;

int sc() {
	int c = getchar();
	int x = 0;
	int neg = 0;
	for (; ((c < 48 || c > 57) && c != '-'); c = getchar());
	if (c == '-') {
		neg = 1;
		c = getchar();
	}
	for (; c > 47 && c < 58; c = getchar()) {
		x = (x << 1) + (x << 3) + c - 48;
	}
	if (neg) x = -x;
	return x;
}

int main () {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	
	n = sc();
	m = sc();
	for (int i = 0; i < m; i++) {
		for (int j = 1; j <= n; j++) {
			int a;
			a = sc();
			if (j > n / 2) {
				H[a] |= (1ll << i);
			}
		}
	}
	
	for (int i = 1; i <= n; i++) {
		M[ H[i] ]++;
	}
	
	for (auto p : M) {
		ans = max(ans, p.second);
	}
	
	cout << ans;

	return 0;
}
