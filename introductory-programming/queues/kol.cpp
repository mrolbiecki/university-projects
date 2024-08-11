#include <cstdlib>
#include "kol.h"

using namespace std;

typedef interesant *person;

struct Queue {
	person head, tail;
};

//creates an empty list with two sentinels
Queue create_queue() {
	Queue res = {
		(interesant*)malloc(sizeof(interesant)),
		(interesant*)malloc(sizeof(interesant))
	};
	res.head->p1 = NULL;
	res.head->p2 = res.tail;
	res.tail->p1 = NULL;
	res.tail->p2 = res.head;
	return res;
}

bool isempty(const Queue q)
{
	return q.head->p2 == q.tail;
}

//changes a link a->old to a->nw
void link(person a, person old, person nw)
{
	if (a) {
		if (a->p1 == old) {
			a->p1 = nw;
		} else {
			a->p2 = nw;
		}
	}
}

//removes a preson 'a' from the queue and fixes the gap
person remove_person(person a)
{
	link(a->p1, a, a->p2);
	link(a->p2, a, a->p1);
	return a;
}

person pop_front(Queue &l) {
	if (!isempty(l)) {
		return remove_person(l.head->p2);
	}
	return NULL;
}

//adds a person 'a' to the end of the queue q
person push_back(Queue &q, person a)
{
	a->p1 = q.tail;
	a->p2 = q.tail->p2;
	link(q.tail->p2, q.tail, a);
	q.tail->p2 = a;
	return a;
}

void destroy (Queue &q)
{
	while (!isempty(q)) pop_front(q);
	free(q.head);
	free(q.tail);
}

bool is_person (person a)
{
	return a->p1 && a->p2;
}

//person that comes after a and b in the queue (or before, depends on orientation)
person next_person (person a, person b) {
	
	if (b->p1 != a) return b->p1;
	else return b->p2;
}

person front(const Queue q)
{
	return q.head->p2;
}

void reverse(Queue &a)
{
	swap(a.head, a.tail);
}

//links queue b to the end of a and clears b
void append(Queue &a, Queue &b)
{
	if (isempty(b)) return;
	
	person l = a.tail;
	person r = b.head;
	//links both queues
	link(l->p2, l, r->p2);
	link(r->p2, r, l->p2);
	a.tail = b.tail;
	//clears qb
	l->p2 = r;
	r->p2 = l;
	b.head = r;
	b.tail = l;
}

vector <Queue> queues;
int akt_numerek;

//-------------------------------------------

int numerek (person a)
{
	return a->val;
}

void otwarcie_urzedu(int m)
{
	queues.resize(m);
	for (int i = 0; i < m; ++i)
		queues[i] = create_queue();
		
	akt_numerek = 0;
}

person nowy_interesant(int k)
{	
	person a = (person)malloc(sizeof(interesant));
	a->val = akt_numerek++;
	return push_back(queues[k], a);
}

person obsluz(int k)
{
  return pop_front(queues[k]);
}

void zmiana_okienka(person a, int k)
{
	remove_person(a);
	push_back(queues[k], a);
}

void zamkniecie_okienka(int k1, int k2)
{
	append (queues[k2], queues[k1]);
}

vector<person> fast_track(person a, person b) {
	
	if (a == b)
	{
		remove_person(a);
		return {a};
	}
	
	//paths in both direcions form i1
	vector <person> paths [2] = {{a, a->p1}, {a, a->p2}};
	//ok == true means a path didn't reach one of the ends yet
	bool ok[2] = {is_person(a->p1), is_person(a->p2)};
	
	while (paths[0].back() != b && paths[1].back() != b)
	{
		person prv, cur, nxt;
		
		for (int i : {0, 1}) if (ok[i])
		{
			//if we haven't reach the end yet, we add next person to the path
			prv = paths[i][paths[i].size() - 2];
			cur = paths[i].back();
			nxt = next_person(prv, cur);
			if (is_person(nxt)) paths[i].push_back(nxt);
			else ok[i] = false;
		}
	}
	
	//the number of the path with i2 in it
	int nr = (paths[0].back() == b ? 0 : 1);
	
	//first people before and after the i1-i2 sequence
	person l, r;
	l = next_person(paths[nr][1], paths[nr][0]);
	r = next_person(paths[nr][paths[nr].size()-2], paths[nr].back());
	
	//removing i1-i2 from the queue and fixing the gap
	link(l, a, r);
	link(r, b, l);

	return paths[nr];
}

void naczelnik(int k)
{
  reverse(queues[k]);
}

vector <person> zamkniecie_urzedu()
{
	vector <person> res;
	
	for (auto q : queues)
	{
		while (!isempty(q)) res.push_back(pop_front(q));
		destroy(q);
	}
	return res;
}
