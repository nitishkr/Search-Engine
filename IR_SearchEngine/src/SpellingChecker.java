import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.File;

public class SpellingChecker {


public static String token,correct_token ;	
	
public static String get_correct_spelling_token() {
	return correct_token;
}

public static void setToken(String token) {
	PythonCaller.token = token;
}

public static void caller( ) {
        try{
           System.out.println("I'm in spelling checker ");
           System.out.println(token );
            ProcessBuilder builder = new ProcessBuilder("python", "/Users/Admin/Desktop/spell.py", token);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
           // System.out.println(reader.lines() + " hoo");
            correct_token = "";
            String line;
            while ((line = reader.readLine ()) != null) {
            	correct_token = line;
               // System.out.println (token +" -> " +line);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
public static void main(String args[])
{
	SpellingChecker.token = "The runing dog is good";
	SpellingChecker.caller();
	System.out.println(SpellingChecker.get_correct_spelling_token());
}
}
