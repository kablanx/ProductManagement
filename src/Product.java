import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;

import static java.math.RoundingMode.HALF_UP;

/**
 * {@code Product} class represents properties and behaviours of product objects
 * in the Product Management System
 * Each product has an id, name, and price
 * Each product can have a discount, calculated based on a
 * {@link DISCOUNT_RATE discount rate}
 */
public class Product {
    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);

    private int id;
    private String name;
    private BigDecimal price;
    public LocalDate bestBefore;

    /*public */Product(){
        this.id=0;
        this.name="no name";
        this.price=BigDecimal.valueOf(0.00);
        this.bestBefore=LocalDate.now().plusDays(5);
    }

    /*public*/ Product(int id, String name, BigDecimal price){
        this.id=id;
        this.name=name;
        this.price=price;
        this.bestBefore=LocalDate.now().plusDays(5);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount(){
        return price.multiply(DISCOUNT_RATE).setScale(2, HALF_UP);
    }

    public LocalDate getBestBefore(){
        return bestBefore;
    }

    @Override
    public String toString(){
        return id+".- "+name+", "+price+", "+getDiscount();
    }

    @Override
    public int hashCode(){
        int hash=5;
        hash=23*hash+this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj){
        if(this==obj){
            return true;
        }
        if(obj instanceof Product){
            final Product other=(Product) obj;
            return this.id==other.id;
        }
        return false;
    }
}
