import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Register extends JDialog {
    private JTextField tfUserName;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JTextField tfEmail;
    private JButton btnRegister;
    private JButton btnBackToLogin;
    private JPanel registerPanel;
    private Login loginDialog;
    public User user;

    public Register(JFrame parent, Login loginDialog){
        super(parent);
        this.loginDialog = loginDialog;
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(350, 450));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        btnBackToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                loginDialog.setVisible(true);
            }
        });

        // Add ActionListener to the JTextFields
        ActionListener textFieldListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        };

        tfUserName.addActionListener(textFieldListener);
        pfPassword.addActionListener(textFieldListener);
        pfConfirmPassword.addActionListener(textFieldListener);
        tfEmail.addActionListener(textFieldListener);

        setVisible(true);
    }

    private void registerUser() {
        String userName = tfUserName.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmPassword = String.valueOf(pfConfirmPassword.getPassword());
        String email = tfEmail.getText();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(userName, password, email);
        if (user != null) {
            dispose();
        }
    }

    private User addUserToDatabase(String userName, String password, String email){
        User user = null;
        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (userName, password, email) " + "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.userName = userName;
                user.password = password;
                user.email = email;
            }
            stmt.close();
            conn.close();

        } catch(SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this,
                    "해당 계정으로 가입된 유저가 존재합니다.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        } catch(SQLException e){
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args){
        Register reg = new Register(null, null);
        User user = reg.user;

        if(user != null){
            System.out.println("Successful registration of: " + user.userName);
        }
        else
            System.out.println("Registration canceled");
    }
}
