//
//  HomeItemDetailViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/31/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ItemDetailViewController.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "HomeFamiliarDetailViewController.h"
#import "WebViewController.h"
#import "HomeItemAddViewController.h"

@interface ItemDetailViewController ()
{
    NSDictionary *itemDic;
    NSString *accountID;
    NSString *callString;
    BOOL isUp;
    UIAlertView *deleteAlert, *upDownAlert;
}
@end

@implementation ItemDetailViewController
@synthesize detailScrollView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    itemDic = [[NSDictionary alloc] init];
    itemDic = [CommonData sharedInstance].selectedItemServiceDic;
    [self setData];
    
    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE) {
        self.navTitleLabel.text = @"服务详情";
        self.commentTitleLabel.text = @"服务介绍";
        self.needTitleLabel.text = @"地址：";
    }
    self.btnEdit.layer.cornerRadius = 4;
    self.btnDelete.layer.cornerRadius = 4;
    self.btnUpDown.layer.cornerRadius = 4;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self getProductDataFromServer];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void)getProductDataFromServer {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    NSString* actionName;
    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE)
    {
        [dicParams setObject:@"getServiceDetail" forKey:@"pAct"];
        [dicParams setObject:itemDic[@"id"] forKey:@"serviceId"];
        actionName = @"getServiceDetail";
    }else
    {
        [dicParams setObject:@"getItemDetail" forKey:@"pAct"];
        [dicParams setObject:itemDic[@"id"] forKey:@"itemId"];
        actionName = @"getItemDetail";
    }
    
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:actionName Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                //[self.noNetworkView setHidden:YES];
                if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE)
                {
                    itemDic = (NSDictionary *)(dicRes[@"service"]);
                }else
                {
                    itemDic = (NSDictionary *)(dicRes[@"item"]);
                }
                
                [self setData];
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
                //[self.noNetworkView setHidden:NO];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }
    }];
    
}

- (void)setData {
    NSString *logoImageName = itemDic[@"logo"];
    if(logoImageName) {
        [self.avatarImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
            if(!image) {
                [self.avatarImageView setImage:[UIImage imageNamed:@"no_image_item.png"]];
            }
        }];
    }
    self.nameLabel.text = itemDic[@"name"];
    [self.nameLabel sizeToFit];
    [self.fenleiNameButton setTitle:itemDic[@"fenleiName"] forState:UIControlStateNormal];
    dispatch_async(dispatch_get_main_queue(), ^{
        CGSize stringSize = [self.fenleiNameButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.0]}];
        if(self.nameLabel.frame.size.width > (self.view.frame.size.width - 75 - stringSize.width - self.nameLabel.frame.origin.x)) {
            [self.nameLabel setFrame:CGRectMake(self.nameLabel.frame.origin.x, self.nameLabel.frame.origin.y, self.view.frame.size.width - 75 - stringSize.width - self.nameLabel.frame.origin.x, self.nameLabel.frame.size.height)];
        }
        CGRect nameLabelFrame = self.nameLabel.frame;
        [self.fenleiNameButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y, stringSize.width, 16)];
    });
    self.addressLabel.text = [NSString stringWithFormat:@"所在城市 : %@", itemDic[@"cityName"]];
    self.codeLabel.text = [NSString stringWithFormat:@"编号：%@", itemDic[@"code"]];
    self.commentTextView.text = itemDic[@"comment"];
    self.networkAddrLabel.text = itemDic[@"weburl"];
    NSString* str = itemDic[@"writeTimeString"];
    self.lblPublicationTime.text = [NSString stringWithFormat:@"发布时间: %@", [str substringWithRange:NSMakeRange(0, str.length - 3)]];

    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE) {
        self.needLabel.text = itemDic[@"addr"];
    }else{
        self.needLabel.text = itemDic[@"need"];
    }
    self.contactNameLabel.text = itemDic[@"contactName"];
    self.contactMobileLabel.text = itemDic[@"contactMobile"];
    self.contactWeixinLabel.text = itemDic[@"contactWeixin"];
    
    isUp = [itemDic[@"status"] boolValue];
    if(isUp)
    {
        self.lblShangJia.text = @"已上架";
        //[self.btnUpDown setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
        //[self.btnUpDown setBackgroundColor:[UIColor whiteColor]];
        [self.btnUpDown setTitle:@"下架" forState:UIControlStateNormal];
        self.btnUpDown.frame = CGRectMake(7, self.btnUpDown.frame.origin.y, self.view.frame.size.width - 14, self.btnEdit.frame.size.height);

        
        self.btnEdit.hidden = YES;
        self.btnDelete.hidden = YES;
    }else
    {
        self.lblShangJia.text = @"未上架";
        //[self.btnUpDown setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        //[self.btnUpDown setBackgroundColor:[UIColor orangeColor]];
        [self.btnUpDown setTitle:@"上架" forState:UIControlStateNormal];
        self.btnUpDown.frame = CGRectMake(self.view.frame.size.width - self.btnDelete.frame.origin.x - self.btnDelete.frame.size.width, self.btnUpDown.frame.origin.y, self.btnDelete.frame.size.width, self.btnDelete.frame.size.height);
        self.btnEdit.hidden = NO;
        self.btnDelete.hidden = NO;
    }
    /*
    NSString *accountLogoImageName = itemDic[@"accountLogo"];
    if(accountLogoImageName) {
        self.accountLogoImageLabel.hidden = YES;
        [self.accountLogoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, accountLogoImageName]] placeholderImage:[UIImage imageNamed:@"no_image.png"]];
    }else{
        self.accountLogoImageLabel.hidden = NO;
    }
    NSInteger aKind = [itemDic[@"akind"] integerValue];
    if (aKind == PERSONAL_KIND) {
        self.officeMarkLabel.text = @"个人";
        self.enterNameLabel.text = itemDic[@"realname"];
    }else{
        self.enterNameLabel.text = itemDic[@"enterName"];
        self.officeMarkLabel.text = itemDic[@"enterKindName"];
    }
    self.enterCodeLabel.text = itemDic[@"accountCode"];
    self.creditLabel.text = [NSString stringWithFormat:@"%ld%%", (long)([itemDic[@"accountCredit"] longValue])];
    self.writeTimeLabel.text = itemDic[@"writeTimeString"];
    accountID = [NSString stringWithFormat:@"%ld", (long)[itemDic[@"accountId"] integerValue]];
    callString = itemDic[@"accountMobile"];
     */
}
#pragma mark - IBAction

