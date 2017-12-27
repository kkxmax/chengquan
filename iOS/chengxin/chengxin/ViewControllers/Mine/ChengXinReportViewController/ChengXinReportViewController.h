//
//  ChengXinReportViewController.h
//  chengxin
//
//  Created by common on 7/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ChengXinReportViewController : UIViewController

@property (nonnull, retain) IBOutlet UIImageView* logoImageView;
@property (nonnull, retain) IBOutlet UILabel* lblName;
@property (nonnull, retain) IBOutlet UILabel* lblChengHuDu;
@property (nonnull, retain) IBOutlet UILabel* lblDianZan;
@property (nonnull, retain) IBOutlet UILabel* lblFeedback;
@property (nonnull, retain) IBOutlet UIButton* btnBusiness;

@property (nonnull, retain) IBOutlet UILabel* lblCodeNumber;
@property (nonnull, retain) IBOutlet UILabel* lblCompanyName;
@property (nonnull, retain) IBOutlet UILabel* lblCompanyWebURL;
@property (nonnull, retain) IBOutlet UILabel* lblAddress;
@property (nonnull, retain) IBOutlet UILabel* lblWeixinNumber;
@property (nonnull, retain) IBOutlet UILabel* lblPosition;
@property (nonnull, retain) IBOutlet UILabel* lblRecommender;
@property (nonnull, retain) IBOutlet UILabel* lblPersonalHistory;
@property (nonnull, retain) IBOutlet UILabel* lblWorkExperience;

@property (nonnull, retain) IBOutlet UILabel* lblRecommenderName;
@property (nonnull, retain) IBOutlet UIButton* btnRecommenderBusiness;
@property (nonnull, retain) IBOutlet UILabel* lblRecommenderCode;
@property (nonnull, retain) IBOutlet UILabel* lblRecommenderChengHuDu;
@property (nonnull, retain) IBOutlet UILabel* lblRecommenderChengHuDu1;
@property (nonnull, retain) IBOutlet UILabel* lblRecommenderDianZan;
@property (nonnull, retain) IBOutlet UILabel* lblRecommenderPingJia;
@property (nonnull, retain) IBOutlet UIView* recommendView;
@property (nonnull, retain) IBOutlet UIScrollView* scrollView;


@property (nonnull, retain) IBOutlet UIImageView* recommenderPhoto;
@property (nonnull, retain) IBOutlet UIImageView* markImage;

- (IBAction)onBack:(id)sender;
- (IBAction)onInviter:(id)sender;
- (IBAction)onURL:(id)sender;
- (IBAction)onCompanyDetail:(id)sender;
@end
