
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

public class MovieReview {

    static HashMap<String, String> userDetails = new HashMap<>();
    static HashMap<String, String> MovieDetails = new HashMap<>();
    static HashMap<String, String> ReviewDetails = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Welcome to Movie Review Service");
        addMovie("Don", "2006", "Action&Comedy");
        addMovie("Tiger", "2008", "Drama");
        addMovie("Padmavat", "2006", "Comedy");
        addMovie("Lunchbox", "2021", "Drama");
        addMovie("Guru", "2006", "Drama");
        addMovie("Metro", "2006", "Romance");

        addUser("SRK");
        addUser("salman");
        addUser("Deepika");

        addReview("SRK", "Don", "2");
        addReview("SRK", "Padmavat", "8");
        addReview("salman", "Don", "5");
        addReview("Deepika", "Don", "9");
        addReview("Deepika", "Guru", "6");
        //addReview("SRK", "Don", "5");            uncomment this when testing multiple review by same user
        addReview("Deepika", "Lunchbox", "5");
        addReview("SRK", "Tiger", "5");
        addReview("SRK", "Metro", "7");

/*
        System.out.println("************* userDetails *****************************");
        for (Entry<String, String> userDetail : userDetails.entrySet()) {
            System.out.println(userDetail.getKey() + "   " + userDetail.getValue());
        }
        System.out.println("************* MovieDetails *****************************");
        for (Entry<String, String> moviedetail : MovieDetails.entrySet()) {
            System.out.println(moviedetail.getKey() + "   " + moviedetail.getValue());
        }

        System.out.println("***************** ReviewDetails *************************");
        for (Entry<String, String> ReviewDetail : ReviewDetails.entrySet()) {
            System.out.println(ReviewDetail.getKey() + "   " + ReviewDetail.getValue());
        }
*/
        System.out.println("End of Movie Review Service");

        System.out.println("***************** Calculation Service start ***************");
        topMovieYear("2006");
        topMovieOfYearCritic("2006");
        topMovieGenre("Drama");

