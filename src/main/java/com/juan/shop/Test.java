package com.juan.shop;

import java.io.*;

/**
 * @author guanhuan_li
 */
public class Test {

    public static void copyFile(String target, String source) throws IOException {

        InputStream input = new FileInputStream(source);
        OutputStream output = new FileOutputStream(target);


        byte[] buff = new byte[2048];

        int len;
        try {
            while ((len = input.read(buff)) != -1) {
                output.write(buff, 0, len);
            }
        } finally {

            input.close();
            output.flush();
            output.close();
        }
    }

    public static void main(String[] args) {
        File f = new File("/Users/guanhuan/Documents/web/juan/理赔");
        show(f);
    }

    public static void show(File f) {

        if (!f.isDirectory()) {
            return;
        }

        File[] files = f.listFiles();
        for (File temp : files) {
            if (temp.isFile()) {
                System.out.println(temp.getPath());
            }
            if (f.isDirectory()) {
                show(temp);
            }
        }
    }

    public static int count(String file, String str) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        int count = 0;
        String currentStr;
        while ((currentStr = bufferedReader.readLine())!= null) {
            String[] s = currentStr.split(" ");
            for (String temp : s) {
                if (str.equals(temp)) {
                    count++;
                }
            }
        }
        return count;
    }

}
