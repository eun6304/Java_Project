package Main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import DB.Sql;
import ExternalLib.Delphi;

public class frmLogin {

	private JFrame frame;
	private JTextField txtID;
	private JPasswordField txtPW;
	
	public static Sql sql = new Sql("test", "test");
	public static String userId;
	public static String userName;
	private JLabel lblmg;
	private JLabel lbLogin;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(new FlatLightLaf());
					frmLogin window = new frmLogin();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public frmLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setForeground(Color.BLACK);
		frame.setBackground(Color.BLACK);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				sql.disConnect();
			}
		});
		frame.setBounds(100, 100, 994, 676);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lbRegister = new JLabel("REGISTER");
		lbRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lbRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (!frmRegister.isShowed)
					new frmRegister();
			}
		});
		lbRegister.setFont(new Font("굴림", Font.PLAIN, 14));
		lbRegister.setBounds(603, 431, 81, 23);
		frame.getContentPane().add(lbRegister);
		
		txtID = new JTextField();
		txtID.setFont(new Font("굴림", Font.PLAIN, 25));
		txtID.setBorder(BorderFactory.createEmptyBorder());
		txtID.setBounds(342, 230, 316, 47);
		frame.getContentPane().add(txtID);
		txtID.setColumns(10);
		
		txtPW = new JPasswordField();
		txtPW.setFont(new Font("굴림", Font.PLAIN, 25));
		txtPW.setBorder(BorderFactory.createEmptyBorder());
		txtPW.setBounds(342, 301, 316, 47);
		frame.getContentPane().add(txtPW);
		
		ImageIcon loginUI = frmMainTab.getImageToIcon("img/ui.jpg", 994, 647);
		System.out.println("dasdasd");
		lbLogin = new JLabel("");
		lbLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String id = txtID.getText();
				String pw = txtPW.getText();
				if (id.length() > 0 && pw.length() > 0) {	
					sql.query = String.format("select * from member where id = '%s' and pw = '%s'", id, pw);
					String buf[][] = sql.executeQuery(1, 3);

					if (buf.length > 0) {
						userId = buf[0][0];
						userName = buf[0][2];
						new frmMainTab();
						frame.dispose();
					}else {
						Delphi.lib.INSTANCE.MessageBox("아이디 또는 비밀번호 오류입니다.", Delphi.lib.MB_ICONEXCLAMATION);
					}
				}else {
					Delphi.lib.INSTANCE.MessageBox("텍스트필드에 값을 입력해주세요.", Delphi.lib.MB_ICONEXCLAMATION);
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lbLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			}
		});
		lbLogin.setBounds(322, 358, 350, 65);
		frame.getContentPane().add(lbLogin);
		
//		ImageIcon loginUI2 = frmMainTab.getImageToIcon("img/button.jpg", 355, 61);
//		lbLogin.setIcon(loginUI2);
//		
		lblmg = new JLabel("");
		lblmg.setVerticalAlignment(SwingConstants.TOP);
		lblmg.setBounds(0, 0, 1280, 805);
		frame.getContentPane().add(lblmg);
		lblmg.setIcon(loginUI);
		
	}
}
