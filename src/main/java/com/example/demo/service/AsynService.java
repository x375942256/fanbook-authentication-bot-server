package com.example.demo.service;

import com.example.demo.entity.TWord;

import java.io.IOException;

public interface AsynService {
    void export(TWord tWord) throws InterruptedException, IOException;
    void update(Long guild);
}
