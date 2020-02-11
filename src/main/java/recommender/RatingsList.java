package recommender; /**
 * recommender.RatingsList.
 * A class that stores movie ratings for a user in a custom singly linked list of
 * recommender.RatingNode objects. Has various methods to manipulate the list. Stores
 * only the head of the list (no tail! no size!). The list should be sorted by
 * rating (from highest to smallest).
 * Fill in code in the methods of this class.
 * Do not modify signatures of methods. Not all methods are needed to compute recommendations,
 * but all methods are required for the assignment.
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RatingsList implements Iterable<RatingNode> {

    private RatingNode head;
    // Note: you are *not* allowed to store the tail or the size of this list

    /**
     * Changes the rating for a given movie to newRating. The position of the
     * node within the list should be changed accordingly, so that the list
     * remains sorted by rating (from largest to smallest).
     *
     * @param movieId id of the movie
     * @param newRating new rating of this movie
     */
    public void setRating(int movieId, double newRating) {
        // FILL IN CODE
        RatingNode previousNode = new RatingNode(0, 1);
        RatingNode currentNode = head;
        RatingNode headCopy = head;
        previousNode.setNext(currentNode);
        while (currentNode != null){
            if (currentNode.getMovieId() == movieId){
                if (currentNode == head) {
                    head = head.next();
                }
                //remove the node we want to set
                previousNode.setNext(currentNode.next());
                //set new rationg
                currentNode.setMovieRating(newRating);
                //insert back
                int insertMovieId = currentNode.getMovieId();
                double insertMovieRating = currentNode.getMovieRating();
                System.out.println("movieId:" + insertMovieId + "   " + " Rating:" + insertMovieRating);
                insertByRating(insertMovieId, insertMovieRating);
                break;
            } else {
                currentNode = currentNode.next();
                previousNode = previousNode.next();
            }
        }

    }

    /**
     * Return the rating for a given movie. If the movie is not in the list,
     * returns -1.
     * @param movieId movie id
     * @return rating of a movie with this movie id
     */
    public double getRating(int movieId) {
        // FILL IN CODE
        RatingNode currentNode = head;
        while (currentNode.next() != null){
            if (currentNode.getMovieId() == movieId){
                return currentNode.getMovieRating();
            } else {
                currentNode = currentNode.next();
            }
        }
        return -1; // don't forget to change it

    }


    /**
     * Insert a new node (with a given movie id and a given rating) into the list.
     * Insert it in the right place based on the value of the rating. Assume
     * the list is sorted by the value of ratings, from highest to smallest. The
     * list should remain sorted after this insert operation.
     *
     * @param movieId id of the movie
     * @param rating rating of the movie
     */
    public void insertByRating(int movieId, double rating) {
        // FILL IN CODE. Make sure to test this method thoroughly
        RatingNode newRating = new RatingNode(movieId, rating);
        if (head == null){
            head = newRating;
        } else {
            RatingNode previousRating = new RatingNode(1, 1);
            RatingNode currentRating = head;
            previousRating.setNext(currentRating);
            while (currentRating != null){
                if (rating > currentRating.getMovieRating()){
                    newRating.setNext(currentRating);
                    previousRating.setNext(newRating);
                    //if current node is head, set head again
                    if (currentRating == head){
                        head = newRating;
                    }
                    break;
                    //if two ratings are equal, check movieId
                } else if (rating == currentRating.getMovieRating()){
                    if (movieId > currentRating.getMovieId()){
                        newRating.setNext(currentRating);
                        previousRating.setNext(newRating);
                        if (currentRating == head){
                            head = newRating;
                        }
                        break;
                    } else {
                        currentRating = currentRating.next();
                        previousRating = previousRating.next();
                    }
                } else {
                    currentRating = currentRating.next();
                    previousRating = previousRating.next();
                }
            }
            previousRating.setNext(newRating);
        }

    }

    @Override
    /**
     * toString method using for check the result when coding
     */
    public String toString() {
        String print = "";
        RatingNode current = head;
        while( current!=null ) {
            print += "ID" + current.getMovieId() + "  " + "Rating" + current.getMovieRating() + "\n";
            current = current.next();
        }
        return print;
    }

    /**
     * Computes similarity between two lists of ratings using Pearson correlation.
     * https://en.wikipedia.org/wiki/Pearson_correlation_coefficient
     * Note: You are allowed to use a HashMap for this method.
     *
     * @param otherList another RatingList
     * @return similarity computed using Pearson correlation
     */
    public double computeSimilarity(RatingsList otherList) {
        double similarity = 0;
        // FILL IN CODE
        HashMap<Integer, Double> otherRating = hashRating(otherList.head);
        //define some variables
        double sumOfX = 0;
        double sumOfY = 0;
        double sumOfXY = 0;
        double sumOfXSquare = 0;
        double sumOfYSquare = 0;
        int countOfN = 0;

        RatingNode currentNode = head;
        while(currentNode != null){
            if (otherRating.get(currentNode.getMovieId()) != null){
                double x = currentNode.getMovieRating();
                double y = otherRating.get(currentNode.getMovieId());
                double xy = x * y;
                double xSquare = x * x;
                double ySquare = y * y;

                sumOfX += x;
                sumOfY += y;
                sumOfXY += xy;
                sumOfXSquare += xSquare;
                sumOfYSquare += ySquare;

                countOfN += 1;
                currentNode = currentNode.next();
            } else {
                currentNode = currentNode.next();
            }
        }
        similarity = (countOfN * sumOfXY - sumOfX * sumOfY) /
                     (Math.sqrt(countOfN * sumOfXSquare - sumOfX * sumOfX) * Math.sqrt(countOfN * sumOfYSquare - sumOfY * sumOfY));
        return similarity;

    }

    /**
     * a method that put ratingList of one other userNode into hashMap
     * use it to check movie that two users both watched
     * @param head
     * @return a hashMap of one other userNode's ratingList
     */
    private HashMap<Integer, Double> hashRating(RatingNode head){
        HashMap<Integer, Double> mapOfIdAndRating = new HashMap<Integer, Double>();
        RatingNode currentNode = head;
        while (currentNode != null){
            mapOfIdAndRating.put(
                    currentNode.getMovieId(),
                    currentNode.getMovieRating());
            currentNode = currentNode.next();
        }
        return mapOfIdAndRating;
    }

    /**
     * Returns a sublist of this list where the rating values are in the range
     * from begRating to endRating, inclusive.
     *
     * @param begRating lower bound for ratings in the resulting list
     * @param endRating upper bound for ratings in the resulting list
     * @return sublist of the recommender.RatingsList that contains only nodes with
     * rating in the given interval
     */
    public RatingsList sublist(int begRating, int endRating) {
        RatingsList res = new RatingsList();
        // FILL IN CODE
        res.head = new RatingNode(0, 1);
        RatingNode currentNode = head;
        RatingNode resCursor = new RatingNode(0, 1);
        resCursor.setNext(res.head);
        while (currentNode!=null && (currentNode.getMovieRating() >= begRating)){
            if (currentNode.getMovieRating() <= endRating){
                int newMovieId = currentNode.getMovieId();
                double newRating = currentNode.getMovieRating();
                RatingNode newNode = new RatingNode(newMovieId, newRating);
                //System.out.println(newNode);
                resCursor.next().setNext(newNode);
                resCursor.setNext(newNode);
                currentNode = currentNode.next();
            } else {
                currentNode = currentNode.next();
            }
        }
        res.head = res.head.next();
        return res;
    }

    /** Traverses the list and prints the ratings list in the following format:
     *  movieId:rating; movieId:rating; movieId:rating;  */
    public void print() {
        // FILL IN CODE
        RatingNode currentNode = head;
        while (currentNode != null){
            System.out.print(currentNode.getMovieId() + ":" + currentNode.getMovieRating() + "; ");
            currentNode = currentNode.next();
        }
    }

    /**
     * Returns the middle node in the list - the one half way into the list.
     * Needs to have the running time O(n), and should be done in one pass
     * using slow & fast pointers (as described in class).
     *
     * @return the middle recommender.RatingNode
     */
    public RatingNode getMiddleNode() {
        // FILL IN CODE
        RatingNode fastNode = head;
        RatingNode slowNode = head;
        while (fastNode != null && fastNode.next() != null && fastNode.next().next() != null){
            fastNode = fastNode.next().next();
            slowNode = slowNode.next();
        }
        return slowNode; // don't forget to change it
    }

    /**
     * Returns the median rating (the number that is halfway into the sorted
     * list). To compute it, find the middle node and return it's rating. If the
     * middle node is null, return -1.
     *
     * @return rating stored in the node in the middle of the list
     */
    public double getMedianRating() {
        // FILL IN CODE
        RatingNode middleNode = getMiddleNode();
        if (middleNode != null){
            return getMiddleNode().getMovieRating();
        } else {
            return -1;
        }
    }

    /**
     * Returns a recommender.RatingsList that contains n best rated movies. These are
     * essentially first n movies from the beginning of the list. If the list is
     * shorter than size n, it will return the whole list.
     *
     * @param n the maximum number of movies to return
     * @return recommender.RatingsList containing movies rated as 5
     */
    public RatingsList getNBestRankedMovies(int n) {
        // FILL IN CODE
        RatingsList nBestRatings = new RatingsList();
        Iterator<RatingNode> it = iterator();
        for (int i = 0; i < n; i++) {
            if (it.hasNext()) {
                RatingNode currNode = it.next();
                nBestRatings.insertByRating(currNode.getMovieId(), currNode.getMovieRating());
            }
        }
        return nBestRatings;
    }

    /**
     * * Returns a recommender.RatingsList that contains n worst rated movies for this user.
     * Essentially, these are the last n movies from the end of the list.
     * Note: This method should compute the result in one pass. Do not use the size variable.
     * Note: To find the n-th node from the end of the list, use the technique we discussed in class:
     * use two pointers, where first, you move only one pointer so that pointers are n-nodes apart,
     * and then move both pointers together until the first pointer reaches null; when it happens,
     * the second pointer would be pointing at the correct node.
     * Do NOT use reverse(). Do NOT destroy the list.
     *
     * @param n the maximum number of movies to return
     * @return recommender.RatingsList containing n lowest ranked movies (ranked by this user)
     */
    public RatingsList getNWorstRankedMovies(int n) {
        // FILL IN CODE
        RatingNode fastNode = head;
        RatingNode slowNode = head;
        for (int i = 0; i < n; i++){
            if (fastNode != null){
                fastNode = fastNode.next();
            }
        }
        while (fastNode != null){
            fastNode = fastNode.next();
            slowNode = slowNode.next();
        }
        RatingsList nWorstRatings = new RatingsList();
        nWorstRatings.head = new RatingNode(slowNode.getMovieId(), slowNode.getMovieRating());
        RatingNode nWorstCurrent = nWorstRatings.head;
        RatingNode nWorstCursor = new RatingNode(0, 1);
        nWorstCursor.setNext(nWorstCurrent);
        RatingNode currentNode = slowNode.next();

        while (currentNode != null){
            RatingNode newNode = new RatingNode(currentNode.getMovieId(), currentNode.getMovieRating());
            nWorstCursor.next().setNext(newNode);
            nWorstCursor.setNext(newNode);
            currentNode = currentNode.next();
        }
        return nWorstRatings; // don't forget to change
    }

    /**
     * Return a new list that is the reverse of the original list. The returned
     * list is sorted from lowest ranked movies to the highest rated movies.
     * Use only one additional recommender.RatingsList (the one you return) and constant amount
     * of memory. You may NOT use arrays, ArrayList and other built-in Java Collections classes.
     * Read description carefully for requirements regarding implementation of this method.
     *
     * @param head head of the RatingList to reverse
     * @return reversed list
     */
    public RatingsList reverse(RatingNode head) {
        // FILL IN CODE

        RatingNode originalCursor = head;
        RatingNode tailNodeOfReverse = new RatingNode(head.getMovieId(),head.getMovieRating());
        RatingNode dummyHead = new RatingNode(0,1);
        dummyHead.setNext(tailNodeOfReverse);
        originalCursor = originalCursor.next();
        while (originalCursor != null){
            RatingNode newNode = new RatingNode(originalCursor.getMovieId(), originalCursor.getMovieRating());
            newNode.setNext(dummyHead.next());
            dummyHead.setNext(newNode);
            originalCursor = originalCursor.next();
        }
        RatingsList reversedList = new RatingsList();
        reversedList.head = dummyHead.next();
        return reversedList;

    }

    /**
     * Returns an iterator for the list
     * @return iterator
     */
    public Iterator<RatingNode> iterator() {

        return new RatingsListIterator(0);
    }

    // ------------------------------------------------------
    /**
     * Inner class, RatingsListIterator
     * The iterator for the ratings list. Allows iterating over the recommender.RatingNode-s of
     * the list.
     */
    private class RatingsListIterator implements Iterator<RatingNode> {

        // FILL IN CODE: add instance variable(s)
        private RatingNode currentNode;

        /**
         * Creates a new the iterator starting at a given index
         * @param index index
         */
        public RatingsListIterator(int index) {
            // FILL IN CODE
            currentNode = head;
            for (int i = 0; i < index; i++){
                currentNode = currentNode.next();
            }
        }

        /**
         * Checks if there is a "next" element of the list
         * @return true, if there is "next" and false otherwise
         */
        public boolean hasNext() {
            // FILL IN CODE
            return currentNode != null; // don't forget to change
        }

        /**
         * Returns the "next" node and advances the iterator
         * @return next node
         */
        public RatingNode next() {
            // FILL IN CODE
            if (!hasNext()){
                System.out.println("No next element.");
                throw new NoSuchElementException();
            }
            RatingNode ratingNode = currentNode;
            currentNode = currentNode.next();
            return ratingNode; // don't forget to change
        }

        public void remove() {
            // No need to implement for this assignment
            throw new UnsupportedOperationException();
        }

    }

    public static void main(String[] args) {
        RatingsList aList = new RatingsList();
        aList.insertByRating(1, 4.0);
        aList.insertByRating(2, 4.0);
        aList.insertByRating(3, 2.0);
        aList.insertByRating(6, 1.5);
        aList.insertByRating(5, 2.5);
        aList.insertByRating(4,4.0);
        aList.insertByRating(8,4.0);
        aList.insertByRating(11, 3.5);
        System.out.println(aList);

//        RatingsList reversedAList =  aList.reverse(aList.head);
//        System.out.println(reversedAList);
//        System.out.println(aList.getMiddleNode());
//        System.out.println(aList.getMedianRating());

//        aList.print();

//        aList.setRating(2,3.0);
//        System.out.println(aList);
//        System.out.println(aList.getRating(3));
//        System.out.println(aList.sublist(1,3));
//        System.out.println(aList.getNBestRankedMovies(9));
        System.out.println(aList.getNWorstRankedMovies(1));
//        System.out.println(aList.reverse(aList.head));
    }

}