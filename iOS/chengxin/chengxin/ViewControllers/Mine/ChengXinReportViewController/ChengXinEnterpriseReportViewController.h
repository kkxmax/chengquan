//
//  ChengXinEnterpriseReportViewController.h
//  chengxin
//
//  Created by common on 4/15/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ChengXinEnterpriseReportViewController : UIViewController

@property (nonnull, retain) IBOutlet UIImageView* logoImageView;
@property (nonnull, retain) IBOutlet UILabel* lblName;
@property (nonnull, retain) IBOutlet UILabel* lblChengHuDu;
@property (nonnull, retain) IBOutlet UILabel* lblDianZan;
@property (nonnull, retain) IBOutlet UILabel* lblFeedback;
@property (nonnull, retain) IBOutlet UIButton* btnBusiness;

@property (nonnull, retain) IBOutlet UILabel* lblCodeNumber;
@property (nonnull, retain) IBOutlet UILabel* lblCompanyName;
@property (nonnull, retain) IBOutlet UILabel* lblCompanyInfo;
@property (nonnull, retain) IBOutlet UILabel* lblCompanyURL;
@property (nonnull, retain) IBOutlet UILabel* lblMainBusiness;
@property (nonnull, retain) IBOutlet UILabel* lblWeixinPublicNumber;
@property (nonnull, retain) IBOutlet UILabel* lblManagerName;
@property (nonnull, retain) IBOutlet UILabel* lblManagerPosition;
@property (nonnull, retain) IBOutlet UILabel* lblManagerPhoneNumber;
@property (nonnull, retain) IBOutlet UILabel* lblManagerWeixinNumber;
@property (nonnull, retain) IBOutlet UILabel* lblOfficeAddress;
@property (nonnull, retain) IBOutlet UIImageView* businessPhoto;

@property (nonnull, retain) IBOutlet UILabel* lblRecommend;
@property (nonnull, retain) IBOutlet UILabel* lblRecommenderName;
@property (nonnull, retain) IBOutlet UIButton* btnRecommenderBusiness;
@property (nonnull, retain) IBOutlet UILabel* lblRecommenderCode;
@property (nonnull, retain) IBOutlet UILabel* lblRecommenderChengHuDu;
@property (nonnull, retain) IBOutlet UILabel* lblRecommenderChengHuDu1;
@property (nonnull, retain) IBOutlet UILabel* lblRecommenderDianZan;
@property (nonnull, retain) IBOutlet UILabel* lblRecommenderPingJia;

@property (nonnull, retain) IBOutlet UIImageView* recommenderPhoto;
@property (nonnull, retain) IBOutlet UIImageView* markImage;

@property (nonnull, retain) IBOutlet UIView* recommendView;
@property (nonnull, retain) IBOutlet UIScrollView* scrollView;

- (IBAction)onBack:(id)sender;
- (IBAction)onURL:(id)sender;
- (IBAction)onInviter:(id)sender;
@end
