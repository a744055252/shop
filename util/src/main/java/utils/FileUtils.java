package utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liguanhuan
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    private static final AtomicInteger fileIdGen = new AtomicInteger(10000);

    public static void uploadFile(byte[] file, String filePath, String fileName) throws FileNotFoundException {

        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        try {
            out.write(file);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getFileSuffix(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        int index = path.lastIndexOf(File.separator);
        String fileName;
        if (index == -1) {
            fileName = path;
        } else {
            fileName = path.substring(index+1);
        }

        int index1 = fileName.lastIndexOf(".");
        String suffix = "";
        if (index1 > 0) {
            suffix = fileName.substring(index1+1);
        }

        return suffix;
    }

    public static String getExtName(File file) {
        String name = file.getName();
        int i = name.lastIndexOf(".");
        String result = "";
        if (i > 0) {
            result = name.substring(i + 1);
        }
        return result;
    }

    public static String getNewFileName(String orignalName) {
        String ext = FilenameUtils.getExtension(orignalName);
        String nowStr = DateUtils.format(new Date(), "yyyyMMddHHmmss");
        return nowStr + "_" + getFileNumPer() + "." + ext;
    }

    private static int getFileNumPer() {
        int id = fileIdGen.get();
        if (id > 99999) {
            synchronized(fileIdGen) {
                id = fileIdGen.get();
                if (id > 99999) {
                    fileIdGen.set(0);
                }
            }
        }

        return fileIdGen.incrementAndGet();
    }

    public static String getFilePath(HttpServletRequest request, String outFile) {
        String ctxPath = request.getSession().getServletContext().getRealPath("") + File.separator + "WEB-INF" + File.separator + outFile;
        File target = new File(ctxPath);
        if (!target.exists()) {
            target.mkdirs();
        }

        return ctxPath;
    }


    public static void main(String[] args) {
        System.out.println(getFileSuffix("1223/21312/213.2/213/1231sfd.23.32"));
    }


}
