package org.hibernate.tutorials;

public class Fibonacci {
    public static void main(String[] args) {
        System.out.println(getFibonacci(11));
    }

    private static int getFibonacci(int number) {
        if (number == 1 || number == 2) {
            return 1;
        }
        return getFibonacci(number - 2) + getFibonacci(number - 1);
    }
}
