//
//  SettingViewController.m
//  chengxin
//
//  Created by common on 7/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "SettingViewController.h"
#import "UpdateVersionViewController.h"
#import "ChangePasswordViewController.h"
#import "Global.h"
#import "AcceptanceViewController.h"
#import "AboutUsViewController.h"

@interface SettingViewController ()

@end

@implementation SettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onNew:(id)sender
{
    UpdateVersionViewController* vc = [[UpdateVersionViewController alloc] initWithNibName:@"UpdateVersionViewController" bundle:nil];
    vc.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    [self.navigationController presentViewController:vc animated:NO completion:nil];
}

- (IBAction)onLogout:(id)sender
{
    [[CommonData sharedInstance] clearData];
    [GeneralUtil setUserPreference:@"password" value:@""];
    [GeneralUtil setUserPreference:@"phone" value:@""];
    [GeneralUtil setUserPreference:@"phoneNumber" value:@""];
    
    AppDelegate* app = (AppDelegate*)[UIApplication sharedApplication].delegate;
    [app setLoginVC];
//    [self.navigationController popToRootViewControllerAnimated:YES];
    
}

- (IBAction)onChangePassword:(id)sender
{
    ChangePasswordViewController* vc = [[ChangePasswordViewController alloc] initWithNibName:@"ChangePasswordViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)onAboutUs:(id)sender
{
    AboutUsViewController* vc = [[AboutUsViewController alloc] initWithNibName:@"AboutUsViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}
- (IBAction)onAgreement:(id)sender
{
    AcceptanceViewController* vc = [[AcceptanceViewController alloc] initWithNibName:@"AcceptanceViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}
@end
