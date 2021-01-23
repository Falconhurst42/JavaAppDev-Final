package dartsApp;

/**
 * @author Josh Shetler
 *  GUI view of dart game
 *  */

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.desktop.UserSessionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DartView extends SavedDataReader{

	private JFrame frmDartGame;
	private JButton newGame;
	private JButton addScorebutton;
	private JTextPane textPane;
	private JPanel panel;
	private JTextPane userTextInfo;
	private JTable dataTable;
	private JScrollPane scrollPane;
	private JFrame pop;
	private JButton addUser;
	private  JToggleButton toggleHighScore;
	
	private String tableCols[] = { "Name", "Score", "Avg. Score" };
	private static String data[][] = {{" "," "," "}, {" "," ", " "}, {" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};
	
	//used a dataModel to reset table 
	//found at https://stackoverflow.com/questions/3879610/how-to-clear-contents-of-a-jtable/3880040
	DefaultTableModel Model = new DefaultTableModel(data, tableCols) {
			
			private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
			       return false;
			    }    
		   };
	
  
	private DartViewModel VVM = new DartViewModel();
	private static ArrayList<User> user;
	private String userArr[];
	private Game gameObj = new ClassicDarts((byte)2);
	
	private ArrayList<Short> Scores = new ArrayList<Short>(VVM.getScores());
	private ArrayList<Double> avg = new ArrayList<Double>(VVM.getAverages());
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
			
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DartView window = new DartView();
					window.frmDartGame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DartView() {
		
		
		user = SavedDataReader.getUsers();
		for(int i = 0; i < user.size(); i++) {
			
			data[0][0] = user.get(0).getName();
		
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmDartGame = new JFrame();
		frmDartGame.setTitle("DART GAME");
		frmDartGame.setBounds(100, 100, 685, 514);
		frmDartGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JToolBar toolBar = new JToolBar();
		frmDartGame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
	    newGame = new JButton("New Game");
	    newGame.addActionListener(actionListener);
		toolBar.add(newGame);
		
	    addScorebutton = new JButton("Add Score");
	    addScorebutton.addActionListener(actionListener);
		
		addUser = new JButton("Add User");
		addUser.addActionListener(actionListener);
		
		toolBar.add(addUser);
		toolBar.add(addScorebutton);
		
		textPane = new JTextPane();
		toolBar.add(textPane);
		
		//panel positioning, designed by eclipse window. 
		panel = new JPanel();
		frmDartGame.getContentPane().add(panel, BorderLayout.EAST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{35, 0, 129, 121, 188, 0};
		gbl_panel.rowHeights = new int[]{21, 0, 202, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
	   
	   setTable();
	   
	   //create new toggle button
	   toggleHighScore = new JToggleButton("User Info");
	   toggleHighScore.addActionListener(actionListener);
	   GridBagConstraints gbc_toggleHighScore = new GridBagConstraints();
	   gbc_toggleHighScore.anchor = GridBagConstraints.NORTH;
	   gbc_toggleHighScore.insets = new Insets(0, 0, 5, 5);
	   gbc_toggleHighScore.gridx = 2;
	   gbc_toggleHighScore.gridy = 1;
	   panel.add(toggleHighScore, gbc_toggleHighScore);
	   
	   userTextInfo = new JTextPane();
	   GridBagConstraints gbc_textPane_1 = new GridBagConstraints();
	   gbc_textPane_1.fill = GridBagConstraints.BOTH;
	   gbc_textPane_1.insets = new Insets(0, 0, 5, 5);
	   gbc_textPane_1.gridx = 2;
	   gbc_textPane_1.gridy = 2;
	   panel.add(userTextInfo, gbc_textPane_1);
	   
	   dataTable = new JTable();
	   
	   dataTable.setModel(Model);
	   
	   scrollPane = new JScrollPane(dataTable);
	   GridBagConstraints gbc_scrollPane = new GridBagConstraints();
	   gbc_scrollPane.gridwidth = 2;
	   gbc_scrollPane.gridheight = 2;
	   gbc_scrollPane.fill = GridBagConstraints.BOTH;
	   gbc_scrollPane.gridx = 3;
	   gbc_scrollPane.gridy = 2;
	   panel.add(scrollPane, gbc_scrollPane);
	   
	   
	}
	
	ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent a) {
	
        	if(a.getSource() == newGame) {
        		
        			Model.setRowCount(0);
        			Model.setRowCount(5); 	
        			
        			JOptionPane.showMessageDialog(pop, "New Darts game", "NEW GAME", JOptionPane.INFORMATION_MESSAGE);
        			
        			VVM = new DartViewModel();
        			setTable();
        			
        	}if(a.getSource() == addScorebutton) {
        		
        		if(textPane.getText() != null) {
        		String txt = textPane.getText();
        		
        		short num = (short)Integer.parseInt(txt);
        		VVM.inputScore(num);
        		
        		if(VVM.hasWinner()) {
        			//researched pop up windows from https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
        			JOptionPane.showMessageDialog(pop, VVM.getWinner().getName() + " has Won!", "WINNER", JOptionPane.INFORMATION_MESSAGE);
        	
        		}
        		
        		textPane.setText("");
        		setTable();
        		
        		}
        	} else if(a.getSource() == toggleHighScore) {
        		
        	if(userTextInfo.getText() == null) {
        		for(int i = 0; i < user.size(); i++) {
        		userTextInfo.setText(user.get(i).getName());
        		}
        		
        	}else {
        			
        			userTextInfo.setText(" ");
        		}
        	}
        		
        	}
        		
        		
	};
       
	
	private void setTable() {
			
		for(int i = 0; i < Scores.size(); i++) {
			
			String name =  VVM.getPlayers().get(i).getName();
			String score = VVM.getScores().get(i).toString();
			String average = VVM.getAverages().get(i).toString();
			
			Model.setValueAt(name, i, 0);
			Model.setValueAt(score, i, 1);
			Model.setValueAt(average, i, 2);	
			
		}
		
	}
	
}
