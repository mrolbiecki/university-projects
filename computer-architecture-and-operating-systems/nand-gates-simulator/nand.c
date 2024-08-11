#include "nand.h"
#include <errno.h>
#include <stdbool.h>
#include <sys/types.h>
#include <stdlib.h>

struct input {
    nand_t *gate;
    bool *signal;
    int id;
};

typedef struct input input_t;

input_t *make_input(nand_t *a, bool *b, int id) {
    input_t *new_input = malloc(sizeof(input_t));
    if (new_input == NULL) {
        errno = ENOMEM;
        return NULL;
    }
    new_input->gate = a;
    new_input->signal = b;
    new_input->id = id;
    return new_input;
}

struct vector {
    int size, max_size;
    input_t **arr;
};

typedef struct vector vector_t;

struct nand {
    vector_t *in, *out;
    unsigned int n;
    bool is_visited, is_processed;
    bool output;
};

typedef struct nand nand_t;

static vector_t *vector_init(void) {
    vector_t *V = malloc(sizeof(vector_t));

    if (V == NULL) {
        errno = ENOMEM;
        return NULL;
    }

    V->size = 0;
    V->max_size = 1;
    V->arr = malloc(V->max_size * sizeof(input_t *));
    if (V->arr == NULL) {
        free(V);
        errno = ENOMEM;
        return NULL;
    }
    return V;
}

static void vector_delete(vector_t *V) {
    if (V == NULL) return;
    for (int i = 0; i < V->size; i++)
        free(V->arr[i]);
    free(V->arr);
    free(V);
}

static int vector_resize(vector_t *V, size_t new_size) {
    if (V == NULL || new_size <= 0) {
        errno = EINVAL;
        return -1;
    }

    input_t **new_arr = realloc(V->arr, new_size * sizeof(input_t *));
    if (new_arr == NULL) {
        errno = ENOMEM;
        return -1;
    }

    V->arr = new_arr;
    V->max_size = new_size;
    return 0;
}

static int vector_push_back(vector_t *V, input_t *new_item) {
    if (V == NULL) {
        errno = EINVAL;
        return -1;
    }

    if (V->size + 1 > V->max_size) {
        size_t new_size = V->max_size * 2;

        int err = vector_resize(V, new_size);
        if (err != 0) return err;
    }
    V->arr[V->size] = new_item;
    V->size++;
    return 0;
}

static void vector_pop_back(vector_t *V) {
    if (V == NULL) return;
    if (V->arr == NULL) return;
    free(V->arr[V->size - 1]);
    V->size--;
}

static void vector_swap(vector_t const *V, int i, int j) {
    void *temp1 = V->arr[j];
    V->arr[j] = V->arr[i];
    V->arr[i] = temp1;
}

nand_t *nand_new(unsigned n) {
    nand_t *res = (nand_t *) malloc(sizeof(nand_t));
    if (res == NULL) {
        errno = ENOMEM;
        return NULL;
    }

    res->n = n;
    res->in = vector_init();
    if (res->in == NULL) {
        free(res);
        errno = ENOMEM;
        return NULL;
    }

    res->out = vector_init();
    if (res->out == NULL) {
        vector_delete(res->in);
        free(res);
        errno = ENOMEM;
        return NULL;
    }

    res->is_visited = false;
    res->is_processed = false;

    for (int i = 0; i < (int) n; i++) {
        input_t *new_item = make_input(NULL, NULL, -1);
        if (new_item == NULL) {
            vector_delete(res->in);
            vector_delete(res->out);
            free(res);
            return NULL;
        }
        if (vector_push_back(res->in, new_item) != 0) {
            free(new_item);
            vector_delete(res->in);
            vector_delete(res->out);
            free(res);
            return NULL;
        }
    }

    return res;
}

static void nand_disconnect_input(nand_t const *g, unsigned k) {
    if (g->in->arr[k]->id == -2) {
        g->in->arr[k]->id = -1;
        return;
    }

    nand_t *kto = g->in->arr[k]->gate;
    int gdzie = g->in->arr[k]->id;

    nand_t *kto2 = kto->out->arr[kto->out->size - 1]->gate;
    int gdzie2 = kto->out->arr[kto->out->size - 1]->id;
    kto2->in->arr[gdzie2]->id = gdzie;

    vector_swap(kto->out, gdzie, kto->out->size - 1);
    vector_pop_back(kto->out);
    g->in->arr[k]->id = -1;
}

