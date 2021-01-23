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
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JTextArea;

public class DartView extends SavedDataReader{

	private JFrame frmDartGame;
	private JButton newGame;
	private JButton addScorebutton;
	private JTextPane textPane;
	private JPanel panel;
	private JTable dataTable;
	private JScrollPane scrollPane;
	private JFrame pop;
	private JButton addUser;
	private JButton userLstBtn;
	private int count = 1;
	private JTextArea userTextInfo;
	private SavedDataReader obj;
	
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
		
		
	//	user = SavedDataReader.getUsers();
	//	for(int i = 0; i < user.size(); i++) {
			
	//		data[0][0] = user.get(0).getName();
		
	//	}
		
		JOptionPane.showMessageDialog(pop, "New Darts game", "NEW GAME", JOptionPane.INFORMATION_MESSAGE);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmDartGame = new JFrame();
		frmDartGame.setTitle("DART GAME");
		frmDartGame.setBounds(100, 100, 765, 577);
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
		frmDartGame.getContentPane().add(panel, BorderLayout.CENTER);
	   
	   setTable();
	   
	   userLstBtn = new JButton("User Info");
	   userLstBtn.setBounds(85, 10, 92, 27);
	   userLstBtn.addActionListener(actionListener);
	   panel.setLayout(null);
	   panel.add(userLstBtn);
	   
	   dataTable = new JTable();
	   
	   dataTable.setModel(Model);
	   
	   scrollPane = new JScrollPane(dataTable);
	   scrollPane.setBounds(306, 10, 409, 203);
	   panel.add(scrollPane);
	   
	   userTextInfo = new JTextArea();
	   userTextInfo.setBounds(20, 47, 230, 171);
	   userTextInfo.setWrapStyleWord(true);
	   panel.add(userTextInfo);
	   
	   JPanel panel_1 = new JPanel();
	   panel_1.setBounds(20, 228, 695, 253);
	   panel.add(panel_1);
	   
	   
	}
	
	ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent a) {
	
        	if(a.getSource() == newGame) {
        		
        			Model.setRowCount(0);
        			Model.setRowCount(5); 	
        			
        			JOptionPane.showMessageDialog(pop, "New Darts game", "NEW GAME", JOptionPane.INFORMATION_MESSAGE);
        			
        			
        			VVM = new DartViewModel();
        			VVM.newGame(ClassicDarts.class, new Object[] { (byte)2 });
        			setTable();
        			
        	}if(a.getSource() == addScorebutton) {
        		
        		if(textPane.getText() != null) {
        		String txt = textPane.getText();
        		
        		short num = (short)Integer.parseInt(txt);
        		VVM.inputScore(num);
        		
        		if(VVM.hasWinner()) {
        			//researched pop up windows from https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
        			JOptionPane.showMessageDialog(pop, VVM.getWinner().getName() + " has Won!", "WINNER", JOptionPane.INFORMATION_MESSAGE);
        			VVM.endGame();
        		}
        		
        		textPane.setText("");
        		setTable();
        		
        		}
        		
        	} else if(a.getSource() == userLstBtn) {
        		
        		count++;
        		if(count % 2 == 0) {
        	
        			for(int i = 0; i < VVM.getPlayers().size(); i++) {
        				
        				ArrayList<User> users = SavedDataReader.getUsers();
        				
        				String name = users.get(i).getName();
        				int win = users.get(i).getWins();
        				double avg = users.get(i).getAverage();
        			
        				
        				
        		      userTextInfo.append(String.format("%s (Wins: %d) (Average: %.2f) \n", name, win, avg ));
        			 //userTextInfo.append(users.get(i).toString()+ "\n\n");
        			}
        		} else {
        				
        				userTextInfo.setText(" "); 				
        				
        			}			
        			
        		} else if(a.getSource() == addUser) {
        		
        		String name = textPane.getText();
        		User one = new User(name);
        		
        		VVM.addUser(one);
        		textPane.setText("");
        		setTable();
        		
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
	


private static void createTable(JTable J, GameInfo obj) {
	
	obj.getPlayers();
	
}



}



