import random
import math

# Monte Carlo method to approximate pi
def monte_carlo(iterations, step_size):
    points_in_circle = 0
    for it in range(iterations):
        x = random.random()
        y = random.random()
        if x * x + y * y <= 1:
            points_in_circle += 1
        # print the approximation every step_size iterations
        if it % step_size == 0:
            print(it, 'steps: ', 4 * points_in_circle / (it + 1))
    return 4 * points_in_circle / iterations

n = int(input('Enter the number of steps: '))
k = int(input('Enter the step size: '))
approximation = monte_carlo(n, k)

error = abs(approximation - math.pi) / math.pi * 100
print('error: ', error, '\b%')
