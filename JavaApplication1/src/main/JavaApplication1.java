package main;

import view.LoginForm;
import javax.swing.SwingUtilities;

public class JavaApplication1 {
	public static void main(String[] args) {

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName()) || "Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {
			System.out.println("Không thể thiết lập Look and Feel: " + ex.getMessage());
		}

		SwingUtilities.invokeLater(() -> {
			LoginForm login = new LoginForm();
			login.setVisible(true);
		});
	}
}