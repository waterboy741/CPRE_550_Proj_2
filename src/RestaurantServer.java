import RestaurantApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

class AdminImpl extends Admin_IntPOA {
  private ORB orb;

  private static final String adminUserName = "admin";
  private static final String adminPassword = "password";
  private static final int adminKey = 23409587;

  private MenuImpl menuInt;
  private OrderImpl orderInt;

  public void setORB(ORB orb_val) {
    orb = orb_val;
  }

  public void setMenuInt(MenuImpl impl) {
    menuInt = impl;
  }

  public void setOrderInt(OrderImpl impl) {
    orderInt = impl;
  }

  // implement getAdminKey() method
  public int getAdminKey(String username, String password) throws Incorrect_Password {
    if (username == null || password == null) {
      throw new Incorrect_Password();
    }

    if (username.equals(adminUserName) && password.equals(adminPassword)) {
      return adminKey;
    } else {
      throw new Incorrect_Password();
    }
  }

  // implement setMenu() method
  public boolean setMenu(RestaurantApp.MenuItem[] menu, int key) throws Incorrect_Key {
    if (menu == null) {
      return false;
    }

    if (key == adminKey) {
      menuInt.menu(new RestaurantApp.Menu(++menuInt.menu().version, menu));
      return true;
    } else {
      throw new Incorrect_Key();
    }
  }

  // implement getAllOrders() method
  public RestaurantApp.Order[] getAllOrders(int key) throws Incorrect_Key {
    if (key == adminKey) {
      return orderInt.orders();
    } else {
      throw new Incorrect_Key();
    }
  }

  // implement getAllActiveOrders() method
  public RestaurantApp.Order[] getAllActiveOrders(int key) throws Incorrect_Key {
    if (key != adminKey) {
      throw new Incorrect_Key();
    }

    ArrayList<RestaurantApp.Order> orders = new ArrayList<RestaurantApp.Order>();

    LocalDateTime currentTime = LocalDateTime.now();

    for (Order order : orderInt.orders()) {

      LocalDateTime completionTime = LocalDateTime.of(
          order.completionTime.year,
          order.completionTime.month,
          order.completionTime.day,
          order.completionTime.hours,
          order.completionTime.minutes,
          0, 0);

      if (completionTime.isAfter(currentTime)) {
        orders.add(order);
      }
    }
    return orders.toArray(new RestaurantApp.Order[0]);
  }

}

class MenuImpl extends Menu_IntPOA {
  private ORB orb;
  private RestaurantApp.Menu menu;

  public void setORB(ORB orb_val) {
    orb = orb_val;
  }

  public RestaurantApp.Menu menu() {
    return this.menu;
  }

  public void menu(RestaurantApp.Menu newMenu) {
    this.menu = newMenu;
  }
}

class OrderImpl extends Order_IntPOA {
  private ORB orb;
  private MenuImpl menuInt;
  private ArrayList<RestaurantApp.Order> storedOrders = new ArrayList<>();

  private static final short orderDuration = 1;

  public void setORB(ORB orb_val) {
    orb = orb_val;
  }

  public void setMenuInt(MenuImpl impl) {
    menuInt = impl;
  }

  public RestaurantApp.Order[] orders() {
    return storedOrders.toArray(new RestaurantApp.Order[0]);
  }

  public void orders(RestaurantApp.Order[] newOrders) {
    storedOrders = new ArrayList<RestaurantApp.Order>();
    storedOrders.addAll(storedOrders);
  }

  // implement placeOrder() method
  public boolean placeOrder(RestaurantApp.Order order)
      throws Empty_Order, Menu_Too_Old, Incorrect_Order_Total, Order_In_Progess {

    if (order == null) {
      throw new Empty_Order();
    } else if (order.orderList.length == 0) {
      throw new Empty_Order();
    }

    int tempCost = 0;
    // Verify that this was ordered on the correct menu version
    if (order.menuVersion == menuInt.menu().version) {

      // Verify that the user calculated the total cost correctl
      for (RestaurantApp.OrderItem orderItem : order.orderList) {
        for (RestaurantApp.MenuItem menu_Item : menuInt.menu().menuList) {
          if (orderItem.item.food.equals(menu_Item.food)) {
            tempCost += menu_Item.cost * orderItem.quantity;
          }
        }
      }
    } else {
      throw new Menu_Too_Old();
    }
    if (tempCost == order.totalCost) {
      LocalDateTime currentTime = LocalDateTime.now();
      RestaurantApp.Time orderTime = new RestaurantApp.Time(
          (short) currentTime.getYear(),
          (short) currentTime.getMonthValue(),
          (short) currentTime.getDayOfMonth(),
          (short) currentTime.getHour(),
          (short) currentTime.getMinute());

      LocalDateTime newTime = currentTime.plusMinutes(orderDuration);
      RestaurantApp.Time completionTime = new RestaurantApp.Time(
          (short) newTime.getYear(),
          (short) newTime.getMonthValue(),
          (short) newTime.getDayOfMonth(),
          (short) newTime.getHour(),
          (short) newTime.getMinute());

      if (getLatestActiveOrder(order.userId) == null) {
        storedOrders.add(new RestaurantApp.Order(
            order.menuVersion,
            order.orderList,
            order.userId,
            order.totalCost,
            orderTime,
            completionTime));
        return true;
      } else {
        throw new Order_In_Progess();
      }

    } else {
      throw new Incorrect_Order_Total();
    }
  }

