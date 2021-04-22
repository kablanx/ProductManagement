import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;

/**
 * {@code shop} class represents an application that manages Products
 */

public class Shop {
    public static void main(String[] args) {
        ProductManager pm = new ProductManager("en-GB");

//        pm.createProduct(232, "Cookies", BigDecimal.valueOf(1.99), LocalDate.now().plusDays(3));
        pm.parseProduct("F,22, Tea, 2");
        pm.parseProduct("D,232,Tea,2,2019-09-19");

//        pm.parseReview("232, Nice hot cup of tea");
//        pm.parseReview("232, Fine tea");
//        pm.parseReview("232, Perfect tea");
        pm.printProductReport(232);
        pm.printProductReport(22);

//        pm.createProduct(323, "Monster", BigDecimal.valueOf(1.99));
//        pm.reviewProduct(323, "Nice drink!");
//        pm.reviewProduct(323, "I always drink it, it's the best!");
//        pm.reviewProduct(323, "Simply, perfect!");
//
//        pm.createProduct(1, "Nigga", BigDecimal.valueOf(23));
//        pm.reviewProduct(1, "The best!");
//
//        pm.changeLocale("ru-RU");
//
//        pm.createProduct(2, "Mirkito", BigDecimal.valueOf(69.69));
//        pm.reviewProduct(2, "Pussy ");
//
//        pm.createProduct(5, "Pepsi", BigDecimal.valueOf(100.99));
//        pm.reviewProduct(5, "Mmmmm...");
//
//        pm.printProducts(p -> p.getPrice().floatValue() < 2, (p1, p2) -> p2.getPrice().compareTo(p1.getPrice()));
//
//        Comparator<Product> priceSorted=(p1,p2)->p2.getPrice().compareTo(p1.getPrice());
//        pm.printProducts(priceSorted);
//

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