        averageReviewScore("2006");
        System.out.println("**************** End of Movie Review Calculation ****************");
    }

    public static void addMovie(String movie, String releaseDate, String genre) {
        if (MovieDetails.containsKey(movie)) {
            System.out.println("Movie Already Exists");
            return;
        }
        MovieDetails.put(movie, releaseDate + "~" + genre + "~" + "0");
    }

    public static void addUser(String username) {
        if (userDetails.containsKey(username)) {
            System.out.println("user Already Exists");
            return;
        }
        userDetails.put(username, "User" + "~" + "0");
    }

    public static void addReview(String user, String Movie, String Rating) {
        int newRating = Integer.parseInt(Rating);
        String userType = "User";
        if (newRating < 0 || newRating > 10) {
            throw new IllegalArgumentException("Exception: Movie rating is expected between 0 to 10");
        }
        if (ReviewDetails.containsKey(user + "~" + Movie)) {
            throw new IllegalArgumentException("Exception: multiple reviews are not " + "allowed for Movie " + Movie + " by user " + user);
        }
        if (!MovieDetails.containsKey(Movie)) {
            System.out.println("Movie " + Movie + " not yet Released");
            return;
        }
        int MovieYear = Integer.parseInt(MovieDetails.get(Movie)
                .split("~")[0]);
        int year = Calendar.getInstance()
                .get(Calendar.YEAR);
        if (MovieYear > year) {
            throw new IllegalArgumentException("Exception:  movie " + Movie + " yet to be released");
        }
        int noOfReviews = Integer.parseInt(userDetails.get(user)
                .split("~")[1]);
        if (noOfReviews <= 2) {
            userDetails.put(user, "User" + "~" + (noOfReviews + 1));
        } else if (noOfReviews <= 6) {
            userDetails.put(user, "Critic" + "~" + (noOfReviews + 1));
            newRating = 2 * newRating;
            userType = "Critic";
        } else if (noOfReviews <= 9) {
            userDetails.put(user, "Expert" + "~" + (noOfReviews + 1));
            newRating = 3 * newRating;
            userType = "Expert";
        } else {
            userDetails.put(user, "Admin" + "~" + (noOfReviews + 1));
            newRating = 4 * newRating;
            userType = "Admin";
        }

        ReviewDetails.put(user + "~" + Movie, newRating + "~" + userType);
        String[] moviedata = MovieDetails.get(Movie)
                .split("~");

        int movieRating = Integer.parseInt(moviedata[2]) + newRating;
        MovieDetails.put(Movie, moviedata[0] + "~" + moviedata[1] + "~" + movieRating);

    }

    public static void topMovieYear(String year) {
        System.out.print("Top Movie in Year " + year);
        int highestRating = 0;
        String movieName = "";
        for (Entry<String, String> moviedetail : MovieDetails.entrySet()) {
            String[] moviedata = moviedetail.getValue()
                    .split("~");
            int curRating = Integer.parseInt(moviedata[2]);
            if (year.equals(moviedata[0])) {
                if (curRating > highestRating) {
                    highestRating = curRating;
                    movieName = moviedetail.getKey();
                }
            }

        }
        System.out.println(" is " + movieName + " with rating " + highestRating);
    }

    public static void topMovieOfYearCritic(String year) {
        int topRating = 0;
        String movieName = "";
        for (Entry<String, String> moviedetail : MovieDetails.entrySet()) {

            int rating = getMaxRatingByUserType(moviedetail.getKey(), "Critic");
            if (topRating < rating) {
                topRating = rating;
                movieName = moviedetail.getKey();
            }
        }
        System.out.println("Top Movie in Year " + year + " as critic is : " + movieName + " with rating " + topRating);
    }

    private static int getMaxRatingByUserType(String movieName, String userType) {
        int max = 0;
        for (Entry<String, String> ReviewDetail : ReviewDetails.entrySet()) {
            String movie = ReviewDetail.getKey().split("~")[1];
            String[] reviewData = ReviewDetail.getValue().split("~");
            if (movie.equalsIgnoreCase(movieName) && reviewData[1].equalsIgnoreCase(userType)) {
                max = Integer.max(max, Integer.parseInt(reviewData[0]));
            }
        }
        return max;
    }


    public static void topMovieGenre(String genre) {
        System.out.print("Top movie in Genre " + genre);
        int highestRating = 0;
        String highratedmovie = "";
        for (Entry<String, String> moviedetail : MovieDetails.entrySet()) {
            String[] moviedata = moviedetail.getValue()
                    .split("~");
            if (moviedata[1].contains(genre)) {
                int curRating = Integer.parseInt(moviedata[2]);
                if (curRating > highestRating) {
                    highestRating = curRating;
                    highratedmovie = moviedetail.getKey();
                }
            }

        }
        System.out.println(" is " + highratedmovie + " with rating " + highestRating);
    }

    public static void averageReviewScore(String year) {
        int sum = 0;
        int count = 0;
        for (Entry<String, String> moviedetail : MovieDetails.entrySet()) {
            String[] moviedata = moviedetail.getValue()
                    .split("~");

            int currRating;
            if (year.equals(moviedata[0])) {
                currRating = Integer.parseInt(moviedata[2]);
                sum += currRating;
                count += getReviewCount(moviedetail.getKey());
            }
        }

        System.out.println("Average rating in year " + year + " is " + (float) sum / count);
    }


    private static int getReviewCount(String movieName) {
        int count = 0;
        for (Entry<String, String> ReviewDetail : ReviewDetails.entrySet()) {
            String[] ReviewData = ReviewDetail.getKey().split("~");
            if (ReviewData[1].equalsIgnoreCase(movieName)) {
                count++;
            }
        }
        return count;
    }
}
