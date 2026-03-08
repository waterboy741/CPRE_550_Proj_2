import RestaurantApp.*;
import org.omg.CosNaming.*;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.omg.CORBA.*;

public class RestaurantClient {
  static Admin_Int adminImpl;
  static Menu_Int menuImpl;
  static Order_Int orderImpl;
  static JFrame frame;
  static String username;
  static int adminKey;
  static short menuVersion;

  static DefaultListModel<RestaurantApp.OrderItem> clientMenuListModel;

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
      frame.setMaximumSize(new Dimension(1920 / 2, 1080 / 2));
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

    RestaurantApp.Order activeOrder;
    int divisor;

    try {
      activeOrder = orderImpl.getActiveOrder(username);
    } catch (No_Active_Order noActiveOrderException) {
      activeOrder = null;
    }

    ArrayList<RestaurantApp.Order> allOrders = new ArrayList<>();

    Collections.addAll(allOrders, orderImpl.getPreviousOrders(username));
    Collections.reverse(allOrders);

    JList<RestaurantApp.Order> allOrderList = new JList<>(allOrders.toArray(new RestaurantApp.Order[0]));
    allOrderList.setCellRenderer(new RestaurantClient().new ClientOrderRenderer());
    JScrollPane allScrollPane = new JScrollPane(allOrderList);

