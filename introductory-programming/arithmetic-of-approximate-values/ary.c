#include <stdbool.h>
#include <stdio.h>
#include <math.h>
#include "ary.h"

#define eps 1e-13
#define inf INFINITY
 
/*
void print_przedzial(wartosc w)
{
  if(w.anty) {
    printf("[-inf, %f] U [%f, inf]\n", w.l, w.r);
  } else {
    printf("[%f, %f]\n", w.l, w.r);
  }
}
*/

/*
DEFINICJA STRUCT WARTOSC:

zmienne l i r oznaczają odpowienio lewy i prawy kraniec przedziału
anty domyślnie = 0. Jeżeli anty = 1, to zamiast przedziału (l, r) reprezentujemy
przedział (-inf, l) u (r, inf)

typedef struct wartosc {
	double l, r;
	bool anty;
} wartosc;
*/

bool czy_zero(double x)
{ 
	return fabs(x) < eps;
}

bool czy_zerowy (wartosc w)
{
	return !w.anty && czy_zero(w.l) && czy_zero(w.r);
}

bool czy_pusty(wartosc w)
{
	return w.anty && isinf(w.l) && isinf(w.r);
}

bool czy_pelny (wartosc w)
{
	return !w.anty && isinf(w.l) && isinf(w.r);
}

wartosc pelny () //przedział (-inf, inf)
{
	wartosc w;
	w.anty = 0;
	w.l = -inf;
	w.r = inf;
	return w;
}
wartosc pusty () //przedział R - (inf, inf)
{ 
	wartosc w;
	w.anty = 1;
	w.l = -inf;
	w.r = inf;
	return w;
}

double max (double a, double b)
{
	if (isnan(a)) return b;
	if (isnan(b)) return a;
	
	return (a >= b ? a : b);
}

double min (double a, double b)
{
	if (isnan(a)) return b;
	if (isnan(b)) return a;

	return (a <= b ? a : b);
}

void swap_w (wartosc * a, wartosc * b)
{
	wartosc tmp = *a;
	*a = *b;
	*b = tmp;
}

void swap_d (double * a, double * b)
{
	double tmp = *a;
	*a = *b;
	*b = tmp;
}

double mult (double a, double b) //mnożenie, które zapewnia inf * 0 = 0
{
	if (czy_zero(a) || czy_zero(b))
		return 0.0;
		
	return a * b;
}

wartosc wartosc_od_do(double x, double y)
{
	wartosc res;
	res.anty = 0;
	res.l = x;
	res.r = y;
	return res;
}

wartosc wartosc_anty_od_do(double x, double y)
{
	if (x >= y)
		return pelny();
	
	wartosc res;
	res.anty = 1;
	res.l = x;
	res.r = y;
	return res;
}

wartosc wartosc_dokladnosc(double x, double p)
{
	return wartosc_od_do(x-fabs(p*x/100.0), x+fabs(p*x/100.0));
}

wartosc wartosc_dokladna(double x)
{
	return wartosc_dokladnosc(x, 0);
}

bool in_wartosc(wartosc w, double x)
{
	if (w.anty)
		return x  - eps <= w.l || w.r <= x + eps;

	return w.l <= x + eps && x - eps <= w.r;
}

double min_wartosc(wartosc w)
{
	if (czy_pusty(w))
		return NAN;
	
	if (w.anty)
		return -inf;
	
	return w.l;
}

double max_wartosc(wartosc w)
{
	if (czy_pusty(w))
		return NAN;
	
	if (w.anty)
		return inf;
	
	return w.r;
}

double sr_wartosc(wartosc w)
{
	if (czy_pusty(w) || czy_pelny(w))
		return NAN;
	
	return (min_wartosc(w) + max_wartosc(w)) / 2;
}

wartosc plus(wartosc A, wartosc B)
{
	if (czy_pusty(A) || czy_pusty(B))
		return pusty();
	
	if (!A.anty && !B.anty)
		return wartosc_od_do(A.l + B.l, A.r + B.r);
	
	if (A.anty && B.anty)
		return pelny();
		
	return wartosc_anty_od_do(A.l + B.r, A.r + B.l);
}

wartosc minus (wartosc A, wartosc B) //odejmowanie to dodawanie przedziału pomnożonego przez -1
{
	if (czy_pusty(A) || czy_pusty(B))
		return pusty();
	
	return plus(A, razy(B, wartosc_od_do(-1, -1)));
}

wartosc razy (wartosc A, wartosc B)
{
	
	if (czy_pusty(A) || czy_pusty(B))
		return pusty();
		
	if (czy_zerowy(A) || czy_zerowy(B))
		return wartosc_dokladna(0.0);
	
	if (!A.anty && !B.anty) {
		
		double tab[] = {mult(A.l, B.l), mult(A.l, B.r), mult(A.r, B.l), mult(A.r, B.r)};
		
		double mini = tab[0];
		double maxi = tab[0];
		
		for (int i = 0; i < 4; i++) {
			mini = min(mini, tab[i]);
			maxi = max(maxi, tab[i]);
		}
		
		return wartosc_od_do(mini, maxi);
	}
	
	if (A.anty && B.anty) {
		
		if (in_wartosc(A, 0) || in_wartosc(B, 0))
			return pelny();
			
		return wartosc_anty_od_do(max( mult(A.l, B.r), mult(A.r, B.l)), min(mult(A.l, B.l), mult(A.r, B.r)) );
	}
		
	if (B.anty)
		swap_w(&A, &B);
		
	if (in_wartosc(B, 0))
		return pelny();
		
	if (B.l < 0 && B.r < 0) { //aby uprościć zapis, "przenoszę" minus z prawego czynnika do lewego
		
		B.l *= -1.0; B.r *= -1.0;
		swap_d(&B.l, &B.r);
		A.l *= -1.0; A.r *= -1.0;
		swap_d(&A.l, &A.r);
	}
		
	return wartosc_anty_od_do(max( mult(A.l, B.l), mult(A.l, B.r)), min(mult(A.r, B.l), mult(A.r, B.r) ));
}

wartosc odwrotnosc (wartosc A) {
	
	if (A.anty) {
		
		if(czy_zero(A.l))
			return wartosc_od_do(-inf, 1/A.r);
		
		if (czy_zero(A.r))
			return wartosc_od_do(1/A.l, inf);
		
		if (A.l > 0 || A.r < 0)
			return wartosc_anty_od_do(1/A.r, 1/A.l);
		
		return wartosc_od_do(1/A.l, 1/A.r);
	}
	
	if(czy_zero(A.l))
		return wartosc_od_do(1/A.r, inf);
		
	if (czy_zero(A.r))
		return wartosc_od_do(-inf, 1/A.l);
	
	if (in_wartosc(A, 0))
		return wartosc_anty_od_do(1/A.l, 1/A.r);
		
	return wartosc_od_do(1/A.r, 1/A.l);
}

wartosc podzielic (wartosc A, wartosc B) { //dzielenie to mnożenie przez odwrotność
	
	if (czy_pusty(A) || czy_pusty(B))
		return pusty();
		
	if (czy_zerowy(B))
		return pusty();
	
	return razy(A, odwrotnosc(B));
}
