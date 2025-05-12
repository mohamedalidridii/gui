package tests;

import java.io.IOException;

public class TestPython {
    public static void main(String[] args) {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "python/dreamdestination.py");

            pb.directory(new java.io.File(System.getProperty("user.dir")));

            pb.inheritIO();

            Process process = pb.start();

            int exitCode = process.waitFor();
            System.out.println("Python script finished with exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
