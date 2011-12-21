package com.sina.openapi;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sina.openapi.model.CommentOpen;
import com.sina.openapi.model.CountsResults;
import com.sina.openapi.model.Status;
import com.sina.openapi.model.UserShow;

public final class ApiParser {
    private static Gson gson = new GsonBuilder().setDateFormat(
            "EEE MMM dd HH:mm:ss Z yyyy").create();;

    public static List<Status> parseStatusList(String response) {
        Type listType = new TypeToken<List<Status>>() {
        }.getType();
        synchronized (gson) {
            return gson.fromJson(response, listType);
        }
    }

    public static Status parseStatus(String response) {
        synchronized (gson) {
            return gson.fromJson(response, Status.class);
        }
    }

    public static List<UserShow> parseUserShowList(String response) {
        Type listType = new TypeToken<List<UserShow>>() {
        }.getType();
        synchronized (gson) {
            return gson.fromJson(response, listType);
        }
    }

    public static UserShow parseUserShow(String response) {
        synchronized (gson) {
            return gson.fromJson(response, UserShow.class);
        }
    }

    public static List<CommentOpen> parseCommentOpenList(String response) {
        Type listType = new TypeToken<List<CommentOpen>>() {
        }.getType();
        synchronized (gson) {
            return gson.fromJson(response, listType);
        }
    }

    public static CommentOpen parseCommentOpen(String response) {
        synchronized (gson) {
            return gson.fromJson(response, CommentOpen.class);
        }
    }

    public static CountsResults parseCountsResults(String response) {
        synchronized (gson) {
            return gson.fromJson(response, CountsResults.class);
        }
    }
}
