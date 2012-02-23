package com.sina.weibo.models;

public final class SuggestPair {

    public String resultWord;
    public MatchedResult mr;
    public String searchWord;
    public int suggestType;
    public boolean isMatch = false;
    public boolean isHeader = false;

    /*
     * public SuggestPair(String resultWord, MatchedResult mr, boolean isTopic,
     * boolean isTopicRecent, boolean isTopicHot, boolean isTopicNoMatch, String
     * searchWord) { this.resultWord = resultWord; this.mr = mr; this.isTopic =
     * isTopic; this.isTopicRecent = isTopicRecent; this.isTopicHot =
     * isTopicHot; this.isTopicNoMatch = isTopicNoMatch; this.searchWord =
     * searchWord; }
     * 
     * public SuggestPair(String resultWord, MatchedResult mr, boolean isAt,
     * boolean isAtRecent, boolean isAtHot, boolean isAtHome, boolean
     * isAtNoMatch, String searchWord) { this.resultWord = resultWord; this.mr =
     * mr; this.isAt = isAt; this.isAtRecent = isAtRecent; this.isAtHot =
     * isAtHot; this.isAtHome = isAtHome; this.isAtNoMatch = isAtNoMatch;
     * this.searchWord = searchWord; }
     * 
     * public SuggestPair(boolean isHeader, int headerText) { this.isHeader =
     * isHeader; this.headerText = headerText; }
     */
    public SuggestPair(String resultWord, MatchedResult mr, int bigType,
            int suggestType, boolean isHeader, boolean isMatch,
            String searchWord) {
        this.resultWord = resultWord;
        this.mr = mr;
        this.suggestType = suggestType;
        this.isHeader = isHeader;
        this.isMatch = isMatch;
        this.searchWord = searchWord;
    }
}