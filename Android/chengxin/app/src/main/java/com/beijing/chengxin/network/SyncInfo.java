package com.beijing.chengxin.network;

import android.content.Context;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.debug.Logger;
import com.beijing.chengxin.network.model.AccountModel;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.CarouselListModel;
import com.beijing.chengxin.network.model.ComedityDetailModel;
import com.beijing.chengxin.network.model.ComedityListModel;
import com.beijing.chengxin.network.model.ErrorDetailModel;
import com.beijing.chengxin.network.model.ErrorListModel;
import com.beijing.chengxin.network.model.EvalDetailModel;
import com.beijing.chengxin.network.model.EvalListModel;
import com.beijing.chengxin.network.model.FavouriteListModel;
import com.beijing.chengxin.network.model.FenleiListModel;
import com.beijing.chengxin.network.model.HotDetailModel;
import com.beijing.chengxin.network.model.HotListModel;
import com.beijing.chengxin.network.model.InviterModel;
import com.beijing.chengxin.network.model.ItemListModel;
import com.beijing.chengxin.network.model.LoginModel;
import com.beijing.chengxin.network.model.MarkLogListModel;
import com.beijing.chengxin.network.model.MyEvalNoticeModel;
import com.beijing.chengxin.network.model.MyInterestModel;
import com.beijing.chengxin.network.model.NoticeCountModel;
import com.beijing.chengxin.network.model.ProvinceListModel;
import com.beijing.chengxin.network.model.ReqCodeModel;
import com.beijing.chengxin.network.model.ServeListModel;
import com.beijing.chengxin.network.model.SystemNoticeListModel;
import com.beijing.chengxin.network.model.UserDetailModel;
import com.beijing.chengxin.network.model.UserListModel;
import com.beijing.chengxin.network.model.VerifyCodeModel;
import com.beijing.chengxin.network.model.XyleixingListModel;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by star on 11/2/2017.
 */
public class SyncInfo {

    public static final String TAG = SyncInfo.class.getName();
    Context mContext;

    public SyncInfo(Context mContext) {
        this.mContext = mContext;
    }

