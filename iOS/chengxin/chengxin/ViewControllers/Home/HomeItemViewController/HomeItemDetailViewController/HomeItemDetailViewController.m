//
//  HomeItemDetailViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/31/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeItemDetailViewController.h"
#import "Global.h"
#import "UIImageView+WebCache.h"

@interface HomeItemDetailViewController ()
{
    NSDictionary *itemDic;
}
@end

@implementation HomeItemDetailViewController
@synthesize detailScrollView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    itemDic = [[NSDictionary alloc] init];
    itemDic = [CommonData sharedInstance].selectedItemServiceDic;
    [self setData];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setData {
    NSString *logoImageName = itemDic[@"logo"];
    if(logoImageName) {
        self.logoImageLabel.hidden = YES;
        [self.avatarImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]]];
    }else{
        self.logoImageLabel.hidden = NO;
    }
    self.nameLabel.text = itemDic[@"name"];
    [self.nameLabel sizeToFit];
    [self.fenleiNameButton setTitle:itemDic[@"fenleiName"] forState:UIControlStateNormal];
    dispatch_async(dispatch_get_main_queue(), ^{
        CGRect nameLabelFrame = self.nameLabel.frame;
        CGSize stringSize = [self.fenleiNameButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.0]}];
        [self.fenleiNameButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y, stringSize.width, 16)];
    });
    self.addressLabel.text = [NSString stringWithFormat:@"所在城市 : %@", itemDic[@"cityName"]];
    self.codeLabel.text = [NSString stringWithFormat:@"编号：%@", itemDic[@"code"]];
    self.commentTextView.text = itemDic[@"comment"];
    self.networkAddrLabel.text = itemDic[@"weburl"];
    self.needLabel.text = itemDic[@"need"];
    self.contactNameLabel.text = itemDic[@"contactName"];
    self.contactMobileLabel.text = itemDic[@"contactMobile"];
    self.contactWeixinLabel.text = itemDic[@"contactWeixin"];
    NSString *accountLogoImageName = itemDic[@"accountLogo"];
    if(accountLogoImageName) {
        self.accountLogoImageLabel.hidden = YES;
        [self.accountLogoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, accountLogoImageName]]];
    }else{
        self.accountLogoImageLabel.hidden = NO;
    }
    self.enterNameLabel.text = itemDic[@"enterName"];
    self.officeMarkLabel.text = itemDic[@"enterKindName"];
    self.enterCodeLabel.text = itemDic[@"accountCode"];
    self.creditLabel.text = [NSString stringWithFormat:@"诚信度：%ld%%", (long)([itemDic[@"accountCredit"] longValue])];
    self.writeTimeLabel.text = itemDic[@"writeTimeString"];
}
#pragma mark - IBAction

- (IBAction)onShowEnterAction:(id)sender {
    
}

- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onShareAction:(id)sender {
    
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
