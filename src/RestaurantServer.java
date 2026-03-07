import RestaurantApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

class DataStorage {

  public RestaurantApp.Menu menu;
  public ArrayList<RestaurantApp.Order> orders;

  public DataStorage() {
    MenuItem[] menuItems = {
        new MenuItem("Burger", (short) 10),
        new MenuItem("Fries", (short) 5),
        new MenuItem("Cola", (short) 1)
    };
    menu = new Menu((short) 0, menuItems);
    orders = new ArrayList<>();
  }

}

class AdminImpl extends Admin_IntPOA {
  private ORB orb;

  private static final String adminUserName = "admin";
  private static final String adminPassword = "password";
  private static final int adminKey = 23409587;

  private DataStorage storage;

  public void setORB(ORB orb_val) {
    orb = orb_val;
  }

  public void setDataStorage(DataStorage data) {
    storage = data;
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
      storage.menu = new RestaurantApp.Menu(++storage.menu.version, menu);
      return true;
    }
    return false;
  }

  // implement getAllOrders() method
  public RestaurantApp.Order[] getAllOrders(int key) throws Incorrect_Key {
    return storage.orders.toArray(new RestaurantApp.Order[0]);
  }

  // implement getAllActiveOrders() method
  public RestaurantApp.Order[] getAllActiveOrders(int key) throws Incorrect_Key {
    ArrayList<RestaurantApp.Order> orders = new ArrayList<RestaurantApp.Order>();

    LocalDateTime currentTime = LocalDateTime.now();
    RestaurantApp.Time compareTime = new RestaurantApp.Time(
        (short) currentTime.getYear(),
        (short) currentTime.getMonthValue(),
        (short) currentTime.getDayOfMonth(),
        (short) currentTime.getHour(),
        (short) currentTime.getMinute());

    for (Order order : storage.orders) {
      if (order.completionTime.year >= compareTime.year &&
          order.completionTime.month >= compareTime.month &&
          order.completionTime.day >= compareTime.day &&
          order.completionTime.hours >= compareTime.hours &&
          order.completionTime.minutes >= compareTime.minutes) {
        orders.add(order);
      }
    }
    return orders.toArray(new RestaurantApp.Order[0]);
  }

}

class MenuImpl extends Menu_IntPOA {
  private ORB orb;
  private DataStorage storage;

  public void setORB(ORB orb_val) {
    orb = orb_val;
  }

  public void setDataStorage(DataStorage data) {
    storage = data;
  }

  // implement getMenu() method
  public RestaurantApp.Menu getMenu() throws No_Menu_Set {
    if (storage == null || storage.menu == null || storage.menu.menuList == null) {
      throw new No_Menu_Set();
    }

    return storage.menu;
  }

}

class OrderImpl extends Order_IntPOA {
  private ORB orb;
  private DataStorage storage;

  private static final short orderDuration = 5;

  public void setORB(ORB orb_val) {
    orb = orb_val;
  }

  public void setDataStorage(DataStorage data) {
    storage = data;
  }

  // implement placeOrder() method
  public boolean placeOrder(RestaurantApp.Order order) throws Empty_Order, Menu_Too_Old, Incorrect_Order_Total {

    if (order == null) {
      throw new Empty_Order();
    } else if (order.orderList.length == 0) {
      throw new Empty_Order();
    }

    int tempCost = 0;
    // Verify that this was ordered on the correct menu version
    if (order.menuVersion == storage.menu.version) {

      // Verify that the user calculated the total cost correctl
      for (RestaurantApp.OrderItem orderItem : order.orderList) {
        for (RestaurantApp.MenuItem menu_Item : storage.menu.menuList) {
          if (orderItem.item.food.equals(menu_Item.food)) {
            tempCost += menu_Item.cost;
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

      RestaurantApp.Time completionTime = new RestaurantApp.Time(
          orderTime.year,
          orderTime.month,
          orderTime.day,
          orderTime.hours,
          (short) (orderTime.minutes + orderDuration));

      storage.orders.add(new RestaurantApp.Order(
          order.menuVersion,
          order.orderList,
          order.userId,
          order.totalCost,
          orderTime,
          completionTime));
      return true;
    } else {
      throw new Incorrect_Order_Total();
    }
  }

  // implement getActiveOrder() method
  public RestaurantApp.Order getActiveOrder(String userId) {

    for (int i = storage.orders.size() - 1; i >= 0; i--) {
      RestaurantApp.Order order = storage.orders.get(i);
      if (order.userId.equals(userId)) {
        return order;
      }
    }
    return null;
  }

  // implement getPreviousOrders() method
  public RestaurantApp.Order[] getPreviousOrders(String userId) {
    ArrayList<RestaurantApp.Order> orders = new ArrayList<RestaurantApp.Order>();

    for (Order order : storage.orders) {
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

      DataStorage data = new DataStorage();

      // create and initialize the ORB
      ORB orb = ORB.init(args, null);

      // get reference to rootpoa & activate the POAManager
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();

      // create servants and register them with the ORB
      AdminImpl adminImpl = new AdminImpl();
      adminImpl.setORB(orb);
      adminImpl.setDataStorage(data);

      MenuImpl menuImpl = new MenuImpl();
      menuImpl.setORB(orb);
      menuImpl.setDataStorage(data);

      OrderImpl orderImpl = new OrderImpl();
      orderImpl.setORB(orb);
      orderImpl.setDataStorage(data);

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