//
//  CategoryHistoryViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "CategoryHistoryViewController.h"
#import "CategoryHistoryTableViewCell.h"
#import "Global.h"

@interface CategoryHistoryViewController ()
{
    NSMutableArray *curCategoryArray;
}
@end

@implementation CategoryHistoryViewController
@synthesize categorySearchbar, curCategoryIndex;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.searchTableView registerNib:[UINib nibWithNibName:@"CategoryHistoryTableViewCell" bundle:nil] forCellReuseIdentifier:@"CategoryHistoryCellIdentifier"];
    curCategoryArray = [NSMutableArray array];
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
    //
    [self getDataFromCommon];
    [self.searchTableView reloadData];
}

- (void)getDataFromCommon {
    switch (curCategoryIndex) {
        case 0:
        {
            curCategoryArray = [CommonData sharedInstance].arrayFamiliarHistory;
            categorySearchbar.text = [CommonData sharedInstance].searchFamiliarText;
        }
            break;
        case 1:
        {
            curCategoryArray = [CommonData sharedInstance].arrayEnterpriseHistory;
            categorySearchbar.text = [CommonData sharedInstance].searchEnterpriseText;
        }
            break;
        case 2:
        {
            curCategoryArray = [CommonData sharedInstance].arrayProductHistory;
            categorySearchbar.text = [CommonData sharedInstance].searchProductText;
        }
            break;
        case 3:
        {
            curCategoryArray = [CommonData sharedInstance].arrayItemHistory;
            categorySearchbar.text = [CommonData sharedInstance].searchItemText;
        }
            break;
        case 4:
        {
            curCategoryArray = [CommonData sharedInstance].arrayServiceHistory;
            categorySearchbar.text = [CommonData sharedInstance].searchServiceText;
        }
            break;
        case 5:
        {
            curCategoryArray = [CommonData sharedInstance].arrayCodeHistory;
            categorySearchbar.text = [CommonData sharedInstance].searchCodeText;
        }
            break;
        default:
            break;
    }
}

- (void)setDataToCommon {
    switch (curCategoryIndex) {
        case 0:
        {
            [CommonData sharedInstance].arrayFamiliarHistory = curCategoryArray;
            [CommonData sharedInstance].searchFamiliarText = categorySearchbar.text;
        }
            break;
        case 1:
        {
            [CommonData sharedInstance].arrayEnterpriseHistory = curCategoryArray;
            [CommonData sharedInstance].searchEnterpriseText = categorySearchbar.text;
        }
            break;
        case 2:
        {
            [CommonData sharedInstance].arrayProductHistory = curCategoryArray;
            [CommonData sharedInstance].searchProductText = categorySearchbar.text;
        }
            break;
        case 3:
        {
            [CommonData sharedInstance].arrayItemHistory = curCategoryArray;
            [CommonData sharedInstance].searchItemText = categorySearchbar.text;
        }
            break;
        case 4:
        {
            [CommonData sharedInstance].arrayServiceHistory = curCategoryArray;
            [CommonData sharedInstance].searchServiceText = categorySearchbar.text;
        }
            break;
        case 5:
        {
            [CommonData sharedInstance].arrayCodeHistory = curCategoryArray;
            [CommonData sharedInstance].searchCodeText = categorySearchbar.text;
            [CommonData sharedInstance].searchFamiliarText = categorySearchbar.text;
            [CommonData sharedInstance].searchEnterpriseText = categorySearchbar.text;
        }
            break;
        default:
            break;
    }
}

- (void)completeSearch {
    [self.searchTableView reloadData];
    [self setDataToCommon];
    if(curCategoryIndex < 5)
        [CommonData sharedInstance].subHomeIndex = curCategoryIndex;
    else
        [CommonData sharedInstance].subHomeIndex = 0;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_RESULT_SEARCH_VIEW_NOTIFICATION object:nil];
    UINavigationController *navVC = (UINavigationController *)self.parentViewController;
    [self.navigationController popToViewController:[navVC.childViewControllers objectAtIndex:(navVC.childViewControllers.count - 3)] animated:YES];
}

- (void)addSearchTextToHistory {
    if(![categorySearchbar.text isEqualToString:@""]) {
        if(curCategoryArray.count > 10) {
            [curCategoryArray removeObjectAtIndex:0];
        }
        [curCategoryArray addObject:categorySearchbar.text];
    }
}

#pragma mark - IBAction
- (IBAction)onBackAction:(id)sender {
    [self setDataToCommon];
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onDeleteAction:(id)sender {
    [curCategoryArray removeAllObjects];
    [self.searchTableView reloadData];
}

#pragma mark - UITableViewDelegate, UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return curCategoryArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CategoryHistoryTableViewCell *cell = (CategoryHistoryTableViewCell*)[tableView dequeueReusableCellWithIdentifier: @"CategoryHistoryCellIdentifier" forIndexPath:indexPath];
    NSString *strHistory = (NSString *)curCategoryArray[indexPath.row];
    cell.titleLabel.text = strHistory;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSString *strHistory = (NSString *)curCategoryArray[indexPath.row];
    categorySearchbar.text  = strHistory;
    [self completeSearch];
}

#pragma mark - UISearchBarDelegate
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    [self addSearchTextToHistory];
    [self completeSearch];
}

- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar {
    if([searchBar.text isEqualToString:@""]) {
        [self addSearchTextToHistory];
        [self completeSearch];
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
