import RestaurantApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

public class RestaurantClient {
  static Hello helloImpl;
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
      String helloName = "Hello";
      String adminName = "Admin_Int";
      String menuName = "Menu_Int";
      String orderName = "Order_Int";

      helloImpl = HelloHelper.narrow(ncRef.resolve_str(helloName));
      adminImpl = Admin_IntHelper.narrow(ncRef.resolve_str(adminName));
      menuImpl = Menu_IntHelper.narrow(ncRef.resolve_str(menuName));
      orderImpl = Order_IntHelper.narrow(ncRef.resolve_str(orderName));

      System.out.println("Obtained a handle on server object: " + helloImpl);
      System.out.println(helloImpl.sayHello());

      System.out.println(adminImpl.getAdminKey("a", "b"));
      System.out.println(adminImpl.setMenu(new RestaurantApp.MenuItem[0], adminImpl.getAdminKey("a", "b")));
      // System.out.println(orderImpl.placeOrder(new RestaurantApp.Order()));

    } catch (Exception e) {
      System.out.println("ERROR : " + e);
      e.printStackTrace(System.out);
    }
  }

}