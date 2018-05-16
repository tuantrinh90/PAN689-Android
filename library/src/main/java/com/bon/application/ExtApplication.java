package com.bon.application;

import android.support.multidex.MultiDexApplication;

import com.bon.cloud_message.GcmNotification;
import com.bon.image.ImageLoaderUtils;
import com.bon.logger.Logger;
import com.bon.share_preferences.AppPreferences;
import com.bon.util.ExtUtils;
import com.bon.util.FileUtils;
import com.bon.util.LeakCanaryUtils;

import java.io.File;

/**
 * Created by Administrator on 24/01/2017.
 */

public class ExtApplication extends MultiDexApplication {
    protected static File pathProject;
    protected static ExtApplication extApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // ext utils
            ExtUtils.init(this);

            // init instance of app preference
            AppPreferences.getInstance(this);

            // init gcm notification
            GcmNotification.getInstance(this);

            // init leak canary
            LeakCanaryUtils.init(this);

            // set instance application
            ExtApplication.extApplication = this;

            // setup path project
            ExtApplication.getPathProject();

            // init image loader
            ImageLoaderUtils.initImageLoader(this, 50, 100);

            // setup Logger
            Logger.setEnableLog(true);

            if (ExtApplication.getPathProject() != null) {
                Logger.setPathSaveLog(ExtApplication.getPathProject().getAbsolutePath(), getPackageName(), "log");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get instance of base application
     *
     * @return
     */
    public static ExtApplication getInstance() {
        return ExtApplication.extApplication;
    }

    /**
     * get path root project
     *
     * @return
     */
    public static File getPathProject() {
        if (ExtApplication.pathProject == null) {
            ExtApplication.pathProject = FileUtils.getRootFolderPath(ExtApplication.getInstance(),
                    ExtApplication.getInstance().getPackageName());
        }

        return ExtApplication.pathProject;
    }
}
