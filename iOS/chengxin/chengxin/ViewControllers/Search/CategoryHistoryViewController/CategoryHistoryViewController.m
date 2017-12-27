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
    curCategoryArray = [NSMutableArray array];
    @try {
        for (id object in [[[categorySearchbar subviews] firstObject] subviews])
        {
            if (object && [object isKindOfClass:[UITextField class]])
            {
                UITextField *textFieldObject = (UITextField *)object;
                //textFieldObject.returnKeyType = UIReturnKeySearch;
                textFieldObject.enablesReturnKeyAutomatically = NO;
                break;
            }
        }
    }
    @catch (NSException *exception) {
        NSLog(@"Error while customizing UISearchBar");
    }
    @finally {
        
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    // Customize Search Bar
   // [categorySearchbar setImage:[UIImage imageNamed:@"nav_search"] forSearchBarIcon:UISearchBarIconSearch state:UIControlStateNormal];
//    [categorySearchbar setBackgroundImage:[UIImage imageNamed:@"nav_bg"]];
    //[[UISearchBar appearance] setBackgroundImage:[UIImage imageNamed:@"nav_bg"]];
    [[UISearchBar appearance] setBackgroundImage:[UIImage imageNamed:@"transparent.png"]];
    
    for(UIView* i in [categorySearchbar subviews])
    {
        if([i isKindOfClass:[UITextField class]])
        {
            [((UITextField*)i) setBorderStyle:UITextBorderStyleNone];
        }
        for(UIView* j in [i subviews])
        {
            if([j isKindOfClass:[UITextField class]])
            {
                [((UITextField*)j) setBorderStyle:UITextBorderStyleNone];
            }
        }
    }
    //
    [self getDataFromCommon];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self setData];
}

- (void)setData {
    float xPos = 12.f;
    float yPos = 16.f;
    float xStartPos = 12.f;
    float rowInterval = 16.f;
    float columnInterval = 12.f;
    float buttonHeight = 30.f;
    float buttonWidth = 60.f;
    
    NSString *trailText = @"";
    switch(curCategoryIndex)
    {
        case 0:
            trailText = @"熟人";
            break;
        case 1:
            trailText = @"企业";
            break;
        case 2:
            trailText = @"产品";
            break;
        case 3:
            trailText = @"项目";
            break;
        case 4:
            trailText = @"服务";
            break;
        case 5:
            trailText = @"诚信代码";
            break;
    }
    for (int i = 0; i < curCategoryArray.count; i++) {
        
        NSString *categoryStr = [NSString stringWithFormat:@"%@%@", curCategoryArray[i], trailText];

        UIButton *button = [[UIButton alloc] initWithFrame:CGRectMake(xPos, yPos, buttonWidth, buttonHeight)];
        button.tag = i;
        [button setBackgroundColor:BLACK_COLOR_245];
        [button setTitle:categoryStr forState:UIControlStateNormal];
        [button setTitleColor:BLACK_COLOR_51 forState:UIControlStateNormal];
        [button.titleLabel setFont:FONT_12];
        [button addTarget:self action:@selector(onClickCategoryButton:) forControlEvents:UIControlEventTouchUpInside];
        [button sizeToFit];
        float btnWidth = button.width + 10;
        float btnHeight = button.height + 4;
        if((xPos + btnWidth + columnInterval)> self.historyView.frame.size.width) {
            xPos = xStartPos;
            yPos += btnHeight + rowInterval;
        }
        [button setFrame:CGRectMake(xPos, yPos, btnWidth, btnHeight)];
        xPos += button.width + columnInterval;
        [self.historyView addSubview:button];
    }
}

- (void) onClickCategoryButton:(UIButton*) button {
    NSString *strHistory = (NSString *)curCategoryArray[button.tag];
    categorySearchbar.text  = strHistory;
    [self completeSearch];
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
        case 6:
        {
            curCategoryArray = [CommonData sharedInstance].arrayEvaluatePersonalHistory;
            categorySearchbar.text = [CommonData sharedInstance].searchEvaluatePersonalText;
        }
            break;
        case 7:
        {
            curCategoryArray = [CommonData sharedInstance].arrayEvaluateEnterpriseHistory;
            categorySearchbar.text = [CommonData sharedInstance].searchEvaluateEnterpriseText;
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
        }
            break;
        case 6:
        {
            [CommonData sharedInstance].arrayEvaluatePersonalHistory = curCategoryArray;
            [CommonData sharedInstance].searchEvaluatePersonalText = categorySearchbar.text;
        }
            break;
        case 7:
        {
            [CommonData sharedInstance].arrayEvaluateEnterpriseHistory = curCategoryArray;
            [CommonData sharedInstance].searchEvaluateEnterpriseText = categorySearchbar.text;
        }
            break;
        default:
            break;
    }
}

- (void)completeSearch {
    [self setDataToCommon];
    if(curCategoryIndex == 6 || curCategoryIndex == 7) {
        [self.navigationController popViewControllerAnimated:YES];
        return;
    }
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
    [self setData];
}

#pragma mark - IBAction
- (IBAction)onBackAction:(id)sender {
//    [self setDataToCommon];
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onDeleteAction:(id)sender {
    [curCategoryArray removeAllObjects];
    categorySearchbar.text = @"";
    NSArray *viewsToRemove = [self.historyView subviews];
    for (UIButton *button in viewsToRemove) [button removeFromSuperview];
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
