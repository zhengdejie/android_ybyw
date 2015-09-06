package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import appframe.appframe.R;

/**
 * Created by Administrator on 2015/8/14.
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener{
    private ImageView img_avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.img_avatar:
                startActivity(new Intent(this,FriendsInfoActivity.class));
                break;


        }
    }

    public void init()
    {
        img_avatar = (ImageView)findViewById(R.id.img_avatar);
        img_avatar.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_order,menu);
        MenuItem addItem = menu.findItem(R.id.action_order);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
}
