package udp.network.utilities;

/**
 * Hash password. This code utilizes jBCrypt.
 */

public class HashPassword {
    // Define the BCrypt workload to use when generating password hashes. 10-31 is a valid value.
    private final static int workload = 10;

    /**
     * This method can be used to generate a string representing an account password
     * suitable for storing in a database. It will be an OpenBSD-style crypt(3) formatted
     * hash string of length=60
     * The bcrypt workload is specified in the above static variable, a value from 10 to 31.
     * A workload of 12 is a very reasonable safe default as of 2013.
     * This automatically handles secure 128-bit salt generation and storage within the hash.
     * @param password_plaintext The account's plaintext password as provided during account creation,
     *			     or when changing an account's password.
     * @return String - a string of length 60 that is the bcrypt hashed password in crypt(3) format.
     */
    public static String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(workload);

        return BCrypt.hashpw(password_plaintext, salt);
    }

    /**
     * This method can be used to verify a computed hash from a plaintext (e.g. during a login
     * request) with that of a stored hash from a database. The password hash from the database
     * must be passed as the second variable.
     * @param password_plaintext The account's plaintext password, as provided during a login request
     * @param stored_hash The account's stored password hash, retrieved from the authorization database
     * @return boolean - true if the password matches the password of the stored hash, false otherwise
     */
    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified;

        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return password_verified;
    }

    /**
     * A simple test case for the main method, verify that a pre-generated test hash verifies successfully
     * for the password it represents, and also generate a new hash and ensure that the new hash verifies
     * just the same.
     */
    public static void main(String[] args) {
//        String test_passwd = "abcdefghijklmnopqrstuvwxyz";
//        String test_hash = "$2a$06$.rCVZVOThsIa97pEDOxvGuRRgzG64bvtJ0938xuqzv18d3ZpQhstC";
//
//        System.out.println("Testing BCrypt Password hashing and verification");
//        System.out.println("Test password: " + test_passwd);
//        System.out.println("Test stored hash: " + test_hash);
//        System.out.println("Hashing test password...");
//        System.out.println();
//
//        String computed_hash = hashPassword(test_passwd);
//        System.out.println("Test computed hash: " + computed_hash);
//        System.out.println();
//        System.out.println("Verifying that hash and stored hash both match for the test password...");
//        System.out.println();
//
//        String compare_test = checkPassword(test_passwd, test_hash)
//                ? "Passwords Match" : "Passwords do not match";
//        String compare_computed = checkPassword(test_passwd, computed_hash)
//                ? "Passwords Match" : "Passwords do not match";
//
//        System.out.println("Verify against stored hash:   " + compare_test);
//        System.out.println("Verify against computed hash: " + compare_computed);

//        System.out.println(hashPassword("hobune"));
        // $2a$10$caAf6MWpsIexIa68/rPFouVr52jQm20krm7fTJg235BXTHINvwXD2
        // $2a$10$LX3SXBWpmj3iL1HoeoIvjO5zEdNmz7cnhT6T/aPjZ7g1FV/F1UZTi
//        System.out.println(checkPassword("fa", "$2a$10$LX3SXBWpmj3iL1HoeoIvjO5zEdNmz7cnhT6T/aPjZ7g1FV/F1UZTi"));
    }

}