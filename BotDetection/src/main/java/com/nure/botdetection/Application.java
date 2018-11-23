package com.nure.botdetection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.nure.botdetection.utils.UtilsForFiles;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Gathers twits.
 *
 * @author Alla Kislaia
 */
public class Application {

    private static final String FILE_NAME = "queries.txt";
    private static final String RESULTS_FILE = "result2.txt";
    private static final String CSV_FILE = "csvresults.csv";
    private static final String LANGUAGE = "EN";
    private static final int COUNT = 100;

    public static void main(String[] args) throws Exception {
        List<String> queries = readFile();
        List<Status> tweets = searchTweets(queries);
        UtilsForFiles.writeStatusesToCsvFile(CSV_FILE, tweets);
    }

    private static List<String> readFile() throws IOException {
        File file = new File(FILE_NAME);
        return FileUtils.readLines(file);
    }

    public static List<Status> searchTweets(List<String> queries) throws IOException, InterruptedException {
        List<Status> resultingTweets = new ArrayList<>();
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = createQuery();
        int count = 0;

        try {
//            for (String queryString : queries) {
//                if (count % 180 == 0){
//                    Thread.sleep(900000);
//                }
            for (int i = 0; i < 5; i++){
                //query.setQuery(queryString);
                query.setQuery(queries.get(i));
                resultingTweets.addAll(twitter.search(query).getTweets());
                count++;
            }
        } catch (TwitterException ex) {
            UtilsForFiles.writeTweetToFile(resultingTweets, RESULTS_FILE);
            System.out.println("Attempt number " + count);
            System.out.println("Amount of tweets received: " + resultingTweets.size());
            System.out.println(ex.getMessage());
        }

        return resultingTweets;
    }

    private static Query createQuery() {
        Query query = new Query();
        query.setLang(LANGUAGE);
        query.setCount(COUNT);
        return query;
    }
}
