package Board;

import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import DB.Sql;
import ExternalLib.Delphi;
import Main.frmLogin;
import Main.frmMainTab;
import Test.dellibTest;

public class frmRecordContent {
	public static Sql sql = frmLogin.sql;
	private JFrame frame;
	private JComboBox comImg;
	private JTextField txtTitle;
	private static int curContentIndex;
	
	public static boolean isShowed = false;

	/**
	 * Create the application.
	 */
	public frmRecordContent() {
		initialize();
		frame.setVisible(true);
		isShowed = true;
		if (curContentIndex == 0) 
			curContentIndex = getCurrentContentIndex();
		//System.out.println(curContentIndex);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {	
				// 이미지첨부 후 글 작성하지 않고 폼을 닫았다면 서버에 업로드 된 이미지 삭제
				while (comImg.getItemCount() > 0) {
					Delphi.lib.INSTANCE.idHTTPGet(String.format("ㅁㄴㅇㅁㄴㅇㅁㄴㅇ/remove.php?fname=con[%s].%s", curContentIndex, comImg.getItemAt(0)));
					comImg.removeItemAt(0);
				}
				isShowed = false;
			}
		});
		frame.setBounds(100, 100, 592, 614);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		
		txtTitle = new JTextField();
		txtTitle.setBounds(41, 43, 491, 32);
		frame.getContentPane().add(txtTitle);
		txtTitle.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBounds(41, 83, 491, 378);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JTextArea txtContent = new JTextArea();
		txtContent.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(txtContent);
		scroll.setBounds(0, 0, 491, 378);
		panel.add(scroll);
		
		comImg = new JComboBox();
		comImg.setBounds(41, 471, 210, 23);
		frame.getContentPane().add(comImg);
		
		JButton btnUploadImg = new JButton("이미지 첨부");
		btnUploadImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String file = Delphi.lib.INSTANCE.ShowUploadForm(String.valueOf(curContentIndex));
				if (!file.equals("nil"))
					comImg.addItem(file);
			}
		});
		btnUploadImg.setBounds(41, 504, 102, 32);
		frame.getContentPane().add(btnUploadImg);
		
		JButton btnSubmit = new JButton("제출");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = txtTitle.getText();
				String content = txtContent.getText();
				String urls = "";
				if (comImg.getItemCount() > 0 ) {
					for (int i = 0; i < comImg.getItemCount(); i++) {
						urls += comImg.getItemAt(i).toString() + "/";
					}
					//System.out.println(urls);
				}

				if (title.length() > 0 && content.length() > 0 && recordBoard(title, content, urls) > 0) {

					isShowed = false;
					curContentIndex = 0;
//					frmLobby.updateBoard();
					frmMainTab.updateBoard();
					frame.dispose();
				} else {
					Delphi.lib.INSTANCE.MessageBox("필드에 값을 입력해주세요.", Delphi.lib.MB_ICONEXCLAMATION);
				}
			}
		});
		btnSubmit.setBounds(379, 471, 153, 65);
		frame.getContentPane().add(btnSubmit);
		
		JButton btnRemoveImg = new JButton("취소");
		btnRemoveImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comImg.getItemCount() > 0) {
					Delphi.lib.INSTANCE.idHTTPGet(String.format("ㅇㅁㄴㅇㅁㄴㅇ/remove.php?fname=con[%s].%s", curContentIndex, comImg.getSelectedItem()));
					comImg.removeItemAt(comImg.getSelectedIndex());
				}
			}
		});
		btnRemoveImg.setBounds(149, 504, 102, 32);
		frame.getContentPane().add(btnRemoveImg);
	}
	
	public static int recordBoard(String title, String content, String imgURL) {
		sql.query = String.format("insert into board(idx, id, title, content, img_url) values(seq_content_index.nextval, '%s', '%s', '%s', '%s')", frmLogin.userId, title, content, imgURL);
		return sql.executeUpdate();
	}
	
	
	public static int getCurrentContentIndex() {
        sql.query = "select seq_content_index.nextval+1 from dual";
        String buf[][] = sql.executeQuery(1, 1); 
        return Integer.parseInt(buf[0][0]);
        
	}
}
