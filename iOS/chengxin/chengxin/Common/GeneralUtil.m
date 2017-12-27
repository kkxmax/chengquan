//
//  GeneralUtil.m
//  Yappo
//
//  Created by Rahul on 14/04/14.
//  Copyright (c) 2014 kETAN. All rights reserved.
//

#import "GeneralUtil.h"
#import "Reachability.h"
#import "Global.h"
#import "CustomIOS7AlertView.h"

@implementation GeneralUtil

#pragma mark - TO VALIDATE EMAILID

+(BOOL)isInternetConnection {
    
    Reachability *reachability = [Reachability reachabilityForInternetConnection];
    NetworkStatus networkStatus = [reachability currentReachabilityStatus];
    
    if(networkStatus == NotReachable) {
        return NO;
    }
    else{
        return YES;
    }
}

+(BOOL) NSStringIsValidEmail:(NSString *)emailid
{
    BOOL stricterFilter = YES;
    NSString *stricterFilterString = @"[A-Z0-9a-z\\._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}";
    NSString *laxString = @".+@([A-Za-z0-9]+\\.)+[A-Za-z]{2}[A-Za-z]*";
    NSString *emailRegex = stricterFilter ? stricterFilterString : laxString;
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:emailid];
}

#pragma mark - For Storing user prefernce to NSUSERDEFAULT

+(void)setUserPreference:(NSString *)key value:(NSString *)strValue {
    
    [[NSUserDefaults standardUserDefaults] setObject:strValue forKey:key];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

+ (NSString *)getUserPreference:(NSString *)key {
    
    NSString *strUserDetail = [[NSUserDefaults standardUserDefaults] objectForKey:key];
    
    //strUserDetail = [[NSUserDefaults standardUserDefaults] objectForKey:key];
    
    if(![self isNotNullValue:strUserDetail]) {
        return @"";
    }
    
    return strUserDetail;
}

+(BOOL)isNotNullValue:(NSString*)strText {
    
    if([strText class] != [NSNull class] || strText != NULL) {
        return YES;
    }
    else {
        return NO;
    }
}

static NSInteger nShowProgressCount = 0;
+(void)showProgress {
    nShowProgressCount ++;
    if(!appDelegate.progressHUD) {
        appDelegate.progressHUD = [[MBProgressHUD alloc]init];
        
        appDelegate.progressHUD.dimBackground = YES;
        appDelegate.progressHUD.color = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.0f];
        
//        appDelegate.progressHUD.labelText = @"Loading...";
        
        [appDelegate.window addSubview:appDelegate.progressHUD];
    }
    
    if(appDelegate.progressHUD) {
//        appDelegate.progressHUD.labelText = @"Loading...";
        [appDelegate.window bringSubviewToFront:appDelegate.progressHUD];
        [appDelegate.progressHUD show:YES];
    }
}

+(void)hideProgress {
    nShowProgressCount --;
    if(appDelegate.progressHUD && nShowProgressCount < 1) {
        [appDelegate.progressHUD hide:YES];
        nShowProgressCount = 0;
    }
}

+(NSMutableArray *)getYear:(NSDate *)date {
    
    NSDateComponents *components = [[NSCalendar currentCalendar] components:NSCalendarUnitDay | NSCalendarUnitMonth | NSCalendarUnitYear fromDate:date];
    
    NSInteger curant = [components year];
    
    NSMutableArray *arrYear = [[NSMutableArray alloc] init];
    
    for (int i = (int)curant; i > curant - 20; i--) {
        NSString *str = [NSString stringWithFormat:@"%d-%d",i,i+1];
        [arrYear addObject:str];
    }
    
    return arrYear;
}

+(BOOL)checkValidMobile:(NSString *)strMsg {
    
    
    if([strMsg isEqualToString:@""]){
        return FALSE;
    }
    else {
        if ([self isRegularMobileNumber:strMsg]) {
            return TRUE;
        }
        else {
            return FALSE;
        }
    }
}

+(BOOL)isRegularMobileNumber :(NSString*)number
{
    NSString *numberRegEx = @"[0-9]{11}";
    NSPredicate *numberTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", numberRegEx];
    if ([numberTest evaluateWithObject:number] == YES)
        return TRUE;
    else
        return FALSE;
}

+(BOOL)checkPinCode:(NSString *)strMsg {
    
    
    if([strMsg isEqualToString:@""]){
        return FALSE;
    }
    else {
        if ([self isRegularPinnum:strMsg]) {
            return TRUE;
        }
        else {
            return FALSE;
        }
    }
}

+(BOOL)isRegularPinnum :(NSString*)number
{
    NSString *numberRegEx = @"[0-9]{4}";
    NSPredicate *numberTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", numberRegEx];
    if ([numberTest evaluateWithObject:number] == YES)
        return TRUE;
    else
        return FALSE;
}

