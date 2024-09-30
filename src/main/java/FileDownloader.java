import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {
    public static void downloadFile(String fileUrl, String defaultFileName) {
        // FileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择保存位置");
        fileChooser.setFont(new Font("微软雅黑", Font.BOLD, 16));
        fileChooser.setSelectedFile(new File(defaultFileName)); // 默认文件名

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            String savePath = saveFile.getAbsolutePath();

            // 进度窗口
            JDialog progressDialog = new JDialog();
            progressDialog.setTitle("下载进度");
            progressDialog.setSize(300, 100);
            progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            progressDialog.setLocationRelativeTo(null);

            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true); // 设置为不确定状态
            progressDialog.add(progressBar, BorderLayout.CENTER);
            progressDialog.setVisible(true); // 显示进度窗口

            // 线程下载
            new Thread(() -> {
                try {
                    URL url = new URL(fileUrl);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    int responseCode = httpConn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = httpConn.getInputStream();
                        FileOutputStream outputStream = new FileOutputStream(savePath);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        long totalBytesRead = 0;
                        int fileLength = httpConn.getContentLength(); // 获取文件长度

                        // 读取数据并写入文件
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;
                            // 更新进度条
                            int progress = (int) ((totalBytesRead * 100) / fileLength);
                            progressBar.setIndeterminate(false); // 切换为确定状态
                            progressBar.setValue(progress);
                        }

                        outputStream.close();
                        inputStream.close();
                        progressDialog.dispose(); // 关闭进度窗口
                        JOptionPane.showMessageDialog(null, "文件已成功下载到: " + savePath);
                    } else {
                        progressDialog.dispose();
                        JOptionPane.showMessageDialog(null, "无法下载文件，HTTP 响应码: " + responseCode);
                    }
                    httpConn.disconnect();
                } catch (IOException ex) {
                    progressDialog.dispose();
                    JOptionPane.showMessageDialog(null, "下载出错，请尝试重新登录账号");
                }
            }).start();
        } else {
            JOptionPane.showMessageDialog(null, "下载已取消");
        }
    }
}
