public class Function {
    public static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[\\\\/:*?\"<>|]", ""); // 替换非法字符
    }
}
