package com.elegion.githubclient.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.elegion.githubclient.AppDelegate;
import com.elegion.githubclient.MyImageView;
import com.elegion.githubclient.R;
import com.elegion.githubclient.api.ApiClient;
import com.elegion.githubclient.model.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author Artem Mochalov.
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {

    private Button mRepositoriesButton;
    private TextView mUserName;
    private MyImageView mUserAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_user);
        mRepositoriesButton = (Button) findViewById(R.id.btn_my_repositories);
        mUserName = (TextView) findViewById(R.id.txt_user_name);
        mUserAvatar = (MyImageView) findViewById(R.id.img_user_avatar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new GetUserTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRepositoriesButton != null) {
            mRepositoriesButton.setOnClickListener(this);
        }
    }

    @Override
    protected void onPause() {
        if (mRepositoriesButton != null) {
            mRepositoriesButton.setOnClickListener(null);
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v == mRepositoriesButton) {
            startActivity(MyRepositoriesActivity.class, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetUserTask extends AsyncTask<Void, Void, User> implements Transformation {

        @Override
        protected void onPreExecute() {
            mRepositoriesButton.setEnabled(false);
        }

        @Override
        protected User doInBackground(Void... params) {
            try {
                JSONObject responseObject = new ApiClient()
                        .addAuthHeader()
                        .setUrl(ApiClient.GET_CURRENT_USER_URL)
                        .executeGet();

                int statusCode = responseObject.optInt(ApiClient.STATUS_CODE);

                if (statusCode != ApiClient.STATUS_CODE_OK) {
                    //TODO: handle error
                } else {
                    String userName = responseObject.optString("login");
                    String userAvatar = responseObject.optString("avatar_url");

                    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userAvatar)) {
                        return new User(userName, userAvatar);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                mUserName.setText(user.getName());
                AppDelegate.getSettings().edit()
                        .putString(AppDelegate.USER_NAME, user.getName())
                        .apply();
                RequestCreator creator = Picasso.with(UserActivity.this).load(user.getAvatarUrl());
                if (mUserAvatar.RoundCrop)
                    creator.transform(this);
                creator.into(mUserAvatar);
                mRepositoriesButton.setEnabled(true);
            }
        }

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
