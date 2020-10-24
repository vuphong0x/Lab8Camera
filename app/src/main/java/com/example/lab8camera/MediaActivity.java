package com.example.lab8camera;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MediaActivity extends AppCompatActivity {
    // Media Player
    MediaPlayer mediaPlayer;
    public TextView startTimeField, endTimeField, tvSongName;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekBar;
    private ImageButton playButton, pauseButton;
    public static int oneTimeOnly = 0;
    private List<Song> songsList;
    ListView lvPlaylist;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        // Media Player
        songsList = new ArrayList<>();
        lvPlaylist = findViewById(R.id.lvPlaylist);
        tvSongName = findViewById(R.id.tvSongName);
        startTimeField = findViewById(R.id.tvStartTime);
        endTimeField = findViewById(R.id.tvEndTime);
        seekBar = findViewById(R.id.seekBar);
        playButton = findViewById(R.id.imgPlay);
        pauseButton = findViewById(R.id.imgPause);

        seekBar.setClickable(false);

        // Playlist
        songsList.add(new Song("Hoa Hải Đường","https://data25.chiasenhac.com/download2/2119/6/2118057-1a95e408/128/Hoa%20Hai%20Duong%20-%20Jack.mp3"));
        songsList.add(new Song("Vài Lần Đón Đưa", "https://data00.chiasenhac.com/downloads/1813/6/1812695-b79f07eb/128/Vai%20Lan%20Don%20Dua%20-%20Soobin%20Hoang%20Son_%20Toul.mp3"));
        songsList.add(new Song("Anh Thanh Niên", "https://data20.chiasenhac.com/downloads/2061/6/2060821-946b2223/128/Anh%20Thanh%20Nien%20-%20HuyR.mp3"));
        songsList.add(new Song("Đừng Lo Anh Đợi Mà", "https://data20.chiasenhac.com/downloads/2078/6/2077122-498dad69/128/Dung%20Lo%20Anh%20Doi%20Ma%20-%20Mr%20Siro.mp3"));
        songsList.add(new Song("Gánh Mẹ", "https://data2.chiasenhac.com/stream2/1705/6/1704174-436da6f5/128/Ganh%20Me%20-%20Luu%20Minh%20Tuan_%20Quach%20Beem.mp3"));

        ArrayList<String> songName = new ArrayList<>();
        for (Song song : songsList) {
            songName.add(song.name);
        }

        ArrayAdapter adapter = new ArrayAdapter(MediaActivity.this, android.R.layout.simple_list_item_1, songName);
        lvPlaylist.setAdapter(adapter);
        lvPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvSongName.setText(songsList.get(i).name);
                url = songsList.get(i).link;
            }
        });
    }

    public void play(View view) {
        Uri uri = Uri.parse(url);
        mediaPlayer = MediaPlayer.create(this, uri);
        Toast.makeText(this, "Playing sound", Toast.LENGTH_SHORT).show();
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if (oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        endTimeField.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes((long) finalTime))));
        startTimeField.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes((long) startTime))));
        seekBar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 100);
        pauseButton.setEnabled(true);
        playButton.setEnabled(false);
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeField.setText(String.format(
                    "%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes((long) startTime))));

            seekBar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    public void pause(View view) {
        Toast.makeText(this, "Pausing sound", Toast.LENGTH_SHORT).show();
        mediaPlayer.pause();
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
    }

    public void forward(View view) {
        int temp = (int) startTime;
        if ((temp + forwardTime) <= finalTime) {
            startTime += forwardTime;
            mediaPlayer.seekTo((int) startTime);
        } else {
            Toast.makeText(this, "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
        }
    }
}