+(NSString *)formateData:(NSString *)date {
    
    NSDate *edate = [[NSDate alloc] init];
    
    NSDateFormatter *dformat = [[NSDateFormatter alloc]init];
    
    [dformat setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    
    edate = [dformat dateFromString:date];
    
    NSTimeZone* sourceTimeZone = [NSTimeZone timeZoneWithAbbreviation:@"UTC"];
    
    NSTimeZone* destinationTimeZone = [NSTimeZone systemTimeZone];

    NSInteger sourceGMTOffset = [sourceTimeZone secondsFromGMTForDate:edate];
    NSInteger destinationGMTOffset = [destinationTimeZone secondsFromGMTForDate:edate];
    NSTimeInterval interval = destinationGMTOffset - sourceGMTOffset;
    
    NSDate* destinationDate = [[NSDate alloc] initWithTimeInterval:interval sinceDate:edate];
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setTimeZone:[NSTimeZone systemTimeZone]];
    
    //[formatter setDateFormat:@"dd-MMM-yyyy, HH:mm"];
    
   // [formatter setDateStyle:NSDateFormatterFullStyle];
    
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    
    NSString *ed=[formatter stringFromDate:destinationDate];
    
    return ed;
}

+(NSString *)formateDataLocalize:(NSString *)date {
    
    NSDate *edate = [[NSDate alloc] init];
    
    NSDateFormatter *dformat = [[NSDateFormatter alloc]init];
    
    [dformat setDateFormat:@"yyyy-MM-dd"];
    
    edate = [dformat dateFromString:date];
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    //[formatter setDateFormat:@"dd-MMM-yyyy, HH:mm"];
    
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    // [formatter setTimeStyle:NSDateFormatterShortStyle];
    
    NSString *ed=[formatter stringFromDate:edate];
    
    return ed;
}

+(NSString *)formateDataWithDate:(NSDate *)date {
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    
    NSString *ed=[formatter stringFromDate:date];
    
    return ed;
}

+(NSString *)relativeDateStringForDate:(NSDate *)date
{
    
    NSCalendarUnit units = NSCalendarUnitDay | NSCalendarUnitWeekOfYear | NSCalendarUnitMonth | NSCalendarUnitYear | NSCalendarUnitMinute | NSCalendarUnitHour;
    
    NSCalendar *cal = [NSCalendar currentCalendar];
    
    NSDateComponents *components1 = [cal components:(NSCalendarUnitEra|NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay) fromDate:[NSDate date]];
    
    NSDate *today = [cal dateFromComponents:components1];
    
    components1 = [cal components:(NSCalendarUnitEra|NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay|NSCalendarUnitHour|NSCalendarUnitMinute ) fromDate:date];
    
    NSDate *thatdate = [cal dateFromComponents:components1];
    
    // if `date` is before "now" (i.e. in the past) then the components will be positive
    NSDateComponents *components = [[NSCalendar currentCalendar] components:units fromDate:today toDate:thatdate options:0];
    
    NSDate *prevDate = date;
    
    NSDateFormatter *dformat = [[NSDateFormatter alloc]init];
    
    [dformat setDateFormat:@"dd-MMM-yyyy"];
    
    NSString *strPrevDate=[dformat stringFromDate:prevDate];
    
    // return strPrevDate;
    
    if (components.day <= 0) {
       // return [NSString stringWithFormat:@"%ld:%ld", (long)components.hour,(long)components.minute];
        
        int h = (int)(+components.hour);
        int m = (int)(+components.minute);
        
        return [NSString stringWithFormat:@"%d:%d", h,m];
    }
    else {
        return strPrevDate;
    }
}


+(CGFloat)screenWidth
{
    return [UIScreen mainScreen].bounds.size.width;
}

+(CustomIOS7AlertView *)alertInfo:(NSString *)msg {
    
    CustomIOS7AlertView *alertView = [self customAlertDisplay:msg Btns:[NSMutableArray arrayWithObjects:BTN_OK_TITLE, nil]];
    [alertView show];
    
    return alertView;
}

+(CustomIOS7AlertView *)alertInfo:(NSString *)msg withDelegate:(id)delegate {
    
    CustomIOS7AlertView *alertView = [self customAlertDisplay:msg Btns:[NSMutableArray arrayWithObjects:BTN_OK_TITLE, nil]];
    alertView.delegate = delegate;
    [alertView show];
    
    return alertView;
}

+(CustomIOS7AlertView *)alertInfo:(NSString *)msg WithDelegate:(id)delegate {
    
    CustomIOS7AlertView *alertView = [self customAlertDisplay:msg Btns:[NSMutableArray arrayWithObjects:BTN_OK_TITLE, nil]];
    if (delegate) {
        alertView.delegate = delegate;
    }
    
    [alertView show];
    
    return alertView;
}

+(CustomIOS7AlertView *)alertInfo:(NSString *)msg Delegate:(id)delegate {
    
    CustomIOS7AlertView *alertView = [self customAlertDisplay:msg Btns:[NSMutableArray arrayWithObjects:BTN_YES_TITLE, BTN_NO_TITLE, nil] ];
    alertView.delegate = delegate;
    [alertView show];
    
    return alertView;
}


