package com.wearezeta.auto.android.common;

import com.wearezeta.auto.common.image_send.ImageGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

public class AndroidCommonUtilsTest {
    @Test
    public void addTestImageToGallery() throws Exception {
//        AndroidCommonUtils.addTestImageToGallery("text2");
        ImageGenerator.getTestPictureFile("test 123");
    }

}