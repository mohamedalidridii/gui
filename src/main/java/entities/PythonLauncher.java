package entities;
import java.io.*;



public class PythonLauncher {
    public static void launchPython() {

        new Thread(()  -> {
            try
            {
                String pythonScriptPath="Python/app.py";
                ProcessBuilder pb=new ProcessBuilder("python",pythonScriptPath);
                pb.redirectErrorStream(true);
                Process process= pb.start();



                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while((line =reader.readLine()) !=null){
                    System.out.println(line);
                }
                int exitCode=process.waitFor();
                System.out.println("Exited with code : "+exitCode);

            }
            catch (IOException |InterruptedException e){
                e.printStackTrace();
            }
        }).start();

    }
}
