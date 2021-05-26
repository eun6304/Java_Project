package Board;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import DB.Sql;
import Main.frmLogin;

import java.awt.CardLayout;

public class frmLobby {

	private JFrame frame;
	public static Sql sql = frmLogin.sql;
	private static String bdBuffer[][];
	private JButton btnUpdateBoard;
	private static JTable tblBoard;
	
	private static JLabel lbPageState;
	private static int currentPage = 0;
	private static int lastPage;

	/**
	 * Launch the application.
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(new FlatLightLaf());
					frmLobby window = new frmLobby();
					window.frame.setVisible(true);
			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
		});
	}
	 */


	public frmLobby() {
		initialize();
		updateBoard();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle(frmLogin.userId);
		frame.setBounds(100, 100, 1094, 1017);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel pnBoardLobby = new JPanel();
		pnBoardLobby.setBounds(40, 40, 629, 373);
		frame.getContentPane().add(pnBoardLobby);
		pnBoardLobby.setLayout(null);
		
		JButton btnPrev = new JButton("<");
		btnPrev.setBounds(0, 317, 62, 35);
		pnBoardLobby.add(btnPrev);
		
		JButton btnNext = new JButton(">");
		btnNext.setBounds(67, 317, 62, 35);
		pnBoardLobby.add(btnNext);
		
		btnUpdateBoard = new JButton("새로고침");
		btnUpdateBoard.setBounds(134, 317, 129, 35);
		pnBoardLobby.add(btnUpdateBoard);
		
		JButton btnRecordContent = new JButton("글작성");
		btnRecordContent.setBounds(481, 317, 130, 35);
		pnBoardLobby.add(btnRecordContent);
		
		JPanel pnBoardView = new JPanel();
		pnBoardView.setBounds(0, 30, 611, 277);
		pnBoardLobby.add(pnBoardView);
		
		tblBoard = new JTable();
		tblBoard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!frmContentView.isShowed) {
					int row = tblBoard.getSelectedRow() + currentPage * 10;
					if (row < bdBuffer.length && e.getClickCount() > 1) {
						// [row][0] -> 게시물 인덱스
						new frmContentView(bdBuffer[row][0]);
					}
				}
			}
		});
		tblBoard.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
			},
			new String[] {
				"\uC81C\uBAA9", "\uC791\uC131\uC790", "\uC791\uC131\uC77C"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		});
		tblBoard.getColumnModel().getColumn(0).setPreferredWidth(171);
		tblBoard.getColumnModel().getColumn(0).setMinWidth(40);
		tblBoard.getColumnModel().getColumn(1).setPreferredWidth(15);
		tblBoard.getColumnModel().getColumn(2).setPreferredWidth(16);
		pnBoardView.setLayout(null);
		JScrollPane scroll = new JScrollPane(tblBoard);
		scroll.setBounds(0, 0, 611, 278);
		tblBoard.setRowHeight(25);
		tblBoard.setFont(new Font("굴림", Font.PLAIN, 14));
		pnBoardView.add(scroll);
		
	    lbPageState = new JLabel("1/1");
	    lbPageState.setBounds(0, 10, 84, 15);
	    pnBoardLobby.add(lbPageState);
		btnRecordContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!frmRecordContent.isShowed)
					new frmRecordContent();
			}
	
		});
		btnUpdateBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateBoard();
			}
		});
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (bdBuffer.length > (currentPage+1) * 10) {
					currentPage++;
					updateBoard();
				}

			}
		});
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentPage > 0) {
					currentPage--;
					updateBoard();
				}
			}
		});
	}
	
	
	public static void updateBoard() {
		sql.query = "select * from board order by idx desc";
	    bdBuffer = sql.executeQuery(1, 6);
	    int rowCount = bdBuffer.length;

	    lastPage = 0;
	    if (rowCount > 0) {
		    int tmp = bdBuffer.length;
		    while (tmp > 0 ) {
		    	tmp -= 10;
		    	++lastPage;
		    }
	    }else {
	    	++lastPage;
	    }
		    	
		for (int i = 0; i <= 9; i++) {

			int pageRow = i + currentPage * 10;

			if (pageRow < rowCount) {
				tblBoard.setValueAt(bdBuffer[pageRow][2], i, 0); // 제목
				tblBoard.setValueAt(bdBuffer[pageRow][1], i, 1); // 아이디
				tblBoard.setValueAt(bdBuffer[pageRow][5].substring(2, 16), i, 2); // 작성일
			}else {
				for (int j = 0; j <= 2; j++)
					tblBoard.setValueAt("", i, j); 
			}
		}
		lbPageState.setText(String.format("%s/%s", currentPage+1, lastPage));

	}
}
