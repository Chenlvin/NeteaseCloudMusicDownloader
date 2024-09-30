import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Login extends JFrame {
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JCheckBox rememberMe;

    public Login() throws Exception {

// DEBUG
//        if(true) {
//            String[] loginInfo = loadLoginInfo();
//            String cookie = loginInfo[2];
//            System.out.println("isLogged: " + ApiClient.checkLoginStatus(cookie));
//            new MusicDownloader(cookie);
//            dispose();
//            return;
//        }

        setTitle("登录网易云音乐账号");
        setResizable(false);
        setSize(500, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 加载登录信息
        String[] loginInfo = loadLoginInfo();
        String cookie = loginInfo[2];

        if (cookie != null && ApiClient.checkLoginStatus(cookie)) {
            new MusicDownloader(cookie);
            dispose();
            return;
        }

        JLabel phoneLabel = new JLabel("手机号");
        phoneLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        phoneLabel.setBounds(50, 20, 100, 30);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setFont(new Font("微软雅黑", Font.BOLD, 16));
        phoneField.setBounds(150, 20, 300, 30);
        add(phoneField);

        JLabel passwordLabel = new JLabel("密码");
        passwordLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        passwordLabel.setBounds(50, 70, 100, 30);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("微软雅黑", Font.BOLD, 16));
        passwordField.setBounds(150, 70, 300, 30);
        add(passwordField);

        rememberMe = new JCheckBox("记住登录信息");
        rememberMe.setFont(new Font("微软雅黑", Font.BOLD, 16));
        rememberMe.setBounds(150, 110, 200, 30);
        add(rememberMe);

        loginButton = new JButton("登录");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        loginButton.setBounds(200, 150, 100, 30);
        add(loginButton);

        JTextField notice = new JTextField(Constant.Notice);
        notice.setEditable(false);
        notice.setFont(new Font("微软雅黑", Font.BOLD, 12));
        notice.setBounds(50, 200, 400, 30);
        notice.setBorder(null);
        notice.setHorizontalAlignment(JTextField.CENTER);
        add(notice);

        if (loginInfo[0] != null && loginInfo[1] != null) {
            phoneField.setText(loginInfo[0]);
            passwordField.setText(loginInfo[1]);
            rememberMe.setSelected(true);
        }

        loginButton.addActionListener(e -> {
            String phone = phoneField.getText();
            String password = new String(passwordField.getPassword());
            String newCookie = ApiClient.login(phone, password);
            if (newCookie != null) {
                if (rememberMe.isSelected()) {
                    saveLoginInfo(phone, password, newCookie);
                } else {
                    clearLoginInfo();
                }
                try {
                    new MusicDownloader(newCookie);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "登录失败，请检查您的手机号和密码。");
            }
        });

        setVisible(true);
    }

    private boolean checkLoginInfo() {
        File file = new File("login_info.txt");
        return file.exists() && file.length() > 0;  // 检查文件是否存在且不为空
    }

    private void saveLoginInfo(String phone, String password, String cookie) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("login_info.txt"))) {
            writer.write(phone);
            writer.newLine();
            writer.write(password);
            writer.newLine();
            writer.write(cookie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] loadLoginInfo() {
        String[] loginInfo = new String[3];
        try (BufferedReader reader = new BufferedReader(new FileReader("login_info.txt"))) {
            loginInfo[0] = reader.readLine();
            loginInfo[1] = reader.readLine();
            loginInfo[2] = reader.readLine();
        } catch (IOException e) {}
        return loginInfo;
    }

    private void clearLoginInfo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("login_info.txt"))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Loader.LoadFont();
        new Login();
    }
}
