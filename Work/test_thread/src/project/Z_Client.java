package project;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;

public class Z_Client {

    public static void main(String[] args) {
        Socket server = null;
        try {
            server = new Socket("localhost", 3000);
            System.out.println("Connected to server");
            //
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            PrintWriter out = new PrintWriter(server.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            int c_dl = 0;
            //
            File folder = new File("C:\\Users\\suchin\\Downloads");
            //
            String file;
            while (true) {
                file = in.readLine();
                if ("END_OF_LIST".equals(file)) {
                    break;
                }
                System.out.println(file);
            }
            //
            System.out.println("file name: ");
            String filename = scanner.nextLine();
            out.println(filename);
            //
            String response = in.readLine();
            if ("DOWNLOAD_READY".equalsIgnoreCase(response)) {
                System.out.println(response);
                //
                System.out.println("download for zerocopy? \n y/n");
                String zero = scanner.nextLine();
                out.println(zero);
                //
                for (File files : folder.listFiles()) {
                    if (files.isFile()) {
                        String M_file = files.getName().substring(0, files.getName().indexOf("."));
                        if (M_file.indexOf("(") != -1) {
                            M_file = M_file.substring(0, M_file.indexOf("("));
                        }
                        if (M_file.equals(filename.substring(0, filename.indexOf(".")))) {
                            c_dl += 1;
                        }
                    }
                }
                if (c_dl != 0) {
                    filename = filename.substring(0, filename.indexOf(".")) + "(" + c_dl + ")" + filename.substring(filename.indexOf("."));
                }
                //
                long start = System.currentTimeMillis();
                //
                if (zero.equalsIgnoreCase("y")) {
                    //System.out.println("y");
                    //
                    FileChannel destination = new FileOutputStream(folder + "\\" + filename).getChannel();
                    ReadableByteChannel source = Channels.newChannel(server.getInputStream());
                    try {
                        //destination.transferFrom(source, 0, Long.MAX_VALUE);
                        long count_size = 0;
                        long size;
                        DataInputStream dis = new DataInputStream(server.getInputStream());
                        long fileSize = dis.readLong();
                        while ((size = destination.transferFrom(source, count_size, fileSize - count_size)) > 0) {
                            count_size += size;
                        }
                        dis.close();
                    } finally {
                        if (destination != null) {
                            destination.close();
                        }
                        if (source != null) {
                            source.close();
                        }
                    }
                } else {
                    //System.out.println("n");
                    FileOutputStream fos = new FileOutputStream(folder + "\\" + filename);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    try {
                        byte[] buffer = new byte[65536];
                        int size;
                        DataInputStream dis = new DataInputStream(server.getInputStream());
                        long fileSize = dis.readLong();
                        long count_size = 0;
                        while (/*true*/(size = server.getInputStream().read(buffer)) /*> 0*/!= -1) {
                            //size = server.getInputStream().read(buffer);
                            bos.write(buffer, 0, size);
                            count_size+=size;
                            if(count_size==fileSize){
                                break;
                            }
                            /*if (size < buffer.length) {
                                break;
                            }*/
                            //System.out.print(size+"\n");
                        }
                        bos.flush();
                        dis.close();
                        //
                    } finally {
                        fos.close();
                        bos.close();
                    }
                }
                //
                System.out.println("Download complete.");
                //
                long end = System.currentTimeMillis();
                long time = end - start;
                System.out.println("Time " + time + " millisecond");
            } else {
                System.out.println(response);
            }
            //
            //out.println("RECEIVED");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //server.close();
                if (server != null && !server.isClosed()) {
                    server.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
