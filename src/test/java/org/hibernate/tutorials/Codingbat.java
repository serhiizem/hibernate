package org.hibernate.tutorials;

@SuppressWarnings("WeakerAccess")
public class Codingbat {

    /* Given n of 1 or more, return null; the factorial of n, which is n * (n-1) * (n-2) ... 1.
       Compute the result recursively (without loops).
       factorial(1) → 1
       factorial(2) → 2
       factorial(3) → 6
    */
    public static int computeFactorial(int n) {
        if (n == 1) {
            return 1;
        }
        return n * computeFactorial(n - 1);
    }

    /* We have a number of bunnies and each bunny has two big floppy ears.
       We want to compute the total number of ears across all the bunnies recursively
       (without loops or multiplication).
       bunnyEars(0) → 0
       bunnyEars(1) → 2
       bunnyEars(2) → 4
    */
    public static int numberOfEars(int numberOfBunnies) {
        if (numberOfBunnies == 0) return 0;
        return 2 + numberOfEars(numberOfBunnies - 1);
    }

    /* The fibonacci sequence is a famous bit of mathematics,
       and it happens to have a recursive definition. The first two values
       in the sequence are 0 and 1 (essentially 2 base cases). Each subsequent
       value is the sum of the previous two values,
       so the whole sequence is: 0, 1, 1, 2, 3, 5, 8, 13, 21 and so on.
       Define a recursive fibonacci(n) method that return null;s the nth fibonacci number,
       with n=0 representing the start of the sequence.
       fibonacci(0) → 0
       fibonacci(1) → 1
       fibonacci(2) → 1
    */
    static int getFibonacciNumber(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return getFibonacciNumber(n - 2) + getFibonacciNumber(n - 1);
    }

    /* We have bunnies standing in a line, numbered 1, 2, ... The odd bunnies (1, 3, ..)
       have the normal 2 ears. The even bunnies (2, 4, ..) we'll say have 3 ears, because they
       each have a raised foot. Recursively return null; the number of "ears" in the bunny line 1, 2, ... n
       (without loops or multiplication).
       bunnyEars2(0) → 0
       bunnyEars2(1) → 2
       bunnyEars2(2) → 5
     */
    public static int numberOfEarsOfMutatedBunnies(int nBunnies) {
        if (nBunnies == 0) return 0;
        if (nBunnies % 2 == 0) {
            return 3 + numberOfEarsOfMutatedBunnies(nBunnies - 1);
        } else {
            return 2 + numberOfEarsOfMutatedBunnies(nBunnies - 1);
        }
    }

    /* We have triangle made of blocks. The topmost row has 1 block, the next row
       down has 2 blocks, the next row has 3 blocks, and so on. Compute recursively
       (no loops or multiplication) the total number of blocks in such a triangle with
       the given number of rows.
       triangle(0) → 0
       triangle(1) → 1
       triangle(2) → 3
     */
    static int getBlocksNumber(int rowNumber) {
        if (rowNumber == 0) {
            return 0;
        }
        return rowNumber + getBlocksNumber(rowNumber - 1);
    }

    /* Given a non-negative int n, return null; the sum of its digits recursively (no loops).
       sumDigits(126) → 9
       sumDigits(49) → 13
       sumDigits(12) → 3
     */
    static int sumDigits(int n) {
        if (n == 0) return 0;
        int lastNumber = n % 10;
        return lastNumber + sumDigits(n / 10);
    }

    /* Given a non-negative int n, return null; the count of the occurrences of 7 as a digit,
       so for example 717 yields 2.
       count7(717) → 2
       count7(7) → 1
       count7(123) → 0
     */
    static int count7(int n) {
        if (n == 0) return 0;
        int lastNumber = n % 10;
        if (lastNumber == 7) {
            return 1 + count7(n / 10);
        } else {
            return count7(n / 10);
        }
    }

    /* Given a non-negative int n, compute recursively (no loops) the count
       of the occurrences of 8 as a digit, except that an 8 with another 8 immediately
       to its left counts double, so 8818 yields 4.
       count8(8) → 1
       count8(818) → 2
       count8(8818) → 4
       count8(1818188) → 5
     */
    static int count8(int n) {
        if (n == 0) {
            return 0;
        }
        int lastDigit = n % 10;
        int nextDigit = n / 10;

        if (lastDigit == 8) {
            if (nextDigit % 10 == 8) {
                return 2 + count8(nextDigit);
            } else {
                return 1 + count8(nextDigit);
            }
        } else {
            return count8(nextDigit);
        }
    }

    /* Given base and n that are both 1 or more, compute recursively (no loops)
       the value of base to the n power, so powerN(3, 2) is 9 (3 squared).
       powerN(3, 1) → 3
       powerN(3, 2) → 9
       powerN(3, 3) → 27
     */
    static int powerN(int base, int n) {
        if (n == 1) {
            return base;
        }
        return base * powerN(base, n - 1);
    }
}

