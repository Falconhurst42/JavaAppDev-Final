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
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

public class DartView extends SavedDataReader {

    private JFrame frmDartGame;
    private JButton newGame;
    private JButton addScorebutton;
    private JTextPane textPane;
    private JPanel panel;
    private JTable dataTable;
    private JScrollPane scrollPane;
    private static JFrame pop;
    private JButton addUser;
    private JButton userLstBtn;
    private int count = 1;
    private JTextArea userTextInfo;
    private SavedDataReader obj;

    private static String tableCols[] = { "Name", "Score", "Avg. Score" };
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

        // panel positioning, designed by eclipse window.
        panel = new JPanel();
        frmDartGame.getContentPane().add(panel, BorderLayout.CENTER);

        // setTable();

        userLstBtn = new JButton("Old User Info");
        userLstBtn.setBounds(78, 10, 113, 27);
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

        JScrollPane scroller = new JScrollPane(userTextInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setBounds(20, 47, 253, 238);
        panel.add(scroller);

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
            if (a.getSource() == addScorebutton) {

                if (textPane.getText() != null) {
                    String txt = textPane.getText();

                    short num = (short) Integer.parseInt(txt);
                    VVM.inputScore(num);

                    if (VVM.hasWinner()) {
                        // researched pop up windows from
                        // https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
                        JOptionPane.showMessageDialog(pop, VVM.getWinner().getName() + " has Won!", "WINNER",
                                JOptionPane.INFORMATION_MESSAGE);
                        // todo, save previous game into file
                        
                        restartGame();
                     
                    }

                    textPane.setText("");
                    updateTable();

                }

            } else if (a.getSource() == userLstBtn) {

                count++;
                if (count % 2 == 0) {

                    for (int i = 0; i < VVM.getPlayers().size(); i++) {

                        ArrayList<User> users = SavedDataReader.getUsers();

                        userTextInfo.append(users.get(i).toString() + "\n\n");
                    }

                } else {

                    userTextInfo.setText(" ");

                }

            } else if (a.getSource() == addUser) {

                String name = textPane.getText();
                User one = new User(name);

                VVM.addUser(one);
                textPane.setText("");
                updateTable();

            }

        }

    };

    // current update of the game
    private void updateTable() {

        for (int i = 0; i < VVM.getPlayerCount(); i++) {

            String name = VVM.getPlayers().get(i).getName();
            String score = VVM.getScores().get(i).toString();
            String average = VVM.getAverages().get(i).toString();

            Model.setValueAt(name, i, 0);
            Model.setValueAt(score, i, 1);
            Model.setValueAt(average, i, 2);

        }

    }

    // function to return a new JTable
    private static JTable createTable() {
        JTable j = new JTable();
        
        DefaultTableModel tempModel = new DefaultTableModel(data, tableCols) {

            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        j.setModel(tempModel);

        ArrayList<GameInfo> gameInfo = new ArrayList<GameInfo>(SavedDataReader.getGameInfosForType(ClassicDarts.class));
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
            	
            	String scores = Short.toString(score);
            	String averages = Double.toString(avg);
            	
            	tempModel.setValueAt(name, x, 0);
            	tempModel.setValueAt(scores, x, 0);
            	tempModel.setValueAt(averages, x, 0);
            	
            	
            }
        }
        
        
    
        
        return j;
    }

    /**
     * Function to Create a new game, with user prompts, and input.
     * 
     */

    private void CreateGame() {

        // get map of user names to users
        Map<String, User> user_names = new HashMap<String, User>();
        VVM.getUsers().forEach(u -> user_names.put(u.getName(), u));

        // get player count
        String player_count = JOptionPane.showInputDialog(pop, "How many players would you like to add? ");
        int count = Integer.parseInt(player_count);

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
            }
            // else
            else {
                // create new player
                players[i] = new User(name);
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
        int j = JOptionPane.showConfirmDialog(pop, "Would you like to restart the game with the same players?");
        if (j == JOptionPane.YES_OPTION) {

            // copy and pass the players array
            User[] players = new User[VVM.getPlayerCount()];
            Object[] player_obj = VVM.getPlayers().toArray();
            for (int i = 0; i < player_obj.length; i++) {
                players[i] = (User) player_obj[i];
            }

            VVM.newGame(ClassicDarts.class, new Object[] { players });

        } else {

            CreateGame();

        }

    }

}


