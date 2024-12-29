
package project;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class test_con {
    public static void main(String[] args) throws Exception {
        System.out.println("starting");
        Socket s = new Socket("localhost", 3000);
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out.println("GET /echo?m=a HTTP/1.1");
        out.println("");
    }
}
