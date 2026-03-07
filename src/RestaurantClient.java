import RestaurantApp.*;
import org.omg.CosNaming.*;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.omg.CORBA.*;

public class RestaurantClient {
  static Admin_Int adminImpl;
  static Menu_Int menuImpl;
  static Order_Int orderImpl;
  static JFrame frame;
  static String user_name;
  static long adminKey;

  public static void main(String args[]) {
    try {
      // create and initialize the ORB
      ORB orb = ORB.init(args, null);

      // get the root naming context
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
      // Use NamingContextExt instead of NamingContext. This is
      // part of the Interoperable naming Service.
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

      // resolve the Object Reference in Naming
      String adminName = "Admin_Int";
      String menuName = "Menu_Int";
      String orderName = "Order_Int";

      adminImpl = Admin_IntHelper.narrow(ncRef.resolve_str(adminName));
      menuImpl = Menu_IntHelper.narrow(ncRef.resolve_str(menuName));
      orderImpl = Order_IntHelper.narrow(ncRef.resolve_str(orderName));

      // ==================================================================

      // Create the main frame
      frame = new JFrame("Will's Resturnat");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(800, 600);

      setLoginPage();
      createMenuBar();

      frame.setVisible(true);

      // ==================================================================

      //
      ///
      /// ///
      ///
      ///
      ///
      ///
      ///
      ///
      ///

      // // Create a panel with a button
      // JPanel panel = new JPanel();
      // JButton button = new JButton("Click Me");
      // panel.add(button);

      // RestaurantApp.Menu menu = menuImpl.getMenu();
      // System.out.println("Initial Version");
      // System.out.println(menu.version);
      // for (RestaurantApp.MenuItem item : menu.menuList) {
      // System.out.println(item.food);
      // System.out.println(item.cost);
      // }

      // System.out.println(adminImpl.setMenu(menu.menuList,
      // adminImpl.getAdminKey("admin", "password")));
      // System.out.println("New Version");
      // System.out.println(menuImpl.getMenu().version);

      // ==================================================================

    } catch (Exception e) {
      System.out.println("ERROR : " + e);
      e.printStackTrace(System.out);
    }

  }

  public static void removeAllPanels() {
    frame.getContentPane().removeAll();
    frame.revalidate();
    frame.repaint();
  }

  public static void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Login");
    JMenuItem adminItem = new JMenuItem("Admin");
    JMenuItem clientItem = new JMenuItem("Client");
    menu.add(clientItem);
    menu.addSeparator();
    menu.add(adminItem);
    menuBar.add(menu);

    adminItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cleanMenuBar();
        setAdminLoginPage();
      }
    });

    clientItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cleanMenuBar();
        setLoginPage();
      }
    });

    frame.setJMenuBar(menuBar);
  }

  public static void cleanMenuBar() {
    JMenuBar menuBar = frame.getJMenuBar();
    for (int i = menuBar.getMenuCount() - 1; i > 0; i--) {
      menuBar.remove(i);
    }
    frame.revalidate();
    frame.repaint();
  }

  public static void createClientMenu() {
    JMenu clientMenu = new JMenu("Client");
    JMenuItem orderItem = new JMenuItem("Orders");
    JMenuItem menuItem = new JMenuItem("Menu");
    clientMenu.add(orderItem);
    clientMenu.addSeparator();
    clientMenu.add(menuItem);
    frame.getJMenuBar().add(clientMenu);

    frame.revalidate();
    frame.repaint();

    orderItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setClientOrderPage();
      }
    });

    menuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setClientMenuPage();
      }
    });
  }

  public static void createAdminMenu() {
    JMenu adminMenu = new JMenu("Admin");
    JMenuItem orderItem = new JMenuItem("Orders");
    JMenuItem menuItem = new JMenuItem("Menu");
    adminMenu.add(orderItem);
    adminMenu.addSeparator();
    adminMenu.add(menuItem);
    frame.getJMenuBar().add(adminMenu);

    frame.revalidate();
    frame.repaint();

    orderItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setAdminOrderPage();
      }
    });

    menuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setAdminMenuPage();
      }
    });
  }

  public static void setLoginPage() {

    JPanel Login_Panel = new JPanel();
    Login_Panel.setLayout(new BoxLayout(Login_Panel, BoxLayout.PAGE_AXIS));

    JLabel Login_First_Label = new JLabel("Please enter first name");
    JTextField Login_First_Field = new JTextField("", 25);
    JLabel Login_Last_Label = new JLabel("Please enter last name");
    JTextField Login_Last_Field = new JTextField("", 25);

    JButton Login_Button = new JButton("Submit");

    Login_First_Label.setAlignmentX(Component.CENTER_ALIGNMENT);
    Login_Last_Label.setAlignmentX(Component.CENTER_ALIGNMENT);
    Login_Button.setAlignmentX(Component.CENTER_ALIGNMENT);
    Login_First_Field.setHorizontalAlignment(JTextField.CENTER);
    Login_Last_Field.setHorizontalAlignment(JTextField.CENTER);
    Login_First_Field.setMaximumSize(new Dimension(Integer.MAX_VALUE, Login_First_Field.getPreferredSize().height));
    Login_Last_Field.setMaximumSize(new Dimension(Integer.MAX_VALUE, Login_Last_Field.getPreferredSize().height));

    // Add action to the button
    Login_Button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ClientLogin(Login_First_Field.getText().trim() + Login_Last_Field.getText().trim());
      }
    });

    Login_Panel.add(Login_First_Label);
    Login_Panel.add(Login_First_Field);
    Login_Panel.add(Login_Last_Label);
    Login_Panel.add(Login_Last_Field);
    Login_Panel.add(Login_Button);

    Login_Panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

    removeAllPanels();
    frame.add(Login_Panel);
    frame.setSize(Login_Panel.getPreferredSize());
    frame.revalidate();
    frame.repaint();
  }

  public static void setAdminLoginPage() {

    JPanel Admin_Login_Panel = new JPanel();
    Admin_Login_Panel.setLayout(new BoxLayout(Admin_Login_Panel, BoxLayout.PAGE_AXIS));

    JLabel Login_Username_Label = new JLabel("Please enter user name");
    JTextField Login_Username_Field = new JTextField("", 25);
    JLabel Login_Password_Label = new JLabel("Please password");
    JPasswordField Login_Password_Field = new JPasswordField("", 25);

    JButton Login_Button = new JButton("Submit");

    Login_Username_Label.setAlignmentX(Component.CENTER_ALIGNMENT);
    Login_Password_Label.setAlignmentX(Component.CENTER_ALIGNMENT);
    Login_Button.setAlignmentX(Component.CENTER_ALIGNMENT);
    Login_Username_Field.setHorizontalAlignment(JTextField.CENTER);
    Login_Password_Field.setHorizontalAlignment(JTextField.CENTER);
    Login_Username_Field
        .setMaximumSize(new Dimension(Integer.MAX_VALUE, Login_Username_Field.getPreferredSize().height));
    Login_Password_Field
        .setMaximumSize(new Dimension(Integer.MAX_VALUE, Login_Password_Field.getPreferredSize().height));

    // Add action to the button
    Login_Button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AdminLogin(Login_Username_Field.getText().trim(), new String(Login_Password_Field.getPassword()).trim());
      }
    });

    Admin_Login_Panel.add(Login_Username_Label);
    Admin_Login_Panel.add(Login_Username_Field);
    Admin_Login_Panel.add(Login_Password_Label);
    Admin_Login_Panel.add(Login_Password_Field);
    Admin_Login_Panel.add(Login_Button);

    Admin_Login_Panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

    removeAllPanels();
    frame.add(Admin_Login_Panel);
    frame.setSize(Admin_Login_Panel.getPreferredSize());
    frame.revalidate();
    frame.repaint();
  }

  public static void setClientOrderPage() {

  }

  public static void setAdminOrderPage() {

  }

  public static void setClientMenuPage() {

  }

  public static void setAdminMenuPage() {

  }

  public static void ClientLogin(String name) {
    if (name != null && !name.isEmpty()) {
      user_name = name;
      createClientMenu();
      setClientOrderPage();
    } else {
      JOptionPane.showMessageDialog(frame, "Invalid name entered. Please try again.");
    }
  }

  public static void AdminLogin(String username, String password) {
    try {
      adminKey = adminImpl.getAdminKey(username, password);
      createAdminMenu();
      setAdminOrderPage();
    } catch (Incorrect_Password e) {
      JOptionPane.showMessageDialog(frame, "Incorrect Username / Password. Please try again.");
    }
  }

}