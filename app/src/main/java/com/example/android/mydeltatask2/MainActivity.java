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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    cellcanvas cell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cell= new cellcanvas(this);
        setContentView(R.layout.activity_main);
        setframe();
    }
    float x=6,y=6;
    int l=6;
    float r;
    float h,w;
    int tx;
    float cx=-1,cy=-1;
    int[] d={0,0,0,0,0,0};

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
        {   bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
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
            d[tx]++;
            dropballs();


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
            red.setColor(Color.RED);
            yellow=new Paint();
            yellow.setColor(Color.YELLOW);
        }

        @Override
        public void onDraw(Canvas canvas)
        {
            canvas.drawCircle(((2*tx+1)/(2*x))*w,h/(2*y),r,yellow);
        }

    }
    public void dropballs()
    {   balls b;
        b=new balls(this);
        RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        b.setLayoutParams(vp);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.lay);
        relativeLayout.addView(b);
        setframe();
        b.animate().translationY((1-(1/y))*h-(d[tx]-1)*h/y);
        b.animate().setDuration(1500);

    }
    public void setframe()
    {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.lay);
        cell= new cellcanvas(this);
        Button button=(Button) findViewById(R.id.temp);
        RelativeLayout.LayoutParams ap = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        ap.addRule(RelativeLayout.ABOVE,button.getId());
        cell.setLayoutParams(ap);
        relativeLayout.addView(cell);
    }

}