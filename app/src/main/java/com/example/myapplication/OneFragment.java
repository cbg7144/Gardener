package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.databinding.FragmentOneBinding;

public class OneFragment extends Fragment {
    // fragment_one.xml 파일과 연결되어 있는 바인딩 클래스 참조 변수
    FragmentOneBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOneBinding.inflate(inflater, container, false);
        return binding.getRoot(); // fragment가 보여줄 view로서 바인딩클래스 객체의 최상위 뷰
    }

    // fragment가 보여준 뷰들이 파괴돼도 프래그먼트 객체가 살아있을 수 있기에 뷰들이 메모리에서 없어질 때 바인딩 클래스의 인스턴스도 파괴
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
}