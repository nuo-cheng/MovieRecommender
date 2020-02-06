package recommender;

import java.io.*;
import java.util.HashMap;

/** recommender.MovieRecommender. A class that is responsible for:
 - Reading movie and ratings data from the file and loading it in the data structure recommender.UsersList.
 *  - Computing movie recommendations for a given user and printing them to a file.
 *  - Computing movie "anti-recommendations" for a given user and printing them to file.
 *  Fill in code in methods of this class.
 *  Do not modify signatures of methods.
 */
public class MovieRecommender {
    private UsersList usersData; // linked list of users
    private HashMap<Integer, String> movieMap; // maps each movieId to the movie title

    public MovieRecommender() {
        movieMap = new HashMap<>();
        usersData = new UsersList();
    }

    /**
     * Read user ratings from the file and save data for each user in this list.
     * For each user, the ratings list will be sorted by rating (from largest to
     * smallest).
     * @param movieFilename name of the file with movie info
     * @param ratingsFilename name of the file with ratings info
     */
    public void loadData(String movieFilename, String ratingsFilename) {

        loadMovies(movieFilename);
        loadRatings(ratingsFilename);
    }

    /** Load information about movie ids and titles from the given file.
     *  Store information in a hashmap that maps each movie id to a movie title
     *
     * @param movieFilename csv file that contains movie information.
     *
     */
    private void loadMovies(String movieFilename) {
        // FILL IN CODE
        String line = "";
        String splitBy = ",";
        try(BufferedReader br = new BufferedReader(new FileReader(movieFilename))){
            while ((line = br.readLine()) != null){
                String[] movie = line.split(splitBy);
                assert(movie.length != 0);
//                if (movie.length < 2) {
//                    System.out.println("id " + movie[0] + " has no movie name");
//                }
//                System.out.println("id" + movie[0] + "    " + movie[1]);
                if(Character.isDigit(movie[0].charAt(0))){
                    int movieId = Integer.parseInt(movie[0]);
                    String movieName = "";
                    for (int i = 1; i + 1 < movie.length; i++) {
                        if (i >= 2) {
                            movieName += ",";
                        }
                        movieName += movie[i];
                    }
                    if (movieName.charAt(0) == '\"') {
                        String movieNameCopy = movieName;
                        movieName = "";
                        for (int i = 1; i + 1 < movieNameCopy.length(); i++) {
                            movieName += movieNameCopy.charAt(i);
                        }
                    }
                    movieMap.put(movieId, movieName);
                }
            }
            System.out.println(movieMap);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "MovieRecommender{" +
                "movieMap=" + movieMap +
                '}';
    }

    /**
     * Load users' movie ratings from the file into recommender.UsersList
     * @param ratingsFilename name of the file that contains ratings
     */
    private void loadRatings(String ratingsFilename) {
        // FILL IN CODE
        String line = "";
        String splitBy = ",";
        try(BufferedReader br = new BufferedReader(new FileReader(ratingsFilename))){
            while ((line = br.readLine()) != null){
                String[] rating = line.split(splitBy, 4);
                assert(rating.length != 0);
//                System.out.println("userId" + rating[0] + "    " + "movieId" + rating[1] + "   " + "rating" + rating[2] +"   "+ "time" + rating[3]);
                if (Character.isDigit(rating[0].charAt(0))){
                    int userId = Integer.parseInt(rating[0]);
                    int movieId = Integer.parseInt(rating[1]);
                    float rate = Float.parseFloat(rating[2]);
                    usersData.insert(userId, movieId, rate);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * * Computes up to num movie recommendations for the user with the given user
     * id and prints these movie titles to the given file. First calls
     * findMostSimilarUser and then getFavoriteMovies(num) method on the
     * "most similar user" to get up to num recommendations. Prints movies that
     * the user with the given userId has not seen yet.
     * @param userid id of the user
     * @param num max number of recommendations
     * @param filename name of the file where to output recommended movie titles
     *                 Format of the file: one movie title per each line
     */
    public void findRecommendations(int userid, int num, String filename) {

        // compute similarity between userid and all the other users
        // find the most similar user and recommend movies that the most similar
        // user rated as 5.
        // Recommend only the movies that userid has not seen (has not
        // rated).
        // FILL IN CODE
        String res = "";
        UserNode thisUser = usersData.get(userid);
        UserNode mostSimilarUser = usersData.findMostSimilarUser(userid);
        int[] favoriteMovies = mostSimilarUser.getFavoriteMovies(num);
        for (int i = 0; i < favoriteMovies.length; i++){
            if (!thisUser.hasRated(favoriteMovies[i])){
                if (null == movieMap.get(favoriteMovies[i])) {
                    continue;
                }
                res += movieMap.get(favoriteMovies[i]);
                res += "\n";
            }
        }
        try {
            BufferedWriter out = new BufferedWriter((new FileWriter(filename)));
            out.write(res);
            out.close();
        } catch (IOException e){

        }


    }

    /**
     * Computes up to num movie anti-recommendations for the user with the given
     * user id and prints these movie titles to the given file. These are the
     * movies the user should avoid. First calls findMostSimilarUser and then
     * getLeastFavoriteMovies(num) method on the "most similar user" to get up
     * to num movies the most similar user strongly disliked. Prints only
     * those movies to the file that the user with the given userid has not seen yet.
     * Format: one movie title per each line
     * @param userid id of the user
     * @param num max number of anti-recommendations
     * @param filename name of the file where to output anti-recommendations (movie titles)
     */
    public void findAntiRecommendations(int userid, int num, String filename) {

        // compute similarity between userid and all the other users
        // find the most similar user and anti-recommend movies that the most similar
        // user rated as 1.
        // Anti-recommend only the movies that userid has not seen (has not
        // rated).
        // FILL IN CODE
        String res = "";
        UserNode thisUser = usersData.get(userid);
        usersData.findMostSimilarUser(userid);
        UserNode mostSimilarUser = usersData.findMostSimilarUser(userid);
        int[] antiMovies = mostSimilarUser.getLeastFavoriteMovies(num);
        for (int i = 0; i < antiMovies.length; i++){
            if (!thisUser.hasRated(antiMovies[i])){
                if (null == movieMap.get(antiMovies[i])) {
                    continue;
                }
                res += movieMap.get(antiMovies[i]);
                res += "\n";
            }
        }
        try {
            BufferedWriter out = new BufferedWriter((new FileWriter(filename)));
            out.write(res);
            out.close();
        } catch (IOException e){

        }
    }

}