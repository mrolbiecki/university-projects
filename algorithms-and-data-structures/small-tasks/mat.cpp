// author: Marcin Rolbiecki
// date: 2024-10-06
// problem: 

#include <bits/stdc++.h>
using namespace std;

string s;
int n, ans;

int main () {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	
	cin >> s;
	int n = s.size();
	
	ans = n;
	char prev = 0;
	int d = 1;
	for (int i = 0; i < n; i++) {
		if (s[i] != '*' && prev == 0) {
			prev = s[i];
		}
		if (s[i] == '*') {
			d++;
		} else if (s[i] == prev) {
			d = 1;
		} else {
			ans = min(ans, d);
			d = 1;
			prev = s[i];
		}
	}
	
	cout << n - ans + 1;

	return 0;
}
