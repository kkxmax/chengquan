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

@interface RealNameAuthenticationViewController ()

@end

@implementation RealNameAuthenticationViewController
{
    NSMutableArray* aryEnterpriseArray;
    NSMutableArray* aryPersonalArray;
    NSMutableArray* aryEnterpriseLabel;
    NSMutableArray* aryPersonalLabel;
    ImageChooseViewController* logoPicker, *certPicker;
    UINavigationController *choiceNavVC;
    UIViewController *choiceVC;
    NSInteger sideViewIndex;
    NSMutableArray *aryCategory1, *aryCategory2;
    NSMutableArray *aryCategorySelect;
    UIButton* currentButton;
}

@synthesize homeChoiceView, homeChoiceBackgroundView, homeChoiceTransView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    aryCategory1 = [[NSMutableArray alloc] init];
    aryCategory2 = [[NSMutableArray alloc] init];
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
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showSideView:) name:SHOW_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(hideSideView:) name:HIDE_REALNAME_CHOICE_VIEW_NOTIFICATION object:nil];
    sideViewIndex = SUB_HOME_ENTERPRISE;
}
-(void)viewWillAppear:(BOOL)animated
{
    [self.btnPersonalAuth setSelected:YES];
    [self.btnEnterpriseAuth setSelected:NO];
    
    [self setUI];
    [self getBusinessListFromServer];
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    
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
                
                [aryCategory1 removeAllObjects];
                
                
                for (int i = 0; i < aryData.count; i++) {
                    [aryCategory1 addObject:aryData[i]];
                }
                for(int i = 0; i < 6; i++)
                {
                    
                    for(int j = 0; j < [[aryCategory1[i] objectForKey:@"children"] count]; j++)
                    {
                        NSArray* children = [aryCategory1[i] objectForKey:@"children"];
                        [[children objectAtIndex:j] setObject:[NSNumber numberWithBool:NO] forKey:@"selected"];
                    }
                    ((UILabel*)aryEnterpriseLabel[i]).text = [aryCategory1[i] objectForKey:@"title"];
                }
                
                [[WebAPI sharedInstance] sendPostRequest:ACTION_GETXYLEIXINGLIST Parameters:dicParams :^(NSObject *resObj) {
                    
                    NSDictionary *dicRes = (NSDictionary *)resObj;
                    
                    if (dicRes != nil ) {
                        if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                            NSArray *aryData = dicRes[@"data"];
                            
                            [aryCategory2 removeAllObjects];
                            
                            for (int i = 0; i < aryData.count; i++) {
                                [aryCategory2 addObject:aryData[i]];
                            }
                            for(int i = 0; i < 6; i++)
                            {
                                for(int j = 0; j < [[aryCategory2[i] objectForKey:@"children"] count]; j++)
                                {
                                    NSArray* children = [aryCategory2[i] objectForKey:@"children"];
                                    [[children objectAtIndex:j] setObject:[NSNumber numberWithBool:NO] forKey:@"selected"];
                                }
                                ((UILabel*)aryPersonalLabel[i]).text = [aryCategory2[i] objectForKey:@"title"];
                            }
                            
                        }
                    }
                }];
            }
        }
    }];
    
}

