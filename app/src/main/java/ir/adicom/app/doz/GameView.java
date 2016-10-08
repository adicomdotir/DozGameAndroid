package ir.adicom.app.doz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by adicom on 10/5/16.
 */
public class GameView extends ImageView {

    int[][] cells = new int[8][8];
    byte user = 1;
    protected static final int[] DX = { -1,  0,  1, -1, 1, -1, 0, 1 };
    protected static final int[] DY = { -1, -1, -1,  0, 0,  1, 1, 1 };

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        cells[3][3] = 1;
        cells[3][4] = 2;
        cells[4][3] = 2;
        cells[4][4] = 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int padding = 20;
        Paint back = new Paint();
        back.setColor(Color.GRAY);
        int cell = (getWidth()-padding)/8;
        canvas.drawRect(0,0,getWidth(),getWidth(), back);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStrokeWidth(3);
        for(int i=0; i<=8; i++) {
            canvas.drawLine(0+(padding/2), i*cell+(padding/2), getWidth()-padding/2, i*cell+(padding/2), p);
            canvas.drawLine(i*cell+(padding/2), 0+(padding/2), i*cell+(padding/2), getWidth()-(padding/2), p);
        }
        Paint pWhite = new Paint();
        pWhite.setColor(Color.WHITE);
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                float leftP = (padding / 2) + i * cell + 5;
                float topP = (padding / 2) + j * cell + 5;
                float rightP = (padding / 2) + (i + 1) * cell - 5;
                float bottomP = (padding / 2) + (j + 1) * cell - 5;
                RectF rectF = new RectF(leftP, topP, rightP, bottomP);
                if (cells[i][j]==1) {
                    canvas.drawArc (rectF, 90, 360, true, p);
                } else if (cells[i][j]==2) {
                    canvas.drawArc (rectF, 90, 360, true, pWhite);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int padding = 10;
        int xIndex = -1, yIndex = -1;
        int cell = (getWidth()-padding-padding)/8;
        float x = event.getX();
        float y = event.getY();
        for(int i=0; i<8; i++) {
            if(x>padding+i*cell && x<padding+cell*(i+1)) {
                xIndex = i;
                break;
            }
        }
        for(int i=0; i<8; i++) {
            if(y>padding+i*cell && y<padding+cell*(i+1)) {
                yIndex = i;
                break;
            }
        }
        if(yIndex!=-1 && xIndex!=-1) {
            if (cells[xIndex][yIndex] == 0) {

                if (checkRow(xIndex, yIndex)) {
                    cells[xIndex][yIndex] = user;
                    user = (byte) ((user == 1) ? 2 : 1);
                }
            }
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    private boolean checkRow(int xIndex, int yIndex) {
        for (int ii = 0; ii < DX.length; ii++) {
            boolean sawOther = false;
            int x = xIndex, y = yIndex;
            for (int dd = 0; dd < 8; dd++) {
                x += DX[ii];
                y += DY[ii];

                if (!inBounds(x, y)) break;
                else if (user != cells[x][y] && cells[x][y] != 0) {
                    sawOther = true;
                } else if (sawOther && user == cells[x][y]) {
                    while(x!=xIndex || y!=yIndex) {
                        cells[x][y] = user;
                        x -= DX[ii];
                        y -= DY[ii];
                        invalidate();
                    }
                    return true;
                } else break;
            }
        }
        return false;
    }

    private boolean inBounds(int x, int y) {
        return (x >= 0) && (x < 8) && (y >= 0) && (y < 8);
    }
}
