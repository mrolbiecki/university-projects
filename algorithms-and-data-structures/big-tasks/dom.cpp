#include <bits/stdc++.h>
#define ll long long
using namespace std;

// Helper function to check if a specific bit is on in a mask
bool is_on(int mask, int bit) {
    return mask & (1 << bit);
}

int main() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);

    int n, k;
    cin >> n >> k;

    int max_mask = 1 << k; // Total number of possible states for k rows
    vector<vector<ll>> grid(k, vector<ll>(n, 0)); // Grid values for k rows and n columns
    vector<vector<ll>> dp(n + 1, vector<ll>(max_mask, 0)); // DP array to store maximum sum for each column and mask
    vector<vector<ll>> vert_sum(n, vector<ll>(max_mask, 0)); // Vertical domino sum for each column and mask
    vector<vector<ll>> hor_sum(n, vector<ll>(max_mask, 0)); // Horizontal domino sum for each column and mask
    vector<vector<int>> disjoint_masks(max_mask); // Masks that don’t overlap with each other

    // Precompute disjoint masks (masks that don’t overlap with each other)
    for (int mask1 = 0; mask1 < max_mask; mask1++) {
        for (int mask2 = 0; mask2 < max_mask; mask2++) {
            if (mask1 & mask2) continue; // Skip if there’s overlap
            disjoint_masks[mask1].push_back(mask2);
        }
    }

    // Read grid input
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < n; j++) {
            cin >> grid[i][j];
        }
    }

    // Calculate maximum possible sum for placing vertical dominoes in each column
    for (int j = 0; j < n; j++) {
        for (int mask = 0; mask < max_mask; mask++) {
            vector<ll> vert_dp(k + 1, 0); // Temporary DP for vertical placements in current column

            // Initialize first possible vertical domino for k >= 2
            if (k >= 2 && !is_on(mask, 0) && !is_on(mask, 1)) {
                vert_dp[1] = max(vert_dp[1], grid[0][j] + grid[1][j]);
            }

            // Process remaining positions in column for vertical dominoes
            for (int b = 2; b < k; b++) {
                vert_dp[b] = vert_dp[b - 1]; // Carry forward maximum sum
                if (!is_on(mask, b) && !is_on(mask, b - 1)) {
                    // Place vertical domino covering rows (b-1, b) in column j
                    vert_dp[b] = max(vert_dp[b], vert_dp[b - 2] + grid[b - 1][j] + grid[b][j]);
                }
            }
            vert_sum[j][mask] = vert_dp[k - 1]; // Store the maximum vertical sum for this mask and column
        }
    }

    // Calculate sum for horizontal domino placements in each column
    for (int j = 0; j < n - 1; j++) { // No need to go to last column as horizontal domino spans two columns
        for (int mask = 0; mask < max_mask; mask++) {
            for (int b = 0; b < k; b++) {
                if (is_on(mask, b)) {
                    // Place a horizontal domino with its left side in cell (b, j)
                    hor_sum[j][mask] += grid[b][j] + grid[b][j + 1];
                }
            }
        }
    }

    // Dynamic programming: maximize sum by placing dominoes up to each column
    for (int j = 0; j < n; j++) {
        for (int mask1 = 0; mask1 < max_mask; mask1++) {
            for (int mask2 : disjoint_masks[mask1]) { // Iterate over non-overlapping masks
                // Calculate score by combining previous column with current horizontal and vertical domino placements
                ll score = dp[j][mask1] + hor_sum[j][mask2] + vert_sum[j][mask1 | mask2];
                dp[j + 1][mask2] = max(dp[j + 1][mask2], score);
            }
        }
    }

    // The answer is the maximum sum achievable by using the last column (n-th) with no dominoes overlapping
    cout << dp[n][0];
}
