package recommender;

import java.util.Iterator;

/** recommender.UserNode. The class represents a node in the recommender.UsersList.
 *  Stores a userId, a list of ratings of type MovieRatingsList,
 *  and a reference to the "next" user in the list.
 *  FILL IN CODE in methods getFavoriteMovies and getLeastFavoriteMovies
 *
 *  Do not modify signatures of methods.
 *  */
public class UserNode {
    private int userId;
    private RatingsList movieRatings;
    private UserNode nextUser;

    /** A constructor for the recommender.UserNode.
     * @param id 	User id
     * */
    public UserNode(int id) {
        userId = id;
        movieRatings = new RatingsList();
        nextUser = null;
    }

    /**
     * Getter for the next reference
     * @return the next node in the linked list of users
     */
    public UserNode next() {
        return nextUser;
    }

    /**
     * Setter for the next reference
     * @param anotherUserNode A user node
     */
    public void setNext(UserNode anotherUserNode) {
        this.nextUser = anotherUserNode;
    }

    /** Return a userId stored in this node */
    public int getId() {
        return userId;
    }

    /** Print info contained in this node:
     *  userId and a list of ratings.
     *  Expected format: (userid) movieId:rating; movieId:rating; movieId:rating; */
    public void print() {
        System.out.print("(" + userId + ") ");
        movieRatings.print();
    }

    public String toString(){
        String result = "(" + userId + ") ";
        Iterator<RatingNode> it = movieRatings.iterator();
        while (it.hasNext()){
            RatingNode nextNode = it.next();
            result += nextNode.getMovieId() + ":" + nextNode.getMovieRating() + "; ";
        }
         return result;
    }


    /**
     * Add rating info for a given movie to the MovieRatingsList
     *  for this user node
     *
     * @param movieId id of the movie
     * @param rating  rating of the movie
     */
    public void insert(int movieId, double rating) {
        movieRatings.insertByRating(movieId, rating);

    }

    /**
     * Returns an array of user's favorite movies (up to n). These are the
     * movies that this user rated the highest.
     *
     * @param n  number of movies to return
     * @return array containing movie ids this user rated the highest
     */
    public int[] getFavoriteMovies(int n) {
        // FILL IN CODE
        int[] favoriteMovies = new int[n];
        RatingsList nBestRatedMovies = movieRatings.getNBestRankedMovies(n).sublist(5,5);
        Iterator<RatingNode> it = nBestRatedMovies.iterator();
        for (int i = 0; i < n; i++){
            if (it.hasNext()) {
                favoriteMovies[i] = it.next().getMovieId();
                System.out.println(favoriteMovies[i]);
            }
        }
        return favoriteMovies;
    }


    /**
     * Returns an array of movies the user likes the least (up to n).
     *
     * @param n the maximum number of movies to return
     * @return array of movie ids the user rated the lowest
     */
    public int[] getLeastFavoriteMovies(int n) {
        // FILL IN CODE
        int[] leastFavoriteMovies = new int[n];
        RatingsList nWorstRatedMovies = movieRatings.getNWorstRankedMovies(n).sublist(1,1);
        Iterator<RatingNode> it = nWorstRatedMovies.iterator();
        for (int i = 0; i < n; i++){
            if (it.hasNext()){
                leastFavoriteMovies[i] = it.next().getMovieId();
            }
        }
        return leastFavoriteMovies; // don't forget to change
    }

    /**
     * Computes the similarity of this user with the given "other" user using
     * Pearson correlation - simply calls computeSimilarity method
     * from MovieRatingsList
     *
     * @param otherUser a user to compare the current user with
     * @return similarity score
     */
    public double computeSimilarity(UserNode otherUser) {
        return movieRatings.computeSimilarity(otherUser.movieRatings);
    }

    public boolean hasRated(int movieId){
        return movieRatings.getRating(movieId) != -1;
    }

}