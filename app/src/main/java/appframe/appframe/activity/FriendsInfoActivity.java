package appframe.appframe.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import appframe.appframe.R;

/**
 * Created by Administrator on 2015/8/21.
 */
public class FriendsInfoActivity extends BaseActivity {
    private ImageView img_avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsinfo);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_finfo, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
