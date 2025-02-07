// author: Marcin Rolbiecki
// date: 2024-10-02

#include <bits/stdc++.h>
using namespace std;

const int maxN = 1e6+2;

int n, m;
int c[maxN];
long long ans[maxN];

int next_p (int i) {
	do {
		++i;
	} while (i <= n && c[i] % 2 == 1);
	return i;
}

int next_np (int i) {
	do {
		++i;
	} while (i <= n && c[i] % 2 == 0);
	return i;
}

int main () {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	
	
	
	cin >> n;
	for (int i = 1; i <= n; i++) {
		cin >> c[i];
	}
	
	reverse(c+1, c+1+n);
	fill(ans+1, ans+1+n, -1);
	
	long long sum = 0;
	int min_p_in = 0, min_np_in = 0;
	int max_p_out = next_p(0), max_np_out = next_np(0);
	
	for (int i = 1; i <= n; i++) {
		if (c[i] % 2 == 0) {
			min_p_in = i;
		} else {
			min_np_in = i;
		}
		if (max_p_out == i) {
			max_p_out = next_p(i);
		}
		if (max_np_out == i) {
			max_np_out = next_np(i);
		}
		
		sum += c[i];
		
		if (sum % 2 == 1) {
			ans[i] = max(ans[i], sum);
		} else {
			if (min_p_in != 0 && max_np_out <= n) {
				ans[i] = max(ans[i], sum - c[min_p_in] + c[max_np_out]);
			}
			if (min_np_in != 0 && max_p_out <= n) {
				ans[i] = max(ans[i], sum - c[min_np_in] + c[max_p_out]);
			}
		}
	}
	
	cin >> m;
	for (int i = 1; i <= m; i++) {
		int k; cin >> k;
		cout << ans[k] << '\n';
	}

    return 0;
}
