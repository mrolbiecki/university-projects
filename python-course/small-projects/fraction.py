import math

class Fraction:
    def __init__(self, numerator: int, denominator: int):
        assert isinstance(numerator, int) and isinstance(denominator, int), "Numerator and denominator must be integers."
        assert denominator != 0, "Denominator cannot be zero."
        self.numerator = numerator
        self.denominator = denominator
        self.normalize()

    def normalize(self):
        if self.numerator == 0:
            self.denominator = 1
        else:
            gcd = math.gcd(self.numerator, self.denominator)
            self.numerator //= gcd
            self.denominator //= gcd
            if self.denominator < 0:
                self.numerator = -self.numerator
                self.denominator = -self.denominator

    def __add__(self, other: "Fraction") -> "Fraction":
        new_numerator = self.numerator * other.denominator + other.numerator * self.denominator
        new_denominator = self.denominator * other.denominator
        return Fraction(new_numerator, new_denominator)

    def __sub__(self, other: "Fraction") -> "Fraction":
        new_numerator = self.numerator * other.denominator - other.numerator * self.denominator
        new_denominator = self.denominator * other.denominator
        return Fraction(new_numerator, new_denominator)

    def __mul__(self, other: "Fraction") -> "Fraction":
        new_numerator = self.numerator * other.numerator
        new_denominator = self.denominator * other.denominator
        return Fraction(new_numerator, new_denominator)

    def __truediv__(self, other: "Fraction") -> "Fraction":
        assert other.numerator != 0, "Cannot divide by zero."
        new_numerator = self.numerator * other.denominator
        new_denominator = self.denominator * other.numerator
        return Fraction(new_numerator, new_denominator)

    def __lt__(self, other: "Fraction") -> bool:
        return self.numerator * other.denominator < self.denominator * other.numerator

    def __le__(self, other: "Fraction") -> bool:
        return self.numerator * other.denominator <= self.denominator * other.numerator

    def __gt__(self, other: "Fraction") -> bool:
        return self.numerator * other.denominator > self.denominator * other.numerator

    def __ge__(self, other: "Fraction") -> bool:
        return self.numerator * other.denominator >= self.denominator * other.numerator

    def __eq__(self, other: "Fraction") -> bool:
        return self.numerator == other.numerator and self.denominator == other.denominator

    def __ne__(self, other: "Fraction") -> bool:
        return not self == other

    def __repr__(self) -> str:
        return f"'{self.__str__()}'"

    def __str__(self) -> str:
        if self.numerator == 0:
            return "0"
        return f"{self.numerator}/{self.denominator}" if self.denominator != 1 else str(self.numerator)


def test(a: Fraction, b: Fraction):
    print(f"{a=}")
    print(f"{b=}")
    print("a + b: ", a + b)
    print("a - b: ", a - b)
    print("a * b: ", a * b)
    print("a / b: ", a / b)
    print("a < b: ", a < b)
    print("a > b: ", a > b)
    print("a <= b: ", a <= b)
    print("a >= b: ", a >= b)
    print("a == b: ", a == b)
    print("a != b: ", a != b)


def main():
    test(Fraction(1, 2), Fraction(2, 3))
    test(Fraction(3, 4), Fraction(-1, 5))
    test(Fraction(0, 1), Fraction(7, 3))
    test(Fraction(4, -8), Fraction(-2, -4))


if __name__ == "__main__":
    main()
