package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Board.frmContentView;
import Board.frmRecordContent;
import DB.Sql;
import ExternalLib.Delphi;
import javax.swing.Icon;

public class frmMainTab {
	public static Sql sql = frmLogin.sql;
	
	private JFrame frame;
	private JTabbedPane tabMain;
	private JPanel subMypage;
	private JPanel subCalendar;
	private JPanel subRoutine;
	private JPanel subBoard;

	private static JTable tblBoard;
	private static JLabel lbPageState;
	private static String bdBuffer[][];
	private static int currentPage = 0;
	private static int lastPage;
	
	private static String routineBuffer[][];
	private static JComboBox comMain;
	private static JComboBox comSub;
	private static JLabel lbRoutineImg;
	
	private JPanel pnCalendar;
	private JPanel pnCalendarSub;

	private static JTable tblCalendar;
	private static Calendar cal = Calendar.getInstance();


	private static int currentYear = cal.get(Calendar.YEAR);
	private static int currentMonth = cal.get(Calendar.MONTH) + 1;
	private static int maxYear = currentYear;
	private static int maxMonth = currentMonth;
	private static JTextArea txtNote;
	private static JTextArea txtHeight;
	private static JTextArea txtWeight;
	private static JTextArea txtFat;
	private static JTextArea txtMuscle;
	private static JLabel lbCurrentMonth;
	public static String[][] calBuffer;
	private String entryDay;
	private String finalDay;

	
	private JScrollPane scroll_1;
	private static JPanel pnChart;

	private static JLabel lbTierPicture;
	private static JLabel lbMemberPicture;
	private static JTextArea txtContent;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel pnContent_1;
	private static JTextArea txtRoutine;
	private static int chartType = 1;



