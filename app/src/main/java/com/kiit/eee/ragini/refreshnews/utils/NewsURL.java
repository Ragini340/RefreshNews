package com.kiit.eee.ragini.refreshnews.utils;

/**
 * Created by 1807340_RAGINI on 01,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class NewsURL {
public static final String BASE_NEWS_URL = "https://newsapi.org/v2/";
public static final String NEWS_BASED_TOP_HEADLINE = BASE_NEWS_URL + "top-headlines";

public static final String NEWS_TYPE_COUNTRY = "country";
public static final String NEWS_TYPE_BUSINESS = "business";
public static final String NEWS_TYPE_ENTERTAINMENT = "entertainment";
public static final String NEWS_TYPE_GENERAL = "general";
public static final String NEWS_TYPE_HEALTH = "health";
public static final String NEWS_TYPE_SCIENCE = "science";
public static final String NEWS_TYPE_SPORTS = "sports";
public static final String NEWS_TYPE_TECHNOLOGY = "technology";
public static final String NEWS_SOURCES = BASE_NEWS_URL + "sources";

public static final int NEWS_API_FEATURE = 1;
public static final int SOURCE_API_FEATURE = 2;
}
