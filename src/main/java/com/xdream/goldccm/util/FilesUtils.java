package com.xdream.goldccm.util;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FilesUtils {


    /**
     * 生成Byte流 TODO
     *
     * @param
     * @return
     * @throws
     * @history
     * @knownBugs
     */
    public static byte[] getBytesFromFile(File file) {
        byte[] ret = null;
        try {
            if (file == null) {
                // log.error("helper:the file is null!");
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (IOException e) {
            // log.error("helper:get bytes from file process error!");
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 把流生成图片 TODO
     *
     * @param
     * @return
     * @throws
     * @history
     * @knownBugs
     */
    public static File getFileFromBytes(byte[] files, String outputFile, String fileName) {
        File ret = null;

        BufferedOutputStream stream = null;
        try {
            if (StringUtils.isBlank(fileName)) {
                ret = new File(outputFile);
            } else {
                ret = new File(outputFile + fileName);
            }


            File fileParent = ret.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            ret.createNewFile();

            FileOutputStream fstream = new FileOutputStream(ret);

            stream = new BufferedOutputStream(fstream);

            stream.write(files);


        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // log.error("helper:get file from byte process error!");
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /***
     * 根据路径获取
     *
     * @param path
     * @return
     */
    public static byte[] getPhoto(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    public static void nioCopy(String inPuth, String outPuth) throws IOException {
        long startTime = System.currentTimeMillis();
        FileChannel inChannel = null;
        FileChannel outChennel = null;
        try {

            inChannel = FileChannel.open(Paths.get(inPuth), StandardOpenOption.READ);

            outChennel = FileChannel.open(Paths.get(outPuth), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

            outChennel.transferFrom(inChannel, 0, inChannel.size());

            long end = System.currentTimeMillis();
            System.out.println("nioCopy耗费时间:" + (end - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inChannel.isOpen()) {
                inChannel.close();
            }
            if (outChennel.isOpen()) {
                outChennel.close();
            }

        }
    }


    public static String ImageToBase64ByLocal(String imgFile) throws Exception {
        InputStream in = null;
        byte[] data = null;

        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);

        } catch (IOException var4) {
            var4.printStackTrace();
        } finally {
            try {
                if (in != null) {

                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}
