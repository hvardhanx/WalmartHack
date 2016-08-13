package com.example.android.hospice.HomePage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.hospice.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class Home extends android.support.v4.app.Fragment {
    ArrayList<String> title;
    ArrayList<String> description;
    ArrayList<String> links;
    ArrayList<String> date;
    ProgressDialog prog;
    ListView ne;
    TextView emptyView;
    boolean firstTimeLaunch = false;
    public String test;
    private Context mContext = null;
    Realm myNews;

    public Home(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        Log.v("Context: ", mContext+"");
        RealmConfiguration config = new RealmConfiguration.Builder(mContext).build();
        myNews = Realm.getInstance(config);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_news_activity_main, container, false);
        ne = (ListView) view.findViewById(R.id.list);
        emptyView = (TextView) view.findViewById(R.id.empty);
        // Determine if app is being run for the first time
        final String PREFS_NAME = "MyPrefs";
        SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time
            Log.d("HOME: ", "First time launch");

            prog = ProgressDialog.show(getActivity(), "Retreiving News", "Please Wait..");
            new ReadRss().execute("http://www.ibnlive.com/xml/rss/health.xml");
            ne.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Log.v("if Size link:", links.size()+"");
                    Uri u = Uri.parse(links.get(position));
                    Intent in = new Intent(Intent.ACTION_VIEW, u);
                    startActivity(in);
                }
            });

            // Record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        } else {
            RealmResults<News> getNewsDb = myNews.where(News.class).findAll();
            final ArrayList<HashMap<String, String>> newsList = new ArrayList<HashMap<String, String>>();
            final ArrayList<String> mLink = new ArrayList<String>();
            for (News news: getNewsDb) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("title", news.getNewsTitle());
                map.put("description", news.getNewsDescription());
                map.put("pubDate", news.getNewsDate());
                map.put("link", news.getNewsUrl());
                mLink.add(news.getNewsUrl());
                newsList.add(map);
                ListAdapter adapter = new SimpleAdapter(getActivity(),
                        newsList,
                        R.layout.home_news_listview_layout,
                        new String[]{"title", "pubDate", "description", "link"},
                        new int[]{R.id.title, R.id.date, R.id.desc, R.id.link}
                );

                ne.setAdapter(adapter);
            }
            ne.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Log.v("else Size link:", mLink.size()+"");
                    Uri u = Uri.parse(mLink.get(position));
                    Intent in = new Intent(Intent.ACTION_VIEW, u);
                    startActivity(in);
                }
            });
        }

        // When News is refreshed
        Button refresh = (Button) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prog = ProgressDialog.show(getActivity(), "Retreiving News", "Please Wait..");
                new ReadRss().execute("http://www.ibnlive.com/xml/rss/health.xml");
                ne.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Log.v("refresh Size link:", links.size()+"");
                        Uri u = Uri.parse(links.get(position));
                        Intent in = new Intent(Intent.ACTION_VIEW, u);
                        startActivity(in);
                    }
                });
            }
        });
        return view;
    }

    public String getNews(String url) {
        String news = "";
        title = new ArrayList<String>();
        description = new ArrayList<String>();
        links = new ArrayList<String>();
        date = new ArrayList<String>();
        try {
            XmlPullParserFactory pullPar = XmlPullParserFactory.newInstance();
            XmlPullParser pull = pullPar.newPullParser();
            pull.setInput(getHttpInputStream(url), "UTF-8");
            boolean notInsideChannel = false;
            int event = pull.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    if (pull.getName().equals("item")) {
                        notInsideChannel = true;
                    }
                    if (pull.getName().equals("title")) {
                        if (notInsideChannel) {
                            title.add(pull.nextText());
                        }
                    }
                    if (pull.getName().equals("description")) {
                        if (notInsideChannel) {
                            description.add(pull.nextText());
                        }
                    }
                    if (pull.getName().equals("link")) {
                        if (notInsideChannel) {
                            links.add(pull.nextText());
                        }
                    }
                    if (pull.getName().equals("pubDate")) {
                        if (notInsideChannel) {
                            date.add(pull.nextText());
                        }
                    }
                } else if (event == XmlPullParser.END_TAG && pull.getName().equals("item")) {
                    notInsideChannel = false;
                }
                event = pull.next();
            }
        } catch (Exception e) {

        }
        return news;
    }

    public InputStream getHttpInputStream(String url) {
        InputStream in = null;
        try {
            URL u = new URL(url);
            URLConnection ucon = u.openConnection();
            if (ucon instanceof HttpURLConnection) {
                HttpURLConnection httpcon = (HttpURLConnection) ucon;
                httpcon.setAllowUserInteraction(false);
                httpcon.setInstanceFollowRedirects(true);
                httpcon.setRequestMethod("GET");
                httpcon.connect();
                int resp = httpcon.getResponseCode();
                if (resp == HttpURLConnection.HTTP_OK)
                    in = httpcon.getInputStream();
            }
        } catch (Exception e) {
            Log.v("Oops:", "Exception!!");
            Toast.makeText(getActivity(), "Check Your Internet Connection!!", Toast.LENGTH_SHORT).show();
        }
        return in;
    }

    public String imgUrlExtract(String str){
        String url;
        int firstOccurrence = str.indexOf("'");
        int secondOccurrence = str.indexOf("'", firstOccurrence + 1);
        url = str.substring(firstOccurrence+1,secondOccurrence);
        return url;
    }

    class ReadRss extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Loading..", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            return getNews(url[0]);
        }

        protected void onPostExecute(String news) {


            final ArrayList<HashMap<String, String>> newsList = new ArrayList<HashMap<String, String>>();
            if (news != null) {
                Log.v("News: ", news);
                prog.dismiss();
                if (title.size() == 0) {
                    emptyView.setText("No News Found.Check back Later. :/");
                    Toast.makeText(getActivity(), "No Results Found", Toast.LENGTH_SHORT).show();
                } else {
                    String nTitle, nURL, nDescription, nDate;
                    emptyView.setText("");
                    for (int i = 0; i < 20; i++) {
                        nTitle = title.get(i);
                        nURL = links.get(i);
                        nDescription = description.get(i);
                        nDate = date.get(i);
                        Log.v("TITLE: ", nTitle);
                        Log.v("DESC: ", nDescription);

                        String imgUrl = imgUrlExtract(nDescription);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("title", nTitle);
                        map.put("description", nDescription);
                        map.put("pubDate", nDate);
                        map.put("link", imgUrl);
                        newsList.add(map);
                        // Save Data in Realm
                        myNews.beginTransaction();
                        // Create an object
                        News myNewsObject = myNews.createObject(News.class);
                        // Set its fields
                        myNewsObject.setNewsTitle(nTitle);
                        myNewsObject.setNewsDescription(nDescription);
                        myNewsObject.setNewsDate(nDate);
                        myNewsObject.setNewsUrl(imgUrl);
                        // Commit Transaction
                        myNews.commitTransaction();

                        ListAdapter adapter = new SimpleAdapter(getActivity(),
                                newsList,
                                R.layout.home_news_listview_layout,
                                new String[]{"title", "pubDate", "description", "link"},
                                new int[]{R.id.title, R.id.date, R.id.desc, R.id.link}
                        );

                        ne.setAdapter(adapter);

                    }
                }
            }
        }
    }



    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