    JLabel Active_Label = new JLabel("Active Order");
    Active_Label.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel Client_Orders_Panel = new JPanel();
    Client_Orders_Panel.setLayout(new BoxLayout(Client_Orders_Panel, BoxLayout.Y_AXIS));
    Client_Orders_Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    if (activeOrder != null) {
      JList<RestaurantApp.Order> activeOrderList = new JList<>(new RestaurantApp.Order[] { activeOrder });
      activeOrderList.setCellRenderer(new RestaurantClient().new ClientOrderRenderer());
      JScrollPane activeScrollPane = new JScrollPane(activeOrderList);

      Client_Orders_Panel.add(Active_Label);
      Client_Orders_Panel.add(activeScrollPane);

      divisor = 2;

    } else {
      JLabel Non_Active_Label = new JLabel("No Active Orders");
      Non_Active_Label.setAlignmentX(Component.CENTER_ALIGNMENT);

      JButton Reorder_Button = new JButton("Reorder");
      Reorder_Button.setAlignmentX(Component.CENTER_ALIGNMENT);

      Reorder_Button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (allOrderList.getSelectedIndex() != -1) {
            try {
              if (orderImpl.placeOrder(allOrderList.getSelectedValue())) {
                JOptionPane.showMessageDialog(frame, "Order was successfully submitted.");
                setClientOrderPage();
              } else {
                JOptionPane.showMessageDialog(frame, "There was an error with your order please try again.");
              }
            } catch (Empty_Order emptyOrderException) {
              JOptionPane.showMessageDialog(frame, "We cannot accept an empty order please select some items.");
            } catch (Menu_Too_Old menuTooOldException) {
              JOptionPane.showMessageDialog(frame, "Our Menu has changed please refresh your menu and reorder.");
            } catch (Incorrect_Order_Total incorrectOrderTotalException) {
              JOptionPane.showMessageDialog(frame,
                  "There was an issue calculating your total. Canceling transaction please try again.");
            } catch (Order_In_Progess incorrectOrderTotalException) {
              JOptionPane.showMessageDialog(frame,
                  "You already have an order in progress, please wait until you have recieved that order before you place another.");
            }
          }
        }
      });

      Client_Orders_Panel.add(Non_Active_Label);
      Client_Orders_Panel.add(Reorder_Button);

      divisor = 1;
    }

    JLabel All_Label = new JLabel("Past Orders");
    All_Label.setAlignmentX(Component.CENTER_ALIGNMENT);

    Client_Orders_Panel.add(All_Label);
    Client_Orders_Panel.add(allScrollPane);

    removeAllPanels();
    frame.add(Client_Orders_Panel);
    frame.setSize(
        new Dimension(Client_Orders_Panel.getPreferredSize().width,
            Client_Orders_Panel.getPreferredSize().height / divisor));
    frame.revalidate();
    frame.repaint();

  }

  public static void setAdminOrderPage() {

    try {

      ArrayList<RestaurantApp.Order> activeOrders = new ArrayList<>();
      ArrayList<RestaurantApp.Order> allOrders = new ArrayList<>();

      Collections.addAll(activeOrders, adminImpl.getAllActiveOrders(adminKey));
      Collections.addAll(allOrders, adminImpl.getAllOrders(adminKey));

      Collections.reverse(activeOrders);
      Collections.reverse(allOrders);

      JList<RestaurantApp.Order> activeOrderList = new JList<>(activeOrders.toArray(new RestaurantApp.Order[0]));
      JList<RestaurantApp.Order> allOrderList = new JList<>(allOrders.toArray(new RestaurantApp.Order[0]));

      activeOrderList.setCellRenderer(new RestaurantClient().new AdminOrderRenderer());
      allOrderList.setCellRenderer(new RestaurantClient().new AdminOrderRenderer());

      JScrollPane activeScrollPane = new JScrollPane(activeOrderList);
      JScrollPane allScrollPane = new JScrollPane(allOrderList);

      JLabel Active_Label = new JLabel("Active Orders");
      JLabel All_Label = new JLabel("All Orders");
      Active_Label.setAlignmentX(Component.CENTER_ALIGNMENT);
      All_Label.setAlignmentX(Component.CENTER_ALIGNMENT);

      JPanel Admin_Orders_Panel = new JPanel();
      Admin_Orders_Panel.setLayout(new BoxLayout(Admin_Orders_Panel, BoxLayout.Y_AXIS));
      Admin_Orders_Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      Admin_Orders_Panel.add(Active_Label);
      Admin_Orders_Panel.add(activeScrollPane);
      Admin_Orders_Panel.add(All_Label);
      Admin_Orders_Panel.add(allScrollPane);

      removeAllPanels();
      frame.add(Admin_Orders_Panel);
      frame.setSize(
          new Dimension(Admin_Orders_Panel.getPreferredSize().width, Admin_Orders_Panel.getPreferredSize().height / 2));
      frame.revalidate();
      frame.repaint();

    } catch (Incorrect_Key incorrectKeyException) {
      JOptionPane.showMessageDialog(frame, "You have an incorrect admin key please log out and log back in");
    }

  }

  public static void setClientMenuPage() {

    try {

      RestaurantApp.Menu menu = menuImpl.getMenu();
      menuVersion = menu.version;

      JList<RestaurantApp.MenuItem> menuList = new JList<>(menu.menuList);
      menuList.setCellRenderer(new RestaurantClient().new menuItemRenderer());
      JScrollPane menuScrollPane = new JScrollPane(menuList);

      clientMenuListModel = new DefaultListModel<>();
      JList<RestaurantApp.OrderItem> menuOrderList = new JList<>(clientMenuListModel);
      menuOrderList.setCellRenderer(new RestaurantClient().new orderItemRenderer());
      JScrollPane orderScrollPane = new JScrollPane(menuOrderList);

      JLabel Quantity_Label = new JLabel("1");
      Quantity_Label.setAlignmentX(Component.CENTER_ALIGNMENT);

      JButton Up_Button = new JButton("^");
      JButton Down_Button = new JButton("v");
      Up_Button.setAlignmentX(Component.CENTER_ALIGNMENT);
      Down_Button.setAlignmentX(Component.CENTER_ALIGNMENT);

      Up_Button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          int quantity = Integer.parseInt(Quantity_Label.getText());
          if (quantity < 10) {
            quantity++;
          }
          Quantity_Label.setText("" + quantity);
        }
      });

      Down_Button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          int quantity = Integer.parseInt(Quantity_Label.getText());
          if (quantity > 0) {
            quantity--;
          }
          Quantity_Label.setText("" + quantity);
        }
      });

      JPanel Up_Down_Panel = new JPanel();

      Up_Down_Panel.setLayout(new BoxLayout(Up_Down_Panel, BoxLayout.Y_AXIS));
      Up_Down_Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      Up_Down_Panel.add(Up_Button);
      Up_Down_Panel.add(Down_Button);
      Up_Down_Panel.setAlignmentX(Component.CENTER_ALIGNMENT);

      JLabel Cost_Label = new JLabel("Total Cost: $");

      JButton Add_Button = new JButton("Add");
      JButton Remove_Button = new JButton("Remove");
      Add_Button.setAlignmentX(Component.CENTER_ALIGNMENT);
      Remove_Button.setAlignmentX(Component.CENTER_ALIGNMENT);

      Add_Button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          RestaurantApp.MenuItem menuItem = menuList.getSelectedValue();
          if (menuItem != null) {
            RestaurantApp.OrderItem orderItem = new RestaurantApp.OrderItem(menuItem,
                (short) Integer.parseInt(Quantity_Label.getText()));
            clientMenuListModel.addElement(orderItem);

            int cost = 0;
            for (int i = 0; i < clientMenuListModel.size(); i++) {
              cost += (clientMenuListModel.getElementAt(i).item.cost * clientMenuListModel.getElementAt(i).quantity);
            }
            Cost_Label.setText("Total Cost: $" + cost);
          }
        }
      });

      Remove_Button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          RestaurantApp.OrderItem orderItem = menuOrderList.getSelectedValue();
          if (orderItem != null) {
            clientMenuListModel.removeElement(orderItem);

            int cost = 0;
            for (int i = 0; i < clientMenuListModel.size(); i++) {
              cost += (clientMenuListModel.getElementAt(i).item.cost * clientMenuListModel.getElementAt(i).quantity);
            }
            Cost_Label.setText("Total Cost: $" + cost);
          }
        }
      });

      JPanel Add_Remove_Panel = new JPanel();

      Add_Remove_Panel.setLayout(new BoxLayout(Add_Remove_Panel, BoxLayout.Y_AXIS));
      Add_Remove_Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      Add_Remove_Panel.add(Add_Button);
      Add_Remove_Panel.add(Remove_Button);
      Add_Remove_Panel.setAlignmentX(Component.CENTER_ALIGNMENT);

      JPanel Order_Creation_Panel = new JPanel();
      Order_Creation_Panel.setLayout(new BoxLayout(Order_Creation_Panel, BoxLayout.X_AXIS));
      Order_Creation_Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      Order_Creation_Panel.add(Up_Down_Panel);
      Order_Creation_Panel.add(Quantity_Label);
      Order_Creation_Panel.add(Add_Remove_Panel);
      Order_Creation_Panel.setAlignmentX(Component.CENTER_ALIGNMENT);

      JButton Order_Button = new JButton("Order");

      Order_Button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try {
            ArrayList<RestaurantApp.OrderItem> list = new ArrayList<>();
            int cost = 0;
            for (int i = 0; i < clientMenuListModel.size(); i++) {
              RestaurantApp.OrderItem item = clientMenuListModel.getElementAt(i);
              list.add(item);
              cost += (item.item.cost * item.quantity);
            }

            RestaurantApp.Time currentTime = new RestaurantApp.Time();
            RestaurantApp.Time finishTime = new RestaurantApp.Time();

            RestaurantApp.Order order = new RestaurantApp.Order(menuVersion,
                list.toArray(new RestaurantApp.OrderItem[0]),
                username, (short) cost,
                currentTime,
                finishTime);

            if (orderImpl.placeOrder(order)) {
              JOptionPane.showMessageDialog(frame, "Order was successfully submitted.");
              setClientOrderPage();
            } else {
              JOptionPane.showMessageDialog(frame, "There was an error with your order please try again.");
            }

          } catch (Empty_Order emptyOrderException) {
            JOptionPane.showMessageDialog(frame, "We cannot accept an empty order please select some items.");
          } catch (Menu_Too_Old menuTooOldException) {
            JOptionPane.showMessageDialog(frame, "Our Menu has changed please refresh your menu and reorder.");
          } catch (Incorrect_Order_Total incorrectOrderTotalException) {
            JOptionPane.showMessageDialog(frame,
                "There was an issue calculating your total. Canceling transaction please try again.");
          } catch (Order_In_Progess incorrectOrderTotalException) {
            JOptionPane.showMessageDialog(frame,
                "You already have an order in progress, please wait until you have recieved that order before you place another.");
          }

        }
      });

      JPanel Order_Panel = new JPanel();
      Order_Panel.setLayout(new BoxLayout(Order_Panel, BoxLayout.Y_AXIS));
      Order_Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      Order_Panel.add(orderScrollPane);
      Order_Panel.add(Order_Creation_Panel);
      Order_Panel.add(Cost_Label);
      Order_Panel.add(Order_Button);

      JPanel Client_Menu_Panel = new JPanel();
      Client_Menu_Panel.setLayout(new BoxLayout(Client_Menu_Panel, BoxLayout.X_AXIS));
      Client_Menu_Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      Client_Menu_Panel.add(menuScrollPane);
      Client_Menu_Panel.add(Order_Panel);

      removeAllPanels();
      frame.add(Client_Menu_Panel);
      frame.setSize(Client_Menu_Panel.getPreferredSize());
      frame.revalidate();
      frame.repaint();

    } catch (No_Menu_Set noMenuSetException) {
      JOptionPane.showMessageDialog(frame, "No Menu has been set yet please contact the General Manager");
    }

  }

  public static void setAdminMenuPage() {

  }

  public static void ClientLogin(String name) {
    if (name != null && !name.isEmpty()) {
      username = name;
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

  public static String timeToString(RestaurantApp.Time time) {
    return "" + time.month + "/" + time.day + "/" + time.year + " " + time.hours + ":" + time.minutes;
  }

  class menuItemRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
        JList<?> list,
        java.lang.Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {

      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

      if (value instanceof RestaurantApp.MenuItem) {
        RestaurantApp.MenuItem item = (RestaurantApp.MenuItem) value;
        setText(item.food + ":          $" + item.cost);
      }

      this.setHorizontalAlignment(JLabel.RIGHT);

      return this;
    }
  }

  class orderItemRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
        JList<?> list,
        java.lang.Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {

      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

      if (value instanceof RestaurantApp.OrderItem) {
        RestaurantApp.OrderItem item = (RestaurantApp.OrderItem) value;
        setText(item.item.food + ":          $" + item.item.cost + " x " + item.quantity + " = "
            + (item.item.cost * item.quantity));
      }

      this.setHorizontalAlignment(JLabel.RIGHT);

      return this;
    }
  }

  class ClientOrderRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
        JList<?> list,
        java.lang.Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {

      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

      if (value instanceof RestaurantApp.Order) {
        RestaurantApp.Order order = (RestaurantApp.Order) value;

        String ret = "Order Time: " + timeToString(order.orderTime) + "<br>";
        ret += "Completion Time: " + timeToString(order.completionTime) + "<br>";
        ret += "Total Cost: $" + order.totalCost + "<br>";
        for (RestaurantApp.OrderItem orderItem : order.orderList) {
          ret += orderItem.quantity + " " + orderItem.item.food + "<br>";
        }

        ret.replaceAll("\n", "<br>");
        setText("<html>" + ret + "</html>");
      }
      return this;
    }
  }

  class AdminOrderRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
        JList<?> list,
        java.lang.Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {

      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

      if (value instanceof RestaurantApp.Order) {
        RestaurantApp.Order order = (RestaurantApp.Order) value;

        String ret = order.userId + "<br>";
        ret += "Order Time: " + timeToString(order.orderTime) + "<br>";
        ret += "Completion Time: " + timeToString(order.completionTime) + "<br>";
        ret += "Total Cost: $" + order.totalCost + "<br>";
        for (RestaurantApp.OrderItem orderItem : order.orderList) {
          ret += orderItem.quantity + " " + orderItem.item.food + "<br>";
        }

        setText("<html>" + ret + "</html>");
      }
      return this;
    }
  }
}