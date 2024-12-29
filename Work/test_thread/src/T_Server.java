import java.io.*;
import java.net.*;

public class T_Server {

    public static void main(String[] args) {
        ServerSocket server = null;
        Socket client = null;
        File folder = new File("C:\\Users\\suchin\\Downloads\\OS");
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
            if (server != null && !server.isClosed()) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null && !client.isClosed()) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            ){
            System.out.println("Client connected");
            String message;
            while (true) {
                message = in.readLine();
                System.out.println("client: " + message);
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("client disconnected.");
                    break;
                } else if ("file".equalsIgnoreCase(message)) {
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
                } else if ("download".equalsIgnoreCase(message)) {
                    String filename = in.readLine();
                    System.out.println("file: " + filename);
                    //String filePath = folder+"\\"+filename;
                    File file = new File(folder + "\\" + filename);
                    //System.out.println("path: " +file);
                    if (file.exists() && file.isFile()) {
                        out.println("DOWNLOAD_READY");
                        sendFile(file);
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
        } finally {
            try {
                if (client != null && !client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendFile(File file) {
        try (
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fileInputStream);
            BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
            ){
            byte[] buffer = new byte[4096];
            int bytesRead;
            //bytesRead = bis.read(buffer);
            while (/*(bytesRead = bis.read(buffer)) != -1*/true) {
                bytesRead = bis.read(buffer);
                System.out.println("read: " + bytesRead);
                if(bytesRead == -1){
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
/*private void sendFile(File file, OutputStream outputStream) {
        int numThreads = 4; // กำหนดจำนวน Thread ที่ต้องการใช้
        long fileSize = file.length();
        long partSize = fileSize / numThreads;

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            long startByte = i * partSize;
            long endByte = (i == numThreads - 1) ? fileSize : (startByte + partSize);

            threads[i] = new Thread(new FileSender(file, outputStream, startByte, endByte));
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class FileSender implements Runnable {
    private File file;
    private OutputStream outputStream;
    private long startByte;
    private long endByte;

    public FileSender(File file, OutputStream outputStream, long startByte, long endByte) {
        this.file = file;
        this.outputStream = outputStream;
        this.startByte = startByte;
        this.endByte = endByte;
    }

    @Override
    public void run() {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {

            raf.seek(startByte);
            byte[] buffer = new byte[4096];
            long bytesToRead = endByte - startByte;
            int bytesRead;
            while (bytesToRead > 0 && (bytesRead = raf.read(buffer, 0, (int) Math.min(buffer.length, bytesToRead))) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
                bytesToRead -= bytesRead;
            }
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/

/*
Thread stopServerThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (isRunning) {
                System.out.println("Type 'STOP' to stop the server.");
                String command = scanner.nextLine();
                if ("STOP".equalsIgnoreCase(command)) {
                    isRunning = false;
                    try {
                        if (server != null && !server.isClosed()) {
                            server.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Server is stopping...");
                }
            }
            scanner.close();
        });

        stopServerThread.start();
*/