import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

/**
 * {@code shop} class represents an application that manages Products
 */

public class Shop {
    public static void main(String[] args) {
        ProductManager pm=new ProductManager(Locale.FRANCE);
        Product p1=pm.createProduct(232, "Tea", BigDecimal.valueOf(1.99), LocalDate.now().plusDays(3));
        pm.printProductReport(p1);
        p1=pm.reviewProduct(p1,"Nice hot cup of tea");
        p1=pm.reviewProduct(p1,"Fine tea");
        p1=pm.reviewProduct(p1,"Perfect tea");
        pm.printProductReport(p1);
        /* Product p1 = new Food(1, "Papas", BigDecimal.valueOf(2.3), LocalDate.now().plusDays(3));
        Product p2 = new Drink(2, "Monster", BigDecimal.valueOf(1.5));
        Product p3 = new Product(3, "Mirko", BigDecimal.valueOf(2.3));
        Product p4 = new Product();
        Product p5 = pm.createProduct(23, "Nigga", BigDecimal.valueOf(2.32), LocalDate.now().plusDays(4));
        System.out.println(p1);
        System.out.println(p2.toString());
        System.out.println(p3);
        System.out.println(p4);
        System.out.println(p5);*/
    }
}
