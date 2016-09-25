package com.oose2015.grapevine;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by venussoontornprueksa on 11/11/15.
 */
public class Requester {
    /**
     * Sends a request to create a new user.
     * @param requestQueue the RequestQueue to add the request to
     * @param context the Context of the activity that called the method
     * @param cls the next class for the intent
     * @param params the hashmap containing the user information
     */
    public static void createNewAccount(RequestQueue requestQueue, final Context context,
                                        final Class cls, HashMap<String, String> params) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_USERS,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Grapevine.setMyID(Parser.parseLoginUserIDJSON(response));
                        Grapevine.setMyUsername(Parser.parseLoginUsernameJSON(response));
                        Intent intent = new Intent(context, cls);
                        context.startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.longToast("Login ID already taken");
            }
        });
        requestQueue.add(request);
    }

    /**
     * Logs the user in
     * @param requestQueue the RequestQueue to add the request to
     * @param context the Context of the activity that called the method
     * @param cls the next class for the intent
     * @param params the hashmap containing the login information
     */
    public static void login(final RequestQueue requestQueue, final Context context,
                             final Class cls, HashMap<String, String> params) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_USERS,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                public void onResponse(JSONObject response) {
                        Grapevine.setMyID(Parser.parseLoginUserIDJSON(response));
                        Grapevine.setMyUsername(Parser.parseLoginUsernameJSON(response));

                        requestFeedsNow(requestQueue);

                        Intent intent = new Intent(context, cls);
                        context.startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.longToast("Login unsuccessful");
            }
        });
        requestQueue.add(request);
    }

    /**
     * Sends a request to get a list of events.
     * @param requestQueue the RequestQueue to add the request to
     * @return the response to the request
     */
    public static JSONArray requestEventsJSON(RequestQueue requestQueue, int feedId) {
        JSONArray response = null;
        RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();
        Logger.debugLog("feedId: " + feedId);
        String url = (feedId == 0 ? UrlEndpoints.URL_DB +
                UrlEndpoints.URL_EVENTS : UrlEndpoints.URL_DB +
                UrlEndpoints.URL_FEEDS + UrlEndpoints.URL_EVENTS + feedId);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, requestFuture, requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Logger.debugLog(e + "");
        } catch (ExecutionException e) {
            Logger.debugLog(e + "");
        } catch (TimeoutException e) {
            Logger.debugLog(e + "");
        }
        return response;
    }

    /**
     * Request user's events
     * @param requestQueue the queue to add the request
     * @return JSONArray of events
     */
    public static JSONArray requestMyEvents(RequestQueue requestQueue) {
        JSONArray response = null;
        RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.URL_DB +
                UrlEndpoints.URL_USERS + UrlEndpoints.URL_EVENTS + Grapevine.getMyID(), requestFuture, requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Logger.debugLog(e + "");
        } catch (ExecutionException e) {
            Logger.debugLog(e + "");
        } catch (TimeoutException e) {
            Logger.debugLog(e + "");
        }
        return response;
    }

    /**
     * Create Event Request
     * @param requestQueue the queue to add the request
     * @param params the params for event
     */
    public static void createEvent(RequestQueue requestQueue, HashMap<String, Object> params) {
        Logger.debugLog("createEvent params: " + params.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_EVENTS,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.longToast("Event creation unsuccessful");
            }
        });
        requestQueue.add(request);
    }

    /**
     * Profile Request
     * @param requestQueue the queue to add the request
     * @param userID the user to find the profile for
     * @return the profile
     */
    public static JSONObject requestProfile(RequestQueue requestQueue, int userID) {
        JSONObject response = null;
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlEndpoints.URL_DB +
                UrlEndpoints.URL_USERS + userID, requestFuture, requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Logger.debugLog(e + "");
        } catch (ExecutionException e) {
            Logger.debugLog(e + "");
        } catch (TimeoutException e) {
            Logger.debugLog(e + "");
        }
        return response;
    }

    /**
     * Request to attend an event
     * @param requestQueue the queue to add the request to
     * @param eventToAttend event that user is attending
     */
    public static void attendEvent(RequestQueue requestQueue, final int eventToAttend) {
        final HashMap<String, String> user = new HashMap<>();
        user.put(Keys.EndpointUser.KEY_USER_ID, Grapevine.getMyID() + "");
        Logger.debugLog("userParam: " + user.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_EVENTS + UrlEndpoints.URL_ATTENDING + eventToAttend + "/",
                new JSONObject(user),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.longToast("Attend unsuccessful");
                Logger.debugLog("Failure-- User: " + user.toString() + ", event to attend: " + eventToAttend);
            }
        });
        requestQueue.add(request);
    }

    /**
     * Request to not attend event
     * @param requestQueue the queue to add the request to
     * @param eventToAttend event user wants to not attend
     */
    public static void notAttendEvent(RequestQueue requestQueue, final int eventToAttend) {
        final HashMap<String, String> user = new HashMap<>();
        user.put(Keys.EndpointUser.KEY_USER_ID, Grapevine.getMyID() + "");
        Logger.debugLog("userParam: " + user.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_EVENTS +
                        UrlEndpoints.URL_NOT + UrlEndpoints.URL_ATTENDING + eventToAttend + "/",
                new JSONObject(user),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Logger.debugLog("Success-- User: " + user.toString() + ", event to attend: " + eventToAttend);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.longToast("Maybe event unsuccessful");
                Logger.debugLog("Failure-- User: " + user.toString() + ", event to attend: " + eventToAttend);
            }
        });
        requestQueue.add(request);
    }

    /**
     * Request to maybe an event
     * @param requestQueue queue to be added to
     * @param eventToMaybe event that is getting maybe
     */
    public static void maybeEvent(RequestQueue requestQueue, final int eventToMaybe) {
        final HashMap<String, String> user = new HashMap<>();
        user.put(Keys.EndpointUser.KEY_USER_ID, Grapevine.getMyID() + "");
        Logger.debugLog("userParam: " + user.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_EVENTS + UrlEndpoints.URL_MAYBE + eventToMaybe + "/",
                new JSONObject(user),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Logger.debugLog("Success-- User: " + user.toString() + ", event to maybe: " + eventToMaybe);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.longToast("Maybe event unsuccessful");
                Logger.debugLog("Failure-- User: " + user.toString() + ", event to maybe: " + eventToMaybe);
            }
        });
        requestQueue.add(request);
    }

    /**
     * Request to change status of user event to not maybe
     * @param requestQueue the queue to add the request to
     * @param eventToMaybe event they are currently maybe going to
     */
    public static void notMaybeEvent(RequestQueue requestQueue, final int eventToMaybe) {
        final HashMap<String, String> user = new HashMap<>();
        user.put(Keys.EndpointUser.KEY_USER_ID, Grapevine.getMyID() + "");
        Logger.debugLog("userParam: " + user.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_EVENTS +
                        UrlEndpoints.URL_NOT + UrlEndpoints.URL_MAYBE + eventToMaybe + "/",
                new JSONObject(user),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Logger.debugLog("Success-- User: " + user.toString() + ", event to maybe: " + eventToMaybe);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.longToast("Maybe event unsuccessful");
                Logger.debugLog("Failure-- User: " + user.toString() + ", event to maybe: " + eventToMaybe);
            }
        });
        requestQueue.add(request);
    }

    /**
     * Request to search for an Event
     * @param requestQueue the queue to add the request to
     * @param param search params
     * @return the JSONArray of events
     */
    public static JSONArray searchEvents(RequestQueue requestQueue, HashMap<String, String> param) {
        JSONArray response = null;
        RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, UrlEndpoints.URL_DB +
                UrlEndpoints.URL_SEARCH, new JSONObject(param), requestFuture, requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Logger.debugLog(e + "");
        } catch (ExecutionException e) {
            Logger.debugLog(e + "");
        } catch (TimeoutException e) {
            Logger.debugLog(e + "");
        }
        return response;
    }

    /**
     * Sends request to get feeds
     * @param requestQueue the queue to add a request to
     */
    public static void requestFeedsNow(RequestQueue requestQueue) {
        Logger.debugLog("my id: " + Grapevine.getMyID());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.URL_DB +
                UrlEndpoints.URL_USERS + UrlEndpoints.URL_FEEDS + Grapevine.getMyID(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Logger.debugLog("Got response from request feeds now: " + response.toString());
                        ArrayList<Feed> foundFeeds = Parser.parseFeeds(response);
                        Logger.debugLog("Found feeds " + foundFeeds.toString());
                        Grapevine.getWritableDatabase().insertFeeds(GrapevineDB.GRAPEVINE_DB, foundFeeds, true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(request);
    }

    /**
     * Request feeds
     * @param requestQueue the queue to add a request to
     * @return the JSONArray of feeds
     */
    public static JSONArray requestFeeds(RequestQueue requestQueue) {
        Logger.debugLog("my id: " + Grapevine.getMyID());
        JSONArray response = null;
        RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.URL_DB +
                UrlEndpoints.URL_USERS + UrlEndpoints.URL_FEEDS + Grapevine.getMyID(),
                requestFuture, requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Logger.debugLog(e + "");
        } catch (ExecutionException e) {
            Logger.debugLog(e + "");
        } catch (TimeoutException e) {
            Logger.debugLog(e + "");
        }
        return response;
    }

    /**
     * Create Feed Request
     * @param requestQueue the queue to add the request
     * @param params the params for event
     */
    public static void createFeed(RequestQueue requestQueue, HashMap<String, Object> params) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_FEEDS,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.longToast("Feed creation unsuccessful");
            }
        });
        requestQueue.add(request);
    }

    /**
     * Change email address
     * @param requestQueue the queue to add the request
     * @param context which Activity coming from
     * @param cls which Activity to go to
     * @param params
     */
    public static void changeEmail(RequestQueue requestQueue, final Context context,
                                   final Class cls, HashMap<String, String> params) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_USERS + UrlEndpoints.URL_CHANGE_EMAIL + Grapevine.getMyID() + "/",
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(context, cls);
                        context.startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.longToast("Failed to change Email");
            }
        });
        requestQueue.add(request);
    }

    /**
     * Change user password
     * @param requestQueue the queue to add the request
     * @param context which Activity coming from
     * @param cls which Activity to go to
     * @param params
     */
    public static void changePassword(RequestQueue requestQueue, final Context context,
                                   final Class cls, HashMap<String, String> params) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_USERS + UrlEndpoints.URL_CHANGE_PASSWORD + Grapevine.getMyID() + "/",
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(context, cls);
                        context.startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.longToast("Failed to change Password");
            }
        });
        requestQueue.add(request);
    }

    /**
     * Change user bio
     * @param requestQueue the queue to add the request
     * @param context which Activity coming from
     * @param cls which Activity to go to
     * @param params
     */
    public static void changeBio(RequestQueue requestQueue, final Context context,
                                      final Class cls, HashMap<String, String> params) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_USERS + UrlEndpoints.URL_CHANGE_BIO + Grapevine.getMyID() + "/",
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(context, cls);
                        context.startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.longToast("Failed to change Bio");
            }
        });
        requestQueue.add(request);
    }

    /**
     * Edit an event
     * @param requestQueue the queue to add the request
     * @param params
     * @param id of event to edit
     */
    public static void editEvent(RequestQueue requestQueue, final HashMap<String, Object> params, final int id) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlEndpoints.URL_DB + UrlEndpoints.URL_EVENTS + id + "/",
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Logger.debugLog("editEvent: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.debugLog("params: " + params + ", id: " + id);
                Logger.longToast("Event edit unsuccessful");
            }
        });
        requestQueue.add(request);
    }
}
