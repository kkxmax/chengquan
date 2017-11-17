//
//  AppDelegate.h
//  chengxin
//
//  Created by seniorcoder on 10/23/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MBProgressHUD.h"

@protocol NotificationDelegate<NSObject>
@optional

- (void)updateNotification;
@end

@interface AppDelegate : UIResponder <UIApplicationDelegate>
{
    id<NotificationDelegate> notificationDelegate;

}
@property (nonatomic, retain) id<NotificationDelegate> notificationDelegate;

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic,retain)  MBProgressHUD *progressHUD;


@end

