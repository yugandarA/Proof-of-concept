package com.learn.goa.slider;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener {
    private AQuery aq;
    private SliderLayout Slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aq = new AQuery(getApplicationContext());
        ActionSlide();
    }

    private void ActionSlide(){
        String url = "http://192.168.0.17/api/data.php";
        ProgressDialog progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Loading...");
        progress.setCancelable(false);

        //Image Slider
        Slider = (SliderLayout) findViewById(R.id.slider);
        Slider.setPresetTransformer(SliderLayout.Transformer.Default);
        Slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        Slider.setCustomAnimation(new DescriptionAnimation());
        Slider.setDuration(4000);
        Slider.addOnPageChangeListener(this);

        aq.progress(progress).ajax(url, String.class, new AjaxCallback<String>() {

            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object != null) {
                    try {
                        JSONObject json = new JSONObject(object);
                        JSONArray b = json.getJSONArray("imagedata");
                        ArrayList<HashMap<String, String>> slideList = new ArrayList<>();
                        for (int i = 0; i < b.length(); i++) {
                            JSONObject a = b.getJSONObject(i);
                            String slide = a.getString("imageurl");

                            HashMap<String, String> ss = new HashMap<>();
                            // adding each child node to HashMap key => value
                            ss.put("", slide);
                            // menambahkan data ke list
                            slideList.add(ss);

                            for (String name : ss.keySet()) {

                                DefaultSliderView sliderView = new DefaultSliderView((MainActivity.this));
                                sliderView.image(ss.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit);

//                                Slider.addSlider(textSliderView);
                                Slider.addSlider(sliderView);
                            }
                        }
                        Log.e("IMG", object);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Load Image Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

