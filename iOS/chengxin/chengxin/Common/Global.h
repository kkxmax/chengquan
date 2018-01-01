//
//  Global.h
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#ifndef Global_h
#define Global_h

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "GeneralUtil.h"
#import "UIImageView+AFNetworking.h"
#import "KeyboardManager.h"
#import "CommonData.h"
#import "WebAPI.h"
#import "UIView+Toast.h"

#define appDelegate ((AppDelegate *)[[UIApplication sharedApplication]delegate])
#define APP_NAME                @"ChengXin"

// SUB HOME SCREEN
#define SUB_HOME_PERSONAL   0
#define SUB_HOME_ENTERPRISE     1
#define SUB_HOME_COMMERCE   2
#define SUB_HOME_ITEM       3
#define SUB_HOME_SERVICE    4

// CHOICE
#define CHOICE_HOME_PERSONAL 0
#define CHOICE_HOME_COMMERCE 1
#define CHOICE_EVAL 2
#define CHOICE_HOME_ENTERPRISE 3
#define CHOICE_ADD_COMMERCE 4

// AKIND
#define ALL_KIND            0
#define PERSONAL_KIND       1
#define ENTERPRISE_KIND     2

// realname cert relation
#define ENTER_TYPE_ENTERPRISE   1
#define ENTER_TYPE_PERSONAL     2

// CATEGORY_TYPE
#define PRODUCT_CATEGORY    0
#define ITEM_CATEGORY       1
#define SERVICE_CATEGORY    2

// ITEM & SERVICE PAGE
#define ITEM_PAGE           0
#define SERVICE_PAGE        1

// EVALUATE
#define FRONT_EVALUATE      1
#define BACK_EVALUATE       2

// realname cert relation
#define TEST_STATUS_READY   1
#define TEST_STATUS_PASSED  2
#define TEST_STATUS_REJECT  3

// WEB API
//#define BASE_URL                                @"http://10.70.3.61:8080/BFIP/api.html?"
//#define BASE_WEB_URL                            @"http://10.70.3.61:8080/BFIP"
//#define BASE_URL                                @"http://180.76.183.131/ChengxinTest/api.html?"
//#define BASE_WEB_URL                            @"http://180.76.183.131/ChengxinTest"
//#define BASE_URL                                @"http://cqegz.com/BFIP/api.html?"
//#define BASE_WEB_URL                            @"http://cqegz.com/BFIP"
#define BASE_URL                                @"http://cqegz.com/ChengxinTest/api.html?"
#define BASE_WEB_URL                            @"http://cqegz.com/ChengxinTest"
#define ACTION_LOGIN                            @"login"
#define ACTION_GETCAROUSELLIST                  @"getCarouselList"
#define ACTION_GETHOTLIST                       @"getHotList"
#define ACTION_GETFRIENDLIST                    @"getFriendList"
#define ACTION_SETINTEREST                      @"setInterest"
#define ACTION_GETACCOUNTLISTFORESTIMATE        @"getAccountListForEstimate"
#define ACTION_GETACCOUNTDETAIL                 @"getAccountDetail"
#define ACTION_GETESTIMATELISTFORHOT            @"getEstimateListForHot"
#define ACTION_GETPARTNERLIST                   @"getPartnerList"
#define ACTION_GETXYLEIXINGLIST                 @"getXyleixingList"
#define ACTION_GETFENLEILIST                    @"getFenleiList"
#define ACTION_GETPRODUCTLIST                   @"getProductList"
#define ACTION_GETITEMLIST                      @"getItemList"
#define ACTION_GETSERVICELIST                   @"getServiceList"
#define ACTION_GETENTERLIST                     @"getEnterList"
#define ACTION_GETPRODUCTDETAIL                 @"getProductDetail"
#define ACTION_GETCITYLIST                      @"getCityList"
#define ACTION_RESETPASSWORD                    @"resetPassword"
#define ACTION_LEAVEESTIMATE                    @"leaveEstimate"
#define ACTION_ADDITEM                          @"addItem"
#define ACTION_ADDSERVICE                       @"addService"
#define ACTION_ADDPRODUCT                       @"addProduct"
#define ACTION_GETMYINTERESTLIST                @"getMyInterestList"
#define ACTION_GETNOTICECOUNT                   @"getNoticeCount"
#define ACTION_GETNOTICELIST                    @"getSystemNoticeList"
#define ACTION_GETMYEVALUATELIST                @"getMyEstimateNoticeList"
#define ACTION_GETOTHEREVALUATELIST             @"getEstimateToMeNoticeList"
#define ACTION_DELETEPRODUCT                    @"deleteProduct"
#define ACTION_DELETEITEM                       @"deleteItem"
#define ACTION_DELETESERVICE                    @"deleteService"
#define ACTION_UPPRODUCT                        @"upProduct"
#define ACTION_DOWNPRODUCT                      @"downProduct"
#define ACTION_UPITEM                           @"upItem"
#define ACTION_DOWNITEM                         @"downItem"
#define ACTION_UPSERVICE                        @"upService"
#define ACTION_DOWNSERVICE                      @"downService"
#define ACTION_GETINVITERINFO                   @"getInviterInfo"
#define ACTION_MAKECORRECT                      @"makeCorrect"
#define ACTION_ONPURCHASE                       @"onPurchase"
#define ACTION_SETFAVOURITE                     @"setFavourite"
#define ACTION_AUTHPERSONAL                     @"authPersonal"
#define ACTION_AUTHENTER                        @"authEnter"
#define ACTION_UPDATE_EVALUATE_ME               @"elect"
#define ACTION_LEAVEOPINION                     @"leaveOpinion"
#define ACTION_GETMYFAVOURITELIST               @"getMyFavouriteList"
#define ACTION_GETCOMPANYLIST                   @"getCompanyList"
#define ACTION_ONREADESTIMATE                   @"onReadEstimate"
#define ACTION_GETPASSEDPERSONALLIST            @"getPassedPersonalList"
#define ACTION_GETPASSEDENTERLIST               @"getPassedEnterList"
#define ACTION_LEAVEREPLY                       @"leaveReply"
#define ACTION_INCVIEWCOUNT                     @"incViewCount"
#define ACTION_GETESTIMATE                      @"getEstimate"
#define ACTION_GETERROR                         @"getError"
#define ACTION_GETERRORLIST                     @"getErrorList"
#define ACTION_INVITEFRIEND                     @"inviteFriend"

