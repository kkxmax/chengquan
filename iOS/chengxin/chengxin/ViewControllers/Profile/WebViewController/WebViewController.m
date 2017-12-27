//
//  WebViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/17/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "WebViewController.h"
#import "Global.h"

@interface WebViewController ()

@end

@implementation WebViewController
@synthesize searchBar;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    // Customize Search Bar
    [searchBar setImage:[UIImage imageNamed:@"nav_search"] forSearchBarIcon:UISearchBarIconSearch state:UIControlStateNormal];
//    [searchBar setBackgroundImage:[UIImage imageNamed:@"nav_bg"]];
    [[UISearchBar appearance] setBackgroundImage:[UIImage imageNamed:@"nav_bg"]];
    searchBar.text = self.webUrl;
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:self.webUrl]]];
   
    
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
    [appDelegate.window makeToast:error.userInfo[@"NSLocalizedDescription"]
                duration:3.0
                position:CSToastPositionCenter
                   style:nil];
}


- (IBAction)onBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
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
