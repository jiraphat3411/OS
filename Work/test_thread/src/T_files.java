import java.io.File;

public class T_files {
    public static void main(String[] args) {
        // กำหนดไดเรกทอรีที่ต้องการเรียกดูไฟล์
        String directoryPath = "C:\\Users\\suchin\\Videos\\Captures"; // เปลี่ยน path นี้ให้เป็นไดเรกทอรีที่คุณต้องการดู
        
        // สร้าง object File สำหรับไดเรกทอรีที่กำหนด
        File directory = new File(directoryPath);
        
        // ตรวจสอบว่าเป็นไดเรกทอรีและมีอยู่จริง
        if (directory.exists() && directory.isDirectory()) {
            // รับรายการไฟล์และไดเรกทอรีภายในไดเรกทอรีนี้
            File[] filesList = directory.listFiles();
            
            System.out.println("Files in directory " + directoryPath + ":");
            for (File file : filesList) {
                if (file.isFile()) {
                    System.out.println("File: " + file.getName());
                } else if (file.isDirectory()) {
                    System.out.println("Directory: " + file.getName());
                }
            }
        } else {
            System.out.println("The specified path is not a directory or does not exist.");
        }
    }
}

