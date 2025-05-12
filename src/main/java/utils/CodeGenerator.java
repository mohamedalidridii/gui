package utils;

import java.util.Random;

public class CodeGenerator {

    public static String generateCode() {
        Random rand = new Random();
        int code=100000+rand.nextInt(900000);
        return String.valueOf(code);
    }
}
