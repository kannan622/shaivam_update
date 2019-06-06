package org.shaivam.loaders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.shaivam.model.Selected_songs;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.MyApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SongProvider {
    public static final int ARTISTS_LOADER = 0;

    private static final int TITLE = 0;
    private static final int TRACK = 1;
    private static final int YEAR = 2;
    private static final int DURATION = 3;
    private static final int PATH = 4;
    private static final int ALBUM = 5;
    private static final int ARTIST_ID = 6;
    private static final int ARTIST = 7;

    private static final String[] BASE_PROJECTION = new String[]{
            AudioColumns.TITLE,// 0
            AudioColumns.TRACK,// 1
            AudioColumns.YEAR,// 2
            AudioColumns.DURATION,// 3
            AudioColumns.DATA,// 4
            AudioColumns.ALBUM,// 5
            AudioColumns.ARTIST_ID,// 6
            AudioColumns.ARTIST,// 7
    };

    private static List<Thirumurai_songs> mAllDeviceSongs = new ArrayList<>();

    public static List<Thirumurai_songs> getAllDeviceSongs() {
        return mAllDeviceSongs;
    }

    @NonNull
    static List<Thirumurai_songs> getSongs() {
        final List<Thirumurai_songs> songs = new ArrayList<>();
        songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(1));
        mAllDeviceSongs.addAll(songs);

        if (songs.size() > 1) {
            sortSongsByTrack(songs);
        }
        return songs;
    }

    private static void sortSongsByTrack(List<Thirumurai_songs> songs) {

        Collections.sort(songs, new Comparator<Thirumurai_songs>() {
            @SuppressLint("NewApi")
            public int compare(Thirumurai_songs obj1, Thirumurai_songs obj2) {
                return Long.compare(obj1.getId(), obj2.getId());
            }
        });
    }

    public static class AsyncSongLoader extends WrappedAsyncTaskLoader<List<Selected_songs>> {
        public Thirumurai_songs thirumurai_songs;
        public Context context;

        public AsyncSongLoader(@NonNull final Context context, Thirumurai_songs thirumurai_songs) {
            super(context);
            this.context = context;
            this.thirumurai_songs = thirumurai_songs;
        }

        @Override
        public List<Selected_songs> loadInBackground() {

            final List<Selected_songs> songs = new ArrayList<>();
            if (thirumurai_songs!=null && thirumurai_songs.getAudioUrl()!=null &&
                    !thirumurai_songs.getAudioUrl().equalsIgnoreCase("")) {
                String[] songs_split = thirumurai_songs.getAudioUrl().split(",");
                for (int j = 0; j < songs_split.length; j++) {
                    String[] arrOfStr = songs_split[j].split("audio/");
                    String[] arrays = arrOfStr[1].split("/");

                    songs.add(new Selected_songs(songs_split[j], arrays[0], thirumurai_songs.getTitle(), thirumurai_songs.getThalam()));
                }
            }
            return songs;
        }
    }
    public static class AsyncSongLoaderString extends WrappedAsyncTaskLoader<List<Selected_songs>> {
        public List<Selected_songs>  thirumurai_songs;
        public Context context;

        public AsyncSongLoaderString(@NonNull final Context context, List<Selected_songs> thirumurai_songs) {
            super(context);
            this.context = context;
            this.thirumurai_songs = thirumurai_songs;
        }

        @Override
        public List<Selected_songs> loadInBackground() {

            final List<Selected_songs> songs = new ArrayList<>();
            songs.addAll(thirumurai_songs);
            return songs;
        }
    }
    private static String getSongLoaderSortOrder() {
        return MediaStore.Audio.Artists.DEFAULT_SORT_ORDER + ", " + MediaStore.Audio.Albums.DEFAULT_SORT_ORDER + ", " + MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
    }

    @Nullable
    static Cursor makeSongCursor(@NonNull final Context context, final String sortOrder) {
        try {
            return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    BASE_PROJECTION, null, null, sortOrder);
        } catch (SecurityException e) {
            return null;
        }
    }
}
