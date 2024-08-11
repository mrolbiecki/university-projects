#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int min (int a, int b)
{
	return (a < b ? a : b);
}
int max (int a, int b)
{
	return (a > b ? a : b);
}
bool diff_type (int type[], int a, int b, int c)
{
	return type[a] != type[b] && type[a] != type[c] && type[b] != type[c];
}
int calc_score_max (int dist[], int l, int m, int r)
{
	return max(dist[m] - dist[l], dist[r] - dist[m]);
}
int calc_score_min (int dist[], int l, int m, int r)
{
	return min(dist[m] - dist[l], dist[r] - dist[m]);
}

int main ()
{
	
	int n;
	if(scanf("%d", &n)){};
	
	int * type = (int*)malloc((size_t)n * sizeof(int));
	int * dist = (int*)malloc((size_t)n * sizeof(int));
	
	for (int i = 0; i < n; i++) {
		if(scanf("%d %d", &type[i], &dist[i])){};
	}

//CLOSEST THREE ********************************************************

	int * left = (int*)malloc((size_t)n * sizeof(int));
	//nxt_left[i] = closest element of different type than type[i] on the left of i
	int * right = (int*)malloc((size_t)n * sizeof(int));
	//nxt_left[i] = closest element of different type than type[i] on the right of i
	int * diff_left = (int*)malloc((size_t)n * sizeof(int));
	//nxt_left[i] = closest element of different type than type[i] and nxt_right[i] on the left of i
	int * diff_right = (int*)malloc((size_t)n * sizeof(int));
	//nxt_left[i] = closest element of different type than type[i] and nxt_left[i] on the right of i
	
	//if value == -1, element doesn't exist
	left[0] = -1;
	for (int i = 1; i < n; i++) {
		
		if (type[i-1] != type[i]) {
			left[i] = i-1;
		}
		else {
			left[i] = left[i-1];
			continue;
		}
	}
	right[n-1] = -1;
	for (int i = n-2; i >= 0; i--) {
		
		if (type[i+1] != type[i]) {
			right[i] = i+1;
		}
		else {
			right[i] = right[i+1];
			continue;
		}
	}
	
	diff_left[0] = -1;
	for (int i = 1; i < n; i++) {
		
		if (left[i] == -1) {
			diff_left[i] = -1;
		}
		else if (right[i] == -1 || type[ left[i] ] != type[ right[i] ]) {
			diff_left[i] = left[i];
		}	
		else if (left[ left[i] ] == -1) {
			diff_left[i] = -1;
		}
		else if (type[ left[ left[i] ] ] != type[i]) {
			diff_left[i] = left[ left[i] ];
		}
		else {
			diff_left[i] = diff_left[ left[ left[i] ] ];
		}
	}
	diff_right[n-1] = -1;
	for (int i = n-2; i >= 0; i--) {
		
		if (right[i] == -1) {
			diff_right[i] = -1;
		}
		else if (right[i] == -1 || type[ right[i] ] != type[ left[i] ]) {
			diff_right[i] = right[i];
		}
		else if (right[ right[i] ] == -1) {
			diff_right[i] = -1;
		}
		else if (type[ right[ right[i] ] ] != type[i]) {
			diff_right[i] = right[ right[i] ];
		}
		else {
			diff_right[i] = diff_right[ right[ right[i] ] ];
		}
	}
	
	int inf = dist[n-1] + 1;
	int min_dist = inf;
		
	for (int i = 0; i < n; i++) {
		
		if (diff_left[i] != -1 && right[i] != -1) {
			int d = calc_score_max(dist, diff_left[i], i, right[i]);
			min_dist = min(min_dist, d);
		}
		if (left[i] != -1 && diff_right[i] != -1) {
			int d = calc_score_max(dist, left[i], i, diff_right[i]);
			min_dist = min(min_dist, d);
		}
	}

	printf("%d ", (min_dist == inf ? 0 : min_dist));
	
	free(left);
	free(right);
	free(diff_left);
	free(diff_right);
	
//FURTHEST THREE *******************************************************

	//three different leftmost and rightmost elements
	int fleft[3] = {0}, fright[3] = {0};
	
	for (int l = 0; l < 3; l++) {
		for (int i = 0; i < n; i++) {
			bool ok = 1;
			for (int j = 0; j < l; j++) {
				if (type[i] == type[ fleft[j] ]) {
					ok = 0;
				}
			}
			if (ok) {
				fleft[l] = i;
				break;
			}
		}
	}
	for (int r = 0; r < 3; r++) {
		for (int i = n-1; i >= 0; i--) {
			bool ok = 1;
			for (int j = 0; j < r; j++) {
				if (type[i] == type[ fright[j] ]) {
					ok = 0;
				}
			}
			if (ok) {
				fright[r] = i;
				break;
			}
		}
	}
			
	int max_dist = 0;
			
	for (int i = 0; i < n; i++) {
		for (int l = 0; l < 3; l++) {
			for (int r = 0; r < 3; r++) {
				if (fleft[l] == -1 || i <= fleft[l]) continue;
				if (fright[r] == -1 || i >= fright[r]) continue;
				if (!diff_type(type, fleft[l], i, fright[r])) continue;
				int d = calc_score_min(dist, fleft[l], i, fright[r]);
				max_dist = max(max_dist, d);
			}
		}
	}
						
	printf("%d ", max_dist);
	
//END ******************************************************************
	
	free(type);
	free(dist);
			
	return 0;
}
