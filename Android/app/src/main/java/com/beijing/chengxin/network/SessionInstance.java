package com.beijing.chengxin.network;

import android.content.Context;
import android.util.Log;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.debug.Logger;
import com.beijing.chengxin.network.model.LoginModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Store Session data
 *
 * Created by star on 11/2/2017.
 */
public class SessionInstance implements Serializable {

    private static final long serialVersionUID = -1509339033761436903L;
    public static final String TAG = SessionInstance.class.toString();
    private static SessionInstance INSTANCE;
    
    LoginModel loginData;
    /**
     * Constructor
     *
     * @author kkj
     */
    public SessionInstance() {

    }

    /**
     * return SessionInstance
     *
     * @author ssk
     */
    public static synchronized SessionInstance getInstance() {
        if (INSTANCE == null) {

            try {
                FileInputStream fis = ChengxinApplication.getContext().openFileInput(TAG);
                ObjectInputStream ois = new ObjectInputStream(fis);
                INSTANCE = (SessionInstance) ois.readObject();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }

        return INSTANCE;
    }

    /**
     * clear Session Instance for sign out.
     *
     * @author ssk
     */
    public static void clearInstance() {
        ChengxinApplication.getContext().deleteFile(TAG);
        INSTANCE = null;
    }

    /**
     * initialize Session with login data
     *
     * param data DataLogin
     * @author ssk
     */
    public static void initialize(Context context, LoginModel response) {
        INSTANCE = new SessionInstance();
        INSTANCE.setLoginData(response);
        INSTANCE.serialize(context);
    }

    /**
     * Serialize Session Data
     */
    public void serialize(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(TAG, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.toString());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
        Logger.i(TAG, "Session has been serialized");

    }

	public LoginModel getLoginData() {
		return loginData;
	}
	public void setLoginData(LoginModel loginData) {
		this.loginData = loginData;
	}
}
