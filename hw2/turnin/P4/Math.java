import java.util.ArrayList;

public class Math {

    
    public static ArrayList<Integer> get_factors(int n) {
        ArrayList<Integer> factors = new ArrayList<Integer>();
        for (int i = 1; i <= n/2; i++) {
            if (n % i == 0) {
                factors.add(i);
            }
        }
        return factors;
    }

    //returns all multiples of n which are <= limit
    public static ArrayList<Integer> get_multiples (int n, int limit) {
        ArrayList<Integer> multiples = new ArrayList<Integer>();
        int i = n;
        while (i <= limit) {
            multiples.add(i);
            i += n;
        }
        return multiples;
    }

    public static boolean is_prime(int n) {
        //by definition
        if (n == 1) {
            return false;
        }
        for (int i = 2; i*i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
