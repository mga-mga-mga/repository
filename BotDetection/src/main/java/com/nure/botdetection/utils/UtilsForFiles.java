package com.nure.botdetection.utils;

import org.apache.commons.io.FileUtils;
import twitter4j.Status;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represents writing and reading operations.
 *
 * @author Alla Kislaia
 */
public class UtilsForFiles {

    private static final String DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String EMPTY_STRING = "";

    private static String HEADER = "createdAt;id;text;source;isTruncated;inReplyToStatusId;inReplyToUserId;isFavorited;" +
            "isRetweeted;favoriteCount;inReplyToScreenName;geoLocation;place;retweetCount;isPossiblySensitive;" +
            "lang;contributorsIDs;retweetedStatus;userMentionEntities;urlEntities;" +
            "hashtagEntities;mediaEntities;symbolEntities;currentUserRetweetId;userId;name;screenName;location;" +
            "description;isContributorsEnabled;profileImageUrl;profileImageUrlHttps;url;" +
            "isProtected;followersCount;status;profileBackgroundColor;profileTextColor;profileLinkColor;" +
            "profileSidebarFillColor;profileSidebarBorderColor;profileUseBackgroundImage;" +
            "friendsCount;createdAt;favouritesCount;utcOffset;timeZone;profileBackgroundTiled;lang;statusesCount;" +
            "isGeoEnabled;isVerified;translator;listedCount;isFollowRequestSent;";

    public static List<String> readFile(String fileName) throws IOException {
        File file = new File(fileName);
        return FileUtils.readLines(file);
    }

    public static void writeStatusesToCsvFile(String fileName, List<Status> statuses) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.append(HEADER);
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Status st : statuses) {
                fileWriter.append(st.getCreatedAt().toString()).append(DELIMITER)
                        .append(String.valueOf(st.getId())).append(DELIMITER)
                        .append(st.getText()).append(DELIMITER)
                        .append(st.getSource()).append(DELIMITER)
                        .append(String.valueOf(st.isTruncated())).append(DELIMITER)
                        .append(String.valueOf(st.getInReplyToStatusId())).append(DELIMITER)
                        .append(String.valueOf(st.getInReplyToUserId())).append(DELIMITER)
                        .append(String.valueOf(st.isFavorited())).append(DELIMITER)
                        .append(String.valueOf(st.isRetweeted())).append(DELIMITER)
                        .append(String.valueOf(st.getFavoriteCount())).append(DELIMITER)
                        .append(st.getInReplyToScreenName()).append(DELIMITER)
                        .append(st.getGeoLocation().toString()).append(DELIMITER)
                        .append(st.getPlace().toString()).append(DELIMITER)
                        .append(String.valueOf(st.getRetweetCount())).append(DELIMITER)
                        .append(String.valueOf(st.isPossiblySensitive())).append(DELIMITER)
                        .append(st.getLang()).append(DELIMITER)
                        .append(Arrays.toString(st.getContributors())).append(DELIMITER)
                        .append(Optional.ofNullable(st.getRetweetedStatus().toString()).orElse(EMPTY_STRING)).append(DELIMITER)
                        .append(Arrays.toString(st.getUserMentionEntities())).append(DELIMITER)
                        .append(Arrays.toString(st.getURLEntities())).append(DELIMITER)
                        .append(Arrays.toString(st.getHashtagEntities())).append(DELIMITER)
                        .append(Arrays.toString(st.getMediaEntities())).append(DELIMITER)
                        .append(Arrays.toString(st.getSymbolEntities())).append(DELIMITER)
                        .append(String.valueOf(st.getCurrentUserRetweetId())).append(DELIMITER)
                        .append(String.valueOf(st.getUser().getId())).append(DELIMITER)
                        .append(st.getUser().getName()).append(DELIMITER)
                        .append(st.getUser().getScreenName()).append(DELIMITER)
                        .append(st.getUser().getLocation()).append(DELIMITER)
                        .append(st.getUser().getDescription()).append(DELIMITER)
                        .append(String.valueOf(st.getUser().isContributorsEnabled())).append(DELIMITER)
                        .append(st.getUser().getProfileImageURL()).append(DELIMITER)
                        .append(st.getUser().getProfileBackgroundImageUrlHttps()).append(DELIMITER)
                        .append(st.getUser().getURL()).append(DELIMITER)
                        .append(String.valueOf(st.getUser().isProtected())).append(DELIMITER)
                        .append(String.valueOf(st.getUser().getFollowersCount())).append(DELIMITER)
                        .append(Optional.ofNullable(st.getUser().getStatus().toString()).orElse(EMPTY_STRING)).append(DELIMITER)
                        .append(st.getUser().getProfileBackgroundColor()).append(DELIMITER)
                        .append(st.getUser().getProfileTextColor()).append(DELIMITER)
                        .append(st.getUser().getProfileLinkColor()).append(DELIMITER)
                        .append(st.getUser().getProfileSidebarFillColor()).append(DELIMITER)
                        .append(st.getUser().getProfileSidebarBorderColor()).append(DELIMITER)
                        .append(st.getUser().getProfileBackgroundImageURL()).append(DELIMITER)
                        .append(String.valueOf(st.getUser().getFriendsCount())).append(DELIMITER)
                        .append(st.getUser().getCreatedAt().toString()).append(DELIMITER)
                        .append(String.valueOf(st.getUser().getFavouritesCount())).append(DELIMITER)
                        .append(String.valueOf(st.getUser().getUtcOffset())).append(DELIMITER)
                        .append(st.getUser().getTimeZone()).append(DELIMITER)
                        .append(String.valueOf(st.getUser().isProfileBackgroundTiled())).append(DELIMITER)
                        .append(st.getUser().getLang()).append(DELIMITER)
                        .append(String.valueOf(st.getUser().getStatusesCount())).append(DELIMITER)
                        .append(String.valueOf(st.getUser().isGeoEnabled())).append(DELIMITER)
                        .append(String.valueOf(st.getUser().isVerified())).append(DELIMITER)
                        .append(String.valueOf(st.getUser().isTranslator())).append(DELIMITER)
                        .append(String.valueOf(st.getUser().getListedCount())).append(DELIMITER)
                        .append(String.valueOf(st.getUser().isFollowRequestSent())).append(DELIMITER)
                        .append(NEW_LINE_SEPARATOR);
            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }

}
