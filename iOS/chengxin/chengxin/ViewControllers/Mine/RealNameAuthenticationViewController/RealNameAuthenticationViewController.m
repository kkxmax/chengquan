//
//  RealNameAuthenticationViewController.m
//  chengxin
//
//  Created by common on 7/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "RealNameAuthenticationViewController.h"
#import "Global.h"
#import "CommonData.h"
#import "WebAPI.h"
#import "ImageChooseViewController.h"
#import "HomeOfficeChoiceViewController.h"
#import "HomePersonalChoiceViewController.h"
#import "HomeServiceChoiceViewController.h"
#import "HomeCommerceChoiceViewController.h"
#import "HomeItemChoiceViewController.h"
#import "HomeChoiceAllCityViewController.h"
#import "HomeChoiceBusinessViewController.h"
#import "BusinessSubcategoryViewController.h"
#import "ChoiceCompanyViewController.h"
#import "UIImageView+WebCache.h"
#import "CertZoomViewController.h"

@interface RealNameAuthenticationViewController ()

@end

@implementation RealNameAuthenticationViewController
{
    NSMutableArray* aryEnterpriseArray;
    NSMutableArray* aryPersonalArray;
    NSMutableArray* aryEnterpriseLabel;
    NSMutableArray* aryPersonalLabel;
    ImageChooseViewController* logoPicker, *certPicker, *enterCertPicker, *enterLogoPicker;
    UINavigationController *choiceNavVC;
    UIViewController *choiceVC;
    NSInteger sideViewIndex;
    NSMutableArray *aryCategory;
    NSMutableArray *aryCategorySelect;
    UIButton* currentButton;
    NSString *cityID, *cityOtherID;
    NSArray *cityArray, *otherArray;
    NSInteger cityRow, cityOtherRow;
    NSNumber* selectedBusinessId;
    NSNumber* selectedCompanyId;
    int akind;
    int testStatus;
    BOOL businessReceived;
    BOOL isLogoPicked, isCertPicked, isEnterCertPicked, isEnterLogoPicked;
    BOOL isCompanySelected;
}

@synthesize homeChoiceView, homeChoiceBackgroundView, homeChoiceTransView, citySelectView;

- (void)viewDidLoad {
    [super viewDidLoad];
    isLogoPicked = false;
    isCertPicked = false;
    isEnterCertPicked = false;
    isEnterLogoPicked = false;
    businessReceived = false;
    isCompanySelected = false;
    testStatus = -1;
    akind = -1;
    // Do any additional setup after loading the view from its nib.
    aryCategory = [[NSMutableArray alloc] init];

    aryCategorySelect = [[NSMutableArray alloc] init];
    aryEnterpriseArray = [[NSMutableArray alloc] init];
    aryPersonalArray = [[NSMutableArray alloc] init];
    aryEnterpriseLabel = [[NSMutableArray alloc] init];
    aryPersonalLabel = [[NSMutableArray alloc] init];
    [aryEnterpriseArray addObject:self.btnEnterpriseCheck1];
    [aryEnterpriseArray addObject:self.btnEnterpriseCheck2];
    [aryEnterpriseArray addObject:self.btnEnterpriseCheck3];
    [aryEnterpriseArray addObject:self.btnEnterpriseCheck4];
    [aryEnterpriseArray addObject:self.btnEnterpriseCheck5];
    [aryEnterpriseArray addObject:self.btnEnterpriseCheck6];
    [aryPersonalArray addObject:self.btnPersonalCheck1];
    [aryPersonalArray addObject:self.btnPersonalCheck2];
    [aryPersonalArray addObject:self.btnPersonalCheck3];
    [aryPersonalArray addObject:self.btnPersonalCheck4];
    [aryPersonalArray addObject:self.btnPersonalCheck5];
    [aryPersonalArray addObject:self.btnPersonalCheck6];
    
    [aryEnterpriseLabel addObject:self.lblEnterpriseCheck1];
    [aryEnterpriseLabel addObject:self.lblEnterpriseCheck2];
    [aryEnterpriseLabel addObject:self.lblEnterpriseCheck3];
    [aryEnterpriseLabel addObject:self.lblEnterpriseCheck4];
    [aryEnterpriseLabel addObject:self.lblEnterpriseCheck5];
    [aryEnterpriseLabel addObject:self.lblEnterpriseCheck6];
    
    [aryPersonalLabel addObject:self.lblPersonalCheck1];
    [aryPersonalLabel addObject:self.lblPersonalCheck2];
    [aryPersonalLabel addObject:self.lblPersonalCheck3];
    [aryPersonalLabel addObject:self.lblPersonalCheck4];
    [aryPersonalLabel addObject:self.lblPersonalCheck5];
    [aryPersonalLabel addObject:self.lblPersonalCheck6];
    
    [self getCityFromeServer];
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showSideView:) name:SHOW_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(hideSideView:) name:HIDE_REALNAME_CHOICE_VIEW_NOTIFICATION object:nil];
    sideViewIndex = SUB_HOME_ENTERPRISE;
    
    akind = [[CommonData sharedInstance].userInfo[@"akind"] intValue];
    testStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(akind == 1)
    {
        [self.btnPersonalAuth setSelected:YES];
        [self.btnEnterpriseAuth setSelected:NO];
    }else if(akind == 2)
    {
        [self.btnPersonalAuth setSelected:NO];
        [self.btnEnterpriseAuth setSelected:YES];
    }
    [self.btnEnterprise setSelected:YES];
    [self.btnPersonal setSelected:NO];
    
    self.authKindView.layer.zPosition = 1;
    
    self.txtMainBusiness.text = @"";
    self.txtRecommend.text = @"";
    self.txtCompanyComment.text = @"";
    self.txtHistory.text = @"";
    self.txtExperience.text = @"";
    
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getAccountInfo" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];

    
    [[WebAPI sharedInstance] sendPostRequest:@"getAccountInfo" Parameters:dicParams :^(NSObject *resObj) {
        
        [GeneralUtil hideProgress];
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [CommonData sharedInstance].userInfo = dicRes[@"account"];
                [self setUI];
            }else {
                [appDelegate.window makeToast:dicRes[@"msg"]
                                     duration:3.0
                                     position:CSToastPositionCenter
                                        style:nil];
            }
        }else
        {
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                                 duration:3.0
                                 position:CSToastPositionCenter
                                    style:nil];
        }
    }];

}
-(void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:SHOW_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:HIDE_REALNAME_CHOICE_VIEW_NOTIFICATION object:nil];
}
-(void)viewWillAppear:(BOOL)animated
{
    self.txtMainBusiness.hidden = YES;
    self.txtRecommend.hidden = YES;
    self.txtCompanyComment.hidden = YES;
    self.txtHistory.hidden = YES;
    self.txtExperience.hidden = YES;
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
}
- (void)getCityFromeServer {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getCityList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETCITYLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                cityArray = (NSMutableArray *)(dicRes[@"data"]);
                [self.cityPickerView reloadAllComponents];
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }
        [self getBusinessListFromServer];
    }];
}
- (void) getBusinessListFromServer {
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getXyleixingList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETXYLEIXINGLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
                [aryCategory removeAllObjects];
                
                
                for (int i = 0; i < aryData.count; i++) {
                    [aryCategory addObject:aryData[i]];
                }
                for(int i = 0; i < 6; i++)
                {
                    NSDictionary* dic = aryCategory[i];
                    for(int j = 0; j < [[dic objectForKey:@"children"] count]; j++)
                    {
                        NSDictionary* child = [dic objectForKey:@"children"][j];
                        
                        if([[child objectForKey:@"isMyWatched"] boolValue])
                            [((UIButton*)aryEnterpriseArray[i]) setSelected:true];
                        if([[child objectForKey:@"isMyWatch"] boolValue])
                            [((UIButton*)aryPersonalArray[i]) setSelected:true];
                    }
                    
                    ((UILabel*)aryEnterpriseLabel[i]).text = [aryCategory[i] objectForKey:@"title"];
                    ((UILabel*)aryPersonalLabel[i]).text = [aryCategory[i] objectForKey:@"title"];
                }
                businessReceived = true;
            }else
            {
                businessReceived = false;
            }
        }else
        {
            
        }
    }];
    
}

