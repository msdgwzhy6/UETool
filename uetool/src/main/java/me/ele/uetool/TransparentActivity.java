package me.ele.uetool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Toast;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import me.ele.uetool.function.EditAttrLayout;
import me.ele.uetool.function.GriddingLayout;

import static me.ele.uetool.TransparentActivity.Type.TYPE_EDIT_ATTR;
import static me.ele.uetool.TransparentActivity.Type.TYPE_SHOW_GRDDING;
import static me.ele.uetool.TransparentActivity.Type.TYPE_UNKNOWN;

public class TransparentActivity extends AppCompatActivity {

  public static final String EXTRA_TYPE = "extra_type";

  private Activity bindActivity;
  private ViewGroup vContainer;
  private int type;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Util.setStatusBarColor(getWindow(), Color.TRANSPARENT);
    Util.enableFullscreen(getWindow());
    setContentView(R.layout.uet_activity_transparent);

    bindActivity = UETool.getInstance().getTargetActivity();

    vContainer = findViewById(R.id.container);

    type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_UNKNOWN);

    switch (type) {
      case TYPE_EDIT_ATTR:
        vContainer.addView(new EditAttrLayout(this));
        Toast.makeText(this, "捕捉控件已打开，请点击您感兴趣的控件", Toast.LENGTH_SHORT).show();
        break;
      case TYPE_SHOW_GRDDING:
        vContainer.addView(new GriddingLayout(this));
        break;
      default:
        Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
        finish();
        break;
    }
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    if (type == TYPE_SHOW_GRDDING) {
      bindActivity.dispatchTouchEvent(ev);
    }
    return super.dispatchTouchEvent(ev);
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(0, 0);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (bindActivity.equals(UETool.getInstance().getTargetActivity())) {
      UETool.getInstance().release();
    }
    bindActivity = null;
    UETool.getInstance().release();
  }

  @Override protected void onStop() {
    super.onStop();
    finish();
  }

  @IntDef({
      TYPE_UNKNOWN,
      TYPE_EDIT_ATTR,
      TYPE_SHOW_GRDDING,
  })
  @Retention(RetentionPolicy.SOURCE) public @interface Type {
    int TYPE_UNKNOWN = -1;
    int TYPE_EDIT_ATTR = 1;
    int TYPE_SHOW_GRDDING = 2;
  }
}
