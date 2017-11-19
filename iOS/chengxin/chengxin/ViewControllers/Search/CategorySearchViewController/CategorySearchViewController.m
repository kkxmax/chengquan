//
//  CategorySearchViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/2/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "CategorySearchViewController.h"
#import "CategoryHistoryViewController.h"

@interface CategorySearchViewController ()

@end

@implementation CategorySearchViewController
@synthesize categorySearchbar, categoryButtons;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    // Customize Search Bar
    [categorySearchbar setImage:[UIImage imageNamed:@"nav_search"] forSearchBarIcon:UISearchBarIconSearch state:UIControlStateNormal];
    [categorySearchbar setBackgroundImage:[UIImage imageNamed:@"nav_bg"]];
}
#pragma mark - IBAction
- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onCategorySelect:(id)sender {
    UIButton *button = (UIButton *)sender;
    CategoryHistoryViewController *categoryHistoryVC = [[CategoryHistoryViewController alloc] initWithNibName:@"CategoryHistoryViewController" bundle:nil];
    categoryHistoryVC.curCategoryIndex = button.tag;
    [self.navigationController pushViewController:categoryHistoryVC animated:YES];
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
