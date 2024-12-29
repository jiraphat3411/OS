package project;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        Socket server = null;
        try{
            server = new Socket("localhost", 3000);
            System.out.println("Connected to server");
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            PrintWriter out = new PrintWriter(server.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            String message;
            while (true) {
                int c_dl = 0;
                System.out.println("Enter messages to server:");
                message = scanner.nextLine();
                out.println(message);
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("client disconnected.");
                    break;
                } else if ("file".equalsIgnoreCase(message)) {
                    String file;
                    //int count = 0;
                    while (true) {
                        file = in.readLine();
                        if ("END_OF_LIST".equals(file)) {
                            /*if(count==0){
                                System.out.println("Not file for download.");
                            }*/
                            break;
                        }
                        System.out.println(file);
                        //count++;
                    }
                } else if ("download".equalsIgnoreCase(message)) {
                    File folder = new File("C:\\Users\\suchin\\Downloads\\");
                    System.out.println("file name: ");
                    String filename = scanner.nextLine();
                    out.println(filename);
                    String response = in.readLine();
                    if ("DOWNLOAD_READY".equalsIgnoreCase(response)) {
                        for (File files : folder.listFiles()) {
                            if (files.isFile()) {
                                String file = files.getName().substring(0, files.getName().indexOf("."));
                                //System.out.println("file: "+file);
                                if(file.indexOf("(")!=-1){
                                    file = file.substring(0, file.indexOf("("));
                                }
                                //System.out.println("file: "+file);
                                if(file.equals(filename.substring(0, filename.indexOf(".")))){
                                    c_dl+=1;
                                }
                                //System.out.println("c_dl: "+c_dl);
                            }
                        }
                        if(c_dl!=0){
                            filename = filename.substring(0, filename.indexOf("."))+"("+c_dl+")"+filename.substring(filename.indexOf("."));
                        }
                        //filename = filename.substring(0, filename.indexOf(".")+1)+"("+c_dl+")"+filename.substring(filename.indexOf("."));
                        FileOutputStream fos = new FileOutputStream("C:\\Users\\suchin\\Downloads\\" + filename);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        byte[] buffer = new byte[65536];
                        int size;
                        while(true){
                            size = server.getInputStream().read(buffer);                            
                            bos.write(buffer, 0, size);
                            //System.out.println("size:"+size);
                            if (size < buffer.length) {
                                break;
                            }
                        }
                        bos.flush();
                        //Arrays.fill(buffer, (byte) 0);
                        fos.close();
                        bos.close();
                        System.out.println("Download complete.");
                    } else {
                        System.out.println("File not found.");
                    }
                } else {
                    System.out.println("Server: " + in.readLine());
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