	/**
	 * Create the application.
	 */
	public frmMainTab() {
		initializeMainTab();
		initializeSubMyPage(subMypage);

		initializeSubCalendar(subCalendar);
		initializeSubRoutine(subRoutine);
		
		initializeSubBoard(subBoard);
		frame.setVisible(true);
		
		setBodyValueState();
		initChart(pnChart, "Weight");
		getCalendar(currentMonth);
		updateBoard();
	
	}

	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeMainTab() {
		frame = new JFrame();
		frame.setBounds(100, 100, 617, 618);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		frame.setTitle(String.format("%s님 환영합니다.", frmLogin.userName));
		frame.setLocationRelativeTo(null);
		tabMain = new JTabbedPane(JTabbedPane.TOP);
		tabMain.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Rectangle bounds = frame.getBounds();
				switch (tabMain.getSelectedIndex()) {

					case 0, 1, 2: {
						bounds.height = 625;
						break;	
					}		
					case 3: {
						bounds.height = 450;
					}		
				}
				frame.setBounds(bounds);
			}
		});
		frame.getContentPane().add(tabMain);
		
		// 마이페이지
		subMypage = new JPanel(); 
		tabMain.addTab("마이페이지", null, subMypage, null);
		
		// 캘린더 탭
		subCalendar = new JPanel(); 
		tabMain.addTab("캘린더", null, subCalendar, null);
		subCalendar.setLayout(new GridLayout(0, 1, 0, 0));
				
		// 루틴 탭
		subRoutine = new JPanel(); 
		tabMain.addTab("운동루틴", null, subRoutine, null);
		subRoutine.setLayout(null);
		
		// 게시판 탭
		subBoard = new JPanel(); 
		tabMain.addTab("게시판", null, subBoard, null);
	}
	
	// 마이페이지 -------------------------------------------------------------------
	private void initializeSubMyPage(JPanel subMyPage) {
		subMypage.setLayout(null);
		
		ImageIcon profile = getImageToIcon("img/empty.jpg", 200, 200);
		String[] profileExt = {".jpg", ".png", ".bmp"};

		for (int i = 0; i <= 2; i++) {
			String f = "C:\\JTemp\\SelfImage" + profileExt[i];
			if (Delphi.lib.INSTANCE.ExitsFiles(f)) {
				profile = getImageToIcon(f, 200, 200);
				break;
			}
		}
		
		lbMemberPicture = new JLabel(profile);
		lbMemberPicture.setBackground(Color.WHITE);
		lbMemberPicture.setText("");
		lbMemberPicture.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

			     FileDialog dialog = new FileDialog(new JFrame(), "", FileDialog.LOAD);
			     dialog.setVisible(true);
			     if (dialog.getFile() != null) {
				     int pos = dialog.getFile().lastIndexOf(".");
				     String newProfile = "SelfImage." + dialog.getFile().substring(pos+1);
				     
				     Delphi.lib.INSTANCE.CopyFile(dialog.getDirectory() + dialog.getFile(), newProfile);
				     lbMemberPicture.setIcon(getImageToIcon("C:\\JTemp\\SelfImage." + dialog.getFile().substring(pos+1), 200, 200));
			     }
			}
		});
		
		JRadioButton rdChart2 = new JRadioButton("체지방");
		rdChart2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initChart(pnChart, "Fat");
				chartType = 2;
			}
		});
		
		lbTierPicture = new JLabel((Icon) null);
		lbTierPicture.setVerticalAlignment(SwingConstants.TOP);
		lbTierPicture.setHorizontalAlignment(SwingConstants.LEFT);
		lbTierPicture.setText("");
		lbTierPicture.setBackground(Color.WHITE);
		lbTierPicture.setBounds(170, 10, 40, 40);
		subMypage.add(lbTierPicture);
		
		// 티어계산
		updateTierImage();
		
		buttonGroup.add(rdChart2);
		rdChart2.setBounds(522, 178, 69, 23);
		subMypage.add(rdChart2);
		
		JRadioButton rdChart1 = new JRadioButton("몸무게");
		rdChart1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initChart(pnChart, "Weight");
				chartType = 1;
			}
		});
		rdChart1.setSelected(true);
		buttonGroup.add(rdChart1);
		rdChart1.setBounds(522, 159, 69, 23);
		subMypage.add(rdChart1);
		
		JRadioButton rdChart3 = new JRadioButton("근육량");
		rdChart3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initChart(pnChart, "Muscle");
				chartType = 3;
			}
		});
		buttonGroup.add(rdChart3);
		rdChart3.setBounds(522, 197, 69, 23);
		subMypage.add(rdChart3);
		lbMemberPicture.setBounds(12, 10, 200, 200);
		subMypage.add(lbMemberPicture);
		
		pnChart = new JPanel();
		pnChart.setBounds(0, 220, 605, 320);
		subMypage.add(pnChart);
		
		
		JPanel pnContent = new JPanel();
		pnContent.setBounds(234, 10, 246, 200);
		subMypage.add(pnContent);
		pnContent.setLayout(new GridLayout(0, 1, 0, 0));
		
	    txtContent = new JTextArea();
		txtContent.setWrapStyleWord(true);
		txtContent.setText((String) null);
		txtContent.setLineWrap(true);
		txtContent.setEditable(false);
		txtContent.setBackground(Color.WHITE);
		JScrollPane scroll_2 = new JScrollPane(txtContent);
		pnContent.add(scroll_2);
		
		JButton btnUpdateDate = new JButton("새로고침");
		btnUpdateDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBodyValueState();
				
				switch (chartType) {
					case 1: 
						initChart(pnChart, "Weight");
						break;
					case 2: 
						initChart(pnChart, "Fat");
						break;
					case 3: 
						initChart(pnChart, "Muscle");
				}
				
				updateTierImage();
			}
		});
		btnUpdateDate.setBounds(492, 10, 92, 31);
		subMypage.add(btnUpdateDate);
	}
	
	// 루틴 ----------------------------------------------------------------------------------------------
	private void initializeSubRoutine(JPanel subRoutine) {
		pnContent_1 = new JPanel();
		pnContent_1.setBounds(12, 69, 572, 190);
		subRoutine.add(pnContent_1);
		pnContent_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		txtRoutine = new JTextArea();
		JScrollPane scroll = new JScrollPane(txtRoutine);
		txtRoutine.setWrapStyleWord(true);
		txtRoutine.setText((String) null);
		txtRoutine.setLineWrap(true);
		txtRoutine.setEditable(false);
		txtRoutine.setBackground(Color.WHITE);
		pnContent_1.add(scroll);
		
		comSub = new JComboBox();
		comSub.setBounds(12, 40, 225, 23);
		subRoutine.add(comSub);
		
		
		//
		comMain = new JComboBox();
		comMain.setBounds(12, 12, 225, 23);
		subRoutine.add(comMain);
		comMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSubRoutine();
			}
		});
		comMain.addItem("하체");
		comMain.addItem("가슴");
		comMain.addItem("back");
		comMain.addItem("어깨");
		comMain.addItem("팔");
		comMain.addItem("복근");

		JPanel pnRoutineImg = new JPanel();
		pnRoutineImg.setBounds(12, 269, 572, 271);
		subRoutine.add(pnRoutineImg);
		pnRoutineImg.setLayout(new GridLayout(0, 1, 0, 0));
		
		lbRoutineImg = new JLabel("");
		lbRoutineImg.setHorizontalAlignment(SwingConstants.CENTER);
		pnRoutineImg.add(lbRoutineImg);
		
		comSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSubRoutineInfoText();
	

			}
		});
		updateSubRoutineInfoText();


	}
	
	
	// 캘린더 ---------------------------------------------------------------------------------------------
	private void initializeSubCalendar(JPanel subCalendar) {
		pnCalendar = new JPanel();
		subCalendar.add(pnCalendar);
		pnCalendar.setLayout(null);
		
		pnCalendarSub = new JPanel();
		pnCalendarSub.setBounds(20, 67, 557, 286);
		pnCalendar.add(pnCalendarSub);
		pnCalendarSub.setLayout(new GridLayout(0, 1, 0, 0));
		
		tblCalendar = new JTable();
		tblCalendar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// 캘린더 마우스클릭할 때 이벤트
				txtNote.setText("");
				txtHeight.setText("");
				txtWeight.setText("");
				txtFat.setText("");
				txtMuscle.setText("");
	
	            int row = tblCalendar.getSelectedRow();
	            int col = tblCalendar.getSelectedColumn(); 
	            Object obj = tblCalendar.getValueAt(row, col);
	            
	            if (obj != null) 
	            {
		            int nextDay = Integer.parseInt(String.valueOf(obj).replace("*", "")) + 1;
		            
		            entryDay = String.format("%s/%s/%s", currentYear, currentMonth, String.valueOf(obj)).replace("*", "");
		            finalDay = String.format("%s/%s/%s", currentYear, currentMonth, String.valueOf(nextDay)).replace("*", "");	

		            sql.query = String.format("select * from calendar where record_date between '%s' and '%s' and id='%s'", entryDay, entryDay, frmLogin.userId); 
		            calBuffer = sql.executeQuery(1, 7);
		            //System.out.println(sql.query);
		            
		            if (calBuffer.length > 0) {
		            	if (calBuffer[0][2] != null)
		            		txtNote.setText(calBuffer[0][2]);	
		            	if (calBuffer[0][3] != null)
		            		txtHeight.setText(calBuffer[0][3]);	
		            	if (calBuffer[0][4] != null)
		            		txtWeight.setText(calBuffer[0][4]);	
		            	if (calBuffer[0][5] != null)
		            		txtFat.setText(calBuffer[0][5]);	
		            	if (calBuffer[0][6] != null)
		            		txtMuscle.setText(calBuffer[0][6]);	
		            	
		            	 getCalendar(currentMonth);
		            }
		            
		           
	            }		
			}
		});
		JScrollPane scroll = new JScrollPane(tblCalendar);
		tblCalendar.setModel(new DefaultTableModel(
				new Object[][] { { null, null, null, null, null, null, null },
						{ null, null, null, null, null, null, null }, { null, null, null, null, null, null, null },
						{ null, null, null, null, null, null, null }, { null, null, null, null, null, null, null },
						{ null, null, null, null, null, null, null }, },
				new String[] { "SUN", "MON", "TUE", "WED", "THR", "FRI", "SAT" }) {
			Class[] columnTypes = new Class[] { String.class, String.class, String.class, String.class, String.class,
					String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		pnCalendarSub.add(scroll);
		tblCalendar.setColumnSelectionAllowed(true);
		tblCalendar.setRowHeight(43);
		tblCalendar.getTableHeader().setReorderingAllowed(false);

		// 테이블 셀 가운데로, 리사이즈 막기
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = tblCalendar.getColumnModel();
		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {
			tcmSchedule.getColumn(i).setResizable(false);
			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);
		}
		
		JPanel pnNote = new JPanel();
		pnNote.setBounds(20, 362, 365, 127);
		pnCalendar.add(pnNote);
		pnNote.setLayout(new GridLayout(0, 1, 0, 0));
		
		txtNote = new JTextArea();
		txtNote.setWrapStyleWord(true);
		txtNote.setLineWrap(true);
		scroll_1 = new JScrollPane(txtNote);
		pnNote.add(scroll_1);
		

		JLabel lblNewLabel = new JLabel("키");
		lblNewLabel.setBounds(425, 367, 20, 15);
		pnCalendar.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("몸무게");
		lblNewLabel_1.setBounds(401, 401, 42, 15);
		pnCalendar.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("체지방");
		lblNewLabel_1_1.setBounds(401, 435, 42, 15);
		pnCalendar.add(lblNewLabel_1_1);
		
		txtHeight = new JTextArea();
		txtHeight.setBounds(451, 362, 126, 24);
		pnCalendar.add(txtHeight);
		
		txtWeight = new JTextArea();
		txtWeight.setBounds(451, 396, 126, 24);
		pnCalendar.add(txtWeight);
		
		txtFat = new JTextArea();
		txtFat.setBounds(451, 430, 126, 24);
		pnCalendar.add(txtFat);
		
		txtMuscle = new JTextArea();
		txtMuscle.setBounds(451, 464, 126, 24);
		pnCalendar.add(txtMuscle);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("근육량");
		lblNewLabel_1_1_1.setBounds(401, 469, 42, 15);
		pnCalendar.add(lblNewLabel_1_1_1);
		
		JButton btnRecordCal = new JButton("기록");
		btnRecordCal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// null 처리
				String note = txtNote.getText().equals("") ? " " : txtNote.getText();
				String height = txtHeight.getText().equals("") ? "0" : txtHeight.getText();
				String weight = txtWeight.getText().equals("") ? "0" : txtWeight.getText();
				String fat = txtFat.getText().equals("") ? "0" : txtFat.getText();
				String muscle = txtMuscle.getText().equals("") ? "0" : txtMuscle.getText();
				
				if (entryDay != null)
				{
					if (calBuffer.length == 0) // 날짜에 데이터가 없어
					{
					
						sql.query = String.format("select * from calendar where record_date between '%s' and '%s' and id = '%s'", entryDay, finalDay, frmLogin.userId);
						if (sql.executeUpdate() == 0) {
							
							sql.query = String.format( 
									"insert into calendar(id, record_date, content, height, weight, fat, muscle) values('%s','%s','%s',%s,%s,%s,%s)", 
									frmLogin.userId, entryDay, note, height, weight, fat, muscle);
							
							//System.out.println(sql.query);
							sql.executeUpdate();
							
	
						}
					} else { 
						// 현재 있는 데이터가 입력데이터값을 업데이트
						sql.query = String.format("update calendar set content='%s', height=%s, weight=%s, fat=%s, muscle=%s where record_date between '%s' and '%s' and id = '%s'", 
													note, height, weight, fat, muscle, entryDay, entryDay, frmLogin.userId);
						sql.executeUpdate();
						//System.out.println(sql.query);
					}
				}else {
					Delphi.lib.INSTANCE.MessageBox("날짜가 선택되지 않았어요", Delphi.lib.MB_ICONEXCLAMATION);
				}
			}
		});
		btnRecordCal.setBounds(20, 496, 95, 38);
		pnCalendar.add(btnRecordCal);
		
		JButton btnCalPrev = new JButton("<");
		btnCalPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (--currentMonth == 0) {
					currentMonth = 12;
					currentYear--;
				}
				getCalendar(currentMonth);
				
			}
		});
		btnCalPrev.setBounds(20, 21, 95, 38);
		pnCalendar.add(btnCalPrev);
		
		JButton btnCalNext = new JButton(">");
		btnCalNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            if (currentYear < maxYear || (currentYear == maxYear && currentMonth < maxMonth)) {
					if (++currentMonth > 12) {
						currentMonth = 1;
						currentYear++;
					}
					getCalendar(currentMonth);
	             }
			}
		});
		btnCalNext.setBounds(482, 21, 95, 38);
		pnCalendar.add(btnCalNext);
		
		lbCurrentMonth = new JLabel("lbCurrentDate");
		lbCurrentMonth.setHorizontalAlignment(SwingConstants.CENTER);
		lbCurrentMonth.setFont(new Font("굴림", Font.PLAIN, 27));
		lbCurrentMonth.setBounds(115, 21, 365, 36);
		pnCalendar.add(lbCurrentMonth);

		
	}
	
	
	// 게시판 ---------------------------------------------------------------------------------------------
	private void initializeSubBoard(JPanel subBoard) {
		subBoard.setLayout(new GridLayout(0, 1, 0, 0));
		JPanel pnBoardLobby = new JPanel();
		subBoard.add(pnBoardLobby);
		pnBoardLobby.setLayout(null);
		
		JButton btnPrev = new JButton("<");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentPage > 0) {
					currentPage--;
					updateBoard();
				}	
			}
		});
		btnPrev.setBounds(20, 324, 62, 35);
		pnBoardLobby.add(btnPrev);
		
		JButton btnNext = new JButton(">");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (bdBuffer.length > (currentPage+1) * 10) {
					currentPage++;
					updateBoard();
				}
			}
		});
		btnNext.setBounds(87, 324, 62, 35);
		pnBoardLobby.add(btnNext);
		
		JButton btnUpdateBoard = new JButton("새로고침");
		btnUpdateBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateBoard();
			}
		});
		btnUpdateBoard.setBounds(154, 324, 129, 35);
		pnBoardLobby.add(btnUpdateBoard);
		
		JButton btnRecordContent = new JButton("글작성");
		btnRecordContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!frmRecordContent.isShowed)
					new frmRecordContent();			
			}
		});
		btnRecordContent.setBounds(452, 324, 130, 35);
		pnBoardLobby.add(btnRecordContent);
		
		JPanel pnBoardView = new JPanel();
		pnBoardView.setBounds(20, 30, 562, 284);
		pnBoardLobby.add(pnBoardView);
		
		tblBoard = new JTable();
		tblBoard.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
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
				new Object[][] { { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null },
						{ null, null, null }, { null, null, null }, { null, null, null }, { null, null, null },
						{ null, null, null }, { null, null, null }, },
				new String[] { "\uC81C\uBAA9", "\uC791\uC131\uC790", "\uC791\uC131\uC77C" }) {
			Class[] columnTypes = new Class[] { String.class, String.class, String.class };

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
		tblBoard.getTableHeader().setReorderingAllowed(false);
		
		// 날짜 컬럼 가운데 정렬
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = tblBoard.getColumnModel();
		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer);
		
		pnBoardView.setLayout(null);
		pnBoardView.setLayout(new GridLayout(0, 1, 0, 0));
		JScrollPane scroll = new JScrollPane(tblBoard);
		tblBoard.setRowHeight(25);
		tblBoard.setFont(new Font("굴림", Font.PLAIN, 14));
		pnBoardView.add(scroll);
		
		lbPageState = new JLabel("1/0");
		lbPageState.setBounds(20, 10, 84, 15);
		pnBoardLobby.add(lbPageState);
	}
	
	
	
	// 함수 ---------------------------------------------------------------------------------------------------------------------
	
    private void initChart(JPanel panel, String type) {
    	panel.removeAll();
    	panel.revalidate();
    	panel.repaint(); 
    	
        XYDataset dataset = createDataset(type);
		JFreeChart chart = createChart(dataset);
		pnChart.setLayout(new GridLayout(0, 1, 0, 0));
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(new Color(240, 240, 240));
		panel.add(chartPanel);
		chartPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
    }
	
	private XYDataset createDataset(String type) {
//		sql.query = String.format(
//				"select * from calendar where id = '%s' and substr(record_date, 1, 5) = substr(sysdate, 1, 5)",
//				frmLogin.userId);

		sql.query = String.format("select * from calendar where id = '%s' and substr(record_date, 4, 2) like ('%%%s%%')", frmLogin.userId, currentMonth); 
		String[][] buf = sql.executeQuery(1, 7);

		var series = new XYSeries(type);
	
		if (buf.length > 1) { // 항목이 2개 이상이여야 차트 연결
			for (int i = 0; i < buf.length; i++) {
				int day = Integer.parseInt(String.valueOf(buf[i][1].substring(8, 10)));

				switch (type) {
					case "Weight": {
						Double weight = Double.parseDouble(String.valueOf(buf[i][4]));
						series.add(day, weight);
						break;
					}
	
					case "Fat": {
						Double fat = Double.parseDouble(String.valueOf(buf[i][5]));
						series.add(day, fat);
						break;
					}
	
					case "Muscle": {
						Double muscle = Double.parseDouble(String.valueOf(buf[i][6]));
						series.add(day, muscle);
						break;
					}
				}

			}

		}
		var dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		return dataset;

	}
	
	private JFreeChart createChart(final XYDataset dataset) {

		JFreeChart chart = ChartFactory.createXYLineChart("", String.format("day (%s / %s)", currentYear, currentMonth), "value", dataset, PlotOrientation.VERTICAL, true,
				true, false);
		XYPlot plot = chart.getXYPlot();

		var renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(1.0f));
		renderer.setSeriesShapesVisible(0, false);

		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);
		
	
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		chart.getLegend().setFrame(BlockBorder.NONE);
		chart.setBackgroundPaint(new Color(240, 240, 240));

		return chart;
	}

	public static ImageIcon getImageToIcon(String dir, int w, int h) { 
	   ImageIcon imgIco = new ImageIcon(dir);
	   Image img = imgIco.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
	   return new ImageIcon(img);
	}

	public static void updateTierImage() {
		ImageIcon profile = getImageToIcon("img/3.png", 40, 40);
	

		String firstHis[][] = getFirstCalendarHistory();
		String lastHis[][] = getLastCalendarHistory();
		if (firstHis.length > 0 && lastHis.length > 0) {
		    double fMuscle = Double.parseDouble(String.valueOf(firstHis[0][3]));
		    double fFat = Double.parseDouble(String.valueOf(firstHis[0][2]));
		    double lMuscle = Double.parseDouble(String.valueOf(lastHis[0][3]));
		    double lFat = Double.parseDouble(String.valueOf(lastHis[0][2]));
		    double perMuscle = (lMuscle - fMuscle) / fMuscle * 100;
		    double perFat = (lFat - fFat) / fFat * 100;

		    if (perFat <= fFat/10 * 10 * -1 && perMuscle >= 5.0) {
				profile = getImageToIcon("img/1.png", 40, 40);	
		    } else if (perFat <= fFat/20 * 10 * -1 && perMuscle >= 2.5) {
				profile = getImageToIcon("img/2.png", 40, 40);
				
		    }		    
		    lbTierPicture.setIcon(profile);   	

//		    if (val < 0.0) {
//				profile = getImageToIcon("img/1.png", 40, 40);
//				lbTierPicture.setIcon(profile);
//		    }
//		    else if (val > 0.0 && val < 1.0) {
//				profile = getImageToIcon("img/2.png", 40, 40);
//				lbTierPicture.setIcon(profile);
//		    }
		}
	}
	
	public static void updateSubRoutine() {
		String s = comMain.getSelectedItem().toString();
		sql.query = String.format("select * from routine where parts = '%s'", s);
		//System.out.println(sql.query);

		comSub.removeAllItems();
		routineBuffer = sql.executeQuery(2, 3);
		for (int i = 0; i < routineBuffer.length; i++) {
			comSub.addItem(routineBuffer[i][0]);
		}
	}
	
	public static void updateSubRoutineInfoText() {
		int selRow = comSub.getSelectedIndex();
		if (selRow >= 0) {
			txtRoutine.setText(routineBuffer[selRow][1]);
			ImageIcon routineImg = getImageToIcon(String.format("img/routine/%s/%s.png", comMain.getSelectedItem().toString(), comSub.getSelectedItem().toString()), 572, 271);
			lbRoutineImg.setIcon(routineImg);
		}
		
	}
	
	public static String[][] getFirstCalendarHistory() {
		sql.query = String.format("select * from calendar where id = '%s' and fat > 0 and muscle > 0 order by record_date asc", frmLogin.userId);
		return sql.executeQuery(4, 7);
	}

	public static String[][] getLastCalendarHistory() {
		sql.query = String.format("select * from calendar where id = '%s' and fat > 0 and muscle > 0 order by record_date desc", frmLogin.userId);
		return sql.executeQuery(4, 7);
	}
	
	public static void setBodyValueState() {
		String buf[][] = getLastCalendarHistory();
		if (buf.length == 0) {
			txtContent.setText("캘린더에 기록이 존재하지 않습니다.");
			return;
		}
		double bmi = Double.parseDouble(buf[0][1]) / Math.pow(Double.parseDouble(buf[0][0]) * 0.01, 2);
		String toState = "저체중";
		if (bmi < 23.0)
			toState = "정상 체중";
		else if (bmi < 25.0)
			toState = "과체중";
		else if (bmi < 30.0)
			toState = "비만";
		else
			toState = "고도비만";
		txtContent.setText(String.format("키: %scm\n몸무게: %skg\n체지방: %s\n근육량: %s\n\nBMI: %s (%s)", buf[0][0], buf[0][1], buf[0][2], buf[0][3], String.format("%.2f", bmi), toState));
		
	}
	
	public static void getCalendar(int month) {
		for (int i = 0; i < tblCalendar.getRowCount(); i++)
			for (int j = 0; j < tblCalendar.getColumnCount(); j++) 
				tblCalendar.setValueAt(null, i, j);
		
        sql.query = String.format("select * from calendar where id = '%s' and substr(record_date, 4, 2) like ('%%%s%%')", frmLogin.userId, month); 
        String buf[][] = sql.executeQuery(2, 2);
        //System.out.println(sql.query);
		cal.set(currentYear, month-1, 1);		
		int w = cal.get(Calendar.DAY_OF_WEEK);
		int end = cal.getActualMaximum(Calendar.DATE);
		int row = 0;
		int col = 0;

		for (col = 0; col < w - 1; col++) {
			tblCalendar.setValueAt(null, 0, col);
		}

		//int today = Integer.parseInt(new SimpleDateFormat("d").format(System.currentTimeMillis()));
		
		// 히스토리가 존재하는 날이 있으면 * 붙여줌ㅇ
		for (int i = 1; i <= end; i++) {
			boolean isRecorded = false;
			if (buf.length > 0) {
				for (int j = 0; j < buf.length; j++) {
					if (Integer.parseInt(buf[j][0].substring(8, 10)) == i) {
						isRecorded = true;
						break;
					}
				}
			}

			if (isRecorded) {
				tblCalendar.setValueAt("*"+i, row, col++);
			}else {
				tblCalendar.setValueAt(i, row, col++);
			}
			
			if (++w % 7 == 1) {
				row++;
				col = 0;
			}
		}	
		lbCurrentMonth.setText(String.format("%s / %s", currentYear, currentMonth));
	}	
	

	public static void updateBoard() {
		sql.query = "select * from board order by idx desc";
		bdBuffer = sql.executeQuery(1, 6);
		int rowCount = bdBuffer.length;

		lastPage = 0;
		if (rowCount > 0) {
			int tmp = bdBuffer.length;
			while (tmp > 0) {
				tmp -= 10;
				++lastPage;
			}
		} else {
			++lastPage;
		}

		for (int i = 0; i <= 9; i++) {
			int pageRow = i + currentPage * 10;
			if (pageRow < rowCount) {
				tblBoard.setValueAt(bdBuffer[pageRow][2], i, 0); // 제목
				tblBoard.setValueAt(bdBuffer[pageRow][1], i, 1); // 아이디
				tblBoard.setValueAt(bdBuffer[pageRow][5].substring(2, 16), i, 2); // 작성일
			} else {
				for (int j = 0; j <= 2; j++)
					tblBoard.setValueAt("", i, j);
			}
		}
		lbPageState.setText(String.format("%s/%s", currentPage + 1, lastPage));
	}
}
