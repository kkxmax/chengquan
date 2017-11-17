//
//  RealNameAuthenticationViewController.h
//  chengxin
//
//  Created by common on 7/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ImageChooseViewController.h"
#import "BusinessSubcategoryViewController.h"

@interface RealNameAuthenticationViewController : UIViewController <ImageChooseViewControllerDelegate, BusinessSubcategoryViewControllerDelegate>

@property (nonatomic, retain) IBOutlet UIButton* btnPersonalAuth;
@property (nonatomic, retain) IBOutlet UIButton* btnEnterpriseAuth;
@property (nonatomic, retain) IBOutlet UIButton* btnEnterpriseCheck1;
@property (nonatomic, retain) IBOutlet UIButton* btnEnterpriseCheck2;
@property (nonatomic, retain) IBOutlet UIButton* btnEnterpriseCheck3;
@property (nonatomic, retain) IBOutlet UIButton* btnEnterpriseCheck4;
@property (nonatomic, retain) IBOutlet UIButton* btnEnterpriseCheck5;
@property (nonatomic, retain) IBOutlet UIButton* btnEnterpriseCheck6;
@property (nonatomic, retain) IBOutlet UIButton* btnPersonalCheck1;
@property (nonatomic, retain) IBOutlet UIButton* btnPersonalCheck2;
@property (nonatomic, retain) IBOutlet UIButton* btnPersonalCheck3;
@property (nonatomic, retain) IBOutlet UIButton* btnPersonalCheck4;
@property (nonatomic, retain) IBOutlet UIButton* btnPersonalCheck5;
@property (nonatomic, retain) IBOutlet UIButton* btnPersonalCheck6;

@property (nonatomic, retain) IBOutlet UILabel* lblEnterpriseCheck1;
@property (nonatomic, retain) IBOutlet UILabel* lblEnterpriseCheck2;
@property (nonatomic, retain) IBOutlet UILabel* lblEnterpriseCheck3;
@property (nonatomic, retain) IBOutlet UILabel* lblEnterpriseCheck4;
@property (nonatomic, retain) IBOutlet UILabel* lblEnterpriseCheck5;
@property (nonatomic, retain) IBOutlet UILabel* lblEnterpriseCheck6;
@property (nonatomic, retain) IBOutlet UILabel* lblPersonalCheck1;
@property (nonatomic, retain) IBOutlet UILabel* lblPersonalCheck2;
@property (nonatomic, retain) IBOutlet UILabel* lblPersonalCheck3;
@property (nonatomic, retain) IBOutlet UILabel* lblPersonalCheck4;
@property (nonatomic, retain) IBOutlet UILabel* lblPersonalCheck5;
@property (nonatomic, retain) IBOutlet UILabel* lblPersonalCheck6;

@property BOOL isPersonal;
@property (nonatomic, retain) IBOutlet UIView* authKindView;
@property (nonatomic, retain) IBOutlet UIView* personalAuthView;
@property (nonatomic, retain) IBOutlet UIView* enterpriseAuthView;
@property (nonatomic, retain) IBOutlet UIView* chooseView;
@property (nonatomic, retain) IBOutlet UIView* scrollContentView;
@property (nonatomic, retain) IBOutlet UIScrollView* scrollView;

@property (nonatomic, retain) IBOutlet UIImageView* logoImageView;
@property (nonatomic, retain) IBOutlet UIImageView* certImageView;

@property (nonatomic, retain) IBOutlet UIImageView* stampImage;
@property (nonatomic, retain) IBOutlet UITextField* txtRealName;
@property (nonatomic, retain) IBOutlet UILabel* lblCompanyName;
@property (nonatomic, retain) IBOutlet UILabel* lblBusiness;
@property (nonatomic, retain) IBOutlet UILabel* lblAddress;
@property (nonatomic, retain) IBOutlet UITextField* txtPosition;
@property (nonatomic, retain) IBOutlet UITextField* txtWeixinNumber;
@property (nonatomic, retain) IBOutlet UITextField* txtExperience;
@property (nonatomic, retain) IBOutlet UITextField* txtHistory;
@property (nonatomic, retain) IBOutlet UITextField* txtCertNumber;

@property (nonatomic, retain) IBOutlet UIButton* btnEnterprise;
@property (nonatomic, retain) IBOutlet UIButton* btnPersonal;
@property (nonatomic, retain) IBOutlet UITextField* txtCompanyName;
@property (nonatomic, retain) IBOutlet UILabel* lblEnterBusiness;
@property (nonatomic, retain) IBOutlet UILabel* lblEnterAddress;
@property (nonatomic, retain) IBOutlet UITextField* lblDetailedAddress;
@property (nonatomic, retain) IBOutlet UITextField* txtCompayWebURL;
@property (nonatomic, retain) IBOutlet UITextField* txtWeiXinPublicNumber;
@property (nonatomic, retain) IBOutlet UITextField* txtMainBusiness;
@property (nonatomic, retain) IBOutlet UITextField* txtBusinessNumber;
@property (nonatomic, retain) IBOutlet UIImageView* imgBusinessPhoto;
@property (nonatomic, retain) IBOutlet UITextField* txtCompanyComment;
@property (nonatomic, retain) IBOutlet UITextField* txtRecommend;
@property (nonatomic, retain) IBOutlet UITextField* txtDutierName;
@property (nonatomic, retain) IBOutlet UITextField* txtDutierPosition;
@property (nonatomic, retain) IBOutlet UITextField* txtDutierPhoneNumber;
@property (nonatomic, retain) IBOutlet UITextField* txtDutierWeixinNumber;

@property (nonatomic, retain) IBOutlet UIButton* btnComplete;

@property (nonatomic, strong) IBOutlet UIView *homeChoiceBackgroundView;
@property (nonatomic, strong) IBOutlet UIView *homeChoiceTransView;
@property (nonatomic, strong) IBOutlet UIView *homeChoiceView;


-(IBAction)onBack:(id)sender;
-(IBAction)onPersonalAuthentication:(id)sender;
-(IBAction)onEnterpriseAuthentication:(id)sender;
-(IBAction)onEnterpriseCheckbox:(id)sender;
-(IBAction)onPersonalCheckbox:(id)sender;
-(IBAction)onCompletion:(id)sender;
-(IBAction)onLogo:(UITapGestureRecognizer*)recognizer;
-(IBAction)onLogoSelectionCancelled:(UITapGestureRecognizer*)recognizer;
-(IBAction)onEnterprise:(id)sender;
-(IBAction)onPersonal:(id)sender;
-(IBAction)addCertImage:(id)sender;

-(IBAction)onCompanySelection:(id)sender;
-(IBAction)onBusinessSelection:(id)sender;
-(IBAction)onAddressSelection:(id)sender;
@end
