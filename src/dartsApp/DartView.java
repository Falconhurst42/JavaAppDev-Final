package dartsApp;

/**
 * @author Josh Shetler
 *  GUI view of dart game
 *  */

import java.awt.EventQueue;
import javax.swing.JFrame;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DartView {

	private JFrame frmDartGame;
	private JButton newGame;
	private JButton playerButton;
	private JButton addScorebutton;
	private JTextPane textPane;
	private JPanel panel;
	private JTextPane highScoreText;
	private JTable dataTable;
	private JScrollPane scrollPane;
	
	//used a dataModel to reset table 
	//found at https://stackoverflow.com/questions/3879610/how-to-clear-contents-of-a-jtable/3880040
	private DefaultTableModel Model;

	
	private String tableCols[] = { "Name", "Score", "Avg. Score" };
	private String data[][] = {{"Gerald","100",".25"}, {"Jake","300", "45"}, {" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};
	
	
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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmDartGame = new JFrame();
		frmDartGame.setTitle("DART GAME");
		frmDartGame.setBounds(100, 100, 566, 418);
		frmDartGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JToolBar toolBar = new JToolBar();
		frmDartGame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
	    newGame = new JButton("New Game");
	    newGame.addActionListener(actionListener);
		toolBar.add(newGame);
		
	    playerButton = new JButton("Add Player");
	    playerButton.addActionListener(actionListener);
		toolBar.add(playerButton);
		
	    addScorebutton = new JButton("Add Score");
	    addScorebutton.addActionListener(actionListener);
		toolBar.add(addScorebutton);
		
		textPane = new JTextPane();
		toolBar.add(textPane);
		
		panel = new JPanel();
		frmDartGame.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{35, 129, 121, 188, 0};
		gbl_panel.rowHeights = new int[]{21, 202, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JToggleButton toggleHighScore = new JToggleButton("Highscores");
		GridBagConstraints gbc_toggleHighScore = new GridBagConstraints();
		gbc_toggleHighScore.anchor = GridBagConstraints.NORTH;
		gbc_toggleHighScore.fill = GridBagConstraints.HORIZONTAL;
		gbc_toggleHighScore.insets = new Insets(0, 0, 5, 5);
		gbc_toggleHighScore.gridx = 1;
		gbc_toggleHighScore.gridy = 0;
		panel.add(toggleHighScore, gbc_toggleHighScore);
		
		highScoreText = new JTextPane();
		GridBagConstraints gbc_textPane_1 = new GridBagConstraints();
		gbc_textPane_1.fill = GridBagConstraints.BOTH;
		gbc_textPane_1.insets = new Insets(0, 0, 0, 5);
		gbc_textPane_1.gridx = 1;
		gbc_textPane_1.gridy = 1;
		panel.add(highScoreText, gbc_textPane_1);
		
	   dataTable = new JTable(data, tableCols);
	   
	   
	   Model = new DefaultTableModel(data, tableCols) {
	
		private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
		       return false;
		    } 
		   
	   };
	   
	   dataTable.setModel(Model);
	   
	   scrollPane = new JScrollPane(dataTable);
	   GridBagConstraints gbc_scrollPane = new GridBagConstraints();
	   gbc_scrollPane.fill = GridBagConstraints.BOTH;
	   gbc_scrollPane.gridx = 3;
	   gbc_scrollPane.gridy = 1;
	   panel.add(scrollPane, gbc_scrollPane);
	   
	   
	}
	
	ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent a) {
	
        	if(a.getSource() == newGame) {
        		
        			Model.setRowCount(0);
        			Model.setRowCount(5); 	
        			
        	}if(a.getSource() == playerButton) {
        		
        		boolean setName = true;
        		int count = 0;
        		
        		while(setName) {	
        			
        			String txt = textPane.getText();
        			
        			if(Model.getValueAt(count, 0) == null && txt != null) {
        				
        				Model.addRow(new Object[2]);
        				Model.setValueAt(txt, count, 0);
        				
        				setName = false;
        				break;
        				
        			}
        			count++;
        		}
        		
        	}
        	
        }
	};
	
	
	
	private void setTable() {
		
		
		
		
	}
	
	
	
}