- (IBAction)onShowEnterAction:(id)sender {
    if([accountID isEqualToString:@""])
        return;
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getAccountDetail" forKey:@"pAct"];
    [dicParams setObject:accountID forKey:@"accountId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETACCOUNTDETAIL Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [GeneralUtil hideProgress];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSDictionary *friendDictionary = (NSDictionary *)(dicRes[@"account"]);
                int nTestStatus = [friendDictionary[@"testStatus"] intValue];
                if(nTestStatus != 2) {
                    [appDelegate.window makeToast:@"未认证的熟人／企业"
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
                }else{
                    HomeFamiliarDetailViewController *homeEnterDetailVC = [[HomeFamiliarDetailViewController alloc] initWithNibName:@"HomeFamiliarDetailViewController" bundle:nil];
                    [CommonData sharedInstance].selectedFriendAccountID = accountID;
                    [self.navigationController pushViewController:homeEnterDetailVC animated:YES];
                }
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
    }];

}
#pragma mark - Actions
-(IBAction)onUpDown:(id)sender
{
    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE)
    {
        if(isUp)
            upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要下架该服务吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        else
            upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要上架该服务吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    }else
    {
        if(isUp)
            upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要下架该项目吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        else
            upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要上架该项目吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    }
    [upDownAlert show];
    
}
-(IBAction)onDelete:(id)sender
{
    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE)
    {
        deleteAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要删除该服务吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    }else
    {
        deleteAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要删除该项目吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    }
    
    [deleteAlert show];
}
-(IBAction)onEdit:(id)sender
{
    [CommonData sharedInstance].lastClick = @"ItemDetailViewController";
    HomeItemAddViewController* vc = [[HomeItemAddViewController alloc] initWithNibName:@"HomeItemAddViewController" bundle:nil];
    
    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE)
        [CommonData sharedInstance].addItemServiceIndex = SERVICE_PAGE;
    else
        [CommonData sharedInstance].addItemServiceIndex = ITEM_PAGE;
    vc.item = itemDic;
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)onBackAction:(id)sender {
    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE)
       [[NSNotificationCenter defaultCenter] postNotificationName:RELOAD_SERVICE_DATA_NOTIFICATION object:nil];
    else
        [[NSNotificationCenter defaultCenter] postNotificationName:RELOAD_ITEM_DATA_NOTIFICATION object:nil];
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onShareAction:(id)sender {
    
}
- (IBAction)onNetworkAction:(id)sender {
    if(![itemDic[@"weburl"] isEqualToString:@""]) {
        NSURL* url = [NSURL URLWithString:itemDic[@"weburl"]];
        if([[url absoluteString] containsString:@"http"] == false)
        {
            url = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@", [url absoluteString] ]];
        }
        [[UIApplication sharedApplication] openURL:url];
        /*
        WebViewController *webVC = [[WebViewController alloc] initWithNibName:@"WebViewController" bundle:nil];
        webVC.webUrl = itemDic[@"weburl"];
        [self.navigationController pushViewController:webVC animated:YES];
         */
    }
}

