#include <bits/stdc++.h>
using namespace std;

constexpr int INF = 1e9;

class Trees {
public:
    void init(int n) {
        T = 1 << (int)ceil(log2(n + 2));
        t.resize(2 * T, 0);
        addOnRange(0, 0, INF);
        addOnRange(1, n, 1);
    }

    int heightAt(int pos) const {
        pos += T;
        int res = 0;
        while (pos > 0) {
            res += t[pos];
            pos /= 2;
        }
        return res;
    }

    void addOnRange(int l, int r, int val) {
        add(1, 0, T - 1, l, r, val);
    }

private:
    vector<int> t;
    int T;

    void add(int id, int lo, int hi, int l, int r, int val) {
        if (hi < l || r < lo) return;
        if (l <= lo && hi <= r) {
            t[id] += val;
            return;
        }
        int mid = (lo + hi) / 2;
        add(id * 2, lo, mid, l, r, val);
        add(id * 2 + 1, mid + 1, hi, l, r, val);
    }
};

class Segments {
public:
    void init(int n) {
        T = 1 << (int)ceil(log2(n + 2));
        t.resize(2 * T, 0);
        insert({0, 0});
        insert({1, n});
        insert({n + 1, n + 1});
    }

    struct Segment {
        int l, r;
        bool operator<(const Segment& other) const { return l < other.l; }
    };

    Segment get(int pos) const {
        return *prev(segs.upper_bound({pos, 0}));
    }

    void insert(Segment seg) {
        segs.insert(seg);
        set(seg.l, seg.r - seg.l + 1);
    }

    void remove(Segment seg) {
        segs.erase(seg);
        set(seg.l, 0);
    }

    int maxBetween(int l, int r) const {
        return getMax(1, 0, T - 1, l, r);
    }

private:
    set<Segment> segs;
    vector<int> t;
    int T;

    void set(int pos, int val) {
        pos += T;
        t[pos] = val;
        while (pos > 1) {
            pos /= 2;
            t[pos] = max(t[pos * 2], t[pos * 2 + 1]);
        }
    }

    int getMax(int id, int lo, int hi, int l, int r) const {
        if (hi < l || r < lo) return 0;
        if (l <= lo && hi <= r) return t[id];
        int mid = (lo + hi) / 2;
        return max(
            getMax(id * 2, lo, mid, l, r),
            getMax(id * 2 + 1, mid + 1, hi, l, r)
        );
    }
};

Trees trees;
Segments segments;

void grow(int l, int r, int k) {
    trees.addOnRange(l, r, k);

    if (trees.heightAt(l - 1) <= trees.heightAt(l) &&
        trees.heightAt(l - 1) > trees.heightAt(l) - k) {
        auto a = segments.get(l - 1);
        auto b = segments.get(l);
        segments.remove(a);
        segments.remove(b);
        segments.insert({a.l, b.r});
    }

    if (trees.heightAt(r) > trees.heightAt(r + 1) &&
        trees.heightAt(r) - k <= trees.heightAt(r + 1)) {
        auto s = segments.get(r);
        segments.remove(s);
        segments.insert({s.l, r});
        segments.insert({r + 1, s.r});
    }
}

int query(int l, int r) {
    auto lseg = segments.get(l);
    auto rseg = segments.get(r);

    if (lseg.l == rseg.l) return r - l + 1;

    int innerMax = 0;
    if (lseg.r + 1 < rseg.l) {
        innerMax = segments.maxBetween(lseg.r + 1, rseg.l - 1);
    }

    return max({lseg.r - l + 1, r - rseg.l + 1, innerMax});
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;

    trees.init(n);
    segments.init(n);

    for (int i = 0; i < m; ++i) {
        char op;
        cin >> op;
        if (op == 'N') {
            int l, r, k;
            cin >> l >> r >> k;
            grow(l, r, k);
        } else if (op == 'C') {
            int l, r;
            cin >> l >> r;
            cout << query(l, r) << '\n';
        }
    }

    return 0;
}