void nand_delete(nand_t *g) {
    if (g == NULL) {
        return;
    }

    if (g->in != NULL) {
        for (int i = 0; i < (int) g->in->size; i++) {
            if (g->in->arr[i]->id != -1) {
                nand_disconnect_input(g, i);
            }
        }
        vector_delete(g->in);
    }

    if (g->out != NULL) {
        while (g->out->size) {
            nand_disconnect_input(g->out->arr[0]->gate, g->out->arr[0]->id);
        }
        vector_delete(g->out);
    }
    free(g);
}

int nand_connect_nand(nand_t *g_out, nand_t *g_in, unsigned k) {
    if (g_out == NULL || g_in == NULL || k >= g_in->n) {
        errno = EINVAL;
        return -1;
    }

    if (g_in->in->arr[k]->id != -1) {
        nand_disconnect_input(g_in, k);
    }

    g_in->in->arr[k]->gate = g_out;

    input_t *new_item = make_input(g_in, NULL, k);
    if (new_item == NULL) {
        errno = ENOMEM;
        return -1;
    }
    if (vector_push_back(g_out->out, new_item) != 0) {
        free(new_item);
        return -1;
    }
    g_in->in->arr[k]->id = g_out->out->size - 1;

    return 0;
}

int nand_connect_signal(bool const *s, nand_t *g, unsigned k) {
    if (g == NULL || s == NULL || k >= g->n) {
        errno = EINVAL;
        return -1;
    }

    if (g->in->arr[k]->id != -1) {
        nand_disconnect_input(g, k);
    }

    g->in->arr[k]->id = -2;
    g->in->arr[k]->signal = (bool *) s;
    return 0;
}

static ssize_t dfs(nand_t *const g) {
    g->is_visited = true;
    g->is_processed = true;

    int maxi = 0;
    bool signal_and = true;
    for (int i = 0; i < (int) g->n; i++) {
        if (g->in->arr[i]->id == -2) {
            signal_and &= *g->in->arr[i]->signal;
        } else {
            if (g->in->arr[i]->id == -1 || g->in->arr[i]->gate->is_processed) {
                errno = ECANCELED;
                return -1;
            }

            if (!g->in->arr[i]->gate->is_visited) {
                int val = dfs(g->in->arr[i]->gate);
                if (val == -1) {
                    return -1;
                }
                if (maxi < val) {
                    maxi = val;
                }
            }

            signal_and &= g->in->arr[i]->gate->output;
        }
    }

    g->output = !signal_and;
    g->is_processed = false;

    if (g->n == 0) {
        return 0;
    }
    else {
        return 1 + maxi;
    }
}

static void dfs_clear(nand_t *g) {
    g->is_visited = false;
    g->is_processed = false;

    for (int i = 0; i < (int) g->n; i++) {
        if (g->in->arr[i]->id >= 0 && g->in->arr[i]->gate->is_visited) {
            dfs_clear(g->in->arr[i]->gate);
        }
    }
}

ssize_t nand_evaluate(nand_t **g, bool *s, size_t m) {
    if (m == 0 || g == NULL || s == NULL) {
        errno = EINVAL;
        return -1;
    }

    for (int i = 0; i < (int) m; i++) {
        if (g[i] == NULL) {
            errno = EINVAL;
            return -1;
        }
    }

    ssize_t res = 0;

    for (int i = 0; i < (int) m; i++) {
        ssize_t path = dfs(g[i]);
        if (path == -1) {
            errno = ECANCELED;
            return -1;
        }

        if (res < path) res = path;
    }

    for (int i = 0; i < (int) m; i++) {
        s[i] = g[i]->output;
        dfs_clear(g[i]);
    }

    return res;
}

ssize_t nand_fan_out(nand_t const *g) {
    if (g == NULL) {
        errno = EINVAL;
        return -1;
    }

    return g->out->size;
}

void *nand_input(nand_t const *g, unsigned k) {
    if (g == NULL || k >= g->n) {
        errno = EINVAL;
        return NULL;
    }

    if (g->in->arr[k]->id >= 0) {
        return g->in->arr[k]->gate;
    } else if (g->in->arr[k]->id == -2) {
        return g->in->arr[k]->signal;
    } else {
        errno = 0;
        return NULL;
    }
}

nand_t *nand_output(nand_t const *g, ssize_t k) {
    return g->out->arr[k]->gate;
}
