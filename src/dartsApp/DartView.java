package dartsApp;

/**
 * @author Josh Shetler
 *  GUI view of dart game
 *  */

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JToggleButton;

public class DartView {

	private JFrame frmDartGame;
	
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
		frmDartGame.setBounds(100, 100, 692, 432);
		frmDartGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JToolBar toolBar = new JToolBar();
		frmDartGame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnNewButton_1 = new JButton("New Game");
		toolBar.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Add Player");
		toolBar.add(btnNewButton_2);
		
		JButton btnNewButton = new JButton("Add Score");
		toolBar.add(btnNewButton);
		
		JTextPane textPane = new JTextPane();
		toolBar.add(textPane);
		
		JPanel panel = new JPanel();
		frmDartGame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("Highscores");
		
		JTextPane textPane_1 = new JTextPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(18, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(tglbtnNewToggleButton, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
							.addGap(505))
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addComponent(textPane_1, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addGap(492))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(tglbtnNewToggleButton)
					.addGap(18)
					.addComponent(textPane_1, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(233, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
	}
}
