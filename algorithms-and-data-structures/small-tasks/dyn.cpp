#include <bits/stdc++.h>
#define ll long long
using namespace std;

//splay operation implementation from https://zhtluo.com/cp/splay-tree-one-tree-to-rule-them-all.html

struct node {
	node *f, *c[2];
	int val, sz;
	ll size;
	node(int v, int s) : val(v), sz(s) {
		f = c[0] = c[1] = nullptr;
		size = sz;
	}
	void update() {
		size = sz;
		for (int t = 0; t < 2; ++t)
			if (c[t]) size += c[t]->size;
	}
};

struct tree {
	node *root;
	tree() {
		root = new node(0, 1);
		root->c[1] = new node(0, 1);
		root->size = 2;
		root->c[1]->f = root;
  }
  // Helper function to rotate node.
	void rotate(node *n) {
		int v = n->f->c[0] == n;
		node *p = n->f, *m = n->c[v];
		if (p->f) p->f->c[p->f->c[1] == p] = n;
		n->f = p->f, n->c[v] = p;
		p->f = n, p->c[v ^ 1] = m;
		if (m) m->f = p;
		p->update(), n->update();
	}
  // Splay n so that it is under s (or to root if s is null).
	void splay(node *n, node *s = nullptr) {
		while (n->f != s) {
			node *m = n->f, *l = m->f;
			if (l == s)
				rotate(n);
			else if ((l->c[0] == m) == (m->c[0] == n))
				rotate(m), rotate(n);
			else
				rotate(n), rotate(n);
		}
	if (!s) root = n;
	}
	int size() { return root->size - 2; }
  
	void insert(node *n, ll pos, node *r) {
		ll ls = r->c[0] ? r->c[0]->size : 0;
		if (pos <= ls) {
			if (r->c[0])
				insert(n, pos, r->c[0]);
			else {
				r->c[0] = n;
				n->f = r;
				splay(n);
			}
		} else if (ls + r->sz <= pos) {
			if (r->c[1])
				insert(n, pos - ls - r->sz, r->c[1]);
			else {
				r->c[1] = n;
				n->f = r;
				splay(n);
			}
		} else {
			node *left = new node(r->val, pos - ls);
			left->c[0] = r->c[0];
			if(left->c[0]) {
				left->c[0]->f = left;
			}
			node *right = new node(r->val, r->sz - left->sz);
			right->c[1] = r->c[1];
			if (right->c[1]) {
				right->c[1]->f = right;
			}
			
			n->f = r->f;
			if (r->f) {
				if (r->f->c[0] == r)
					r->f->c[0] = n;
				else
					r->f->c[1] = n;
			} else {
				root = n;
			}
			
			n->c[0] = left;
			left->f = n;
			n->c[1] = right;
			right->f = n;
			
			left->update();
			right->update();
			n->update();
			if (n->f) {
				n->f->update();
			}
			
			delete(r);
			splay(n);
		}
	}
  
	node *find(ll pos, node *r) {
		ll ls = r->c[0] ? r->c[0]->size : 0;
		
		if (pos < ls)
			return find(pos, r->c[0]);
		else if (ls + r->sz <= pos)
			return find(pos - ls - r->sz, r->c[1]);

		splay(r);
		return r;
	}
};

int m;
tree t;

int main () {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	
	cin >> m;
	int w = 0;
	for (int i = 0; i < m; i++) {
		char op;
		cin >> op;
		if (op == 'i') {
			ll j, x, k;
			cin >> j >> x >> k;
			j = (j + w) % (t.size() + 1);
			t.insert(new node(x, k), j + 1, t.root);
		}
		if (op == 'g') {
			ll j;
			cin >> j;
			j = (j + w) % t.size();
			w = t.find(j + 1, t.root)->val;
			cout << w << '\n';
		}
	}
	
	return 0;
}



