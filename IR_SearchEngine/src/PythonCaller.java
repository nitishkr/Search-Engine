
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.File;

public class PythonCaller{

public static String token, stemmed_token ;	
	
public static String getStemmed_token() {
	return stemmed_token;
}

public static void setToken(String token) {
	PythonCaller.token = token;
}

	public static void caller( ) {
        try{
           System.out.println("I'm in python caller");
            ProcessBuilder builder = new ProcessBuilder("python", "/Users/Admin/Desktop/lemma.py", token);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
           // System.out.println(reader.lines() + " hoo");
            stemmed_token = "";
            String line;
            while ((line = reader.readLine ()) != null) {
            	stemmed_token = line;
               // System.out.println (token +" -> " +line);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
	
	
}

