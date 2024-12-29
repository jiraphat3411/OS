package project;
import java.io.*;
import java.net.*;
//import java.util.*;

public class Server {

    public static void main(String[] args) {
        ServerSocket server = null;
        Socket client = null;
        File folder = new File("C:\\Users\\suchin\\Videos\\Screen Recordings");
        try {
            server = new ServerSocket(3000);
            System.out.println("Server on port 3000");
            while (true) {
                client = server.accept();
                new ClientControler(client, folder).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                server.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class ClientControler extends Thread {

    private Socket client;
    private File folder;

    public ClientControler(Socket client, File folder) {
        this.client = client;
        this.folder = folder;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            System.out.println("Client connected");
            String message;
            while (true) {
                message = in.readLine();
                System.out.println("client: " + message);
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("client disconnected.");
                    break;
                } else if ("file".equalsIgnoreCase(message)) {
                    //new FileList(client, folder).start();
                    fileList();
                } else if ("download".equalsIgnoreCase(message)) {
                    String filename = in.readLine();
                    System.out.println("file: " + filename);
                    File file = new File(folder + "\\" + filename);
                    if (file.exists() && file.isFile()) {
                        out.println("DOWNLOAD_READY");
                        //new SendFile(client, file).start();
                        //out.println(file.length());
                        sendFile(file);
                        //new SendFileControler(client, file).start();
                        //out.println("END_OF_FILE");
                        System.out.println("COMPLETE: " + filename);
                    } else {
                        System.out.println("FILE_NOT_FOUND");
                        out.println("FILE_NOT_FOUND");
                    }
                } else {
                    out.println("?");
                }
            }
        } catch (IOException e) {
            System.out.println("Connection lost with client.");
            e.printStackTrace();
        }
    }
    
    public void fileList(){
        try{
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                for (File file : files) {
                    if (file.isFile()) {
                        out.println("File: " + file.getName());
                    }
                }
                out.println("END_OF_LIST");
            } else {
                out.println("Not folder.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void sendFile(File file){
        try{
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
            byte[] buffer = new byte[65536];
            int size;
            while(true){
                size = bis.read(buffer);
                bos.write(buffer, 0, size);
                if (size < buffer.length) {
                    break;
                }
            }
            bos.flush();
            fis.close();
            bis.close();
            //bos.close();
            //Arrays.fill(buffer, (byte) 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*class FileList extends Thread {

    private Socket client;
    private File folder;

    public FileList(Socket client, File folder) {
        this.client = client;
        this.folder = folder;
    }

    @Override
    public void run() {
        try{
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                for (File file : files) {
                    if (file.isFile()) {
                        out.println("File: " + file.getName());
                    } else if (file.isDirectory()) {
                        out.println("Folder: " + file.getName());
                    }
                }
                out.println("END_OF_LIST");
            } else {
                out.println("Not folder.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/

/*class SendFile extends Thread {
    private Socket client;
    private File file;

    public SendFile(Socket client, File file) {
        this.client = client;
        this.file = file;
    }

    @Override
    public void run() {
        try{
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
            byte[] buffer = new byte[65536];
            int size;
            while(true){
                size = bis.read(buffer);
                //System.out.println("size:"+size);
                bos.write(buffer, 0, size);
                if (size < buffer.length) {
                    break;
                }
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/

/*class SendFileControler extends Thread {
    private Socket client;
    private File file;
    private int thread;
    private int size;

    public SendFileControler(Socket client, File file) {
        this.client = client;
        this.file = file;
        this.thread = 10;
        this.size = (int)(this.file.length())/this.thread;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.thread; i++) {
            int start = i*this.size;
            int end = start+this.size;
            new SendFile(this.client,this.file,start,end).start();
        }
    }
}

class SendFile extends Thread {
    private Socket client;
    private File file;
    private int start;
    private int end;

    public SendFile(Socket client, File file, int start, int end) {
        this.client = client;
        this.file = file;
        this.start = start;
        this.end = end;
    }
    @Override
    public void run() {
        try{
            RandomAccessFile raf = new RandomAccessFile(this.file, "r");
            raf.seek(this.start);
            BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
            byte[] buffer = new byte[65536];
            int size;
            int maxread = end - start;
            while(true){
                size = raf.read(buffer,0,Math.min(buffer.length,maxread));
                bos.write(buffer, 0, size);
                if (size < buffer.length) {
                    break;
                }
                maxread -= size;
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/