-(void)showBusinessChoiceView
{
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    
    // HomeChoiceViewController
    choiceNavVC = [[UINavigationController alloc] init];
    
    choiceVC = [[HomeChoiceBusinessViewController alloc] initWithNibName:@"HomeChoiceBusinessViewController" bundle:nil];
    ((HomeChoiceBusinessViewController*)choiceVC).isCancelButton = YES;
    ((HomeChoiceBusinessViewController*)choiceVC).isSingleSelectionMode = YES;
    
    [choiceNavVC pushViewController:choiceVC animated:NO];
    
    [choiceVC.view setFrame:homeChoiceView.bounds];
    [choiceNavVC.view setFrame:homeChoiceView.bounds];
    [homeChoiceView addSubview:choiceNavVC.view];
    homeChoiceBackgroundView.hidden = NO;
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(0, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    } completion:^(BOOL finished) {
        homeChoiceTransView.hidden = NO;
    }];

}
-(void)showAddressChoiceView
{
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    
    // HomeChoiceViewController
    choiceNavVC = [[UINavigationController alloc] init];
    
    choiceVC = [[HomeChoiceAllCityViewController alloc] initWithNibName:@"HomeChoiceAllCityViewController" bundle:nil];
    [choiceNavVC pushViewController:choiceVC animated:NO];
    
    [choiceVC.view setFrame:homeChoiceView.bounds];
    [choiceNavVC.view setFrame:homeChoiceView.bounds];
    [homeChoiceView addSubview:choiceNavVC.view];
    homeChoiceBackgroundView.hidden = NO;
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(0, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    } completion:^(BOOL finished) {
        homeChoiceTransView.hidden = NO;
    }];
}
-(void)showCompanyChoiceView
{
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    
    // HomeChoiceViewController
    choiceNavVC = [[UINavigationController alloc] init];
    
    choiceVC = [[ChoiceCompanyViewController alloc] initWithNibName:@"ChoiceCompanyViewController" bundle:nil];
    [choiceNavVC pushViewController:choiceVC animated:NO];
    
    [choiceVC.view setFrame:homeChoiceView.bounds];
    [choiceNavVC.view setFrame:homeChoiceView.bounds];
    [homeChoiceView addSubview:choiceNavVC.view];
    homeChoiceBackgroundView.hidden = NO;
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(0, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    } completion:^(BOOL finished) {
        homeChoiceTransView.hidden = NO;
    }];
}
-(void)showSideView:(NSNotification *) notification
{
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    
    // HomeChoiceViewController
    choiceNavVC = [[UINavigationController alloc] init];
    NSInteger index = sideViewIndex;
    if (index == SUB_HOME_PERSONAL)
        choiceVC = [[HomePersonalChoiceViewController alloc] initWithNibName:@"HomePersonalChoiceViewController" bundle:nil];
    else if(index == SUB_HOME_ENTERPRISE)
        choiceVC = [[HomeOfficeChoiceViewController alloc] initWithNibName:@"HomeOfficeChoiceViewController" bundle:nil];
    else if(index == SUB_HOME_COMMERCE)
        choiceVC = [[HomeCommerceChoiceViewController alloc] initWithNibName:@"HomeCommerceChoiceViewController" bundle:nil];
    else if(index == SUB_HOME_ITEM)
        choiceVC = [[HomeItemChoiceViewController alloc] initWithNibName:@"HomeItemChoiceViewController" bundle:nil];
    else if(index == SUB_HOME_SERVICE)
        choiceVC = [[HomeServiceChoiceViewController alloc] initWithNibName:@"HomeServiceChoiceViewController" bundle:nil];
    
    [choiceNavVC popViewControllerAnimated:NO];
    [choiceNavVC pushViewController:choiceVC animated:NO];
    
    [choiceVC.view setFrame:homeChoiceView.bounds];
    [choiceNavVC.view setFrame:homeChoiceView.bounds];
    [homeChoiceView addSubview:choiceNavVC.view];
    homeChoiceBackgroundView.hidden = NO;
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(0, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    } completion:^(BOOL finished) {
        homeChoiceTransView.hidden = NO;
    }];
}
- (void)showPickerView {
    if(citySelectView.isHidden) {
        [citySelectView setFrame:CGRectMake(0, self.view.frame.size.height, citySelectView.frame.size.width, citySelectView.frame.size.height)];
        citySelectView.hidden = NO;
        [UIView animateWithDuration:1.0f animations:^{
            [citySelectView setFrame:CGRectMake(0, self.view.frame.size.height - citySelectView.frame.size.height, citySelectView.frame.size.width, citySelectView.frame.size.height)];
        } completion:^(BOOL finished) {
        }];
    }else{
        [UIView animateWithDuration:1.0f animations:^{
            [citySelectView setFrame:CGRectMake(0, self.view.frame.size.height, citySelectView.frame.size.width, citySelectView.frame.size.height)];
        } completion:^(BOOL finished) {
            citySelectView.hidden = YES;
        }];
    }
}
- (void)hideSideView:(NSNotification *) notification
{
    homeChoiceTransView.hidden = YES;
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    } completion:^(BOOL finished) {
        homeChoiceBackgroundView.hidden = YES;
        [choiceNavVC.view removeFromSuperview];
    }];
    if(sideViewIndex == 1)
    {
        if([CommonData sharedInstance].choiceXyleixingId.length != 0)
        {
            selectedBusinessId = [NSNumber numberWithInt:[[CommonData sharedInstance].choiceXyleixingId intValue]];
            if(self.btnPersonalAuth.selected)
            {
                NSString *str = [NSString stringWithFormat:@"%@", [CommonData sharedInstance].selectedBusiness];
                [self.lblBusiness setText:str];
                [self.lblBusiness setTextColor:[UIColor blackColor]];
            }
            else
            {
                NSString *str = [NSString stringWithFormat:@"%@", [CommonData sharedInstance].selectedBusiness];
                self.lblEnterBusiness.text = str;
                [self.lblEnterBusiness setTextColor:[UIColor blackColor]];
            }
        }
        [CommonData sharedInstance].selectedBusiness = @"";
        [CommonData sharedInstance].choiceXyleixingId = @"";
    }
    else if(sideViewIndex == 2)
    {
        if(self.btnPersonalAuth.selected)
        {
            self.lblAddress.text = [CommonData sharedInstance].choiceCity;
            [self.lblAddress setTextColor:[UIColor blackColor]];
        }
        else
        {
            self.lblEnterAddress.text = [CommonData sharedInstance].choiceCity;
            [self.lblEnterAddress setTextColor:[UIColor blackColor]];
        }
    }
    else if(sideViewIndex == 3)
    {
        
        if([CommonData sharedInstance].choiceCompany.length != 0)
        {
            isCompanySelected = true;
            selectedBusinessId = [NSNumber numberWithInt:[[CommonData sharedInstance].choiceXyleixingId intValue]];
            NSString *str = [NSString stringWithFormat:@"%@", [CommonData sharedInstance].selectedBusiness];
            [self.lblBusiness setText:str];
            [self.lblBusiness setTextColor:[UIColor blackColor]];
            self.lblCompanyName.text = [CommonData sharedInstance].choiceCompany;
            selectedCompanyId = [NSNumber numberWithInt:[[CommonData sharedInstance].choiceCompanyId intValue]];
            [self.lblCompanyName setTextColor:[UIColor blackColor]];
            
            [CommonData sharedInstance].selectedBusiness = @"";
            [CommonData sharedInstance].choiceXyleixingId = @"";
        }
    
    }
}
-(void)setUI
{
    self.view.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    
    switch ([[CommonData sharedInstance].userInfo[@"testStatus"] intValue]) {
        case 0:
        {
            [self.stampImage setImage:nil];
            [self.btnComplete setTitle:@"提交审核" forState:UIControlStateNormal];
            self.scrollView.frame = CGRectMake(0, 64, SCREEN_WIDTH, SCREEN_HEIGHT - 64 - 60);
        }
            break;
        case 1:
        {
            [self.stampImage setImage:[UIImage imageNamed:@"label_shenhezhong.png"]];
            self.scrollView.frame = CGRectMake(0, 64, self.scrollView.frame.size.width,self.view.frame.size.height - 64);
            self.btnComplete.hidden = YES;
            if(self.btnPersonalAuth.selected)
            {
                NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]] ];
                [self.logoImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"add_touxiang1.png"]];
                self.txtRealName.text = [CommonData sharedInstance].userInfo[@"realname"];
                self.txtRealName.enabled = NO;
                self.txtCertNumber.text = [CommonData sharedInstance].userInfo[@"certNum"];
                self.txtCertNumber.enabled = NO;
                url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"certImage"]] ];
                [self.certImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"sfz.png"]];
                self.btnAddCertImage.hidden = YES;
                self.lblAddCertImage.hidden = YES;
                self.lblCompanyName.text = [CommonData sharedInstance].userInfo[@"enterName"];
                self.lblCompanyName.textColor = [UIColor blackColor];
                self.lblBusiness.text = [CommonData sharedInstance].userInfo[@"xyName"];
                self.lblBusiness.textColor = [UIColor blackColor];
                self.lblAddress.text = [CommonData sharedInstance].userInfo[@"provCity"];
                self.lblAddress.textColor = [UIColor blackColor];
                self.txtPosition.text = [CommonData sharedInstance].userInfo[@"job"];
                self.txtPosition.enabled = NO;
                self.txtWeixinNumber.text = [CommonData sharedInstance].userInfo[@"weixin"];
                self.txtWeixinNumber.enabled = NO;
                self.txtExperience.text = [CommonData sharedInstance].userInfo[@"experience"];
                self.txtExperience.editable = NO;
                self.txtHistory.text = [CommonData sharedInstance].userInfo[@"history"];
                self.txtHistory.editable = NO;
            }else
            {
                NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]] ];
                [self.enterLogoImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"add_touxiang1.png"]];
                if([[CommonData sharedInstance].userInfo[@"enterKind"] intValue] == 1)
                {
                    [self.btnPersonal setSelected:NO];
                    [self.btnEnterprise setSelected:YES];
                }else if([[CommonData sharedInstance].userInfo[@"enterKind"] intValue] == 2)
                {
                    [self.btnPersonal setSelected:YES];
                    [self.btnEnterprise setSelected:NO];
                }
                self.txtCompanyName.text = [CommonData sharedInstance].userInfo[@"enterName"];
                self.txtCompanyName.enabled = NO;
                self.lblEnterBusiness.text = [CommonData sharedInstance].userInfo[@"xyName"];
                self.lblEnterBusiness.textColor = [UIColor blackColor];
                self.lblEnterAddress.text = [CommonData sharedInstance].userInfo[@"provCity"];
                self.lblEnterAddress.textColor = [UIColor blackColor];
                self.lblDetailedAddress.text = [CommonData sharedInstance].userInfo[@"addr"];
                self.lblDetailedAddress.enabled = NO;
                self.txtCompayWebURL.text = [CommonData sharedInstance].userInfo[@"weburl"];
                self.txtCompayWebURL.enabled = NO;
                self.txtWeiXinPublicNumber.text = [CommonData sharedInstance].userInfo[@"weixin"];
                self.txtWeiXinPublicNumber.enabled = NO;
                self.txtMainBusiness.text = [CommonData sharedInstance].userInfo[@"mainJob"];
                self.txtMainBusiness.editable = NO;
                self.txtBusinessNumber.text = [CommonData sharedInstance].userInfo[@"enterCertNum"];
                self.txtBusinessNumber.enabled = NO;
                url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"enterCertImage"]] ];
                [self.imgBusinessPhoto sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"sfz.png"]];
                self.btnAddEnterCertImage.hidden = YES;
                self.lblAddEnterCertImage.hidden = YES;
                self.txtCompanyComment.text = [CommonData sharedInstance].userInfo[@"comment"];
                self.txtCompanyComment.editable = NO;
                self.txtRecommend.text = [CommonData sharedInstance].userInfo[@"recommend"];
                self.txtRecommend.editable = NO;
                self.txtDutierName.text = [CommonData sharedInstance].userInfo[@"bossName"];
                self.txtDutierName.enabled = NO;
                self.txtDutierPosition.text = [CommonData sharedInstance].userInfo[@"bossJob"];
                self.txtDutierPosition.enabled = NO;
                self.txtDutierPhoneNumber.text = [CommonData sharedInstance].userInfo[@"bossMobile"];
                self.txtDutierPhoneNumber.enabled = NO;
                self.txtDutierWeixinNumber.text = [CommonData sharedInstance].userInfo[@"bossWeixin"];
                self.txtDutierWeixinNumber.enabled = NO;
            }
        }
            break;
        case 2:
        {
            [self.stampImage setImage:[UIImage imageNamed:@"label_yirenzheng.png"]];
            self.scrollView.frame = CGRectMake(0, 64, SCREEN_WIDTH, SCREEN_HEIGHT - 64 - 60);
            [self.btnComplete setTitle:@"保存" forState:UIControlStateNormal];
            if(self.btnPersonalAuth.selected)
            {
                NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]] ];
                [self.logoImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"add_touxiang1.png"]];
                self.txtRealName.text = [CommonData sharedInstance].userInfo[@"realname"];
                self.txtRealName.enabled = NO;
                self.txtCertNumber.text = [CommonData sharedInstance].userInfo[@"certNum"];
                self.txtCertNumber.enabled = YES;
                url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"certImage"]] ];
                [self.certImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"sfz.png"]];
                self.btnAddCertImage.hidden = YES;
                self.lblAddCertImage.hidden = YES;
                self.lblCompanyName.text = [CommonData sharedInstance].userInfo[@"enterName"];
                self.lblBusiness.text = [CommonData sharedInstance].userInfo[@"xyName"];
                self.lblAddress.text = [CommonData sharedInstance].userInfo[@"provCity"];
                self.txtPosition.text = [CommonData sharedInstance].userInfo[@"job"];
                self.txtWeixinNumber.text = [CommonData sharedInstance].userInfo[@"weixin"];
                self.txtExperience.text = [CommonData sharedInstance].userInfo[@"experience"];
                self.txtHistory.text = [CommonData sharedInstance].userInfo[@"history"];
                self.lblCompanyName.textColor = [UIColor blackColor];
                self.lblBusiness.textColor = [UIColor blackColor];
                self.lblAddress.textColor = [UIColor blackColor];
                selectedCompanyId = [CommonData sharedInstance].userInfo[@"enterId"];
                selectedBusinessId = [CommonData sharedInstance].userInfo[@"xyleixingId"];
                cityID = [[CommonData sharedInstance].userInfo[@"cityId"] stringValue];
                cityOtherID = [[CommonData sharedInstance].userInfo[@"cityId"] stringValue];

            }else
            {
                NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]] ];
                [self.enterLogoImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"add_touxiang1.png"]];
                if([[CommonData sharedInstance].userInfo[@"enterKind"] intValue] == 1)
                {
                    [self.btnPersonal setSelected:NO];
                    [self.btnEnterprise setSelected:YES];
                }else if([[CommonData sharedInstance].userInfo[@"enterKind"] intValue] == 2)
                {
                    [self.btnPersonal setSelected:YES];
                    [self.btnEnterprise setSelected:NO];
                }
                self.txtCompanyName.text = [CommonData sharedInstance].userInfo[@"enterName"];
                self.txtCompanyName.enabled = NO;
                self.lblEnterBusiness.text = [CommonData sharedInstance].userInfo[@"xyName"];
                self.lblEnterAddress.text = [CommonData sharedInstance].userInfo[@"provCity"];
                self.lblDetailedAddress.text = [CommonData sharedInstance].userInfo[@"addr"];
                self.txtCompayWebURL.text = [CommonData sharedInstance].userInfo[@"weburl"];
                self.txtWeiXinPublicNumber.text = [CommonData sharedInstance].userInfo[@"weixin"];
                self.txtMainBusiness.text = [CommonData sharedInstance].userInfo[@"mainJob"];
                self.txtMainBusiness.editable = NO;
                self.txtBusinessNumber.text = [CommonData sharedInstance].userInfo[@"enterCertNum"];
                self.txtBusinessNumber.enabled = YES;
                url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"enterCertImage"]] ];
                [self.imgBusinessPhoto sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"sfz.png"]];
                self.btnAddEnterCertImage.hidden = YES;
                self.lblAddEnterCertImage.hidden = YES;
                self.txtCompanyComment.text = [CommonData sharedInstance].userInfo[@"comment"];
                self.txtRecommend.text = [CommonData sharedInstance].userInfo[@"recommend"];
                self.txtDutierName.text = [CommonData sharedInstance].userInfo[@"bossName"];
                self.txtDutierPosition.text = [CommonData sharedInstance].userInfo[@"bossJob"];
                self.txtDutierPhoneNumber.text = [CommonData sharedInstance].userInfo[@"bossMobile"];
                self.txtDutierWeixinNumber.text = [CommonData sharedInstance].userInfo[@"bossWeixin"];
                
                self.lblEnterBusiness.textColor = [UIColor blackColor];
                self.lblEnterAddress.textColor = [UIColor blackColor];
                
                selectedBusinessId = [CommonData sharedInstance].userInfo[@"xyleixingId"];
                cityID = [[CommonData sharedInstance].userInfo[@"cityId"] stringValue];
                cityOtherID = [[CommonData sharedInstance].userInfo[@"cityId"] stringValue];
            }
        }
            break;
        case 3:
        {
            [self.stampImage setImage:[UIImage imageNamed:@"label_yijujue.png"]];
            [self.btnComplete setTitle:@"重新提交审核" forState:UIControlStateNormal];
            self.scrollView.frame = CGRectMake(0, 64, SCREEN_WIDTH, SCREEN_HEIGHT - 64 - 60);
            
            if(self.btnPersonalAuth.selected)
            {
                NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]] ];
                [self.logoImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
                self.txtRealName.text = [CommonData sharedInstance].userInfo[@"realname"];
                self.txtCertNumber.text = [CommonData sharedInstance].userInfo[@"certNum"];
                url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"certImage"]] ];
                [self.certImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
                self.lblCompanyName.text = [CommonData sharedInstance].userInfo[@"enterName"];
                self.lblBusiness.text = [CommonData sharedInstance].userInfo[@"xyName"];
                self.lblAddress.text = [CommonData sharedInstance].userInfo[@"provCity"];
                cityOtherID = [CommonData sharedInstance].userInfo[@"cityId"];
                self.txtPosition.text = [CommonData sharedInstance].userInfo[@"job"];
                self.txtWeixinNumber.text = [CommonData sharedInstance].userInfo[@"weixin"];
                self.txtExperience.text = [CommonData sharedInstance].userInfo[@"experience"];
                self.txtHistory.text = [CommonData sharedInstance].userInfo[@"history"];
                self.lblCompanyName.textColor = [UIColor blackColor];
                self.lblBusiness.textColor = [UIColor blackColor];
                self.lblAddress.textColor = [UIColor blackColor];
                
                selectedCompanyId = [CommonData sharedInstance].userInfo[@"enterId"];
                selectedBusinessId = [CommonData sharedInstance].userInfo[@"xyleixingId"];
                cityID = [[CommonData sharedInstance].userInfo[@"cityId"] stringValue];
                
            }else
            {
                NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]] ];
                [self.enterLogoImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
                if([[CommonData sharedInstance].userInfo[@"enterKind"] intValue] == 1)
                {
                    [self.btnPersonal setSelected:NO];
                    [self.btnEnterprise setSelected:YES];
                }else if([[CommonData sharedInstance].userInfo[@"enterKind"] intValue] == 2)
                {
                    [self.btnPersonal setSelected:YES];
                    [self.btnEnterprise setSelected:NO];
                }
                self.txtCompanyName.text = [CommonData sharedInstance].userInfo[@"enterName"];
                self.lblEnterBusiness.text = [CommonData sharedInstance].userInfo[@"xyName"];
                cityOtherID = [CommonData sharedInstance].userInfo[@"cityId"];
                self.lblEnterAddress.text = [CommonData sharedInstance].userInfo[@"provCity"];
                self.lblDetailedAddress.text = [CommonData sharedInstance].userInfo[@"addr"];
                self.txtCompayWebURL.text = [CommonData sharedInstance].userInfo[@"weburl"];
                self.txtWeiXinPublicNumber.text = [CommonData sharedInstance].userInfo[@"weixin"];
                self.txtMainBusiness.text = [CommonData sharedInstance].userInfo[@"mainJob"];
                self.txtBusinessNumber.text = [CommonData sharedInstance].userInfo[@"enterCertNum"];
                url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"enterCertImage"]] ];
                [self.imgBusinessPhoto sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
                self.txtCompanyComment.text = [CommonData sharedInstance].userInfo[@"comment"];
                self.txtRecommend.text = [CommonData sharedInstance].userInfo[@"recommend"];
                self.txtDutierName.text = [CommonData sharedInstance].userInfo[@"bossName"];
                self.txtDutierPosition.text = [CommonData sharedInstance].userInfo[@"bossJob"];
                self.txtDutierPhoneNumber.text = [CommonData sharedInstance].userInfo[@"bossMobile"];
                self.txtDutierWeixinNumber.text = [CommonData sharedInstance].userInfo[@"bossWeixin"];
                
                self.lblEnterBusiness.textColor = [UIColor blackColor];
                self.lblEnterAddress.textColor = [UIColor blackColor];
                
                selectedBusinessId = [CommonData sharedInstance].userInfo[@"xyleixingId"];
                cityID = [[CommonData sharedInstance].userInfo[@"cityId"] stringValue];
            }

        }
            break;
        default:
            break;
    }
    
    if(self.btnPersonalAuth.isSelected)
    {
        [self.enterpriseAuthView removeFromSuperview];
        self.scrollContentView.frame = CGRectMake(self.scrollContentView.frame.origin.x, self.scrollContentView.frame.origin.y, self.view.frame.size.width, 1055);
        [self.scrollContentView addSubview:self.personalAuthView];
        self.personalAuthView.frame = CGRectMake(0, 50, self.scrollContentView.frame.size.width, self.personalAuthView.frame.size.height);
        self.chooseView.frame = CGRectMake(0, 798, self.chooseView.frame.size.width, self.chooseView.frame.size.height);
    }else{
        [self.personalAuthView removeFromSuperview];
        self.scrollContentView.frame = CGRectMake(self.scrollContentView.frame.origin.x, self.scrollContentView.frame.origin.y, self.view.frame.size.width, 1459);
        [self.scrollContentView addSubview:self.enterpriseAuthView];
        self.enterpriseAuthView.frame = CGRectMake(0, 50, self.scrollContentView.frame.size.width, self.enterpriseAuthView.frame.size.height);
        self.chooseView.frame = CGRectMake(0, 1202, self.chooseView.frame.size.width, self.chooseView.frame.size.height);
    }
    self.scrollView.contentSize = self.scrollContentView.frame.size;
    
    [self.txtMainBusiness setContentOffset:CGPointZero animated:NO];
    [self.txtMainBusiness setHidden:NO];
    [self.txtCompanyComment setContentOffset:CGPointZero animated:NO];
    [self.txtCompanyComment setHidden:NO];
    [self.txtRecommend setContentOffset:CGPointZero animated:NO];
    [self.txtRecommend setHidden:NO];
    [self.txtExperience setContentOffset:CGPointZero animated:NO];
    [self.txtExperience setHidden:NO];
    [self.txtHistory setContentOffset:CGPointZero animated:NO];
    [self.txtHistory setHidden:NO];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

