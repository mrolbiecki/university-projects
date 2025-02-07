// author: Marcin Rolbiecki
// date: 2024-10-29

#include <bits/stdc++.h>
using namespace std;

const int maxN = 20001;
const int T = 1 << 15;
const int maxK = 11;
const int mod = 1e9;

int n, k;
int a[maxN];

int dp[maxK][2 * T];

void update(int len, int pos, int val) {
	pos += T;
	dp[len][pos] = val;
	int id = pos / 2;
	while (id) {
		dp[len][id] = (dp[len][id * 2] + dp[len][id * 2 + 1]) % mod;
		id /= 2;
	}
}

int query (int len, int id, int l, int r, int lo, int hi) {
	if (r < lo || hi < l) {
		return 0;
	}
	if (l <= lo && hi <= r) {
		return dp[len][id];
	}
	int mid = (lo + hi) / 2;
	return (
			query(len, id * 2, l, r, lo, mid) +
			query(len, id * 2 + 1, l, r, mid + 1, hi)
		) % mod;
}

int sum_in_range(int len, int l, int r) {
	return query(len, 1, l, r, 0, T - 1);
}

int main () {
	ios_base::sync_with_stdio(0);
	cin.tie(0);

	cin >> n >> k;
	for (int i = 0; i < n; i++) {
		cin >> a[i];
	}

	for (int i = 0; i < n; i++) {
		update(1, a[i], 1);
		for (int len = 2; len <= k; len++) {				
			update(len, a[i], sum_in_range(len - 1, a[i] + 1, n));
		}
	}
		
	cout << sum_in_range(k, 0, n);
	
    return 0;
}
