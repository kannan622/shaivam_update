/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.shaivam.playback;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import org.shaivam.model.Selected_songs;

import java.util.List;

public interface PlayerAdapter {

    void initMediaPlayer(Context context);

    void release();

    boolean isMediaPlayer();

    boolean isPlaying();

    void resumeOrPause();

    void reset();
    void resetSong();
    boolean isReset();
    void stop();
    void instantReset();

    void skip(final boolean isNext);

    void openEqualizer(@NonNull final Activity activity);

    void seekTo(final int position);

    void setPlaybackInfoListener(final PlaybackInfoListener playbackInfoListener);

    Selected_songs getCurrentSong();

    @PlaybackInfoListener.State
    int getState();

    int getPlayerPosition();

    int getDuration();

    void registerNotificationActionsReceiver(final boolean isRegister);

    void setCurrentSong(@NonNull final Selected_songs song, @NonNull final List<Selected_songs> songs);

    MediaPlayer getMediaPlayer();

    void onPauseActivity();

    void onResumeActivity();
}
