package com.yuxliu.androidutils;

import com.google.gson.Gson;
import com.yuxliu.androidutils.bean.TestResponse;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_gson_static() {
        String target = "{\"code\":0,\"message\":\"Success!\",\"timer\":{\"\":\"\"}}";
        Gson gson = new Gson();
        TestResponse response = gson.fromJson(target, TestResponse.class);
        System.out.println(response.toString());
    }
}