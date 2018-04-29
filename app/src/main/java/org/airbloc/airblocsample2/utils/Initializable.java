package org.airbloc.airblocsample2.utils;

import android.content.Context;

import org.airbloc.airblocsample2.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 앱 시작시 초기화하기 위하여.
 */
public abstract class Initializable {
    private static List<Initializable> initializationList = new ArrayList<>();

    /**
     * 앱 시작시 초기화 메서드 ({@link Initializable#onInit})가 실행될 수 있도록 등록한다.
     * @param target Initializable
     */
    protected static void register(Initializable target) {
        Logger.e("registering");
        initializationList.add(target);
    }

    /**
     * 초기화를 시작한다. 앱 시작시 호출된다.
     * @param context {@link Context}
     */
    static void doInit(Context context) {
        for (Initializable object : initializationList) object.onInit(context);
    }

    protected abstract void onInit(Context context);
}
