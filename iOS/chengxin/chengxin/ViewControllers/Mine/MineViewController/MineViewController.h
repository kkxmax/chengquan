//
//  MineViewController.h
//  chengxin
//
//  Created by common on 7/25/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

@interface MineViewController : UIViewController<NotificationDelegate>

@property (nonatomic, retain) IBOutlet UIImageView* logoImageView;
@property (nonatomic, retain) IBOutlet UILabel* lblCodeNumber;
@property (nonatomic, retain) IBOutlet UILabel* lblChengHuDu;
@property (nonatomic, retain) IBOutlet UILabel* lblDianZan;
@property (nonatomic, retain) IBOutlet UILabel* lblPingJia;
@property (nonatomic, retain) IBOutlet UILabel* lblName;
@property (nonatomic, strong) IBOutlet UILabel *messageNumberLabel;


-(IBAction)onRealNameAuthentication:(id)sender;
-(IBAction)onChengXinReport:(id)sender;
-(IBAction)onChengXinRecord:(id)sender;
-(IBAction)onMyRating:(id)sender;
-(IBAction)onRatingToMe:(id)sender;
-(IBAction)onFavouriteList:(id)sender;
-(IBAction)onMyPublication:(id)sender;
-(IBAction)onOpinion:(id)sender;
-(IBAction)onSetting:(id)sender;
-(IBAction)onJiuCuo:(id)sender;
-(IBAction)onPhoto:(id)sender;


@end

