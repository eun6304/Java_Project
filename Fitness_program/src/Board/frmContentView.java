package Board;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import DB.Sql;
import ExternalLib.Delphi;
import Main.frmLogin;
import Main.frmMainTab;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class frmContentView {
	public static Sql sql = frmLogin.sql;
	
	private JFrame frame;
	private static JLabel lbTitle;
	private JScrollPane scroll;
	private JScrollPane scroll_2;
	private static JComboBox comComment;
	
	public static boolean isShowed = false;
	public static String contentIndex;
	private JPanel pnCommentList;
	private static String commentBuf[][];
	private static JTextArea txtListComment;
	private JPanel pnContent;
	private static JTextArea txtContent;
	private JButton btnEditContent;
	private JButton btnRemoveContent;
	private static boolean isSelfContent;
	private JComboBox comboBox;
	private static JComboBox comImgList;
	private JButton btnShowImg;

	/**
	 * Create the application.
	 */
	public frmContentView(String contentIndex) {
		initialize();
		frame.setVisible(true);
		isShowed = true;
		this.contentIndex = contentIndex;
		isSelfContent = getContent(contentIndex);
		getComment(contentIndex);
		
		txtContent.setEditable(isSelfContent);
		btnEditContent.setEnabled(isSelfContent);
		btnEditContent.setVisible(isSelfContent);
		btnRemoveContent.setEnabled(isSelfContent);
		btnRemoveContent.setVisible(isSelfContent);	
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isShowed = false;
			}
		});
		frame.setBounds(100, 100, 464, 633);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		

		
		JButton btnRecordComment = new JButton("코멘트 작성");
		btnRecordComment.setEnabled(false);
		btnRecordComment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnRecordComment.getText().equals("코멘트 작성")) {
					if (txtListComment.getText().length() > 0 && recordBoardComment(contentIndex, txtListComment.getText(), "") > 0) {
						getComment(contentIndex);
					} else {
						Delphi.lib.INSTANCE.MessageBox("필드에 값을 입력해 주세요.", Delphi.lib.MB_ICONEXCLAMATION);
					}
				} else {
					removeBoardComment(contentIndex, commentBuf[comComment.getSelectedIndex()][3].substring(2, 19));
					Delphi.lib.INSTANCE.MessageBox("댓글이 삭제되었습니다.", Delphi.lib.MB_ICONEXCLAMATION);
					getComment(contentIndex);
					
				}
			}
		});
		btnRecordComment.setBounds(277, 495, 113, 33);
		frame.getContentPane().add(btnRecordComment);
		
		lbTitle = new JLabel("lbTitle");
		lbTitle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, lbTitle.getText(), "", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		lbTitle.setToolTipText("");
		lbTitle.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		lbTitle.setBounds(51, 44, 339, 25);
		frame.getContentPane().add(lbTitle);
		
		comImgList = new JComboBox();
		comImgList.setBounds(51, 280, 252, 23);
		frame.getContentPane().add(comImgList);
		
		comComment = new JComboBox();
		comComment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 콤보박스 댓글 업데이트
				int selIndex = comComment.getSelectedIndex();	
				boolean isRecordMode = comComment.getSelectedItem() != "[코멘트 작성]";
				txtListComment.setEditable(!isRecordMode);
				btnRecordComment.setEnabled(!isRecordMode);
				btnRecordComment.setText("코멘트 작성");
				
				if (selIndex >= 0 && comComment.getSelectedItem() != "코멘트가 존재하지 않습니다.") {	
										
					if (isRecordMode) {		
					    boolean isSelfComment = commentBuf[selIndex][1].equals(frmLogin.userId);
						btnRecordComment.setEnabled(isSelfComment);
						if (isSelfComment) {
							btnRecordComment.setText("코멘트 삭제");
						}
						txtListComment.setText(commentBuf[selIndex][2]);
						
					
					}else {
						txtListComment.setText("");	
					}
				
				}
		
			}
		});
		comComment.setMaximumRowCount(100);
		comComment.setBounds(51, 327, 339, 25);
		frame.getContentPane().add(comComment);
		
		pnCommentList = new JPanel();
		pnCommentList.setBounds(51, 351, 339, 138);
		frame.getContentPane().add(pnCommentList);
		pnCommentList.setLayout(new GridLayout(0, 1, 0, 0));
		
		txtListComment = new JTextArea();
		txtListComment.setBackground(Color.WHITE);
		txtListComment.setEditable(false);
		txtListComment.setWrapStyleWord(true);
		txtListComment.setLineWrap(true);
	    scroll = new JScrollPane(txtListComment);
		pnCommentList.add(scroll);
		
		pnContent = new JPanel();
		pnContent.setBounds(51, 82, 339, 199);
		frame.getContentPane().add(pnContent);
		pnContent.setLayout(new GridLayout(0, 1, 0, 0));
		
		txtContent = new JTextArea();
		txtContent.setLineWrap(true);
		txtContent.setWrapStyleWord(true);
		txtContent.setBackground(Color.WHITE);
		scroll_2 = new JScrollPane(txtContent);
		pnContent.add(scroll_2);
		
		btnEditContent = new JButton("글수정");
		btnEditContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtContent.getText().length() > 0 && editBoardContent(contentIndex, txtContent.getText(), "") > 0) {
					Delphi.lib.INSTANCE.MessageBox("글 수정 완료", Delphi.lib.MB_ICONASTERISK);
					isShowed = false;
					frame.dispose();
				} else {
					Delphi.lib.INSTANCE.MessageBox("필드에 값을 입력해 주세요.", Delphi.lib.MB_ICONEXCLAMATION);
				}
			}
		});

		btnEditContent.setBounds(51, 495, 92, 33);
		frame.getContentPane().add(btnEditContent);
		
		btnRemoveContent = new JButton("글삭제");
		btnRemoveContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (removeBoardContent(contentIndex) > 0) {
					// 업로드된 이미지가 잇으면 삭제
					while (comImgList.getItemCount() > 0) {
						Delphi.lib.INSTANCE.idHTTPGet(String.format("asdasdasd/remove.php?fname=con[%s].%s", contentIndex, comImgList.getItemAt(0)));
						comImgList.removeItemAt(0);
					}
					Delphi.lib.INSTANCE.MessageBox("글 삭제 완료", Delphi.lib.MB_ICONASTERISK);
					frmMainTab.updateBoard();
//					frmLobby.updateBoard();
					isShowed = false;
					frame.dispose();
				}
			}
		});
		btnRemoveContent.setBounds(149, 495, 92, 33);
		frame.getContentPane().add(btnRemoveContent);
		
		btnShowImg = new JButton("보기");
		btnShowImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comImgList.getItemCount() > 0) {
					Delphi.lib.INSTANCE.ShellExecute(String.format("asdasd/uploads/con[%s].%s", contentIndex, comImgList.getSelectedItem().toString()));
				}else {
					Delphi.lib.INSTANCE.MessageBox("이미지가 없습니다.", Delphi.lib.MB_ICONEXCLAMATION);
				}
			}
		});
		btnShowImg.setBounds(301, 279, 89, 24);
		frame.getContentPane().add(btnShowImg);
	}
	
	public static boolean getContent(String contentIndex) {
		sql.query = String.format("select * from board where idx = %s order by idx desc", contentIndex);

		String buf[][] = sql.executeQuery(1, 6);
		lbTitle.setText(String.format("%s : %s", buf[0][1], buf[0][2]));
		txtContent.setText(buf[0][3]);
		if (buf[0][4] != null) {
			String imgs[] = buf[0][4].split("/");
			for (int i = 0; i < imgs.length; i++)
				comImgList.addItem(imgs[i]);
		}
	
		return buf[0][1].equals(frmLogin.userId);
	}
	
	
	// 댓글(코멘트) 불러오기
	public static void getComment(String contentIndex) {
		comComment.removeAllItems();
		sql.query = String.format("select * from board_comment where idx = %s order by record_date desc", contentIndex);
		commentBuf = sql.executeQuery(1, 4);
		if (commentBuf.length > 0) {
			for (int i = 0; i < commentBuf.length; i++) {
				comComment.addItem(String.format("[%s] %s", commentBuf[i][3].substring(2, 19), commentBuf[i][1]));
			}
		}else {
			comComment.addItem("코멘트가 존재하지 않습니다.");
		}
		comComment.addItem("[코멘트 작성]");
	}
	
	// 댓글(코멘트) 작성
	public static int recordBoardComment(String index, String content, String imgURL) {
		sql.query = String.format("insert into board_comment(idx, id, content) values(%s, '%s', '%s')", index, frmLogin.userId, content, imgURL);
		return sql.executeUpdate();
	}
	
	// 댓글(코멘트) 삭제
	public static int removeBoardComment(String contentIndex, String date) {
		sql.query = String.format("delete from board_comment where idx = %s and to_char(record_date, 'YY-MM-DD HH24:MI:SS') = '%s'", contentIndex, date);
		return sql.executeUpdate();
	}
	
	
	// 게시물 수정
	public static int editBoardContent(String contentIndex, String content, String imgURL) {
		sql.query = String.format("update board set content = '%s', img_url = '%s' where idx = %s", content, imgURL, contentIndex);
		return sql.executeUpdate();
	}
	
	// 게시물 삭제
	public static int removeBoardContent(String contentIndex) {
		// 자식
		sql.query = String.format("delete from board_comment where idx = %s", contentIndex);
		sql.executeUpdate();
		// 부모
		sql.query = String.format("delete from board where idx = %s", contentIndex);
		return sql.executeUpdate();
	}
}
