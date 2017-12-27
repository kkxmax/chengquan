//
//  SignupSuccessViewController.m
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "SignupSuccessViewController.h"
#import "RealNameAuthenticationViewController.h"
#import "MainViewController.h"

@interface SignupSuccessViewController ()

@end

@implementation SignupSuccessViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self setNavigationBar];
    self.btnAuth.layer.cornerRadius = 5;
    self.btnGoOver.layer.cornerRadius = 5;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark
// Navigation Set
-(void) setNavigationBar
{
    self.navigationController.navigationBar.hidden = YES;
  }
- (IBAction)onBack:(id)sender {
    [self.navigationController popToRootViewControllerAnimated:YES];
}

-(IBAction)onAuth:(id)sender
{
    RealNameAuthenticationViewController* vc = [[RealNameAuthenticationViewController alloc] initWithNibName:@"RealNameAuthenticationViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onGoOver:(id)sender
{
    MainViewController *mainVC = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
    [self.navigationController pushViewController:mainVC animated:YES];
}
@end
