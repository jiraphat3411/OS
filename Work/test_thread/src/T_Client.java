import java.io.*;
import java.net.*;
import java.util.Scanner;

public class T_Client {
    public static void main(String[] args) {
        Socket server = null;
        try {
            server = new Socket("localhost", 3000);
            System.out.println("Connected to server");
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            PrintWriter out = new PrintWriter(server.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            //BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String message;
            while(server != null){
                System.out.println("Enter messages to server:");
                message = scanner.nextLine();
                out.println(message);
                if("exit".equalsIgnoreCase(message)){
                    System.out.println("client disconnected.");
                    break;
                }else if("file".equalsIgnoreCase(message)){
                    String file;
                    while (true) {
                        file = in.readLine();
                        if ("END_OF_LIST".equals(file)) {
                            break;
                        }
                        System.out.println(file);
                    }
                }else if("download".equalsIgnoreCase(message)){
                    System.out.println("file name: ");
                    String filename = scanner.nextLine();
                    out.println(filename);
                    String response = in.readLine();
                    if ("DOWNLOAD_READY".equalsIgnoreCase(response)) {
                        receiveFile(filename,server.getInputStream());
                        System.out.println("Download complete.");
                    } else {
                        System.out.println("File not found.");
                    }
                }else{
                    System.out.println("Server: " + in.readLine());
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                server.close();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void receiveFile(String filename,InputStream inputserver) {
        try {
            BufferedInputStream bis = new BufferedInputStream(inputserver);
            FileOutputStream safefile = new FileOutputStream("C:\\Users\\suchin\\Downloads\\" + filename); 
            BufferedOutputStream bos = new BufferedOutputStream(safefile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1/*true*/) {
                /*bytesRead = bis.read(buffer);
                if(bytesRead == -1){
                    break;
                }*/
                if (bytesRead < buffer.length) {
                                break;
                }
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