-(void)showBusinessChoiceView
{
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    
    // HomeChoiceViewController
    choiceNavVC = [[UINavigationController alloc] init];
    
    choiceVC = [[HomeChoiceBusinessViewController alloc] initWithNibName:@"HomeChoiceBusinessViewController" bundle:nil];
    
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
        self.lblBusiness.text = [CommonData sharedInstance].choiceXyleixingIds;
    else if(sideViewIndex == 2)
        self.lblAddress.text = [CommonData sharedInstance].choiceCity;
}
-(void)setUI
{
    self.view.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    
    switch ([[CommonData sharedInstance].userInfo[@"test_status"] intValue]) {
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
        }
            break;
        case 2:
        {
            [self.stampImage setImage:[UIImage imageNamed:@"label_yirenzheng.png"]];
            [self.btnComplete setTitle:@"编辑" forState:UIControlStateNormal];
            self.scrollView.frame = CGRectMake(0, 64, SCREEN_WIDTH, SCREEN_HEIGHT - 64 - 60);
        }
            break;
        case 3:
        {
            [self.stampImage setImage:[UIImage imageNamed:@"label_yijujue.png"]];
            [self.btnComplete setTitle:@"重新提交审核" forState:UIControlStateNormal];
            self.scrollView.frame = CGRectMake(0, 64, SCREEN_WIDTH, SCREEN_HEIGHT - 64 - 60);
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
    self.btnPersonalAuth.selected = YES;
    self.btnEnterpriseAuth.selected = NO;
    [self setUI];
}
-(IBAction)onEnterpriseAuthentication:(id)sender
{
    self.btnPersonalAuth.selected = NO;
    self.btnEnterpriseAuth.selected = YES;
    [self setUI];
}
-(IBAction)onEnterpriseCheckbox:(id)sender
{
    currentButton = [aryEnterpriseArray objectAtIndex:((UIButton*)sender).tag];

    BusinessSubcategoryViewController* vc = [[BusinessSubcategoryViewController alloc] initWithNibName:@"BusinessSubcategoryViewController" bundle:nil];
    [vc setDelegate:self];
    vc.dicBusiness = aryCategory1[((UIButton*)sender).tag];
    vc.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    [self.navigationController presentViewController:vc animated:nil completion:nil];
}
-(IBAction)onPersonalCheckbox:(id)sender
{
    currentButton = [aryPersonalArray objectAtIndex:((UIButton*)sender).tag];
    
    BusinessSubcategoryViewController* vc = [[BusinessSubcategoryViewController alloc] initWithNibName:@"BusinessSubcategoryViewController" bundle:nil];
    [vc setDelegate:self];
    vc.dicBusiness = aryCategory2[((UIButton*)sender).tag];
    vc.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    [self.navigationController presentViewController:vc animated:nil completion:nil];
}
-(void)businessSelected:(NSArray *)aryBusiness
{
    if(aryBusiness.count != 0)
        currentButton.selected = YES;
    else
        currentButton.selected = NO;
}
-(IBAction)onCompletion:(id)sender
{
    [GeneralUtil showProgress];
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"authPersonal" forKey:@"pAct"];

    NSData* logoImage = UIImagePNGRepresentation(self.logoImageView.image);
    [dicParams setObject:logoImage forKey:@"logo"];
    [dicParams setObject:self.txtRealName.text forKey:@"realname"];
    [dicParams setObject:self.txtCertNumber.text forKey:@"certNum"];
    NSData* certImage = UIImagePNGRepresentation(self.certImageView.image);
    [dicParams setObject:certImage forKey:@"certImage"];
    [dicParams setObject:self.lblCompanyName.text forKey:@"enterName"];
    [dicParams setObject:@"1" forKey:@"xyleixingId"];
    [dicParams setObject:@"2" forKey:@"cityId"];
    [dicParams setObject:self.txtPosition.text forKey:@"job"];
    [dicParams setObject:self.txtWeixinNumber.text forKey:@"weixin"];
    [dicParams setObject:self.txtExperience.text forKey:@"experience"];
    [dicParams setObject:self.txtHistory.text forKey:@"history"];
    [dicParams setObject:@"1,8,3" forKey:@"xyWatch"];
    [dicParams setObject:@"1,8,3" forKey:@"xyWatched"];
    
    
    [[WebAPI sharedInstance] sendPostRequestWithUpload:@"authPersonal" Parameters:dicParams :^(NSObject *resObj){
        
        [GeneralUtil hideProgress];
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                
                
            }
            else {
                [GeneralUtil alertInfo:dicRes[@"msg"]];
            }
        }
    }];

}

-(void)chooseViewController:(ImageChooseViewController *)vc shownImage:(UIImage *)image
{
    if(vc == logoPicker)
        [self.logoImageView setImage:image];
    else if(vc == certPicker)
        [self.certImageView setImage:image];
}

-(IBAction)onLogo:(UITapGestureRecognizer*)recognizer
{
    logoPicker = [[ImageChooseViewController alloc] initWithNibName:@"ImageChooseViewController" bundle:nil];
    logoPicker.delegate = self;
    logoPicker.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    [self.navigationController presentViewController:logoPicker animated:YES completion:nil];
    
}

-(IBAction)addCertImage:(id)sender
{
    certPicker = [[ImageChooseViewController alloc] initWithNibName:@"ImageChooseViewController" bundle:nil];
    certPicker.delegate = self;
    certPicker.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    [self.navigationController presentViewController:certPicker animated:YES completion:nil];
}
-(IBAction)onLogoSelectionCancelled:(UITapGestureRecognizer*)recognizer
{

}
-(IBAction)onEnterprise:(id)sender
{
    self.btnEnterprise.selected = YES;
    self.btnPersonal.selected = NO;
}
-(IBAction)onPersonal:(id)sender
{
    self.btnPersonal.selected = YES;
    self.btnEnterprise.selected = NO;
}

-(IBAction)onCompanySelection:(id)sender
{
    //[self showSideView:nil];
}
-(IBAction)onBusinessSelection:(id)sender
{
    sideViewIndex = 1;
    [self showBusinessChoiceView];
}
-(IBAction)onAddressSelection:(id)sender
{
    sideViewIndex = 2;
    [self showAddressChoiceView];
}

@end