  private RestaurantApp.Order getLatestActiveOrder(String userId) {
    LocalDateTime currentTime = LocalDateTime.now();

    for (int i = storedOrders.size() - 1; i >= 0; i--) {
      RestaurantApp.Order order = storedOrders.get(i);
      if (order.userId.equals(userId)) {
        LocalDateTime completionTime = LocalDateTime.of(
            order.completionTime.year,
            order.completionTime.month,
            order.completionTime.day,
            order.completionTime.hours,
            order.completionTime.minutes,
            0, 0);

        if (completionTime.isAfter(currentTime)) {
          return order;
        } else {
          return null;
        }
      }
    }
    return null;
  }

  // implement getActiveOrder() method
  public RestaurantApp.Order getActiveOrder(String userId) throws No_Active_Order {
    RestaurantApp.Order order = getLatestActiveOrder(userId);

    if (order == null) {
      throw new No_Active_Order();
    }

    return order;

  }

  // implement getPreviousOrders() method
  public RestaurantApp.Order[] getPreviousOrders(String userId) {
    ArrayList<RestaurantApp.Order> orders = new ArrayList<RestaurantApp.Order>();

    for (Order order : storedOrders) {
      if (order.userId.equals(userId)) {
        orders.add(order);
      }
    }
    return orders.toArray(new RestaurantApp.Order[0]);
  }

}

public class RestaurantServer {

  public static void main(String args[]) {

    try {

      // create and initialize the ORB
      ORB orb = ORB.init(args, null);

      // get reference to rootpoa & activate the POAManager
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();

      // create servants and register them with the ORB
      MenuImpl menuImpl = new MenuImpl();
      menuImpl.setORB(orb);
      MenuItem[] menuItems = {
          new MenuItem("Burger", (short) 10),
          new MenuItem("Fries", (short) 5),
          new MenuItem("Cola", (short) 1)
      };
      menuImpl.menu(new Menu((short) 0, menuItems));

      OrderImpl orderImpl = new OrderImpl();
      orderImpl.setORB(orb);
      orderImpl.setMenuInt(menuImpl);

      AdminImpl adminImpl = new AdminImpl();
      adminImpl.setORB(orb);
      adminImpl.setMenuInt(menuImpl);
      adminImpl.setOrderInt(orderImpl);

      // get object reference from the servant
      org.omg.CORBA.Object adminRef = rootpoa.servant_to_reference(adminImpl);
      Admin_Int aref = Admin_IntHelper.narrow(adminRef);

      // get object reference from the servant
      org.omg.CORBA.Object menuRef = rootpoa.servant_to_reference(menuImpl);
      Menu_Int mref = Menu_IntHelper.narrow(menuRef);

      // get object reference from the servant
      org.omg.CORBA.Object orderRef = rootpoa.servant_to_reference(orderImpl);
      Order_Int oref = Order_IntHelper.narrow(orderRef);

      // get the root naming context
      // NameService invokes the name service
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
      // Use NamingContextExt which is part of the Interoperable
      // Naming Service (INS) specification.
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

      // bind the Object Reference in Naming
      String adminName = "Admin_Int";
      String menuName = "Menu_Int";
      String orderName = "Order_Int";

      NameComponent apath[] = ncRef.to_name(adminName);
      NameComponent mpath[] = ncRef.to_name(menuName);
      NameComponent opath[] = ncRef.to_name(orderName);

      ncRef.rebind(apath, aref);
      ncRef.rebind(mpath, mref);
      ncRef.rebind(opath, oref);

      System.out.println("Server ready and waiting ...\n");

      // wait for invocations from clients
      orb.run();
    }

    catch (Exception e) {
      System.err.println("ERROR: " + e);
      e.printStackTrace(System.out);
    }

    System.out.println("Server Exiting ...");

  }
}