//Share Statics
#define ACTION_ONSHARE                     @"onShare"

// NOTIFICATION
#define NOTIFICATION_SECOND                     10

// GET DATA COUNT
#define REFRESH_GET_DATA_COUNT                  10

// SET NAME LENGTH TO MAX
#define HOME_NAME_MAX_LENGTH                    7
#define HOME_TAG_MAX_LENGTH                     5
#define EVALUATE_DETAIL_TITLE_MAX_LENGTH        15

// RESPONSE CODE
#define RESPONSE_SUCCESS        200
#define LOGIN_PASSSWORD_WRONG   203

// ALERT DIALOG
#define BTN_OK_TITLE            @"OK"
#define BTN_CANCEL_TITLE        @"Cancel"
#define BTN_YES_TITLE           @"Yes"
#define BTN_NO_TITLE            @"No"

// SCREEN SIZE
#define SCREEN_WIDTH ([[UIScreen mainScreen] bounds].size.width)
#define SCREEN_HEIGHT ([[UIScreen mainScreen] bounds].size.height)
#define SCREEN_MAX_LENGTH (MAX(SCREEN_WIDTH, SCREEN_HEIGHT))
#define SCREEN_MIN_LENGTH (MIN(SCREEN_WIDTH, SCREEN_HEIGHT))
#define NAVIGATION_HEIGHT 64

// DEVICE INFO
#define IS_IPAD (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)
#define IS_IPHONE (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
#define IS_RETINA ([[UIScreen mainScreen] scale] >= 2.0)
#define IS_IPHONE_4_OR_LESS (IS_IPHONE && SCREEN_MAX_LENGTH < 568.0)
#define IS_IPHONE_5 (IS_IPHONE && SCREEN_MAX_LENGTH == 568.0)
#define IS_IPHONE_5_OR_LESS (IS_IPHONE && SCREEN_MAX_LENGTH < 667.0)

#define IS_IPHONE_6 (IS_IPHONE && SCREEN_MAX_LENGTH == 667.0)
#define IS_IPHONE_6P (IS_IPHONE && SCREEN_MAX_LENGTH == 736.0)

