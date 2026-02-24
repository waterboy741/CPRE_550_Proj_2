import RestaurantApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

import java.util.ArrayList;
import java.util.Properties;

class HelloImpl extends HelloPOA {
  private ORB orb;

  public void setORB(ORB orb_val) {
    orb = orb_val;
  }

  // implement sayHello() method
  public String sayHello() {
    return "\nHello world !!\n";
  }
}

class AdminImpl extends Admin_IntPOA {
  private ORB orb;

  public void setORB(ORB orb_val) {
    orb = orb_val;
  }

  // implement getAdminKey() method
  public int getAdminKey(String username, String password) {
    return 1333;
  }

  // implement setMenu() method
  public boolean setMenu(RestaurantApp.MenuItem[] menu, int key) {
    return false;
  }

  // implement getAllOrders() method
  public RestaurantApp.Order[] getAllOrders(int key) {
    return new ArrayList<RestaurantApp.Order>().toArray(new RestaurantApp.Order[0]);
  }
}

class MenuImpl extends Menu_IntPOA {
  private ORB orb;

  public void setORB(ORB orb_val) {
    orb = orb_val;
  }

  // implement getMenu() method
  public RestaurantApp.Menu getMenu() {
    return new Menu();
  }

}

class OrderImpl extends Order_IntPOA {
  private ORB orb;

  public void setORB(ORB orb_val) {
    orb = orb_val;
  }

  // implement placeOrder() method
  public boolean placeOrder(RestaurantApp.Order order) {
    return true;
  }

  // implement getOrder() method
  public RestaurantApp.Order getOrder(String userId) {
    return new RestaurantApp.Order();
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
      HelloImpl helloImpl = new HelloImpl();
      helloImpl.setORB(orb);

      AdminImpl adminImpl = new AdminImpl();
      adminImpl.setORB(orb);

      MenuImpl menuImpl = new MenuImpl();
      menuImpl.setORB(orb);

      OrderImpl orderImpl = new OrderImpl();
      orderImpl.setORB(orb);

      // get object reference from the servant
      org.omg.CORBA.Object helloRef = rootpoa.servant_to_reference(helloImpl);
      Hello href = HelloHelper.narrow(helloRef);

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
      String helloName = "Hello";
      String adminName = "Admin_Int";
      String menuName = "Menu_Int";
      String orderName = "Order_Int";

      NameComponent hpath[] = ncRef.to_name(helloName);
      NameComponent apath[] = ncRef.to_name(adminName);
      NameComponent mpath[] = ncRef.to_name(menuName);
      NameComponent opath[] = ncRef.to_name(orderName);

      ncRef.rebind(hpath, href);
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