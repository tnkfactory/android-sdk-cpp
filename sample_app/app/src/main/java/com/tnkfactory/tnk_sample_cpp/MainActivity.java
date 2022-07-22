package com.tnkfactory.tnk_sample_cpp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.tnkfactory.ad.TemplateLayoutUtils;
import com.tnkfactory.ad.TnkSession;
import com.tnkfactory.ad.cpp.CashpoppopPlus;
import com.tnkfactory.ad.cpp.CppAdViewListener;
import com.tnkfactory.ad.cpp.CppConfig;
import com.tnkfactory.ad.cpp.CppMainViewId;
import com.tnkfactory.tnk_sample_cpp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 유저 고유 식별값 설정
        TnkSession.setUserName(MainActivity.this, "user_uuid");

        // Analytics Report 사용을 위한 앱 시작 로깅
        TnkSession.applicationStarted(this);

        // COPPA 설정 (true - ON / false - OFF)
        TnkSession.setCOPPA(MainActivity.this, false);

        CppConfig config = new CppConfig.Builder()
                .tnkLayout(TemplateLayoutUtils.getBlueTabStyle_01())
                .listener(adViewListener)
                .visiblePopupBtn(true)
                .build();


        binding.btnShowCpp.setOnClickListener(v -> {
            CashpoppopPlus cpp = new CashpoppopPlus(MainActivity.this, config);
            cpp.showAdView(this);
        });

    }

    CppAdViewListener adViewListener = new CppAdViewListener() {
        @Override
        public void onClickMenu(CppMainViewId id) {
            super.onClickMenu(id);
            switch (id) {
                case PPI:
                    Log.d(TAG, "onClick 보상형");
                    break;
                case CPS:
                    Log.d(TAG, "onClick 구매형");
                    break;
            }
        }

        @Override
        public void onDismiss() {
            super.onDismiss();
        }
    };
}