-(IBAction)onPersonalAuthentication:(id)sender
{
    if(testStatus == 1 || testStatus == 2)
        return;
    self.btnPersonalAuth.selected = YES;
    self.btnEnterpriseAuth.selected = NO;
    [self setUI];
}
-(IBAction)onEnterpriseAuthentication:(id)sender
{
    if(testStatus == 1 || testStatus == 2)
        return;
    self.btnPersonalAuth.selected = NO;
    self.btnEnterpriseAuth.selected = YES;
    [self setUI];
}
-(IBAction)onEnterpriseCheckbox:(id)sender
{
    if(testStatus == 1 || businessReceived == false)
        return;
    currentButton = [aryEnterpriseArray objectAtIndex:((UIButton*)sender).tag];

    BusinessSubcategoryViewController* vc = [[BusinessSubcategoryViewController alloc] initWithNibName:@"BusinessSubcategoryViewController" bundle:nil];
    [vc setDelegate:self];
    vc.isMyWatch = false;
    vc.dicBusiness = aryCategory[((UIButton*)sender).tag];
    vc.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    [self.navigationController presentViewController:vc animated:NO completion:nil];
}
-(IBAction)onPersonalCheckbox:(id)sender
{
    if(testStatus == 1 || businessReceived == false)
        return;
    currentButton = [aryPersonalArray objectAtIndex:((UIButton*)sender).tag];
    
    BusinessSubcategoryViewController* vc = [[BusinessSubcategoryViewController alloc] initWithNibName:@"BusinessSubcategoryViewController" bundle:nil];
    [vc setDelegate:self];
    vc.dicBusiness = aryCategory[((UIButton*)sender).tag];
    vc.isMyWatch = true;
    vc.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    [self.navigationController presentViewController:vc animated:NO completion:nil];
}
-(void)businessSelected:(NSArray *)aryBusiness
{
    if(aryBusiness.count != 0)
        [currentButton setSelected:YES];
    else
        [currentButton setSelected:NO];
}
-(IBAction)onCompletion:(id)sender
{
    if(self.btnPersonalAuth.selected)
    {
        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        [dicParams setObject:ACTION_AUTHPERSONAL forKey:@"pAct"];
        
        NSMutableDictionary *imageDictionary = [NSMutableDictionary dictionary];
        
        if(isLogoPicked == false && [[CommonData sharedInstance].userInfo[@"testStatus"] intValue] == 0)
        {
            [appDelegate.window makeToast:@"Logo不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        NSData* logoImage = UIImagePNGRepresentation(self.logoImageView.image);
        [imageDictionary setObject:logoImage forKey:@"logo"];
        
        if(self.txtRealName.text.length == 0)
        {
            [appDelegate.window makeToast:@"真实姓名不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        [dicParams setObject:self.txtRealName.text forKey:@"realname"];
        if(self.txtCertNumber.text.length == 0)
        {
            [appDelegate.window makeToast:@"身份证号码不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        if(self.txtCertNumber.text.length != 18)
        {
            [appDelegate.window makeToast:@"请输入18位身份证号码"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        [dicParams setObject:self.txtCertNumber.text forKey:@"certNum"];
        
        if(isCertPicked == false && [[CommonData sharedInstance].userInfo[@"testStatus"] intValue] == 0)
        {
            [appDelegate.window makeToast:@"身份证不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        
        NSData* certImage = UIImagePNGRepresentation(self.certImageView.image);
        [imageDictionary setObject:certImage forKey:@"certImage"];
        
        if(selectedCompanyId != nil)
            [dicParams setObject:selectedCompanyId forKey:@"enterId"];
        if(selectedBusinessId != nil)
            [dicParams setObject:selectedBusinessId forKey:@"xyleixingId"];
        else
        {
            [appDelegate.window makeToast:@"行业不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        if(cityOtherID != nil)
            [dicParams setObject:cityOtherID forKey:@"cityId"];
        else
        {
            [appDelegate.window makeToast:@"所在地不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        if(self.txtPosition.text.length == 0)
        {
            [appDelegate.window makeToast:@"职位不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        [dicParams setObject:self.txtPosition.text forKey:@"job"];
        if(self.txtWeixinNumber.text.length == 0)
        {
//            [appDelegate.window makeToast:@"微信号不能为空"
//                        duration:3.0
//                        position:CSToastPositionCenter
//                           style:nil];
//            return;
            self.txtWeixinNumber.text = @"";
        }
        [dicParams setObject:self.txtWeixinNumber.text forKey:@"weixin"];
        if(self.txtExperience.text.length == 0)
        {
//            [appDelegate.window makeToast:@"工作经验不能为空"
//                        duration:3.0
//                        position:CSToastPositionCenter
//                           style:nil];
//            return;
            self.txtExperience.text = @"";
        }
        [dicParams setObject:self.txtExperience.text forKey:@"experience"];
        if(self.txtHistory.text.length == 0)
        {
//            [appDelegate.window makeToast:@"个人经历不能为空"
//                        duration:3.0
//                        position:CSToastPositionCenter
//                           style:nil];
//            return;
            self.txtHistory.text = @"";
        }
        [dicParams setObject:self.txtHistory.text forKey:@"history"];
        
        NSString *xywatch = @"";
        NSString *xywatched = @"";
        for(int i = 0; i < aryCategory.count; i++)
        {
            NSDictionary* dic = aryCategory[i];
            NSArray* children = dic[@"children"];
            if(children == nil)
                continue;
            for(int j = 0; j < children.count; j++)
            {
                NSDictionary* child = children[j];
                if([child[@"isMyWatch"] boolValue] == true)
                {
                    if(xywatch.length == 0)
                        xywatch = [child[@"id"] stringValue];
                    else
                        xywatch = [NSString stringWithFormat:@"%@,%@", xywatch, child[@"id"]];
                }
                if([child[@"isMyWatched"] boolValue] == true)
                {
                    if(xywatched.length == 0)
                        xywatched = [child[@"id"] stringValue];
                    else
                        xywatched = [NSString stringWithFormat:@"%@,%@", xywatched, child[@"id"]];
                }
            }
        }
        
        
        [dicParams setObject:xywatch forKey:@"xyWatch"];
        [dicParams setObject:xywatched forKey:@"xyWatched"];
        
        [GeneralUtil showProgress];
        
        [[WebAPI sharedInstance] sendPostRequestWithUpload:@"authPersonal" Parameters:dicParams UploadImages:imageDictionary :^(NSObject *resObj){
            
            
           [GeneralUtil hideProgress];
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    //[GeneralUtil  alertInfo:@"成功！"];
                    /*
                    [appDelegate.window makeToast:@"成功！"
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
                     */
                    [CommonData sharedInstance].userInfo = dicRes[@"userInfo"];
                    [self.navigationController popViewControllerAnimated:YES];
                }
                else {
                    //[GeneralUtil  alertInfo:dicRes[@"msg"]];
                    [appDelegate.window makeToast:dicRes[@"msg"]
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
                }
            }
        }];
    }else
    {
        if(isEnterLogoPicked == false && [[CommonData sharedInstance].userInfo[@"testStatus"] intValue] == 0)
        {
            [appDelegate.window makeToast:@"企业Logo不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }

        

        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        [dicParams setObject:ACTION_AUTHENTER forKey:@"pAct"];
        
        NSMutableDictionary *imageDictionary = [NSMutableDictionary dictionary];
        
        NSData* logoImage = UIImagePNGRepresentation(self.enterLogoImageView.image);
        [imageDictionary setObject:logoImage forKey:@"logo"];
        //[dicParams setObject:logoImage forKey:@"logo"];
        [dicParams setObject:[NSNumber numberWithInt:self.btnEnterprise.selected ? 1 : 2] forKey:@"enterKind"];
        if(self.txtCompanyName.text.length == 0)
        {
            [appDelegate.window makeToast:@"公司全称不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        
        [dicParams setObject:self.txtCompanyName.text forKey:@"enterName"];
       
        if(selectedBusinessId != nil)
            [dicParams setObject:selectedBusinessId forKey:@"xyleixingId"];
        else
        {
            [appDelegate.window makeToast:@"行业不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        
        if(cityOtherID != nil)
            [dicParams setObject:cityOtherID forKey:@"cityId"];
        else{
            [appDelegate.window makeToast:@"办公地址不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        
        
        
        if(self.lblDetailedAddress.text.length == 0)
        {
            [appDelegate.window makeToast:@"具体地址不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }

        [dicParams setObject:self.lblDetailedAddress.text forKey:@"addr"];
        
        if(self.txtCompayWebURL.text.length == 0)
        {
//            [appDelegate.window makeToast:@"公司官网不能为空"
//                        duration:3.0
//                        position:CSToastPositionCenter
//                           style:nil];
//            return;
            self.txtCompayWebURL.text = @"";
        }
        
        [dicParams setObject:self.txtCompayWebURL.text forKey:@"webUrl"];
        
        if(self.txtWeiXinPublicNumber.text.length == 0)
        {
//            [appDelegate.window makeToast:@"微信公众号不能为空"
//                        duration:3.0
//                        position:CSToastPositionCenter
//                           style:nil];
//            return;
            self.txtWeiXinPublicNumber.text = @"";
        }
        
        [dicParams setObject:self.txtWeiXinPublicNumber.text forKey:@"weixin"];
        
        if(self.txtMainBusiness.text.length == 0)
        {
            [appDelegate.window makeToast:@"主营业务不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        
        [dicParams setObject:self.txtMainBusiness.text forKey:@"mainJob"];
        
        if(self.txtBusinessNumber.text.length == 0)
        {
            [appDelegate.window makeToast:@"营业执照编号不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        if(self.txtBusinessNumber.text.length > 25)
        {
            [appDelegate.window makeToast:@"请输入正确的营业执照编号"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        [dicParams setObject:self.txtBusinessNumber.text forKey:@"enterCertNum"];
        
        if(isEnterCertPicked == false && [[CommonData sharedInstance].userInfo[@"testStatus"] intValue] == 0)
        {
            [appDelegate.window makeToast:@"营业执照照片不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        
        NSData* certImage = UIImagePNGRepresentation(self.imgBusinessPhoto.image);
        [imageDictionary setObject:certImage forKey:@"enterCertImage"];
        //[dicParams setObject:certImage forKey:@"enterCertImage"];
        
        if(self.txtCompanyComment.text.length == 0)
        {
            [appDelegate.window makeToast:@"公司介绍不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        
        [dicParams setObject:self.txtCompanyComment.text forKey:@"comment"];
        
        if(self.txtRecommend.text.length == 0)
        {
            [appDelegate.window makeToast:@"我们承诺不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        
        [dicParams setObject:self.txtRecommend.text forKey:@"recommend"];
        
        if(self.txtDutierName.text.length == 0)
        {
            [appDelegate.window makeToast:@"负责人姓名不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        [dicParams setObject:self.txtDutierName.text forKey:@"bossName"];
        
        if(self.txtDutierPosition.text.length == 0)
        {
            [appDelegate.window makeToast:@"负责人职位不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        [dicParams setObject:self.txtDutierPosition.text forKey:@"bossJob"];
        
        if(self.txtDutierPhoneNumber.text.length == 0)
        {
            [appDelegate.window makeToast:@"负责人手机号不能为空"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
        [dicParams setObject:self.txtDutierPhoneNumber.text forKey:@"bossMobile"];
        
        if(self.txtDutierWeixinNumber.text.length == 0)
        {
//            [appDelegate.window makeToast:@"负责人微信号不能为空"
//                        duration:3.0
//                        position:CSToastPositionCenter
//                           style:nil];
//            return;
            self.txtDutierWeixinNumber.text = @"";
        }
        [dicParams setObject:self.txtDutierWeixinNumber.text forKey:@"bossWeixin"];
        
        NSString *xywatch = @"";
        NSString *xywatched = @"";
        for(int i = 0; i < aryCategory.count; i++)
        {
            NSDictionary* dic = aryCategory[i];
            NSArray* children = dic[@"children"];
            if(children == nil)
                continue;
            for(int j = 0; j < children.count; j++)
            {
                NSDictionary* child = children[j];
                if([child[@"isMyWatch"] boolValue] == true)
                {
                    if(xywatch.length == 0)
                        xywatch = [child[@"id"] stringValue];
                    else
                        xywatch = [NSString stringWithFormat:@"%@,%@", xywatch, child[@"id"]];
                }
                if([child[@"isMyWatched"] boolValue] == true)
                {
                    if(xywatched.length == 0)
                        xywatched = [child[@"id"] stringValue];
                    else
                        xywatched = [NSString stringWithFormat:@"%@,%@", xywatched, child[@"id"]];
                }
            }
        }
        
        [dicParams setObject:xywatch forKey:@"xyWatch"];
        [dicParams setObject:xywatched forKey:@"xyWatched"];
        
        
        [GeneralUtil showProgress];
        [[WebAPI sharedInstance] sendPostRequestWithUpload:ACTION_AUTHENTER Parameters:dicParams UploadImages:imageDictionary :^(NSObject *resObj){
            
            
            [GeneralUtil hideProgress];
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    //[GeneralUtil  alertInfo:@"成功！"];
                    /*
                    [appDelegate.window makeToast:@"成功！"
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
                     */
                    [CommonData sharedInstance].userInfo = dicRes[@"userInfo"];
                    [self.navigationController popViewControllerAnimated:YES];
                }
                else {
                    //[GeneralUtil  alertInfo:dicRes[@"msg"]];
                    [appDelegate.window makeToast:dicRes[@"msg"]
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
                }
            }
        }];
    }
    

}

-(void)chooseViewController:(ImageChooseViewController *)vc shownImage:(UIImage *)image
{
    if(!image) {
        [appDelegate.window makeToast:@"图片不能超过1M."
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    
    if(vc == logoPicker)
    {
        [self.logoImageView setImage:image];
        isLogoPicked = true;
    }
    else if(vc == certPicker)
    {
        [self.certImageView setImage:image];
        isCertPicked = true;
        //self.btnAddCertImage.hidden = YES;
        //self.lblAddCertImage.hidden = YES;
    }
    else if(vc == enterLogoPicker)
    {
        [self.enterLogoImageView setImage:image];
        isEnterLogoPicked = true;
    }
    else if(vc == enterCertPicker)
    {
        [self.imgBusinessPhoto setImage:image];
        isEnterCertPicked = true;
        //self.btnAddEnterCertImage.hidden = YES;
        //self.lblAddEnterCertImage.hidden = YES;
    }
}

-(IBAction)onLogo:(UITapGestureRecognizer*)recognizer
{
    if(testStatus == 1)
        return;
    logoPicker = [[ImageChooseViewController alloc] initWithNibName:@"ImageChooseViewController" bundle:nil];
    logoPicker.delegate = self;
    logoPicker.navController = self.navigationController;
    logoPicker.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    logoPicker.isSquareCrop = YES;
    [self.navigationController presentViewController:logoPicker animated:YES completion:nil];
    
}

-(IBAction)addCertImage:(id)sender
{
    if(testStatus == 1 || testStatus == 2)
        return;
    certPicker = [[ImageChooseViewController alloc] initWithNibName:@"ImageChooseViewController" bundle:nil];
    certPicker.delegate = self;
    certPicker.navController = self.navigationController;
    certPicker.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    certPicker.isSquareCrop = NO;
    [self.navigationController presentViewController:certPicker animated:YES completion:nil];
}
-(IBAction)onLogoSelectionCancelled:(UITapGestureRecognizer*)recognizer
{

}

-(IBAction)onEmpty:(UITapGestureRecognizer*)recognizer
{
    if(sideViewIndex == 3)
    {
        [CommonData sharedInstance].choiceCompany = @"";
    }
    [self hideSideView:nil];
}
-(IBAction)onEnterprise:(id)sender
{
    if(testStatus == 1 || testStatus == 2)
        return;
    [self.btnEnterprise setSelected:YES];
    [self.btnPersonal setSelected:NO];
}
-(IBAction)onPersonal:(id)sender
{
    if(testStatus == 1 || testStatus == 2)
        return;
    [self.btnPersonal setSelected:YES];
    [self.btnEnterprise setSelected:NO];
}

-(IBAction)onCompanySelection:(id)sender
{
    if(testStatus == 1)
        return;
    //[self showSideView:nil];
    sideViewIndex = 3;
    [self showCompanyChoiceView];
}
-(IBAction)onBusinessSelection:(id)sender
{
    if(self.btnPersonalAuth.selected)
    {
        if(isCompanySelected)
            return;
    }
    if(self.btnEnterpriseAuth.selected && testStatus == 2)
    {
        return;
    }
    if(testStatus == 1)
        return;
    sideViewIndex = 1;
    [self showBusinessChoiceView];
}
-(IBAction)onAddressSelection:(id)sender
{
    if(testStatus == 1 || (testStatus == 2 && self.btnEnterpriseAuth.selected))
        return;
    [self showPickerView];
}
-(IBAction)onCancel:(id)sender
{
    [self showPickerView];
}
-(IBAction)onSelection:(id)sender
{
    [self.view endEditing:YES];
    [self showPickerView];
    NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:cityRow];
    NSArray *cityOtherArray = (NSArray *)(cityDic[@"cities"]);
    NSDictionary *cityOtherDic = (NSDictionary *)(cityOtherArray[cityOtherRow]);
    cityOtherID = cityOtherDic[@"id"];
    cityID = cityDic[@"id"];
    if(self.btnPersonalAuth.selected)
    {
        self.lblAddress.text = [NSString stringWithFormat:@"%@ , %@", cityDic[@"name"], cityOtherDic[@"name"]];
        [self.lblAddress setTextColor:[UIColor blackColor]];
    }
    else
    {
        self.lblEnterAddress.text = [NSString stringWithFormat:@"%@ , %@", cityDic[@"name"], cityOtherDic[@"name"]];
        [self.lblEnterAddress setTextColor:[UIColor blackColor]];
    }
}

-(IBAction)onEnterLogo:(UITapGestureRecognizer*)recognizer
{
    if(testStatus == 1)
        return;

    enterLogoPicker = [[ImageChooseViewController alloc] initWithNibName:@"ImageChooseViewController" bundle:nil];
    enterLogoPicker.delegate = self;
    enterLogoPicker.navController = self.navigationController;
    enterLogoPicker.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    enterLogoPicker.isSquareCrop = YES;
    [self.navigationController presentViewController:enterLogoPicker animated:YES completion:nil];
}

-(IBAction)addEnterCertImage:(id)sender
{
    if(testStatus == 1 || testStatus == 2)
        return;
    enterCertPicker = [[ImageChooseViewController alloc] initWithNibName:@"ImageChooseViewController" bundle:nil];
    enterCertPicker.delegate = self;
    enterCertPicker.navController = self.navigationController;
    enterCertPicker.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    enterCertPicker.isSquareCrop = NO;
    [self.navigationController presentViewController:enterCertPicker animated:YES completion:nil];
}



#pragma mark - UIPickerViewDelegate, UIPickerViewDataSource

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 2;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if(component == 0) {
        return cityArray.count;
    }else{
        if(cityArray.count > 0) {
            NSDictionary *cityOtherDic = (NSDictionary *)[cityArray objectAtIndex:[pickerView selectedRowInComponent:0]];
            NSArray *cityOtherArray = (NSArray *)(cityOtherDic[@"cities"]);
            return cityOtherArray.count;
        }else{
            return 0;
        }
    }
}


- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    if(component == 0) {
        if(cityArray.count <= row)
            return nil;
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:row];
        return cityDic[@"name"];
    }
    else {
        if([pickerView selectedRowInComponent:0] >= cityArray.count)
            return nil;
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:[pickerView selectedRowInComponent:0]];
        NSArray *cityOtherArray = (NSArray *)(cityDic[@"cities"]);
        if(row >= cityOtherArray.count)
            return nil;
        NSDictionary *cityOtherDic = (NSDictionary *)(cityOtherArray[row]);
        return cityOtherDic[@"name"];
    }
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    if(component == 0) {
        if(cityArray.count <= row)
            return;
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:row];
        cityID = cityDic[@"id"];
        cityRow = row;
        [self.cityPickerView reloadAllComponents];
    }
    else {
        if([pickerView selectedRowInComponent:0] >= cityArray.count)
            return;
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:[pickerView selectedRowInComponent:0]];
        NSArray *cityOtherArray = (NSArray *)(cityDic[@"cities"]);
        if(row >= cityOtherArray.count)
            return;
        NSDictionary *cityOtherDic = (NSDictionary *)(cityOtherArray[row]);
        cityOtherID = cityOtherDic[@"id"];
        cityOtherRow = row;
        self.lblAddress.text = [NSString stringWithFormat:@"%@ , %@", cityDic[@"name"], cityOtherDic[@"name"]];
    }
}

-(IBAction)onEnterCertImage:(UITapGestureRecognizer*)recognizer
{
    if(testStatus == 0 || testStatus == 3)
    {
        return;
    }
    CertZoomViewController* vc = [[CertZoomViewController alloc] initWithNibName:@"CertZoomViewController" bundle:nil];
    vc.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    [self.navigationController presentViewController:vc animated:NO completion:nil];
    [vc.imgCertZoom setImage:self.imgBusinessPhoto.image];
}

-(IBAction)onCertImage:(UITapGestureRecognizer*)recognizer
{
    if(testStatus == 0 || testStatus == 3)
    {
        return;
    }
    CertZoomViewController* vc = [[CertZoomViewController alloc] initWithNibName:@"CertZoomViewController" bundle:nil];
    vc.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    [self.navigationController presentViewController:vc animated:NO completion:nil];
    [vc.imgCertZoom setImage:self.certImageView.image];
}

#pragma mark - UITextFieldDelegate
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    if ((textField == self.txtWeiXinPublicNumber && range.location >= 20) ||
        (textField == self.txtDutierName && range.location >= 6) ||
        (textField == self.txtDutierPosition && range.location >= 10) ||
        (textField == self.txtDutierPhoneNumber && range.location >= 11) ||
        (textField == self.txtDutierWeixinNumber && range.location >= 20) ||
        (textField == self.txtPosition && range.location >= 10) ||
        (textField == self.txtWeixinNumber && range.location >= 20) ||
        (textField == self.txtCertNumber && range.location >= 18) ||
        (textField == self.txtCompanyName && range.location >= 30) ||
        (textField == self.txtBusinessNumber && range.location >= 25) ||
        (textField == self.txtCompayWebURL && range.location >= 300))
    {
        return NO;
    }
    return YES;
}
- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
    if((textView == self.txtMainBusiness && range.location >= 100) ||
       (textView == self.txtCompanyComment && range.location >= 300) ||
       (textView == self.txtRecommend && range.location >= 50) ||
       (textView == self.txtExperience && range.location >= 300) ||
       (textView == self.txtHistory && range.location >= 300))
    {
        return NO;
    }
    return YES;
}
@end