    public LoginModel syncLogin(String mobile, String pw){
        LoginModel result = new LoginModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "login");
        params.put("mobile", mobile);
        params.put("password", pw);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {

            Gson gson = new Gson();
            try {
                LoginModel response = gson.fromJson(responseString, LoginModel.class);
                if (response.getRetCode() == Constants.ERROR_OK) {
                    Logger.i(TAG, "login status:" + mobile);
                }
                result = response;
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    public VerifyCodeModel syncVerifycode(String mobile){
        VerifyCodeModel result = new VerifyCodeModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getVerifyCode");
        params.put("mobile", mobile);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {

            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, VerifyCodeModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncForgotPassword(String mobile, String verifyCode, String pw){
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "forgotPassword");
        params.put("mobile", mobile);
        params.put("verifyCode", verifyCode);
        params.put("password", pw);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {

            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }


    public LoginModel syncRegister(String mobile, String pw, String reqCode, String verifyCode){
        LoginModel result = new LoginModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "register");
        params.put("mobile", mobile);
        params.put("reqCode", reqCode);
        params.put("verifyCode", verifyCode);
        params.put("password", pw);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, LoginModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public CarouselListModel syncCarousel(){
        CarouselListModel result = new CarouselListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getCarouselList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, CarouselListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public UserListModel syncFriendList(String start, String length, String order, String cityName, String akind, String xyleixingIds, String keyword, String keywordCode){
        UserListModel result = new UserListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getFriendList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("start", start);
        if (Integer.valueOf(length) > 0)
            params.put("length", length);
        params.put("order", String.valueOf(order));
        if (!cityName.equals(""))
            params.put("cityName", cityName);
        if (Integer.valueOf(akind) > 0)
            params.put("akind", akind);
        if (!xyleixingIds.equals(""))
            params.put("xyleixingIds", xyleixingIds);
        if (!keyword.equals(""))
            params.put("keyword", keyword);
        if (!keywordCode.equals(""))
            params.put("keywordCode", keywordCode);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, UserListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public UserListModel syncEnterpriseList(String start, String length, String order, String cityName, String enterKind, String xyleixingIds, String keyword){
        UserListModel result = new UserListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getEnterList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("start", start);
        if (Integer.valueOf(length) > 0)
            params.put("length", length);
        params.put("order", String.valueOf(order));
        if (!cityName.equals(""))
            params.put("cityName", cityName);
        if (Integer.valueOf(enterKind) > 0)
            params.put("enterKind", enterKind);
        if (!xyleixingIds.equals(""))
            params.put("xyleixingIds", xyleixingIds);
        if (!keyword.equals(""))
            params.put("keyword", keyword);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, UserListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public ComedityListModel syncComedityList(String start, String length, String order, String cityName, String pleixingIds, String keyword){
        ComedityListModel result = new ComedityListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getProductList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("start", start);
        if (Integer.valueOf(length) > 0)
            params.put("length", length);
        params.put("order", String.valueOf(order));
        if (!cityName.equals(""))
            params.put("cityName", cityName);
        if (!pleixingIds.equals(""))
            params.put("pleixingIds", pleixingIds);
        if (!keyword.equals(""))
            params.put("keyword", keyword);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, ComedityListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public ItemListModel syncItemList(String start, String length, String order, String cityName, String akind, String fenleiIds, String keyword){
        ItemListModel result = new ItemListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getItemList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("start", start);
        if (Integer.valueOf(length) > 0)
            params.put("length", length);
        params.put("order", String.valueOf(order));
        if (!cityName.equals(""))
            params.put("cityName", cityName);
        if (Integer.valueOf(akind) > 0)
            params.put("akind", akind);
        if (!fenleiIds.equals(""))
            params.put("fenleiIds", fenleiIds);
        if (!keyword.equals(""))
            params.put("keyword", keyword);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, ItemListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public ServeListModel syncServeList(String start, String length, String order, String cityName, String akind, String fenleiIds, String keyword){
        ServeListModel result = new ServeListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getServiceList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("start", start);
        if (Integer.valueOf(length) > 0)
            params.put("length", length);
        params.put("order", String.valueOf(order));
        if (!cityName.equals(""))
            params.put("cityName", cityName);
        if (Integer.valueOf(akind) > 0)
            params.put("akind", akind);
        if (!fenleiIds.equals(""))
            params.put("fenleiIds", fenleiIds);
        if (!keyword.equals(""))
            params.put("keyword", keyword);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, ServeListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public HotListModel syncHotList(int start, int length){
        HotListModel result = new HotListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getHotList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("start", "" + start);
        if (length > 0)
            params.put("length", "" + length);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, HotListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public HotDetailModel syncHotDetail(String hotId){
        HotDetailModel result = new HotDetailModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getHotDetail");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("hotId", hotId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, HotDetailModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public UserListModel syncAccountListForEstimate(String start, String length, String akind, String xyleixingIds, String keyword){
        UserListModel result = new UserListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getAccountListForEstimate");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("start", start);
        if (Integer.valueOf(length) > 0)
            params.put("length", length);
        if (Integer.valueOf(akind) > 0)
            params.put("akind", akind);
        if (!xyleixingIds.equals(""))
            params.put("xyleixingIds", xyleixingIds);
        if (!keyword.equals(""))
            params.put("keyword", keyword);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, UserListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public UserListModel syncPartnerList(String start, String length, String hotId){
        UserListModel result = new UserListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getPartnerList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("start", start);
        if (Integer.valueOf(length) > 0)
            params.put("length", length);
        params.put("hotId", hotId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, UserListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public EvalListModel syncEstimateListForHot(String start, String length, String hotId){
        EvalListModel result = new EvalListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getEstimateListForHot");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("start", start);
        if (Integer.valueOf(length) > 0)
            params.put("length", length);
        params.put("hotId", hotId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, EvalListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public UserListModel syncPassedPersonalList(){
        UserListModel result = new UserListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getPassedPersonalList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, UserListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public UserListModel syncPassedEnterList(){
        UserListModel result = new UserListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getPassedEnterList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, UserListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncIncreaseViewCount(String kind, String accountId, String hotId){
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "incViewCount");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("kind", kind);
        params.put("accountId", accountId);
        params.put("hotId", hotId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncSetInterest(String accountId, String val){
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "setInterest");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("accountId", accountId);
        params.put("val", val);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncSetFavourite(String id, String val, String kind){
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "setFavourite");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("id", id);
        params.put("val", val);
        params.put("kind", kind);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncElectHot(String hotId, String val){
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "electHot");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("hotId", hotId);
        params.put("val", val);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncOnPurchase(String productId) {
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "onPurchase");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("productId", productId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncOnContact(String accountId) {
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "onContact");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("accountId", accountId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncLeaveReply(String estimateId, String content) {
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "leaveReply");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("estimateId", estimateId);
        params.put("content", content);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public UserDetailModel syncAccountDetail(String accountId){
        UserDetailModel result = new UserDetailModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getAccountDetail");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("accountId", accountId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, UserDetailModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public ComedityDetailModel syncComedityDetail(String productId){
        ComedityDetailModel result = new ComedityDetailModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getProductDetail");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("productId", productId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, ComedityDetailModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public ProvinceListModel syncCityList(){
        ProvinceListModel result = new ProvinceListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getCityList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, ProvinceListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public XyleixingListModel syncXyleixingList(String xyleixingId){
        XyleixingListModel result = new XyleixingListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getXyleixingList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        if (!xyleixingId.equals(""))
            params.put("xyleixingId", xyleixingId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, XyleixingListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public XyleixingListModel syncPleixingList(String pleixingId){
        XyleixingListModel result = new XyleixingListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getPleixingList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        if (!pleixingId.equals(""))
            params.put("pleixingId", pleixingId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, XyleixingListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public FenleiListModel syncItemFenleiList(){
        FenleiListModel result = new FenleiListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getItemFenleiList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, FenleiListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public FenleiListModel syncServeFenleiList(){
        FenleiListModel result = new FenleiListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getServiceFenleiList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, FenleiListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncElect(String estimateId, String val){
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "elect");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("estimateId", estimateId);
        params.put("val", val);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncAddProduct(int productId, String name, String  isMain, String price, String pleixingId, String comment, String weburl, String saleAddr, List<String> productImages){
        BaseModel result = new BaseModel();

        String url = String.format("%s?%s=%s&%s=%s",Constants.API_ADDR , Constants.P_ACTION, "addProduct", Constants.P_TOKEN,  SessionInstance.getInstance().getLoginData().getToken());

        Map<String, String> params = new HashMap<String, String>();
        params.put("platform", "android");
        if (productId > 0)
            params.put("productId", String.valueOf(productId));
        params.put("name", name);
        params.put("isMain", isMain);
        params.put("price", price);
        params.put("pleixingId", pleixingId);
        params.put("comment", comment);
        params.put("weburl", weburl);
        params.put("saleAddr", saleAddr);


        ArrayList<Constants.UploadFileModel> fileModelList = new ArrayList<Constants.UploadFileModel>();
        for (int i = 0; i < productImages.size(); i ++) {
            File tmpFile = new File(ChengxinApplication.getTempFilePath(productImages.get(i)));
            if (tmpFile.exists()) {
                Constants.UploadFileModel fileModel = new Constants.UploadFileModel();
                fileModel.fileTitle = "productImages";
                fileModel.fileName = String.format("%d%s", i , Constants.FILE_EXTENTION_AUTH_IMAGE);
                fileModel.filePath = ChengxinApplication.getTempFilePath(productImages.get(i));
                fileModelList.add(fileModel);
            }
        }

        String responseString = NetworkEngine.postWithFile(url, params, fileModelList);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncAddItem(String itemId, String name, String fenleiId, String cityId, String addr, String comment, String need, String weburl, String isShow, String contactName, String contactMobile, String contactWeixin, String logo){
        BaseModel result = new BaseModel();

        String url = String.format("%s?%s=%s&%s=%s",Constants.API_ADDR , Constants.P_ACTION, "addItem", Constants.P_TOKEN,  SessionInstance.getInstance().getLoginData().getToken());

        Map<String, String> params = new HashMap<String, String>();
        params.put("platform", "android");
        if (!itemId.equals("0"))
            params.put("itemId", itemId);
        params.put("name", name);
        params.put("fenleiId", fenleiId);
        params.put("cityId", cityId);
        params.put("addr", addr);
        params.put("comment", comment);
        params.put("need", need);
        params.put("weburl", weburl);
        params.put("isShow", isShow);
        params.put("contactName", contactName);
        params.put("contactMobile", contactMobile);
        params.put("contactWeixin", contactWeixin);

        ArrayList<Constants.UploadFileModel> fileModelList = new ArrayList<Constants.UploadFileModel>();
        Constants.UploadFileModel fileModel = new Constants.UploadFileModel();
        fileModel.fileTitle = "logo";
        fileModel.fileName = "logo";
        fileModel.filePath = logo;
        fileModelList.add(fileModel);

        String responseString = NetworkEngine.postWithFile(url, params,fileModelList);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncAddServe(String serviceId, String name, String fenleiId, String addr, String comment, String weburl, String isShow, String contactName, String contactMobile, String contactWeixin, String logo){
        BaseModel result = new BaseModel();

        String url = String.format("%s?%s=%s&%s=%s",Constants.API_ADDR , Constants.P_ACTION, "addService", Constants.P_TOKEN,  SessionInstance.getInstance().getLoginData().getToken());

        Map<String, String> params = new HashMap<String, String>();
        params.put("platform", "android");
        if (!serviceId.equals("0"))
            params.put("serviceId", serviceId);
        params.put("name", name);
        params.put("fenleiId", fenleiId);
        params.put("addr", addr);
        params.put("comment", comment);
        params.put("weburl", weburl);
        params.put("isShow", isShow);
        params.put("contactName", contactName);
        params.put("contactMobile", contactMobile);
        params.put("contactWeixin", contactWeixin);


        ArrayList<Constants.UploadFileModel> fileModelList = new ArrayList<Constants.UploadFileModel>();
        Constants.UploadFileModel fileModel = new Constants.UploadFileModel();
        fileModel.fileTitle = "logo";
        fileModel.fileName = "logo";
        fileModel.filePath = logo;
        fileModelList.add(fileModel);

        String responseString = NetworkEngine.postWithFile(url, params,fileModelList);
//        String responseString = NetworkEngine.postMultipart(url, params, "logo", logo);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public BaseModel syncLeaveEstimate(int type, String accountId, String  hotId, String kind, String method, String reason, String content, List<String> productImages){
        BaseModel result = new BaseModel();

        String url = String.format("%s?%s=%s&%s=%s",Constants.API_ADDR , Constants.P_ACTION, "leaveEstimate", Constants.P_TOKEN,  SessionInstance.getInstance().getLoginData().getToken());

        Map<String, String> params = new HashMap<String, String>();
        params.put("platform", "android");
        params.put("type", String.valueOf(type));
        if (!accountId.equals(""))
            params.put("accountId", accountId);
        if (!hotId.equals(""))
            params.put("hotId", hotId);
        if (!kind.equals(""))
            params.put("kind", kind);
        if (!method.equals(""))
            params.put("method", method);
        if (!reason.equals(""))
            params.put("reason", reason);
        if (!content.equals(""))
            params.put("content", content);

        ArrayList<Constants.UploadFileModel> fileModelList = new ArrayList<Constants.UploadFileModel>();
        if (productImages != null) {
            for (int i = 0; i < productImages.size(); i ++) {
                File tmpFile = new File(ChengxinApplication.getTempFilePath(productImages.get(i)));
                if (tmpFile.exists()) {
                    Constants.UploadFileModel fileModel = new Constants.UploadFileModel();
                    fileModel.fileTitle = "productImages";
                    fileModel.fileName = String.format("%d%s", i , Constants.FILE_EXTENTION_AUTH_IMAGE);
                    fileModel.filePath = ChengxinApplication.getTempFilePath(productImages.get(i));
                    fileModelList.add(fileModel);
                }
            }
        }

        String responseString = NetworkEngine.postWithFile(url, params, fileModelList);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    // 我的评论 目录
    // akind -> 0:all 1:person 2:enterprise
    // kind -> 0:all 1:正面评价 2:负面评价
    public EvalListModel syncEvalList(int start, int length, int akind, int kind) {
        EvalListModel result = new EvalListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getMyEstimateList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("akind", "" + akind);
        params.put("kind", "" + kind);
        params.put("start", "" + start);
        if (length > 0)
            params.put("length", "" + length);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, EvalListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // 对我的评论 目录
    // akind -> 0:all 1:person 2:enterprise
    // kind -> 0:all 1:正面评价 2:负面评价
    public EvalListModel syncEvalToMeList(int start, int length, int akind, int kind) {
        EvalListModel result = new EvalListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getEstimateToMeList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("akind", "" + akind);
        params.put("kind", "" + kind);
        params.put("start", "" + start);
        if (length > 0)
            params.put("length", "" + length);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, EvalListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // 实名认证 Upload API - Enterprise
    public LoginModel syncSubmitAuthEnterprise(Map<String, String> sendParams, ArrayList<Constants.UploadFileModel> fileModelList){
        LoginModel result = new LoginModel();

        ByteArrayOutputStream a;

        sendParams.put("platform", "android");
        sendParams.put(Constants.P_ACTION, "authEnter");
        sendParams.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.postWithFile(Constants.API_ADDR + "?pAct=authEnter&token=" + SessionInstance.getInstance().getLoginData().getToken(), sendParams, fileModelList);
        //String responseString = NetworkEngine.postMultipart(Constants.API_ADDR, sendParams, fileModelList.get(0).fileTitle, fileModelList.get(0).filePath);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, LoginModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    // 实名认证 Upload API - Person
    public LoginModel syncSubmitAuthPerson(Map<String, String> sendParams, ArrayList<Constants.UploadFileModel> fileModelList){
        LoginModel result = new LoginModel();

        sendParams.put("platform", "android");
        sendParams.put(Constants.P_ACTION, "authPersonal");
        sendParams.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.postWithFile(Constants.API_ADDR + "?pAct=authPersonal&token=" + SessionInstance.getInstance().getLoginData().getToken(), sendParams, fileModelList);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, LoginModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    // 诚信报告 -> 引荐人信息
    public InviterModel syncInviter() {
        InviterModel result = new InviterModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getInviterInfo");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, InviterModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // 诚信记录
    public MarkLogListModel syncMarkLogList(int start, int length) {
        MarkLogListModel result = new MarkLogListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getMarkLogList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("start", "" + start);
        if (length > 0)
            params.put("length", "" + length);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, MarkLogListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // 纠错 目录
    // status -> 0:All 1:审核中 2:纠错成功 3:纠错失败
    public ErrorListModel syncErrorList(int start, int length, int status) {
        ErrorListModel result = new ErrorListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getErrorList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("status", "" + status);
        params.put("start", "" + start);
        if (length > 0)
            params.put("length", "" + length);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, ErrorListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // 我的发布-产品
    // keyword : 搜索关键字
    public ComedityListModel syncMyProductList(String keyword, int start, int length) {
        ComedityListModel result = new ComedityListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getMyProductList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("keyword", keyword);
        params.put("start", "" + start);
        if (length > 0)
            params.put("length", "" + length);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, ComedityListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // 我的发布-项目
    // keyword : 搜索关键字
    public ItemListModel syncMyItemList(String keyword, int start, int length) {
        ItemListModel result = new ItemListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getMyItemList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("keyword", keyword);
        params.put("start", "" + start);
        if (length > 0)
            params.put("length", "" + length);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, ItemListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // 我的发布-服务
    // keyword : 搜索关键字
    public ServeListModel syncMyServeList(String keyword, int start, int length) {
        ServeListModel result = new ServeListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getMyServiceList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("keyword", keyword);
        params.put("start", "" + start);
        if (length > 0)
            params.put("length", "" + length);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, ServeListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // 我的发布 Delete API
    // index - 1:产品 2:项目 3:服务
    public BaseModel syncDeleteMyWrite(int index, int id){
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        if (index == 1) { // 产品
            params.put(Constants.P_ACTION, "deleteProduct");
            params.put("productId", "" + id);
        } else if (index == 2) { // 项目
            params.put(Constants.P_ACTION, "deleteItem");
            params.put("itemId", "" + id);
        } else if (index == 3) { // 服务
            params.put(Constants.P_ACTION, "deleteService");
            params.put("serviceId", "" + id);
        }

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    // 我的发布 Up/Down API
    // index - 1:产品 2:项目  3:服务
    // actionIndex - 0:Down 1:Up
    public BaseModel syncUpDownMyWrite(int index, int actionIndex, int id){
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        if (actionIndex == 1) { // Up
            if (index == 1) { // 产品
                params.put(Constants.P_ACTION, "upProduct");
                params.put("productId", "" + id);
            } else if (index == 2) { // 项目
                params.put(Constants.P_ACTION, "upItem");
                params.put("itemId", "" + id);
            } else if (index == 3) { // 服务
                params.put(Constants.P_ACTION, "upService");
                params.put("serviceId", "" + id);
            }
        } else { // Down
            if (index == 1) { // 产品
                params.put(Constants.P_ACTION, "downProduct");
                params.put("productId", "" + id);
            } else if (index == 2) { // 项目
                params.put(Constants.P_ACTION, "downItem");
                params.put("itemId", "" + id);
            } else if (index == 3) { // 服务
                params.put(Constants.P_ACTION, "downService");
                params.put("serviceId", "" + id);
            }
        }

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    // 我的收藏
    // kind - 1:产品 2：热点
    public FavouriteListModel syncMyFavouriteList(int start, int length, int kind) {
        FavouriteListModel result = new FavouriteListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getMyFavouriteList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("kind", "" + kind);
        params.put("start", "" + start);
        if (length > 0)
            params.put("length", "" + length);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, FavouriteListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // 意见反馈
    // content : 意见反馈内用
    public BaseModel syncLeaveOpinion(String content) {
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "leaveOpinion");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("content", content);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    // 修改密码
    // oldPwd : 旧密码 newPwd : 新密码
    public BaseModel syncResetPassword(String oldPwd, String newPwd) {
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "resetPassword");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("oldPass", oldPwd);
        params.put("newPass", newPwd);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    // 关注 Interest Info
    public MyInterestModel syncMyInterest() {
        MyInterestModel result = new MyInterestModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getMyInterestInfo");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, MyInterestModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // 关注 Interest UserModel List
    // akind -> 1:personal 2:enterprise
    // friendLevel -> 0:我的上家 1:1度好友 2:2度好友 3:3度好友
    public UserListModel syncMyInterestList(int akind, int friendLevel) {
        UserListModel result = new UserListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getMyInterestList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("akind", "" + akind);
        if (friendLevel != -1)
            params.put("friendLevel", "" + friendLevel);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, UserListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // getCompanyList
    // keyword : search string
    public UserListModel syncCompanyList(String keyword) {
        UserListModel result = new UserListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getCompanyList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("keyword", keyword);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, UserListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    // 我要纠错
    // estimateId -> 评价ID
    // kind -> 1:夸大评价 2:虚假评价
    // reason -> 纠错原因
    // whyis -> 纠错依据
    public BaseModel syncMakeCorrect(int estimateId, int kind, String reason, String whyis, ArrayList<Constants.UploadFileModel> fileModelList) {
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put("platform", "android");
        params.put(Constants.P_ACTION, "makeCorrect");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("estimateId", "" + estimateId);
        params.put("kind", "" + kind);
        params.put("reason", reason);
        params.put("whyis", whyis);

        String responseString = NetworkEngine.postWithFile(Constants.API_ADDR + "?pAct=makeCorrect&token="+ SessionInstance.getInstance().getLoginData().getToken(), params, fileModelList);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    // 我的 信息
    // accountId : Account's ID
    public AccountModel syncAccountInfo(int accountId) {
        AccountModel result = new AccountModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getAccountInfo");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        if (accountId > 0)
            params.put("accountId", "" + accountId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, AccountModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    public NoticeCountModel syncNoticeCount() {
        NoticeCountModel result = new NoticeCountModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getNoticeCount");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, NoticeCountModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }
        return result;
    }

    public SystemNoticeListModel syncSystemNoticeList() {
        SystemNoticeListModel result = new SystemNoticeListModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getSystemNoticeList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, SystemNoticeListModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    public MyEvalNoticeModel syncMyEstimateNoticeList() {
        MyEvalNoticeModel result = new MyEvalNoticeModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getMyEstimateNoticeList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, MyEvalNoticeModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    public MyEvalNoticeModel syncEstimateToMeNoticeList() {
        MyEvalNoticeModel result = new MyEvalNoticeModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getEstimateToMeNoticeList");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, MyEvalNoticeModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    public BaseModel syncReadEstimate(String estimateId) {
        BaseModel result = new BaseModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "onReadEstimate");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("estimateId", estimateId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, BaseModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    public EvalDetailModel syncEvalDetail(String estimateId) {
        EvalDetailModel result = new EvalDetailModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getEstimate");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("estimateId", estimateId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, EvalDetailModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    public ErrorDetailModel syncErrorDetail(String errorId) {
        ErrorDetailModel result = new ErrorDetailModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "getError");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());
        params.put("errorId", errorId);

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, ErrorDetailModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

    public ReqCodeModel syncInviteFriend() {
        ReqCodeModel result = new ReqCodeModel();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.P_ACTION, "inviteFriend");
        params.put(Constants.P_TOKEN, SessionInstance.getInstance().getLoginData().getToken());

        String responseString = NetworkEngine.post(Constants.API_ADDR, params);

        if (responseString != null) {
            Gson gson = new Gson();
            try {
                result = gson.fromJson(responseString, ReqCodeModel.class);
            } catch (Exception ex) {
                Logger.ex(TAG, ex);
            }
        }

        return result;
    }

}
