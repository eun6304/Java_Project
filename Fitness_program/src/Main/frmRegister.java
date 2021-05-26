package Main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;

import DB.Sql;
import ExternalLib.Delphi;

import javax.swing.ButtonGroup;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;

public class frmRegister {

	private JFrame frame;
	public static boolean isShowed = false;
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	private final ButtonGroup buttonGroup_3 = new ButtonGroup();
	/**
	 * Launch the application.
	 */


	/**
	 * Create the application.
	 */
	public frmRegister() {
		initialize();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		isShowed = true;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("굴림", Font.PLAIN, 13));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isShowed = false;
			}
		});
		frame.setBounds(100, 100, 606, 537);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("아이디");
		lblNewLabel.setBounds(90, 53, 44, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblUsePw = new JLabel("비밀번호");
		lblUsePw.setBounds(78, 96, 57, 15);
		frame.getContentPane().add(lblUsePw);
		
		JLabel lblUserPw = new JLabel("비밀번호 확인");
		lblUserPw.setBounds(50, 139, 89, 15);
		frame.getContentPane().add(lblUserPw);
		
		JLabel lblName = new JLabel("이름");
		lblName.setBounds(101, 182, 32, 15);
		frame.getContentPane().add(lblName);
		
		JLabel lblGender = new JLabel("성별");
		lblGender.setBounds(101, 311, 44, 15);
		frame.getContentPane().add(lblGender);
		
		JLabel lblAge = new JLabel("나이");
		lblAge.setBounds(101, 225, 32, 15);
		frame.getContentPane().add(lblAge);
		
		JLabel lblMail = new JLabel("연락처");
		lblMail.setBounds(90, 268, 44, 15);
		frame.getContentPane().add(lblMail);
		
		JLabel lblNewLabel_1 = new JLabel("운동 경력");
		lblNewLabel_1.setFont(new Font("굴림", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(420, 53, 89, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		JRadioButton rdLv2 = new JRadioButton("3~6 개월");
		buttonGroup_2.add(rdLv2);
		rdLv2.setBounds(420, 101, 121, 23);
		frame.getContentPane().add(rdLv2);
		
		JRadioButton rdLv1 = new JRadioButton("3개월 이하");
		rdLv1.setSelected(true);
		buttonGroup_2.add(rdLv1);
		rdLv1.setBounds(420, 76, 121, 23);
		frame.getContentPane().add(rdLv1);
		
		JRadioButton rdLv3 = new JRadioButton("1년 이상");
		buttonGroup_2.add(rdLv3);
		rdLv3.setBounds(420, 125, 121, 23);
		frame.getContentPane().add(rdLv3);
		
		JRadioButton rdLv4 = new JRadioButton("3년 이상");
		buttonGroup_2.add(rdLv4);
		rdLv4.setBounds(420, 150, 121, 23);
		frame.getContentPane().add(rdLv4);
		
		JLabel lblNewLabel_1_1 = new JLabel("운동 목적");
		lblNewLabel_1_1.setFont(new Font("굴림", Font.PLAIN, 12));
		lblNewLabel_1_1.setBounds(420, 206, 108, 15);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JTextPane txtPW = new JTextPane();
		txtPW.setBounds(150, 94, 182, 21);
		frame.getContentPane().add(txtPW);
		
		JTextPane txtPWCheck = new JTextPane();
		txtPWCheck.setBounds(150, 137, 182, 21);
		frame.getContentPane().add(txtPWCheck);
		
		JTextPane txtName = new JTextPane();
		txtName.setBounds(150, 180, 182, 21);
		frame.getContentPane().add(txtName);
		
		JTextPane txtPhone = new JTextPane();
		txtPhone.setBounds(150, 266, 182, 21);
		frame.getContentPane().add(txtPhone);
		
		JTextPane txtID = new JTextPane();
		txtID.setBounds(150, 51, 182, 21);
		frame.getContentPane().add(txtID);

		JRadioButton rdGenderMan = new JRadioButton("남자");
		rdGenderMan.setSelected(true);
		buttonGroup_1.add(rdGenderMan);
		rdGenderMan.setBounds(150, 309, 57, 23);
		frame.getContentPane().add(rdGenderMan);
		
		JRadioButton rdGenderWoman = new JRadioButton("여자");
		buttonGroup_1.add(rdGenderWoman);
		rdGenderWoman.setBounds(213, 309, 57, 23);
		frame.getContentPane().add(rdGenderWoman);
		
		JRadioButton rdTarget1 = new JRadioButton("다이어트");
		buttonGroup_3.add(rdTarget1);
		rdTarget1.setSelected(true);
		rdTarget1.setBounds(420, 229, 121, 23);
		frame.getContentPane().add(rdTarget1);
		
		JRadioButton rdTarget2 = new JRadioButton("멸치 탈출");
		buttonGroup_3.add(rdTarget2);
		rdTarget2.setBounds(420, 254, 121, 23);
		frame.getContentPane().add(rdTarget2);
		
		JRadioButton rdTarget3 = new JRadioButton("바디 프로필");
		buttonGroup_3.add(rdTarget3);
		rdTarget3.setBounds(420, 279, 121, 23);
		frame.getContentPane().add(rdTarget3);
		
		JComboBox comAge = new JComboBox();
		comAge.setFont(new Font("굴림", Font.PLAIN, 12));
		comAge.setBounds(150, 223, 182, 23);
		frame.getContentPane().add(comAge);
		for (int i = 14; i <= 70; i++) {
			comAge.addItem(i);
		}
		
		JButton btnNewButton = new JButton("REGISTER");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sql sql = frmLogin.sql;
				// click
				String id = txtID.getText().replace(" ", "");			
				String pw = txtPW.getText().replace(" ", "");
				String pwCheck = txtPWCheck.getText();
				String name = txtName.getText().replace(" ", "");
				String gender = rdGenderMan.isSelected() ? "M" : "W";
				String age = comAge.getSelectedItem().toString().replace(" ", "");
				String phoneNumber = txtPhone.getText().replace(" ", "");
			
				int healthLevel = rdLv1.isSelected() ? 1 : rdLv2.isSelected() ? 2 : rdLv3.isSelected() ? 3 : 4;
				System.out.println(healthLevel);
				
				int healthTarget = rdTarget1.isSelected() ? 1 : rdTarget2.isSelected() ? 2 : 3;
				System.out.println(healthTarget);
				
								
				
				if (id.length() > 0 && pw.length() > 0 && pwCheck.length() > 0 && name.length() > 0 && age.length() > 0 && phoneNumber.length() > 0) 	
				{
					if (id.length() >= 3) 
					{
						if (pw.length() >= 4 && pwCheck.length() >= 4) 
						{
							if (pw.equals(pwCheck)) 
							{	
								sql.query = String.format("select id from member where id = '%s'", id);					
								if (sql.executeUpdate() == 0) {
									sql.query = String.format("insert into member (id, pw, name, gender, age, phone_number, lv, tar) values('%s', '%s', '%s', '%s', %s, '%s', %s, %s)",
																id, pw, name, gender, age, phoneNumber, healthLevel, healthTarget);
									if (sql.executeUpdate() > 0) {
										Delphi.lib.INSTANCE.MessageBox("가입 성공", Delphi.lib.MB_ICONASTERISK);
										isShowed = false;
										frame.dispose();
									}else {
										Delphi.lib.INSTANCE.MessageBox("가입 실패", Delphi.lib.MB_ICONEXCLAMATION);
									}		
									
								}else {
									Delphi.lib.INSTANCE.MessageBox("이미 존재하는 아이디 입니다.", Delphi.lib.MB_ICONEXCLAMATION);
								}			
							}else {
								Delphi.lib.INSTANCE.MessageBox("비밀번호가 서로 일치하지 않습니다.", Delphi.lib.MB_ICONEXCLAMATION);
							}
							
						}else {
							Delphi.lib.INSTANCE.MessageBox("비밀번호는 4글자 이상이여야 합니다.", Delphi.lib.MB_ICONEXCLAMATION);
						}
					}else {
						Delphi.lib.INSTANCE.MessageBox("아이디는 3글자 이상이여야 합니다.", Delphi.lib.MB_ICONEXCLAMATION);
					}
				}else {
					Delphi.lib.INSTANCE.MessageBox("입력 값이 없는 필드가 존재합니다.", Delphi.lib.MB_ICONEXCLAMATION);
				}
			}
		});
		btnNewButton.setBounds(50, 397, 491, 54);
		frame.getContentPane().add(btnNewButton);
		
		
		
	}
}
