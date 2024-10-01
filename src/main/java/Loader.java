import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Loader extends DefaultListCellRenderer {
    public static Font LoadFont(String fontPath, float size) throws IOException, FontFormatException {
        try (InputStream is = Loader.class.getResourceAsStream(fontPath)) {
            if (is == null) {
                System.err.println("Font not found: " + fontPath);
                return null;
            }
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            return customFont;
        } catch (IOException | FontFormatException e) {
            System.err.println("Error loading font: " + e.getMessage());
            return null;
        }
    }

    public static String LoadAnnouncement() {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL("https://docs.chenlvin.cc/infor/music-downloader/announcement.txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
            } else {
                return "公告获取失败，HTTP 响应码: " + connection.getResponseCode();
            }
        } catch (IOException e) {
            return "公告获取失败： " + e.getMessage();
        }
        return content.toString();
    }


    private static final int LINE_HEIGHT = 30; // 行高
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        try {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Font HYWH = Loader.LoadFont("/font/HYWH-85W.ttf", 16);
            label.setFont(HYWH);
            label.setPreferredSize(new Dimension(0, LINE_HEIGHT));
            label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            // 分隔线
            if (index < list.getModel().getSize() - 1) {
                label.setBorder(BorderFactory.createCompoundBorder(
                        label.getBorder(),
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY)
                ));
            } else {
                label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            }
            return label;

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }


}