- (IBAction)onCallingAction:(id)sender {
    if(![callString isEqualToString:@""]) {
        NSString *URLString = [@"tel:" stringByAppendingString:callString];
        NSURL *URL = [NSURL URLWithString:URLString];
//        if([[URL absoluteString] containsString:@"http"] == false)
//        {
//            URL = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@", [URL absoluteString] ]];
//        }
        [[UIApplication sharedApplication] openURL:URL];
    }
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
    {
        if(deleteAlert == alertView)
        {
            NSString* actionName;
            NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
            if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE)
            {
                [dicParams setObject:ACTION_DELETESERVICE forKey:@"pAct"];
                [dicParams setObject:itemDic[@"id"] forKey:@"serviceId"];
                actionName = ACTION_DELETESERVICE;
            }else
            {
                [dicParams setObject:ACTION_DELETEITEM forKey:@"pAct"];
                [dicParams setObject:itemDic[@"id"] forKey:@"itemId"];
                actionName = ACTION_DELETEITEM;
            }
            
            [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
            
            [[WebAPI sharedInstance] sendPostRequest:actionName Parameters:dicParams :^(NSObject *resObj){
                
                [GeneralUtil hideProgress];
                
                NSDictionary *dicRes = (NSDictionary *)resObj;
                
                if (dicRes != nil ) {
                    if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                        [appDelegate.window makeToast:@"删除！"
                                    duration:3.0
                                    position:CSToastPositionCenter
                                       style:nil];
                        [self.navigationController popViewControllerAnimated:YES];
                    }
                    else {
                        [appDelegate.window makeToast:dicRes[@"msg"]
                                    duration:3.0
                                    position:CSToastPositionCenter
                                       style:nil];
                    }
                }
            }];
        }else if(upDownAlert == alertView)
        {
            NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
            NSString* actionName;
            if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE)
            {
                [dicParams setObject:isUp ? ACTION_DOWNSERVICE : ACTION_UPSERVICE forKey:@"pAct"];
                [dicParams setObject:itemDic[@"id"] forKey:@"serviceId"];
                actionName = isUp ? ACTION_DOWNSERVICE : ACTION_UPSERVICE;
            }else
            {
                [dicParams setObject:isUp ? ACTION_DOWNITEM : ACTION_UPITEM forKey:@"pAct"];
                [dicParams setObject:itemDic[@"id"] forKey:@"itemId"];
                actionName = isUp ? ACTION_DOWNITEM : ACTION_UPITEM;
            }
            
            [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
            
            [[WebAPI sharedInstance] sendPostRequest:actionName Parameters:dicParams :^(NSObject *resObj){
                
                [GeneralUtil hideProgress];
                
                NSDictionary *dicRes = (NSDictionary *)resObj;
                
                if (dicRes != nil ) {
                    if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                        
                        isUp = !isUp;
                        if(isUp)
                        {
                            self.lblShangJia.text = @"已上架";
                            
                            //[self.btnUpDown.titleLabel setTextColor:[UIColor blackColor]];
                            //[self.btnUpDown setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
                            //[self.btnUpDown setBackgroundColor:[UIColor whiteColor]];
                            [self.btnUpDown setTitle:@"下架" forState:UIControlStateNormal];
                            self.btnUpDown.frame = CGRectMake(7, self.btnUpDown.frame.origin.y, self.view.frame.size.width - 14, self.btnEdit.frame.size.height);
                            self.btnEdit.hidden = YES;
                            self.btnDelete.hidden = YES;
                        }else
                        {
                            self.lblShangJia.text = @"未上架";
                            //[self.lblShangJia setTextColor:[UIColor blackColor]];
                            //[self.btnUpDown setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
                            //[self.btnUpDown setBackgroundColor:[UIColor orangeColor]];
                            [self.btnUpDown setTitle:@"上架" forState:UIControlStateNormal];
                            self.btnUpDown.frame = CGRectMake(self.view.frame.size.width - self.btnDelete.frame.origin.x - self.btnDelete.frame.size.width, self.btnUpDown.frame.origin.y, self.btnEdit.frame.size.width, self.btnEdit.frame.size.height);
                            self.btnEdit.hidden = NO;
                            self.btnDelete.hidden = NO;
                        }
                        
                    }
                    else {
                        [appDelegate.window makeToast:dicRes[@"msg"]
                                    duration:3.0
                                    position:CSToastPositionCenter
                                       style:nil];
                    }
                }
            }];
        }
        
    }
    
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
