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
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextArea;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JTextField;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import java.awt.TextArea;
import javax.swing.JTextPane;

public class DartView extends SavedDataReader {

    private JFrame frmDartGame;
    private JButton newGame;
    private JButton addScorebutton;
    private JTextField textPane;
    private JPanel panel;
    private JTable dataTable;
    private JScrollPane scrollPane;
    private static JFrame pop;
    private JButton userLstBtn;
    private int count = 1;
    private JTextArea userTextInfo;
    private SavedDataReader obj;
    private static JPanel oldTablePanel;
    private static JScrollPane ret;
    private static JTable j;
    private  JTextField txtpnCurrentPlayer; 
    private Color GREEN = new Color(48,159,106);
    private Color RED = new Color(227,154,144);
    private Color CORK = new Color(249,223,188);
    
    private static String tableCols[] = { "Name", "Score","Dart Number", "Avg. Score" };
    private static String prevCols[] = { "Game Number","Name", "Score","Dart Number", "Avg. Score" };
    private static String data[][] = { { " ", " ", " " }, { " ", " ", " " }, { " ", " ", " " }, { " ", " ", " " },
            { " ", " ", " " } };

    // used a dataModel to reset table
    // found at
    // https://stackoverflow.com/questions/3879610/how-to-clear-contents-of-a-jtable/3880040
    static DefaultTableModel Model = new DefaultTableModel(data, tableCols) {

        private static final long serialVersionUID = 1L;

        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    static DefaultTableModel tempModel = new DefaultTableModel(data, prevCols) {

        private static final long serialVersionUID = 1L;

        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    

    private static DartViewModel VVM = new DartViewModel();

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
        CreateGame();
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
    	
        frmDartGame = new JFrame();
        frmDartGame.setTitle("DART GAME");
        frmDartGame.setBounds(100, 100, 833, 632);
        frmDartGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JToolBar toolBar = new JToolBar();
        frmDartGame.getContentPane().add(toolBar, BorderLayout.NORTH);

        newGame = new JButton("New Game");
        newGame.addActionListener(actionListener);
        toolBar.add(newGame);

        addScorebutton = new JButton("Add Score");
        addScorebutton.addActionListener(actionListener);
        toolBar.add(addScorebutton);

        textPane = new JTextField();
        textPane.addActionListener(actionListener);
        toolBar.add(textPane);

        // panel positioning, designed by eclipse window.
        panel = new JPanel();
        frmDartGame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setBackground(GREEN);
       
        // setTable();

        userLstBtn = new JButton("Old User Info");
        userLstBtn.setFont(new Font("Arial Black", Font.PLAIN, 10));
        userLstBtn.setBounds(78, 10, 113, 27);
        userLstBtn.addActionListener(actionListener);
        panel.setLayout(null);
        panel.add(userLstBtn);

        dataTable = new JTable();

        dataTable.setModel(Model);

        scrollPane = new JScrollPane(dataTable);
        scrollPane.setBounds(348, 82, 409, 203);
        panel.add(scrollPane);

        userTextInfo = new JTextArea();
        userTextInfo.setFont(new Font("Arial", Font.BOLD, 12));
        userTextInfo.setBackground(CORK);
        userTextInfo.setEditable(false);
        userTextInfo.setBounds(20, 47, 230, 171);
        userTextInfo.setMargin(new Insets(60,60,10,10));
        
        JScrollPane scroller = new JScrollPane(userTextInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        scroller.setBounds(20, 47, 253, 238);
        panel.add(scroller);
        
        oldTablePanel = new JPanel();
        oldTablePanel.setBounds(372, 349, 416, 170);
        panel.add(oldTablePanel);
        
        txtCurrentGame = new JTextField();
        txtCurrentGame.setEditable(false);
        txtCurrentGame.setFont(new Font("Arial Black", Font.PLAIN, 10));
        txtCurrentGame.setText("Current Game");
        txtCurrentGame.setBounds(504, 53, 95, 19);
        panel.add(txtCurrentGame);
        txtCurrentGame.setColumns(10);
        
        txtPreviousGames = new JTextField();
        txtPreviousGames.setEditable(false);
        txtPreviousGames.setFont(new Font("Arial Black", Font.PLAIN, 10));
        txtPreviousGames.setText("Previous Games");
        txtPreviousGames.setColumns(10);
        txtPreviousGames.setBounds(522, 320, 113, 19);
        panel.add(txtPreviousGames);
        
        txtpnCurrentPlayer = new JTextField();
        txtpnCurrentPlayer.setFont(new Font("Arial Black", Font.PLAIN, 13));
    	txtpnCurrentPlayer.setText("Current Player: " + VVM.getCurrentPlayer().getName());
        txtpnCurrentPlayer.setBounds(20, 308, 322, 140);
        txtpnCurrentPlayer.setBackground(RED);
        txtpnCurrentPlayer.setMargin(new Insets(12,12,12,12));
        
        panel.add(txtpnCurrentPlayer);
        createTable();

    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent a) {

            if (a.getSource() == newGame) {

                Model.setRowCount(0);
                Model.setRowCount(5);

                JOptionPane.showMessageDialog(pop, "New Darts game", "NEW GAME", JOptionPane.INFORMATION_MESSAGE);

                VVM = new DartViewModel();
                VVM.newGame(ClassicDarts.class, new Object[] { (byte) 2 });
                CreateGame();
                updateTable();

            }
            if (a.getSource() == addScorebutton || a.getSource() == textPane) {
            	
                if (textPane.getText() != null) {
               
                	String txt = textPane.getText();
                    String name = VVM.getCurrentPlayer().getName();
                    Game.GameEvent ret = null;
                    short num;
                    try {
                     	num = Short.parseShort(txt);
                    	ret = VVM.inputScore(num);
                    }
                    catch (Exception ex) {
                    	// popup? clear?
                    	JOptionPane.showMessageDialog(pop, "Invalid, not an integer", "ALERT", JOptionPane.WARNING_MESSAGE);
                    	return;
                    }
                    
                 	// researched pop up windows from
                    // https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
                 // popups, checking different game cases
                    txtpnCurrentPlayer.setText("Current Player: " + VVM.getCurrentPlayer().getName());
                    
                   if(ret != null) {
	                  switch (ret) {
		                    case INVALIDSCORE:
		                    	JOptionPane.showMessageDialog(pop, "Invalid Score", "Warning", JOptionPane.WARNING_MESSAGE);
		                    	break;
		                    case PLAYERBUSTED:
		                    	JOptionPane.showMessageDialog(pop, "Player: " + name + " Busted", "PLAYER BUST", JOptionPane.PLAIN_MESSAGE);
		                    	break;
		                    case GAMEOVER:           
		                        JOptionPane.showMessageDialog(pop, VVM.getWinner().getName() + " has Won!", "WINNER",
		                                JOptionPane.INFORMATION_MESSAGE);
		        
	                     
		                        restartGame();
		                        break;
		                    	
	                    }
                   }

                    textPane.setText("");
                    updateTable();

                }

            } else if (a.getSource() == userLstBtn) {

                count++;
                if (count % 2 == 0) {

                    ArrayList<User> users = VVM.getUsers();

                    for (int i = 0; i < users.size(); i++) {

                        userTextInfo.append(users.get(i).toString() + "\n\n");
                    }

                } else {

                    userTextInfo.setText(" ");

                }
            } 
                
        }
    };
    
    private JTextField txtCurrentGame;
    private JTextField txtPreviousGames;


    // current update of the game
    private void updateTable() {
    	
    	Model.setRowCount(VVM.getPlayerCount());
    	
        for (int i = 0; i < VVM.getPlayerCount(); i++) {
        	
            String name = VVM.getPlayers().get(i).getName();
            String score = VVM.getScores().get(i).toString();
            String average = VVM.getAverages().get(i).toString();
            String darts = VVM.getDartCounts().get(i).toString();
            
          
            
            Model.setValueAt(name, i, 0);
            Model.setValueAt(score, i, 1);
            Model.setValueAt(darts, i, 2);
            Model.setValueAt(average, i, 3);

        }

    }
    
    private void clearCURRTable() {
    	
Model.setRowCount(VVM.getPlayerCount());
    	
        for (int i = 0; i < VVM.getPlayerCount(); i++) {
        	
            Model.setValueAt("", i, 0);
            Model.setValueAt("", i, 1);
            Model.setValueAt("", i, 2);
            Model.setValueAt("", i, 3);

        }	
    	
    }

    // function to return a new JTable
    private static void createTable() {
    	

    	// method for clearing the table https://stackoverflow.com/questions/10413977/removing-all-the-rows-of-defaulttablemodel
    	
    	if (tempModel.getRowCount() > 0) {
    	    for (int l = tempModel.getRowCount() - 1; l > -1; l--) {
    	    	tempModel.removeRow(l);
    	    }
    	}
         	
        ArrayList<GameInfo> gameInfo = SavedDataReader.getGameInfosForType(ClassicDarts.class);
        // for each game info
        
        for (int i = 0; i < gameInfo.size(); i++) {
        	GameInfo gi = gameInfo.get(i);
        	
        	// get players
            ArrayList<User> users = gameInfo.get(i).getPlayers();
            
            // for each player
            for(int x = 0; x < users.size(); x++) {
            	String name = users.get(x).getName();
            	short score = gi.getTotalScores().get(x);
            	double avg = gi.getAverages().get(x);
            	int darts = gi.getDartCounts().get(x);
            	int gameNumber = i+1;
            	
            	String dart = Integer.toString(darts);
            	String scores = Short.toString(score);
            	String averages = Double.toString(avg);
            	
            	//tempModel add rows, of ev
            	tempModel.addRow(new Object[] {
            			(x==0 ? gameNumber : ""), 
            			(users.get(x) == gi.getWinner() ? String.format("%s (WON)", name) : name), 
            			scores, 
            			dart, 
            			averages}
            	);
            	
            }
            // add space if not last GameInfo
       
            	tempModel.addRow(new Object[] {"", "", "", "", ""});
           
        }
        oldTablePanel.setLayout(new GridLayout(0, 1, 0, 0));
        
        j = new JTable(); 
        j.setModel(tempModel);
        ret = new JScrollPane(j);
        oldTablePanel.add(ret);
    }
    
    
    /**
     * Function to Create a new game, with user prompts, and input.
     * 
     */

    private void CreateGame() {
   
        // get map of user names to users
        Map<String, User> user_names = new HashMap<String, User>();
        VVM.getUsers().forEach(u -> user_names.put(u.getName(), u));
        
        String player_count = null;
        int count = 0;
        // get player count
        while(count <= 1) {
		    try  { 
		    	player_count = JOptionPane.showInputDialog(pop, "How many players would you like to add? ");
		    	count = Integer.parseInt(player_count);
		    } catch(Exception ex) {
		    	// popup try again
		    	JOptionPane.showMessageDialog(pop, "Input Integer greater than one.", "TRY AGAIN", JOptionPane.WARNING_MESSAGE);
		    }
	    }
       

        // Ethan's take on the problem
        // create players array
        User[] players = new User[count];

        // get player names and create players
        for (int i = 0; i < count; i++) {
            String name = JOptionPane.showInputDialog(pop, "Type in name or leave blank to create guest user: ");
            // if empty input
            if (name.compareTo("") == 0) {
                // create guest user
                players[i] = new User(i + 1);
            }
            // else if player already exists
            else if (user_names.containsKey(name)) {
                // add existing player
                players[i] = user_names.get(name);
                VVM.addUser(players[i]);
            }
            // else
            else {
                // create new player
                players[i] = new User(name);
                VVM.addUser(players[i]);
            }
        }

        // pass players to newGame initializer
        VVM.newGame(ClassicDarts.class, new Object[] { players });

        // update table to display
        updateTable();

        JOptionPane.showMessageDialog(pop, "New Darts game", "NEW GAME", JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * function to restart a game
     */

    private void restartGame() {
    	VVM.endGame();
    	createTable();
        int j = JOptionPane.showConfirmDialog(pop, "Would you like to restart the game with the same players?");
        if (j == JOptionPane.YES_OPTION) {

            // copy and pass the players array
            User[] players = new User[VVM.getPlayerCount()];
            Object[] player_obj = VVM.getPlayers().toArray();
            for (int i = 0; i < player_obj.length; i++) {
                players[i] = (User) player_obj[i];
            }

            VVM.newGame(ClassicDarts.class, new Object[] { players });

        } else if(j == JOptionPane.CANCEL_OPTION) {
        	// this method of closing window from https://stackoverflow.com/a/1235994
        	frmDartGame.dispatchEvent(new WindowEvent(frmDartGame, WindowEvent.WINDOW_CLOSING));
        }
        
        else {

            CreateGame();

        }

    }
}


