package com.example.android.mydeltatask2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    cellcanvas cell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cell= new cellcanvas(this);
        setContentView(R.layout.intro);
    }
    float x=6,y=6;
    int l=6,z=6;
    float r;
    float h,w;
    int tx;
    float cx=-1,cy=-1;
    int[] d;
    int[][] frame;
    public class cellcanvas extends View
    {
        Paint paint;
        Paint trans;

        public Bitmap bitmap;


        public cellcanvas(Context context)
        {
            super(context);
            paint= new Paint();
            trans = new Paint();

        }
        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);

            if (bitmap == null) {
                createWindowFrame();
            }
            canvas.drawBitmap(bitmap, 0, 0, null);
        }

        public void createWindowFrame()
        {   bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ALPHA_8);
            Canvas temp = new Canvas(bitmap);
            paint.setColor(Color.parseColor("#000000"));
            temp.drawRect(0,0,getWidth(),getHeight(),paint);
            trans.setColor(Color.TRANSPARENT);
            trans.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            h=getHeight();
            w=getWidth();
            float tr=((1/ (x)) *w<(1/ (y)) *h)?((1/(x))*w):((1/(y))*h);
            r=tr/2-2*getResources().getDisplayMetrics().density;
            for(float i=1;i<=y;i+=1) {
                for (float j = 1; j <= x; j+=1) {
                    temp.drawCircle(((2*j-1)/(2*x))*w, ((2*i-1)/(2*y))*h,tr/2-2*getResources().getDisplayMetrics().density, trans);
                }
            }

        }


        @Override
        public boolean onTouchEvent(MotionEvent event)
        {   if(event.getAction()==MotionEvent.ACTION_DOWN) {
            cx = event.getX();
            cy = event.getY();
            tx=(int)Math.floor(cx*x/w);
            if(d[tx]<z)
            function();

        }

            return true;
        }

    }
    public class balls extends View
    {
        Paint red,yellow;

        public balls(Context context) {
            super(context);

            red=new Paint();
            red.setColor(Color.parseColor("#f44336"));
            yellow=new Paint();
            yellow.setColor(Color.YELLOW);
        }

        @Override
        public void onDraw(Canvas canvas)
        {   if(count%2==0)
            canvas.drawCircle(((2*tx+1)/(2*x))*w,h/(2*y),r,yellow);
        else
            canvas.drawCircle(((2*tx+1)/(2*x))*w,h/(2*y),r,red);
        }
    }
    ArrayList<Integer> lastid=new ArrayList<Integer>();
    int count=0;
    public void function()
    {   balls b;
        b=new balls(this);
        RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        b.setLayoutParams(vp);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.lay);
        count++;
        if(count%2==1)
        {frame[tx][d[tx]]=1;
        }
        else
        {frame[tx][d[tx]]=2;
        }
        b.setId(tx+d[tx]*l);
        lastid.add(tx+d[tx]*l);
        d[tx]++;
        relativeLayout.addView(b);
        setframe();
        b.animate().translationY((1-(1/y))*h-(d[tx]-1)*h/y);
        b.animate().setDuration(1500);
        check();
        if(count==l*z)
        { tie();
        }
    }

    public void setframe() {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.lay);
            cell = new cellcanvas(this);
            RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            vp.addRule(RelativeLayout.ABOVE,R.id.temp3);
            cell.setLayoutParams(vp);
            relativeLayout.addView(cell);
    }
    public void result()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.result);
                TextView textView = (TextView) findViewById(R.id.res);
                if(frame[tx][d[tx]-1]==1)
                {
                    textView.setText("Red");
                    textView.setTextColor(Color.parseColor("#f44336"));
                }
                else
                {
                    textView.setText("Yellow");
                    textView.setTextColor(Color.YELLOW);
                }

            }
        },1500);

    }

    public void check()
    { int a,b;
        a=tx;
        b=d[tx]-1;
        if(b>2&&frame[a][b]==frame[a][b-1]&&frame[a][b]==frame[a][b-2]&&frame[a][b]==frame[a][b-3])
        {
        result();
        }

        for(int i=0;i<l-3;i++)
        {if(frame[i][b]==frame[a][b]&&frame[i+1][b]==frame[a][b]&&frame[i+2][b]==frame[a][b]&&frame[i+3][b]==frame[a][b])
         {
             result();
         }
        }
        if(a-b>=0) {
            for (int i = 0; i < z - 3&&a-b+i<l-3; i++) {
                if (frame[a-b+i][i]==frame[a][b]&&frame[a-b+i+1][i+1]==frame[a][b]&&frame[a-b+i+2][i+2]==frame[a][b]&&frame[a-b+i+3][i+3]==frame[a][b])
                {
                    result();
                }
            }
        }
        else
        {
            for(int i=0;i<l-3&&b-a+i<z-3;i++)
            {
                if(frame[i][b-a+i]==frame[a][b]&&frame[i+1][b-a+i+1]==frame[a][b]&&frame[i+2][b-a+i+2]==frame[a][b]&&frame[i+3][b-a+i+3]==frame[a][b])
                {
                    result();
                }
            }
        }
        if(a+b<l)
        {
            for(int i=0;i<z-3&&i<a+b-2;i++)
            {
                if(frame[a+b-i][i]==frame[a][b]&&frame[a+b-i-1][i+1]==frame[a][b]&&frame[a+b-i-2][i+2]==frame[a][b]&&frame[a+b-i-3][i+3]==frame[a][b])
                {

                    result();
                }
            }
        }
        else
        {
            for(int i=l-1;i>2&&i>a+b-z+3;i--)
            {
                if(frame[i][a+b-i]==frame[a][b]&&frame[i-1][a+b-i+1]==frame[a][b]&&frame[i-2][a+b-i+2]==frame[a][b]&&frame[i-3][a+b-i+3]==frame[a][b])
                {
                    result();
                }
            }
        }
    }
    public void tie()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.result);
                TextView textView = (TextView) findViewById(R.id.res);
                TextView textView1 = (TextView) findViewById(R.id.restop);
                textView.setText("Tie");
                textView1.setVisibility(View.GONE);

            }
        },1500);

    }

    public void undo(View view)
    {RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.lay);
    if(count!=0)
    {
        View t = relativeLayout.findViewById(lastid.get(count - 1));
        d[lastid.get(count - 1) % l]--;
        frame[lastid.get(count-1)%l][d[lastid.get(count-1)%l]]=0;
        lastid.remove(--count);
        relativeLayout.removeView(t);
    }
    }
    public void dim(View view)
    {
        EditText e1 = (EditText) findViewById(R.id.rows);
        EditText e2 = (EditText) findViewById(R.id.columns);
        if(!(e1.getText().toString().equals(""))&&!(e2.getText().toString().equals("")))
        {z=Integer.parseInt(e1.getText().toString());
         l=Integer.parseInt(e2.getText().toString());
         y=Float.valueOf(e1.getText().toString());
         x=Float.valueOf(e2.getText().toString());
         d=new int[l];
         frame = new int[l][z];
         setContentView(R.layout.mode);
        }
    }
    int mode=0;
    public void computer(View view)
    {mode=1;
    }

    public void player(View view)
    {mode=0;
        setContentView(R.layout.activity_main);
        setframe();
    }

}