// COLOR
#define WHITE_COLOR [UIColor whiteColor]
#define CYNA_COLOR [UIColor colorWithRed:(CGFloat)26/255 green:(CGFloat)148/255 blue:(CGFloat)229/255 alpha:1.0]

#define ORANGE_COLOR   [UIColor colorWithRed:255/255.0f green:138/255.0f blue:0/255.0f alpha:1.0]
#define BLACK_COLOR_51   [UIColor colorWithRed:51/255.0f green:51/255.0f blue:51/255.0f alpha:1.0]
#define BLACK_COLOR_85   [UIColor colorWithRed:85/255.0f green:85/255.0f blue:85/255.0f alpha:1.0]
#define BLACK_COLOR_102   [UIColor colorWithRed:102/255.0f green:102/255.0f blue:102/255.0f alpha:1.0]
#define BLACK_COLOR_153   [UIColor colorWithRed:153/255.0f green:153/255.0f blue:153/255.0f alpha:1.0]
#define BLACK_COLOR_229   [UIColor colorWithRed:229/255.0f green:229/255.0f blue:229/255.0f alpha:1.0]
#define BLACK_COLOR_245   [UIColor colorWithRed:245/255.0f green:245/255.0f blue:245/255.0f alpha:1.0]
#define RGB_COLOR(r, g, b) [UIColor colorWithRed:r/255.0f green:g/255.0f blue:b/255.0f alpha:1.0]
#define RGBA_COLOR(r, g, b, a) [UIColor colorWithRed:r/255.0f green:g/255.0f blue:b/255.0f alpha:a/255.0f]
#define APP_BACKGROUD_COLOR [UIColor colorWithRed:(CGFloat)0/255 green:(CGFloat)64/255 blue:(CGFloat)98/255 alpha:1.0]

// Home
#define NUMBER_OF_SLIDE_IMAGES          4
#define HOME_IMAGE_START            1090
#define FAMILIAR_IMAGE_START            1090
#define ENTERPRISE_IMAGE_START          1095
#define COMMERCE_IMAGE_START            1100
#define ITEM_IMAGE_START                1095
#define SERVICE_IMAGE_START             1100
#define SLIDE_SECOND                    3

// Round Button
#define ROUNDBUTTONRADIUS                   4.f
#define SELECTED_BUTTON_BACKGROUND_COLOR    RGB_COLOR(209, 233, 250)
#define SELECTED_BUTTON_TITLE_COLOR         RGB_COLOR(23, 133, 229)

// City Array
#define CityArray ([[NSArray alloc] initWithObjects:@"北京",@"上海",@"广州",@"深圳", nil])

// Table Section
#define REUSE 0
#define AlphabetsArray ([[NSArray alloc] initWithObjects:@"A",@"B",@"C",@"D",@"E",@"F",@"G",@"H",@"I",@"J",@"K",@"L",@"M",@"N",@"O",@"P",@"Q",@"R",@"S",@"T",@"U",@"V",@"W",@"X",@"Y",@"Z", nil])

// FONT
#define FONT_12     [UIFont fontWithName:@"Helvetica Neue" size:12.0f]
#define FONT_14     [UIFont fontWithName:@"Helvetica Neue" size:14.0f]
#define FONT_16     [UIFont fontWithName:@"Helvetica Neue" size:16.0f]
#define FONT_18     IS_IPAD ? [UIFont fontWithName:@"Helvetica Neue" size:26] : [UIFont fontWithName:@"Helvetica Neue" size:18]

