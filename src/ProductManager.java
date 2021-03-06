import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {

    private static final Map<String, ResourceFormatter> formatters
            = Map.of("en-GB", new ResourceFormatter(Locale.UK),
            "en-US", new ResourceFormatter(Locale.US),
            "fr-FR", new ResourceFormatter(Locale.FRANCE),
            "ru-RU", new ResourceFormatter(new Locale("ru", "RU")),
            "zh-CN", new ResourceFormatter(Locale.CHINA));
    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());
    private Map<Product, List<Review>> products = new HashMap<>();
    private ResourceFormatter formatter;
    private ResourceBundle config = ResourceBundle.getBundle("config");
    private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
    private Path reportsFolder = Path.of(config.getString("reports.folder"));
    private Path dataFolder = Path.of(config.getString("data.folder"));
    private Path tempFolder = Path.of(config.getString("temp.folder"));

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    public void changeLocale(String languageTag) {
        formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    }

    public Product createProduct(int id, String name, BigDecimal price, LocalDate bestBefore) {
        Product product = new Food(id, name, price, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price) {
        Product product = new Drink(id, name, price);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product finProduct(int id) throws ProductManagerException {
        return products.keySet()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProductManagerException("Product with id " + id + " not found"));
//        Product result = null;
//        for (Product product : products.keySet()) {
//            if (product.getId() == id) {
//                result = product;
//                break;
//            }
//        }
//        return result;
//        return products.keySet()
//                .stream()
//                .filter(p -> p.getId() == id)
//                .findFirst()
//                .orElseGet(() -> null);
    }

    public Product reviewProduct(int id, String comments) {
        try {
            return reviewProduct(finProduct(id), comments);
        } catch (ProductManagerException ex) {
            logger.log(Level.INFO, ex.getMessage());
        }
        return null;

    }

    public Product reviewProduct(Product product, String comments) {

        List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(comments));
        products.put(product, reviews);
        return product;
    }

    public void printProductReport(Product product) throws IOException {
        List<Review> reviews = products.get(product);
        Collections.sort(reviews);
        Path productFile = reportsFolder.resolve(MessageFormat.format(config.getString("report.file"), product.getId()));

        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(productFile, StandardOpenOption.CREATE), "UTF-8"))) {
            out.append(formatter.formatProduct(product) + System.lineSeparator());
            if (reviews.isEmpty()) {
                out.append(formatter.getText("no.reviews") + System.lineSeparator());
            } else {
                out.append(reviews.stream().map(r -> formatter.formatReview(r) + System.lineSeparator()).collect(Collectors.joining((CharSequence) Collectors.joining())));
            }
        }


//        StringBuilder txt = new StringBuilder();
//
//        txt.append(formatter.formatProduct(product));
//        if (product.getBestBefore() != null) {
//            txt.append(formatter.formatProduct(product));
//        } else {
//            txt.append("");
//        }
//        txt.append('\n');
//        Collections.sort(reviews);
//        for (Review review : reviews) {
//            txt.append(formatter.formatReview(review));
//            txt.append('\n');
//        }
//        if (reviews.isEmpty()) {
//            txt.append(formatter.getText("no.reviews") + '\n');
//        } else {
//            txt.append(reviews.stream()
//                    .map(r -> formatter.formatReview(r) + '\n')
//                    .collect(Collectors.joining())
//            );
//        }
//        System.out.println(txt);

    }

    public void printProductReport(int id) {
        try {
            printProductReport(finProduct(id));
        } catch (ProductManagerException ex) {
            logger.log(Level.INFO, ex.getMessage());
        }
        catch (IOException ex){
            logger.log(Level.SEVERE, "Error printing product report"+ex.getMessage(), ex);
        }

    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
//        List<Product> productsList = new ArrayList<>(products.keySet());
//        productsList.sort(sorter);
        StringBuilder txt = new StringBuilder();
        products.keySet().stream()
                .sorted(sorter)
                .filter(filter).
                forEach(p -> txt.append(formatter.formatProduct(p) + '\n'));
//        for (Product product : productsList) {
//            txt.append(formatter.formatProduct(product));
//            txt.append('\n');
//        }
        System.out.println(txt);
    }

    public void parseReview(String text) {
        try {
            Object[] values = reviewFormat.parse(text);
            reviewProduct(Integer.parseInt((String) values[0]), (String) values[1]);
        } catch (ParseException | NumberFormatException ex) {
            logger.log(Level.WARNING, "Error parsing review " + text, ex);
        }

    }

    public void parseProduct(String text) {
        try {
            Object[] values = productFormat.parse(text);
            int id = Integer.parseInt((String) values[1]);
            String name = (String) values[2];
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
            switch ((String) values[0]) {
                case "D":
                    createProduct(id, name, price);
                    break;
                case "F":
                    LocalDate bestBefore = LocalDate.parse((String) values[4]);
                    createProduct(id, name, price, bestBefore);
            }
//            reviewProduct(Integer.parseInt((String) values[0]), (String) values[1]);
        } catch (ParseException | NumberFormatException | DateTimeException ex) {
            logger.log(Level.WARNING, "Error parsing product " + text + " " + ex.getMessage());
        }
    }

    private static class ResourceFormatter {
        private final Locale locale;
        private final ResourceBundle resources;
        private final DateTimeFormatter dateFormat;
        private final NumberFormat moneyFormat;

        private ResourceFormatter(Locale locale) {
            this.locale = locale;
            resources = ResourceBundle.getBundle("resources", locale);
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy((locale));
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatProduct(Product product) {
            return MessageFormat.format(resources.getString("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    dateFormat.format(product.getBestBefore()));
        }

        private String formatReview(Review review) {
            return MessageFormat.format(resources.getString("review"),
                    review.getComments());
        }

        private String getText(String key) {
            return resources.getString(key);
        }
    }
}
