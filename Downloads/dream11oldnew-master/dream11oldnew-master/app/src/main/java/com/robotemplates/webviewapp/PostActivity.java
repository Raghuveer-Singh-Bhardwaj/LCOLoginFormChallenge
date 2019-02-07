package com.robotemplates.webviewapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PostActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    LinearLayout mLinearLayout;
    private InterstitialAd mInterstitialAd;
    private WebView mWebView;
    private AdView mAdView;
    private ProgressBar mProgressBar;
    private Button reward;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mLinearLayout = findViewById(R.id.mLinearLayout);
        mToolbar.setLogo(R.drawable.cricket);

        mProgressBar = findViewById(R.id.mProgressBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(getIntent()!=null){
            String title = getIntent().getStringExtra("title");
            String date = getIntent().getStringExtra("date");
            String content = getIntent().getStringExtra("content");            TextView mDate = findViewById(R.id.mDate);
          //  TextView mContent = findViewById(R.id.mContent);

            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yy");

            try {

                String reformattedStr = myFormat.format(fromUser.parse(date));
                mDate.setText(reformattedStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }





            String newC = content.replaceAll("src=\"/","src=\""+new String(Base64.decode("aHR0cDovL3NpeWEueHl6Lw==",Base64.DEFAULT)));

            if(getIntent().getStringExtra("DOMAIN")!=null){
                newC = content.replaceAll("src=\"/","src=\"http://"+getIntent().getStringExtra("DOMAIN")+"/");
            }
            mWebView = findViewById(R.id.main_webview);

            mWebView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            mWebView.setLongClickable(false);
            mWebView.setHapticFeedbackEnabled(false);
            //  setupView();
            mWebView.getSettings().setJavaScriptEnabled(true);
             String data = "<html><link href=\"https://fonts.googleapis.com/css?family=Ubuntu\" rel=\"stylesheet\"> <style> @font-face { font-family: 'MYFONT'; src: url('file:///android_asset/robotoregular.ttf'); } img{display: inline;height: auto;max-width:   100%;}</style> " +
                    "<head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head>"+newC;
            mWebView.loadDataWithBaseURL(null, data, null, "UTF-8", null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            }
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    // TODO Auto-generated method stub
                    super.onPageStarted(view, url, favicon);
                    mProgressBar.setVisibility(View.VISIBLE);
                    mWebView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
                    super.onPageFinished(view, url);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mWebView.setVisibility(View.VISIBLE);
                }

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                        view.getContext().startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            mWebView.setVerticalScrollBarEnabled(true);
            mWebView.setHorizontalScrollBarEnabled(true);

            Document doc = Jsoup.parse(newC);

            Elements all = doc.getAllElements();
            int breakCnt = 0;
            for(Element e:all){
                Log.d("ADVANCE","Tag:"+e.tagName()+" Value:"+e.html());
                if(e.tagName() == "img" ){
                    breakCnt = 0;
                    final String url = e.absUrl("src");
                    Target mTarget = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ImageView imageView = new ImageView(PostActivity.this);
                       //     LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                         //   param.setMargins(0,0,0,0);
                            imageView.setPadding(0,0,0,0);
                            Display display = getWindowManager().getDefaultDisplay();
                            int width = display.getWidth();
                           // imageView.setLayoutParams(new LinearLayout.LayoutParams(width,bitmap.getHeight()));
                        //    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        //    imageView.setAdjustViewBounds(true);
                            Picasso.get().load(url).resize(width,0).into(imageView);
                            mLinearLayout.addView(imageView);

                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };

                    if(e.absUrl("src").startsWith("http")){
                        Picasso.get().load(e.absUrl("src")).into(mTarget);

                    }
                    // /Log.d("YO",image.baseUri());


                } else if(e.tagName() == "br"){
                    TextView t = new TextView(this);
                    t.setText("Brak");
                    t.setVisibility(View.INVISIBLE);
                    if(breakCnt==0){
                       // mLinearLayout.addView(t);
                        breakCnt =1;
                    }
                    //
                } else if(e.tagName() == "p" || e.tagName()=="strong"){
                    Element ch=null;
                    if(e.getAllElements().size()!=0){
                        for(Element element : e.getAllElements()){
                            if (element!=null && !element.tagName().equals("img") &&(element.tagName().equals("p"))) {
                                Elements elements = element.select("img");
                                for(Element element1:elements){
                                    element1.remove();
                                }
                                Elements breaks = element.select("br");
                              /*  int deleteCnt = 0;
                                if(breaks.size()>1){
                                    for(Element br:breaks){
                                        if(deleteCnt==0)
                                            deleteCnt = 1;
                                        else
                                            br.remove();

                                    }
                                }*/
                                for(Element br:breaks){

                                        br.remove();

                                }

                                TextView textView = new TextView(this);
                                Typeface typeface = ResourcesCompat.getFont(this, R.font.bungeereg);
                                textView.setTypeface(typeface);
                                Log.d("HTMLINFO",element.html()+" TAG:"+element.tagName());
                                textView.setText(Html.fromHtml(element.html(),new Html.ImageGetter() {

                                    @Override
                                    public Drawable getDrawable(String source) {
                                        return null;
                                    }
                                }, null));
                                textView.setTextSize(16);

                                textView.setTextColor(Color.parseColor("#000000"));

                                mLinearLayout.addView(textView);
                                breakCnt = 0;
                            }
                        }
                    }/*else{
                        TextView textView = new TextView(this);
                        Typeface typeface = ResourcesCompat.getFont(this, R.font.robotoregular);
                        textView.setTypeface(typeface);
                        textView.setText(Html.fromHtml(e.html()));
                        textView.setTextColor(Color.parseColor("#000000"));
                        textView.setTextSize(16);
                        mLinearLayout.addView(textView);
                    }*/





                }
            }

        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }





    private class myWebClient extends WebViewClient {
    }
}
