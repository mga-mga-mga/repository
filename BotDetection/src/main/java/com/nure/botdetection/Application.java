package com.nure.botdetection;

import org.apache.commons.io.FileUtils;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gathers twits.
 *
 * @author Alla Kislaia
 */
public class Application {

    private static final String FILE_NAME = "queries.txt";

    public static void main(String[] args) throws Exception {
        List<String> queries = readFile();
        System.out.println(searchTweets(queries).size());
    }

    private static List<String> readFile() throws IOException {
        File file = new File(FILE_NAME);
        return FileUtils.readLines(file);
    }

    public static List<Status> searchTweets(List<String> queries) throws IOException {
        List<Status> resultingTweets = new ArrayList<>();
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = createQuery();
        int count = 0;

        try {
            for (String queryString : queries) {
                query.setQuery(queryString);
                resultingTweets.addAll(twitter.search(query).getTweets());
                count++;
            }
        } catch (TwitterException ex) {
            writeTweetToFile(resultingTweets);
            System.out.println("Attempt number " + count);
            System.out.println(ex.getMessage());
        }

        return resultingTweets;
    }

    private static Query createQuery() {
        Query query = new Query();
        query.setLang("EN");
        query.setCount(100);
        return query;
    }

    private static void writeTweetToFile(List<Status> tweets) throws IOException {
        File file = new File("result.txt");
        FileUtils.writeLines(file, tweets);
    }
}