+(CustomIOS7AlertView *)customAlertDisplay:(NSString *)msgClassObj Btns:(NSMutableArray *)btnArr {
    
    CustomIOS7AlertView *alertView = [[CustomIOS7AlertView alloc] init];
    
    [alertView setContainerView:[self createCustomAlertView:msgClassObj]];
    
    [alertView setButtonTitles:btnArr];
    
    [alertView setUseMotionEffects:true];
    
    return alertView;
}

+(CustomIOS7AlertView *)customAlertDisplay: (NSString *)msgClassObj {
    return [self customAlertDisplay:msgClassObj Btns:[NSMutableArray arrayWithObjects:BTN_OK_TITLE, BTN_CANCEL_TITLE, nil] ];
}

+(UIView *)createCustomAlertView : (NSString *) msg {
    
    UIView *MainView = [[UIView alloc] init];
    [MainView setFrame:CGRectMake(0, 20, SCREEN_WIDTH ,SCREEN_HEIGHT - 20)];
    MainView.backgroundColor = APP_BACKGROUD_COLOR;
    
    UIView *alertView = [[UIView alloc] init];
    alertView.tag = 1000;
    
    if (IS_IPAD) {
        [alertView setFrame:CGRectMake(0, 0, SCREEN_WIDTH /2 , SCREEN_HEIGHT /3)];
    }
    else {
        [alertView setFrame:CGRectMake(0, 0, SCREEN_WIDTH - 60, 220)];
        
    }
    
    alertView.backgroundColor = WHITE_COLOR;    
    alertView.layer.cornerRadius = 15;
    
    [MainView addSubview:alertView];
    
    UIView *subview = [[UIView alloc]init];
    
    UILabel *alertTitle = [[UILabel alloc]init];
    [alertTitle setFont:FONT_18];
    alertTitle.textAlignment = NSTextAlignmentCenter;
    alertTitle.text = APP_NAME;
    [alertTitle setTextColor:CYNA_COLOR];
    
    UIImageView *rightIcon = [[UIImageView alloc]init];
    rightIcon.image = [UIImage imageNamed:@"cs"];
    
    if (IS_IPAD) {
        [subview  setFrame:CGRectMake(0, 0, alertView.frame.size.width, 64)];
        [alertTitle setFrame:CGRectMake(0, 5, subview.frame.size.width, 44)];
        [rightIcon setFrame:CGRectMake(subview.frame.size.width - 50, 10, 30, 30)];
    }
    else {
        [subview  setFrame:CGRectMake(0, 0, alertView.frame.size.width, 64)];
        [alertTitle setFrame:CGRectMake(0, 5, subview.frame.size.width, 44)];
        [rightIcon setFrame:CGRectMake(subview.frame.size.width - 50, 10, 30, 30)];
    }
    
    [subview addSubview:alertTitle];
    [subview addSubview:rightIcon];
    
    UILabel *alertDesc = [[UILabel alloc]init];
    if (IS_IPAD) {
        [alertDesc setFrame:CGRectMake(10, alertTitle.frame.size.height - 5, alertView.frame.size.width-20, 220)];
    }
    else {
        [alertDesc setFrame:CGRectMake(10, alertTitle.frame.size.height - 5, alertView.frame.size.width-20, 120)];
    }
    alertDesc.font = FONT_18;
    alertDesc.textColor = APP_BACKGROUD_COLOR;
    alertDesc.textAlignment = NSTextAlignmentCenter;
    
    alertDesc.numberOfLines = 0;
    alertDesc.baselineAdjustment = UIBaselineAdjustmentAlignCenters;
    alertDesc.text = msg;
    
    [alertView addSubview:alertDesc];
    [alertView addSubview:subview];
    
    
    return alertView;
}
+ (NSString*) convertCnToEn:(NSString*)cnString {

    CFStringRef aCFString = (__bridge CFStringRef)cnString;
    CFMutableStringRef string = CFStringCreateMutableCopy(NULL, 0, aCFString);
    CFStringTransform(string, NULL, kCFStringTransformToLatin, false);
    CFStringTransform(string, NULL, kCFStringTransformStripCombiningMarks,
                      false);
    NSString *enString = (NSString *)CFBridgingRelease(string);
    return enString;
}

+ (void)showRealnameAuthAlertWithDelegate:(id)delegate {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"您好，请先进行个人认证，再进行操作！" message:@"" delegate:delegate cancelButtonTitle:@"取消" otherButtonTitles:@"立即认证", nil];
    [alert show];
}

+ (NSString*) getUserName:(NSDictionary*)dic {
    NSString * name = dic[@"mobile"];
    NSInteger aKind = [dic[@"akind"] integerValue];
    
    if (aKind == ENTERPRISE_KIND) {
        if (![dic[@"enterName"] isEqualToString:@""]) {
            name = dic[@"enterName"];
        }
    } else {
        if (![dic[@"realname"] isEqualToString:@""]) {
            name = dic[@"realname"];
        }
    }
    return name;
}

+ (NSString *) getDateHourMinFrom:(NSString *)timeStr {
    return [timeStr substringToIndex:timeStr.length-3];
}

@end
