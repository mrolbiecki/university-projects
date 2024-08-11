/*
* author: Marcin Rolbiecki
* I used the idea from:
* https://stackoverflow.com/questions/3306838/algorithm-for-reflecting-a-point-across-a-line
* to reflect a point over a line
*/

#include <stdio.h>
#include <malloc.h>
#include <stdbool.h>
#include <math.h>
	
typedef long long int ll;

//ACCURACY OF FLOATING-POINT ARITHMETIC

const double eps = 1e-10;

bool equals (double a, double b)
{	
	if (fabs(a - b) < eps) return true;
	else return false;
}

//STRUCTURES

typedef struct point
{
	double x, y;
} point;

typedef struct figure
{
	char type;
	point p1, p2;
	//when type = P, p1 and p2 are the verticies of the rectangle, lower left nad upper right respectively
	//when type = K, p1 is the center of the circle, p2 is unused
	//when type = Z, p1 and p2 are the points that define the bend
	double r;
	//when type = K, r is the radius of the circle, otherwise unused
	int nr;
	//when type = Z, nr is the number of the paper sheet that is being modified
} figure;

//GEOMETRIC FUNCTIONS

double cross_prod (point *a, point *b)
{
	return a->x * b->y - b->x * a->y;
}

int orientation (point *a, point *b, point *c)
{
	double p = cross_prod(a, b) + cross_prod(b, c) + cross_prod(c, a);
	
	if (equals(p, 0)) return 0; //colinear
	else if (p < 0) return -1; //c is on the right
	else return 1; //c is on the left
}

point reflection (point *p1, point *p2, point *p)
{
	//x = const
    if(equals(p1->x, p2->x)) return (point){2.0 * p1->x - p->x, p->y};
    //y = const
    if(equals(p1->y, p2->y)) return (point){p->x, 2.0 * p1->y - p->y};
    
    //y = ax + c
    double a = (p2->y - p1->y) / (p2->x - p1->x);
    double c = p1->y - a * p1->x;
    
    double d = (p->x + (p->y - c) * a) / (1.0 + a*a);
    return (point){2 * d - p->x, 2 * d * a - p->y + 2 * c};
}

int is_inside_rectangle(figure *f, point *p)
{
	if (f->p1.x > p->x || f->p1.y > p->y) return 0;
	if (f->p2.x < p->x || f->p2.y < p->y) return 0;
	return 1;
}

int is_inside_circle(figure *f, point *p)
{
	double dist_sq = pow(p->x - f->p1.x, 2) + pow(p->y - f->p1.y, 2);
	if (dist_sq <= pow(f->r, 2)) return 1;
	return 0;
}

ll count_layers (figure *T, int *k, point *p)
{
	figure *f = &T[*k];
	
	if (f->type == 'P') return is_inside_rectangle(f, p);
	if (f->type == 'K') return is_inside_circle(f, p);
	if (f->type == 'Z')
	{
		int o = orientation(&f->p1, &f->p2, p);
		
		//if the point is on the right side of the bend, there are zero layers
		if (o == -1) return 0;
		//if the point lies exactly on the bend, the number of layers doesn't change
		if (o == 0) return count_layers(T, &f->nr, p);
		//if the point liest on the left side of the bend, we sum the number of layers from both sides of the bend
		if (o == 1)
		{
			point pr = reflection(&f->p1, &f->p2, p);
			return count_layers(T, &f->nr, p) + count_layers(T, &f->nr, &pr);
		}
	}
	return -1;
}

//INPUT AND OUTPUT

void query (figure *T)
{
	int k; double x1, y1;
	scanf("%d %lf %lf", &k, &x1, &y1);
	point p = (point){x1, y1};
	
	printf("%lld\n", count_layers(T, &k, &p));
}

void read_figure (figure * T, int i)
{
	char type; scanf(" %c", &type);
		
	figure F;
	//rectangle
	if (type == 'P')
	{
		double x1, y1, x2, y2;
		scanf("%lf %lf %lf %lf", &x1, &y1, &x2, &y2);
		
		F.type = 'P';
		F.p1 = (point){x1, y1};
		F.p2 = (point){x2, y2};

	}
	//circle
	if (type == 'K')
	{
		double x1, y1, r;
		scanf("%lf %lf %lf", &x1, &y1, &r);
		
		F.type = 'K';
		F.p1 = (point){x1, y1};
		F.r = r;
	}
	//bended sheet
	if (type == 'Z')
	{
		int nr;
		double x1, y1, x2, y2;
		scanf("%d %lf %lf %lf %lf", &nr, &x1, &y1, &x2, &y2);
		
		F.type = 'Z';
		F.p1 = (point){x1, y1};
		F.p2 = (point){x2, y2};
		F.nr = nr;
	}
	
	T[i] = F;
}

int main()
{	
	int n, q;
	scanf("%d %d", &n, &q);
	
	struct figure *T = malloc((size_t)(n + 1) * sizeof(figure));
	
	for (int i = 1; i <= n; i++) read_figure(T, i);
	
	for (int i = 1; i <= q; i++) query(T);
	
	return 0;
}
