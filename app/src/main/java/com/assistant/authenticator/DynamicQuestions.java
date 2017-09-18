package com.assistant.authenticator;

import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by TANS on 16-Feb-17.
 */

public class DynamicQuestions {

    static String answer;

    public static void friends(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            MainActivity.mainbox.removeAllViews();
                            LinearLayout holder = new LinearLayout(getApplicationContext());
                            holder.setOrientation(LinearLayout.VERTICAL);
                            ArrayList<LinearLayout> arr = new ArrayList<LinearLayout>();
                            ArrayList<Integer> profile = new ArrayList<Integer>();
                            profile.addAll(Arrays.asList(
                                    R.drawable.kishore_dubey,
                                    R.drawable.mohan_kumar,
                                    R.drawable.mukesh_patel,
                                    R.drawable.nidhi_shah,
                                    R.drawable.rocky_sharma
                            ));
                            ArrayList <String> names=new ArrayList<String>();
                            names.addAll(Arrays.asList("Kishore Dubey","Mohan Kumar","Mukesh Patel","Nidhi Shah","Rocky Sharma"));
                            Random rn = new Random();
                            int index;
                            for (int i = 0; i < 3; i++) {
                                LinearLayout ll = new LinearLayout(getApplicationContext());
                                ll.setOrientation(LinearLayout.HORIZONTAL);
                                TextView opt = new TextView(getApplicationContext());
                                opt.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                                opt.setGravity(Gravity.CENTER_VERTICAL);
                                opt.setTextSize(36);
                                opt.setPadding(5,5,20,5);
                                ll.addView(opt);
                                TextView tv = new TextView(getApplicationContext());
                                tv.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                                ProfilePictureView ppv = new ProfilePictureView(getApplicationContext());
                                ppv.setPresetSize(ProfilePictureView.NORMAL);
                                ppv.setPadding(5,5,15,5);
                                if(i==2){
                                    JSONArray jarray = response.getJSONObject().getJSONArray("data");
                                    index = Math.abs(rn.nextInt()) % jarray.length();
                                    ppv.setProfileId((jarray.getJSONObject(index)).get("id").toString());
                                    tv.setText(jarray.getJSONObject(index).get("name").toString());
                                    answer=tv.getText().toString();
                                }

                                else {
                                    index = Math.abs(rn.nextInt() % (5 - i));
                                    ppv.setDefaultProfilePicture(BitmapFactory.decodeResource(getApplicationContext().getResources(), profile.get(index)));
                                    tv.setText(names.get(index));
                                    names.remove(index);
                                    profile.remove(index);
                                }
                                tv.setGravity(Gravity.CENTER_VERTICAL);
                                ll.addView(ppv);
                                ll.addView(tv);
                                ll.setPadding(10, 10, 10, 10);
                                arr.add(ll);
                            }
                            Collections.shuffle(arr);
                            char option='A';
                            for (int i = 0; i < 3; i++) {
                                ((TextView)arr.get(i).getChildAt(0)).setText(option+++"");
                                holder.addView(arr.get(i));
                            }
                            MainActivity.mainbox.addView(holder);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }

    public static void posts(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            TextView tv1=new TextView(getApplicationContext());
                            JSONArray jarray = response.getJSONObject().getJSONArray("data");
                            tv1.setText("\nYour Posts");
                            int val=(5>jarray.length())? jarray.length():5;
                            for (int i = 0; i < 2; i++)
                                tv1.append("\n"+(i+1)+". "+jarray.getJSONObject(i).get("message"));
                            MainActivity.mainbox.addView(tv1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }

    public static void Interests(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/likes",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try{
                            TextView tv2=new TextView(getApplicationContext());
                            JSONArray jarray = response.getJSONObject().getJSONArray("data");
                            tv2.setText("\nYour interests");
                            int val=(5>jarray.length())? jarray.length():5;
                            for (int i = 0; i < val; i++)
                                tv2.append("\n"+(i+1)+". "+jarray.getJSONObject(i).get("name")+" ");
                            MainActivity.mainbox.addView(tv2);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();
    }

}
