package com.henry.timer;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.media.SoundPool;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

public class MainActivity<sec> extends AppCompatActivity {
    public Button oneBtn;
    public  Button startOrStopBtn;
    public  Button pauseBtn;
    TextView TimerView;
    Timer timer = new Timer(true);
    private TimerTask task;
    private Handler mHandler = null;

    int sec = 10;

    private static int count = 0;
    private boolean isPause = false;
    private boolean isStop = true;

    private static int delay = 1000;  //1s
    private static int period = 1000;  //1s

    private static final int UPDATE_TEXTVIEW = 0;
    MediaPlayer ring;


    @Override
    protected void onStart() {
        super.onStart();//
    }

    //android app 生命週期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ////////***** UI初始化******///////////////////////
        oneBtn = (Button)findViewById(R.id.button2);
        startOrStopBtn = (Button)findViewById(R.id.btn_startOrStop);
        TimerView = (TextView)findViewById(R.id.textView2);
        pauseBtn =  (Button)findViewById(R.id.btn_Pause);


        ////////***** UI初始化******///////////////////////
        //pauseBtn.setBackgroundColor(Color.BLUE);

        //播放聲音初始化
        ring= MediaPlayer.create(MainActivity.this,R.raw.btn_noise);

        //傳地訊息的Handler來做初始化
        mHandler = new Handler()
        {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_TEXTVIEW:
                        updateTextView();
                        break;
                    default:
                        break;
                }
            }
        };


    }

    private void updateTextView()
    {
        TimerView.setText(String.valueOf(sec));
    }



    private void stopTimer(){

        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }

        if (task != null) {
            task.cancel();
            task = null;
        }
        sec = 10;
        TimerView.setText(String.valueOf(sec));
    }
    private void startTimer(){
        if (timer == null) {
            timer = new Timer();
        }

        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    sendMessage(UPDATE_TEXTVIEW);//寄送訊息告訴UI主線程畫面更新

                    do {
                        try {
                            Thread.sleep(1000);//暫停1秒
                        } catch (InterruptedException e) {
                        }
                    } while (isPause);//如果按下pause就在這邊作暫停

                   // count ++;
                    sec -=1;
                    if(sec==-1)
                    {
                        stopTimer();
                        startOrStopBtn.setText("Start");
                    }
                }
            };
        }

        //如果以下timer物件跟task物件存在的話
        if(timer != null && task != null )
            //
            timer.schedule(task, delay, period);

    }

    //負責各個線程之間的訊息傳遞
    public void sendMessage(int id){
        if (mHandler != null) {
            Message message = Message.obtain(mHandler, id);
            mHandler.sendMessage(message);
        }
    }

/////////////////////////////下面是UI按鈕的function
    public void Btn_PauseFunc(View view) {

        isPause = !isPause;

        if (isPause) {
            pauseBtn.setBackgroundColor(Color.BLUE);
            startOrStopBtn.setText("Start");
        } else {
            pauseBtn.setBackgroundColor(Color.WHITE);

            startOrStopBtn.setText("Stop");
        }

    }
    public void minus(View view) {
        sec -= 60;
        if(sec >= 0) {
            String second = String.valueOf(sec);
            TimerView.setText(second);
        }
        else{
            TimerView.setText("不要鬧");
        }
        //播放聲音
        ring.start();

    }

    public void plus(View view) {
        TextView PlusView = (TextView)findViewById(R.id.textView2);
        sec += 60;
        if(sec < 0) {
            sec = 0;
        }
        String second = String.valueOf(sec);
        PlusView.setText(second);
        ring.start();
    }

    public void enter(View view)
    {
        isStop = !isStop;

        if (!isStop) {
            startTimer();
        }else {
            stopTimer();
        }

        if (isStop)
        {
            startOrStopBtn.setText("Start");
        } else {
            startOrStopBtn.setText("Stop");
        }


    }
}
