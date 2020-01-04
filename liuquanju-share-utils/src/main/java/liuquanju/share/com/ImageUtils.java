package liuquanju.share.com;

import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Objects;

public class ImageUtils {

    public static String imageToBase64(String imgPath) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        String encode = encoder.encode(Objects.requireNonNull(data));
        return encode;
    }


    public static boolean generateImage(String imgStr, String imgFilePath) {
        // 图像数据为空
        if (imgStr == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean bytes2Image(byte[] bytes, String imgFilePath) {
        // 图像数据为空
        if (bytes == null) {
            return false;
        }
        try {
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据指定大小压缩图片
     *
     * @param base64String  源图片base64字符串
     * @param desFileSize 指定图片大小，单位kb
     * @param imageId     影像编号
     * @return 压缩质量后的图片字节数组
     */

    public static byte[] compressPicForScale(String base64String, long desFileSize, String imageId) {

        BASE64Decoder decoder = new BASE64Decoder();
        // Base64解码
        byte[] imageBytes = null;
        try {
            imageBytes = decoder.decodeBuffer(base64String);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageBytes == null || imageBytes.length <= 0 || imageBytes.length < desFileSize * 1024) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / 1024);
        try {
            while (imageBytes.length > desFileSize * 1024) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
            System.out.println("【图片压缩】imageId={" + imageId + "} | 图片原大小={" + srcSize / 1024 + "}kb | 压缩后大小={" + imageBytes.length / 1024 + "}kb");
        } catch (Exception e) {
            System.out.println("【图片压缩】msg=图片压缩失败!" + e.toString());
        }
        return imageBytes;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

    public static void main(String[] args) {
        String base64Str = ImageUtils.imageToBase64("I:\\205221.jpg");
        System.out.println(base64Str);
        byte[] compressBytes = ImageUtils.compressPicForScale(base64Str, 200, "compress1");
        ImageUtils.bytes2Image(compressBytes, "I:\\20522.jpg");
//        ImageUtils.generateImage(base64Str, "I:\\1.jpg");
    }

}
