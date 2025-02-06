# Function create(n) creates a dictionary of integers and their multiples up to n.
def create(n):
    result = {}
    for i in range(1, n + 1):
        result[i] = [i * j for j in range(1, n // i + 1)]
    return result

# Function reverse(dictionary) takes a dictionary of integers and their multiples and returns a dictionary of multiples and their integers.
def reverse(dictionary):
    n = len(dictionary)
    result = {}
    for i in range(1, n + 1):
        for j in dictionary[i]:
            if j not in result:
                result[j] = {i}
            else:
                result[j].add(i)
    return result

# Function write(dictionary) prints the dictionary in the format: key: value1 value2 ...
def write(dictionary):
    for key, value in dictionary.items():
        if value:
            print(key, ':', end=' ')
            print(*value)


def main():
    n = 10
    container = create(n)
    print('Original dictionary:')
    write(container)
    container = reverse(container)
    print('Reversed dictionary:')
    write(container)

if __name__ == '__main__':
    main()