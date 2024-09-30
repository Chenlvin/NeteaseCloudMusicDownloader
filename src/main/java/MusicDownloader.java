import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class MusicDownloader extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JList<Song> songList;
    private DefaultListModel<Song> songListModel;
    private String cookie;

    public MusicDownloader(String cookie) throws Exception {
        this.cookie = cookie;
        setTitle(Constant.AppName + " " + Constant.AppVersion + "  " + "@" + Constant.AppAuthor);
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        songListModel = new DefaultListModel<>();
        songList = new JList<>(songListModel);
        songList.setCellRenderer(new Loader()); // 自定义渲染器
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 搜索框
        searchField = new JTextField();
        searchField.setBounds(50, 30, 370, 30);
        searchField.setFont(new Font("微软雅黑", Font.BOLD, 16));
        add(searchField);

        // 搜索按钮
        searchButton = new JButton("搜索");
        searchButton.setBounds(450, 30, 100, 30); // 设置位置和大小
        searchButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                List<Song> songs = ApiClient.searchSongs(keyword, cookie);
                songListModel.clear();
                for (Song song : songs) {
                    songListModel.addElement(song);
                }
            }
        });
        add(searchButton);

        // 歌曲列表
        JScrollPane scrollPane = new JScrollPane(songList);
        scrollPane.setBounds(50, 90, 500, 500);
        add(scrollPane);

        // 下载按钮
        JButton downloadButton = new JButton("下载");
        downloadButton.setBounds(250, 620, 100, 30);
        downloadButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Song selectedSong = songList.getSelectedValue();
                if (selectedSong != null) {
                    String downloadUrl = null;
                    try {
                        downloadUrl = ApiClient.getDownloadUrl(selectedSong.getId(), cookie);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    FileDownloader.downloadFile(downloadUrl, Function.sanitizeFileName(selectedSong.getName() + ".mp3"));
                } else {
                    JOptionPane.showMessageDialog(null, "请选择一首歌曲进行下载");
                }
            }
        });
        add(downloadButton);

        //公告
        JTextArea notice = new JTextArea(Loader.LoadAnnouncement());
        notice.setEditable(false);
        notice.setFont(new Font("微软雅黑", Font.BOLD, 14));
        notice.setBounds(50, 680, 500, 50);
        add(notice);

        setVisible(true);
    }
}
