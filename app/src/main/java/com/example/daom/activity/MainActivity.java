package com.example.daom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.daom.R;
import com.example.daom.fragment.InformationFragment;
import com.example.daom.fragment.InvitationFragment;
import com.example.daom.fragment.JoinFragment;
import com.example.daom.fragment.LobbyFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    // Bottom Navigation
    private NavigationBarView mBottomNav;

    // Tag
    private String TAG = "hello";

    // Fragment Declaration
    private Fragment lobbyFrag;
    private Fragment invitationFrag;
    private Fragment joinFrag;
    private Fragment informationFrag;
    private int menuNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fragment 선언
        lobbyFrag = new LobbyFragment();
        invitationFrag = new InvitationFragment();
        joinFrag = new JoinFragment();
        informationFrag = new InformationFragment();

        // 네비게이션 바
        // setOnNavigationItemSelectedListener -> setOnNavigationItemSelected
        mBottomNav = findViewById(R.id.bottom_nav);


        // 네비게이션 바 초기 셋팅
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, lobbyFrag).commitAllowingStateLoss();
        mBottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    // 각 Fragment가 들어감
                    case R.id.item_lobby:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, lobbyFrag).commitAllowingStateLoss();
                        menuNum = 1;
                        break;
                    case R.id.item_invite:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, invitationFrag).commitAllowingStateLoss();
                        menuNum = 2;
                        break;
                    case R.id.item_join:
                        if (menuNum != 3) {
                            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_main, joinFrag).commitAllowingStateLoss();
                            menuNum = 3;
                            break;
                        } else {
                            menuNum = 3;
                            break;
                        }
                    case R.id.item_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, informationFrag).commitAllowingStateLoss();
                        menuNum = 4;
                        break;
                }
                return true;
            }
        });
    }
}