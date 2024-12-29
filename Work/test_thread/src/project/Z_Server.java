package project;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

public class Z_Server {

    public static void main(String[] args) {
        ServerSocket server = null;
        Socket client = null;
        File folder = new File("C:\\Users\\suchin\\Videos\\Screen Recordings");
        try {
            server = new ServerSocket(3000);
            System.out.println("Server on port 3000");
            //
            client = server.accept();
            System.out.println("Client connected.");
            //
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            //
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
            //
            String filename = in.readLine();
            System.out.println("file: " + filename);
            File file = new File(folder + "\\" + filename);
            //
            if (file.exists() && file.isFile()) {
                out.println("DOWNLOAD_READY");
                //
                String zero = in.readLine();
                //
                long start = System.currentTimeMillis();
                //
                if (zero.equalsIgnoreCase("y")) {
                    //System.out.println("y");
                    //
                    FileChannel source = new FileInputStream(file).getChannel();
                    WritableByteChannel destination = Channels.newChannel(client.getOutputStream());
                    try {
                        //source.transferTo(0, source.size(), destination);
                        long count_size = 0;
                        long size;
                        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                        long fileSize = file.length();/*source.size();*/
                        dos.writeLong(fileSize);
                        while ((size = source.transferTo(count_size, fileSize - count_size, destination)) > 0) {
                            count_size += size;
                        }
                        dos.close();
                    } finally {
                        if (destination != null) {
                            destination.close();
                        }
                        if (source != null) {
                            source.close();
                        }
                    }
                    //client.shutdownOutput();
                } else {
                    //System.out.println("n");
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
                    try {
                        byte[] buffer = new byte[65536];
                        int size;
                        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                        long fileSize = file.length();
                        dos.writeLong(fileSize);
                        while (/*true*/(size = bis.read(buffer)) != -1) {
                            //size = bis.read(buffer);
                            bos.write(buffer, 0, size);
                            /*if (size < buffer.length) {
                                break;
                            }*/
                            //System.out.print(size+"\n");
                        }
                        bos.flush();
                        dos.close();
                        //
                    } finally {
                        bis.close();
                        bos.close();
                    }
                    //client.shutdownOutput();
                }
                //
                System.out.println("COMPLETE: " + filename);
                //
                long end = System.currentTimeMillis();
                long time = end - start;
                System.out.println("Time " + time + " millisecond");
            } else {
                System.out.println("FILE_NOT_FOUND");
                out.println("FILE_NOT_FOUND");
            }
            //
            /*String clientResponse = in.readLine();
            if ("RECEIVED".equals(clientResponse)) {
                System.out.println("Client confirmed file receipt. Closing connection.");
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //client.close();
                //server.close();
                if (client != null && !client.isClosed()) {
                    client.close();
                }
                if (server != null && !server.isClosed()) {
                    server.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
