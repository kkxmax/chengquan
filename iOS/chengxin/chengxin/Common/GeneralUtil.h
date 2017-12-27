//
//  GeneralUtil.h
//  Yappo
//
//  Created by Rahul on 14/04/14.
//  Copyright (c) 2014 kETAN. All rights reserved.
//

#import <Foundation/Foundation.h>

@class CustomIOS7AlertView;

@interface GeneralUtil : NSObject{
    
}
+(BOOL)isInternetConnection;
+(void)setUserPreference:(NSString *)key value:(NSString *)strValue;
+ (NSString *)getUserPreference:(NSString *)key;

+(BOOL) NSStringIsValidEmail:(NSString *)emailid;
+(void)showProgress;
+(void)hideProgress;

+(BOOL)isNotNullValue:(NSString*)strText;

+(CustomIOS7AlertView *)alertInfo:(NSString *)msg;
+(CustomIOS7AlertView *)alertInfo:(NSString *)msg withDelegate:(id)delegate;
+(CustomIOS7AlertView *)alertInfo:(NSString *)msg WithDelegate:(id)delegate;
+(CustomIOS7AlertView *)alertInfo:(NSString *)msg Delegate:(id)delegate;

+(BOOL)isRegularMobileNumber :(NSString*)number;
+(BOOL)checkValidMobile:(NSString *)strMsg;
+(BOOL)checkPinCode:(NSString *)strMsg;

+(NSString *)formateData:(NSString *)date;
+(NSString *)formateDataWithDate:(NSDate *)date;
+(NSString *)relativeDateStringForDate:(NSDate *)date;
+(NSString *)formateDataLocalize:(NSString *)date;

+ (NSString*) convertCnToEn:(NSString*)cnString;
+ (void)showRealnameAuthAlertWithDelegate:(id)delegate;

+ (NSString *) getUserName:(NSDictionary*)dic;
+ (NSString *) getDateHourMinFrom:(NSString *)timeStr;

@end
