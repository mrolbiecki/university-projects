#include <bits/stdc++.h>
using namespace std;

const int maxN = 1e3+2;

int n;


long long calc (int l, int r) {
	
	
}


int main () {
	ios.base::sync_with_stdio(0);
	cin.tie(0);
	
	cin >> n;
	for (int i = 1; i <= n; i++) {
		cin >> c[i];
	}
	
	for (int i = 1; i <= n; i++) {
		dp[i][i] = 1;
	}
	
	l[1][n] = (c[1] == n ? 0 : 1);
	r[1][n] = (c[1] == 1 ? 0 : 1);
	
	for (int i = 1; i <= n; i++) {
		for (int j = n; j >= i; j--) {
			if (c[i] < c[i-1]) {
				
			}
			l[i-1][j] += l[i][j]
		} 
	}
}
