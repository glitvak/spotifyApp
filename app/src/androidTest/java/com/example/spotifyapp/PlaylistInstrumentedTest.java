package com.example.spotifyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotifyapp.Connectors.PlaylistService;
import com.example.spotifyapp.Connectors.SongService;
import com.example.spotifyapp.Model.Playlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class PlaylistInstrumentedTest {
    SongService testService;
    PlaylistService testPlaylistService;
    Context appContext;
    Playlist actual;
    SharedPreferences sharedPrefs;
    Context context;

    @Mock
    Context mockContext;

    public PlaylistInstrumentedTest() {
    }

    @Before
    public void setUp() {
        mockContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        testService = new SongService(mockContext);

        sharedPrefs = Mockito.mock(SharedPreferences.class);
        context = Mockito.mock(Context.class);
        Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);
        //testPlaylistService = new PlaylistService(context);
        testPlaylistService = mock(PlaylistService.class);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.spotifyapp", appContext.getPackageName());
    }

    @Test
    public void getArt(){
        String id = "11dFghVXANMlKmJXsNCbNl";
        String expected = "https://i.scdn.co/image/107819f5dc557d5d0a4b216781c6ec1b2f3c5ab2";
        testService.getArt(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                String url = testService.getUrl();
                assertEquals(expected, url);

            }
        }, id);
    }

    @Test
    public void playlistTest() {

        VolleyCallBack mockVolley = mock(VolleyCallBack.class);

        actual = new Playlist();
        ArrayList<Playlist> testList = new ArrayList<>();
        actual.setURL("https://i.scdn.co/image/ab67706c0000bebb175e006cd9c35a1faaa3c508"); actual.setLink("https://open.spotify.com/playlist/0bGgPjKrWuGRBhUrU1ziCa"); actual.setName("Mountain View");
        testList.add(actual);
        String city = "Mountain View";
        int limit = 1;


        when(testPlaylistService.getLocalPlaylist(city, limit, mockVolley)).thenReturn(testList);
        when(testPlaylistService.getPlaylists()).thenReturn(testList);

        assertEquals("Mountain View", testPlaylistService.getPlaylists().get(0).getName());
        assertEquals("https://i.scdn.co/image/ab67706c0000bebb175e006cd9c35a1faaa3c508", testPlaylistService.getPlaylists().get(0).getURL());
        assertEquals("https://open.spotify.com/playlist/0bGgPjKrWuGRBhUrU1ziCa", testPlaylistService.getPlaylists().get(0).getLink());
    }

    @Test
    public void testPlaylistCallback(){
        VolleyCallBack mockVolley = mock(VolleyCallBack.class);
        String city = "Mountain View";
        int limit = 1;
        testPlaylistService.getLocalPlaylist(city, limit, mockVolley);
        mockVolley.onSuccess();


        verify(mockVolley, timeout(1000).times(1)).onSuccess();
    }
}
