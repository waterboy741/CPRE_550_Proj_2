import RestaurantApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

public class RestaurantClient {
  static Admin_Int adminImpl;
  static Menu_Int menuImpl;
  static Order_Int orderImpl;

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

      // System.out.println(adminImpl.getAdminKey("admin", "password"));
      // System.out.println(menuImpl.getMenu() == null);
      // System.out.println(orderImpl.placeOrder(new RestaurantApp.Order()));

      RestaurantApp.Menu menu = menuImpl.getMenu();
      System.out.println("Initial Version");
      System.out.println(menu.version);
      for (RestaurantApp.MenuItem item : menu.menuList) {
        System.out.println(item.food);
        System.out.println(item.cost);
      }

      System.out.println(adminImpl.setMenu(menu.menuList, adminImpl.getAdminKey("admin", "password")));
      System.out.println("New Version");
      System.out.println(menuImpl.getMenu().version);

      // ==================================================================

    } catch (Exception e) {
      System.out.println("ERROR : " + e);
      e.printStackTrace(System.out);
    }
  }

}