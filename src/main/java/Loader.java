import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Loader extends DefaultListCellRenderer {
    public static void LoadFont() throws IOException, FontFormatException {
        InputStream is = Loader.class.getResourceAsStream("/font/HarmonyOS_Sans_SC_Regular.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
        font = font.deriveFont(Font.PLAIN, 12);
        System.out.println("Font Loaded: " + font.getName());
    }

    public static String LoadAnnouncement() throws IOException {
        URL url = new URL("https://docs.chenlvin.cc/infor/music-downloader/announcement.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)); // 指定UTF-8编码
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();

        return content.toString();
    }

    private static final int LINE_HEIGHT = 30; // 行高
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setFont(new Font("HarmonyOS Sans SC", Font.BOLD, 18));
        label.setPreferredSize(new Dimension(0, LINE_HEIGHT)); // 设置行高
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
    }


}