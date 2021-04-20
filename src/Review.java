public class Review {
    private String comments;

    public Review(String comments){
        this.comments=comments;
    }

    public String getComments(){
        return comments;
    }

    @Override
    public String toString(){
        return "Review{comments= "+comments+'}';
    }
}