// Notification Name
#define SHOW_TRANS_TAB_NOTIFICATION                 @"ShowTransparencyTabNotification"
#define HIDE_TRANS_TAB_NOTIFICATION                 @"HideTransparencyTabNotification"
#define SHOW_HOMECHOICE_VIEW_NOTIFICATION               @"ShowHomeChoiceViewNotification"
#define HIDE_HOMECHOICE_VIEW_NOTIFICATION               @"HideHomeChoiceViewNotification"
#define HIDE_REALNAME_CHOICE_VIEW_NOTIFICATION          @"HideRealNameChoiceViewNotification"
#define SET_HOMECHOICE_VIEW_NOTIFICATION                @"SetHomeChoiceViewNotification"
#define SET_EVALCHOICE_VIEW_NOTIFICATION                @"SetEvalChoiceViewNotification"
#define RESET_EVALCHOICE_VIEW_NOTIFICATION              @"ResetEvalChoiceViewNotification"
#define SHOW_HOMEITEMDETAIL_VIEW_NOTIFICATION           @"ShowHomeItemDetailViewNotification"
#define SHOW_HOMESERVICEDETAIL_VIEW_NOTIFICATION        @"ShowHomeServiceDetailViewNotification"
#define SHOW_HOMEITEMADD_VIEW_NOTIFICATION              @"ShowHomeItemAddViewNotification"
#define SHOW_HOMEFAMILIARDETAIL_VIEW_NOTIFICATION       @"ShowHomeFamiliarDetailViewNotification"
#define SHOW_HOMEENTERPRISEDETAIL_VIEW_NOTIFICATION     @"ShowHomeEnterpriseDetailViewNotification"
#define SHOW_HOMECOMMERCEDETAIL_VIEW_NOTIFICATION       @"ShowHomeCommerceDetailViewNotification"
#define SHOW_CATEGORYSEARCH_VIEW_NOTIFICATION           @"ShowCategorySearchViewNotification"
#define SHOW_PROFILE_VIEW_NOTIFICATION                  @"ShowProfileViewNotification"
#define SHOW_EVALUTEDETAIL_VIEW_NOTIFICATION            @"ShowEvaluateDetailViewNotification"
#define SHOW_MORE_REPLY_VIEW_NOTIFICATION               @"ShowMoreReplyViewNotification"
#define SHOW_HOMEPRODUCTADD_VIEW_NOTIFICATION           @"ShowHomeProductAddViewNotification"
#define HIDE_SORT_VIEW_NOTIFICATION                     @"HideSortViewNotification"
#define SHOW_RESULT_SEARCH_VIEW_NOTIFICATION            @"ShowResultSearchViewNotification"
#define SHOW_NOTIFICATION_VIEW_NOTIFICATION             @"ShowNotificationViewNotification"
#define SHOW_ALLFRONTBACK_VIEW_NOTIFICATION             @"ShowAllFrontBackViewNotification"
#define UPDATE_EVALUATE_VIEW_NOTIFICATION               @"UpdateEvaluateViewNotification"
#define UPDATE_EVALUATE_DETAIL_VIEW_NOTIFICATION        @"UpdateEvaluateDetailViewNotification"
#define SHOW_FIXED_ERROR_VIEW_NOTIFICATION              @"ShowFixedErrorViewNotification"
#define UPDATE_FAMILIAR_INTERESTING_NOTIFICATION        @"UpdateFamiliarInterestingNotification"
#define UPDATE_ENTERPRISE_INTERESTING_NOTIFICATION      @"UpdateEnterpriseInterestingNotification"
#define RELOAD_FAMILIAR_DATA_NOTIFICATION               @"ReloadFamiliarDataNotification"
#define RELOAD_ENTERPRISE_DATA_NOTIFICATION             @"ReloadEnterpriseDataNotification"
#define RELOAD_COMMERCE_DATA_NOTIFICATION               @"ReloadCommerceDataNotification"
#define RELOAD_ITEM_DATA_NOTIFICATION                   @"ReloadItemDataNotification"
#define RELOAD_SERVICE_DATA_NOTIFICATION                @"ReloadServiceDataNotification"
#define SHOW_HISTORYSEARCH_VIEW_NOTIFICATION            @"ShowHistorySearchViewNotification"
#define SHOW_EVALUATE_FIXED_ERROR_VIEW_NOTIFICATION     @"ShowEvaluateFixedErrorViewNotification"
#define SHOW_HOT_MORE_REPLY_VIEW_NOTIFICATION           @"ShowHotMoreReplyViewNotification"
#define UPDATE_HOT_INTERESTING_NOTIFICATION             @"UpdateHotInterestingNotification"
#define SHOW_REALNAMEAUTH_VIEW_NOTIFICATION             @"ShowRealNameAuthViewNotification"

#if DEBUG
#include <libgen.h>
#define ZDebug(fmt, args...)  NSLog(@"[%s:%d] %@\n", basename(__FILE__), __LINE__, [NSString stringWithFormat:fmt, ##args])
#else
#define ZDebug(fmt, args...)  ((void)0)
#endif

#endif /* Global_h */
