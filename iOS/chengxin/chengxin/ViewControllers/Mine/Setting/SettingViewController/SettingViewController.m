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
    [self.navigationController popToRootViewControllerAnimated:YES];
}

- (IBAction)onChangePassword:(id)sender
{
    ChangePasswordViewController* vc = [[ChangePasswordViewController alloc] initWithNibName:@"ChangePasswordViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)onAboutUs:(id)sender
{
    
}
- (IBAction)onAgreement:(id)sender
{
    
}
@end
