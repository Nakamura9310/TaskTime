package com.example.demo.service;

import com.example.demo.repository.UserMapper;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    //UserMapperをコンストラクタインジェクション
    private final UserMapper mapper;

    //top.html表示時にuserIDとsortを登録する処理
    //IGNOREにより、初回のみ実行する
    public void insertOneUser(String userID) {
        mapper.insertOneUser(userID);
    }

    //ログイン中のユーザーのsort情報を取得
    public String selectSort(String userID) {
        return mapper.selectSort(userID);
    }

    //ログイン中のユーザーのsortをdateかpriorityに更新
    public void updateUserSort(String sort, String userID) {
        mapper.updateUserSort(sort, userID);
    }




    
}
