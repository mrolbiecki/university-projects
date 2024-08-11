/*
* author: Marcin Rolbiecki
* I used some code from https://cp-algorithms.com/data_structures/segment_tree.html#preserving-the-history-of-its-values-persistent-segment-tree
*/
#include <vector>
#include <memory>
using namespace std;

#define ll long long

const ll MAX = 4294967296; //2^32
const ll shift = 2147483648; //-INT_MIN

struct node;
typedef shared_ptr<node> ptr;

struct node
{
	int maxi;
	ptr l, r;

	node (int val) : maxi(val), l(nullptr), r(nullptr) {}
	node (ptr _l, ptr _r) : maxi(-1), l(_l), r(_r)
	{
		if (l) maxi = max(maxi, l->maxi);
		if (r) maxi = max(maxi, r->maxi);
	}
};

//returns a pointer to the new segment tree where the value at pos in tree rooted at v is changed to val
ptr update (ptr v, ll val, ll pos, ll tl = 0, ll tr = MAX)
{
	//instead of changing the value I just create a new node
	if (tl == tr) return make_shared<node>(val);

	ll tm = (tl + tr) / 2;

	//since the tree is created dynamically, some nodes do not exist yet
	//I create the missing nodes in the old tree to copy and update them in the new tree
	if (pos <= tm) //left son
	{
		if (!v->l) v->l = make_shared<node>(-1);
		return make_shared<node>(update(v->l, val, pos, tl, tm), v->r);
	} 
	else //right son
	{
		if (!v->r) v->r = make_shared<node>(-1);
		return make_shared<node>(v->l, update(v->r, val, pos, tm + 1, tr));
	}
}

vector <ptr> roots;

//returns maximum value on segment [l, r] in tree rooted at v
int max_query (ptr v, ll l, ll r, ll tl = 0, ll tr = MAX)
{
	if (l > r) return -1;
	if (l == tl && tr == r) return v->maxi;

	ll tm = (tl + tr) / 2;
	int res = -1;
	
	//I ignore non-existing nodes in the query
	if (v->l) res = max(res, max_query(v->l, l, min(r, tm), tl, tm));
	if (v->r) res = max(res, max_query(v->r, max(l, tm + 1), r, tm + 1, tr));
	return res;
}

void pushBack(int value)
{
	roots.push_back(update(roots.back(), (ll)roots.size() - 1, value + shift));
}

void init(const vector<int> &seq)
{
	roots.push_back(make_shared<node>(-1));
	for (int s : seq) pushBack(s);
}

int prevInRange(int i, int lo, int hi)
{
	return max_query(roots[i + 1], lo + shift, hi + shift);
}

void done()
{
	roots.clear();
}
