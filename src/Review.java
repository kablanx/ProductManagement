public class Review implements Comparable<Review> {
    private String comments;

    public Review(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "Review{comments= " + comments + '}';
    }

    @Override
    public int compareTo(Review o) {
        return 0;
    }
}
