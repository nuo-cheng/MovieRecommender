package recommender;

/**
 * A custom linked list that stores user info. Each node in the list is of type
 * recommender.UserNode.
 * FILL IN CODE. Also, add other methods as needed.
 *
 * @author okarpenko
 *
 */
public class UsersList {
    private UserNode head = null;
    private UserNode tail = null; // ok to store tail here, will be handy for appending


    /** Insert the rating for the given userId and given movieId.
     *
     * @param userId  id of the user
     * @param movieId id of the movie
     * @param rating  rating given by this user to this movie
     */
    public void insert(int userId, int movieId, double rating) {

        // Check if recommender.UserNode already exists;
        // if not, create it and append to this list.
        // Then call insert(movieId, rating) method on the recommender.UserNode
        // FILL IN CODE
        if (head == null){
            head = new UserNode(-1);
        }

        if (head.next() == null){
            UserNode newUserNode = new UserNode(userId);
            head.setNext(newUserNode);
            newUserNode.insert(movieId, rating);
            tail = new UserNode(-1);
            tail.setNext(newUserNode);
        } else {
            UserNode currentNode = head.next();
            while (currentNode != null){
                if (currentNode.getId() == userId){
                    currentNode.insert(movieId, rating);
                    return;
                } else {
                    currentNode = currentNode.next();
                }
            }
            UserNode newUserNode = new UserNode(userId);
            newUserNode.insert(movieId, rating);
            append(newUserNode);
        }
    }

    /**
     * Append a new node to the list
     * @param newNode a new node to append to the list
     */
    public void append(UserNode newNode) {
        // This is where tail will come in handy
        // FILL IN CODE
        tail.next().setNext(newNode);
        tail.setNext(newNode);
    }

    /** Return a recommender.UserNode given userId
     *
     * @param userId id of the user (as defined in ratings.csv)
     * @return recommender.UserNode for a given userId
     */
    public UserNode get(int userId) {
        // FILL IN CODE
        UserNode currentNode = head.next();
        while (currentNode != null){
            if (currentNode.getId() == userId){
                return currentNode;
            } else {
                currentNode = currentNode.next();
            }
        }


        return null; // don't forget to change it
    } // get method


    /**
     * The method computes the similarity between the user with the given userid
     * and all the other users. Finds the maximum similarity and returns the
     * "most similar user".
     * Calls computeSimilarity method in class MovieRatingsList/
     *
     * @param userid id of the user
     * @return the node that corresponds to the most similar user
     */
    public UserNode findMostSimilarUser(int userid) {
        UserNode mostSimilarUser = null;
        // FILL IN CODE
        double mostSimilarGrade = 1;
        double similarityGrade = 0;
        double similarity = 0;
        UserNode thisNode = new UserNode(-1);
        if (head != null){
            thisNode = get(userid);
            //calculate other users similarity
            UserNode currentOtherNode = head.next();
            while (currentOtherNode != null) {
                if (currentOtherNode.getId() == userid) {
                    currentOtherNode = currentOtherNode.next();
                    continue;
                }
                similarity = thisNode.computeSimilarity(currentOtherNode);
                similarityGrade = 1 - Math.abs(similarity);
                if (similarityGrade < mostSimilarGrade){
                    mostSimilarGrade = similarityGrade;
                    mostSimilarUser = currentOtherNode;
                } else if (similarityGrade == mostSimilarGrade){
                    if (null == mostSimilarUser || currentOtherNode.getId() < mostSimilarUser.getId()){
                        mostSimilarUser = currentOtherNode;
                    }
                }
                currentOtherNode = currentOtherNode.next();
            }
        }
        return mostSimilarUser;

    }

    /** Print recommender.UsersList to a file  with the given name in the following format:
     (userid) movieId:rating; movieId:rating; movieId:rating;
     (userid) movieId:rating; movieId:rating;
     (userid) movieId:rating; movieId:rating; movieId:rating; movieId:rating;
     Info for each userid should be printed on a separate line
     * @param filename name of the file where to output recommender.UsersList info
     */
    public void print(String filename) {
        // FILL IN CODE

    }

    public void debugPrint() {
        UserNode current = head.next();
        while(current!=null) {
            current.print();
            current = current.next();
        }
    }

    public static void main(String[] args) {
        UsersList newUsersList = new UsersList();
        newUsersList.insert(4, 2,5.0);
        newUsersList.insert(3,1,4.5);
        newUsersList.insert(4,1,5.0);
        newUsersList.debugPrint();
//        newUsersList.get(4).print();
//        UserNode newUserNode = newUsersList.head.next();
//        newUserNode.getFavoriteMovies(2);
        System.out.println(newUsersList.findMostSimilarUser(3));
    }
}